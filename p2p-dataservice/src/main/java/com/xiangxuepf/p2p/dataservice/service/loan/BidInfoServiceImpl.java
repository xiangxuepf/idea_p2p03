package com.xiangxuepf.p2p.dataservice.service.loan;

import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.dataservice.mapper.loan.BidInfoMapper;
import com.xiangxuepf.p2p.dataservice.mapper.loan.LoanInfoMapper;
import com.xiangxuepf.p2p.dataservice.mapper.user.FinanceAccountMapper;
import com.xiangxuepf.p2p.exterface.model.loan.BidInfo;
import com.xiangxuepf.p2p.exterface.model.loan.LoanInfo;
import com.xiangxuepf.p2p.exterface.service.loan.BidInfoService;
import com.xiangxuepf.p2p.exterface.vo.BidInfoVO;
import com.xiangxuepf.p2p.exterface.vo.BidUserTopVO;
import com.xiangxuepf.p2p.exterface.vo.ResultObjectVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * @author mhw
 */
@Service("bidInfoServiceImpl")
public class BidInfoServiceImpl implements BidInfoService {
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 获取平台累计投资金额；
     * @return
     */
    @Override
    public Double queryAllBidMoney() {
        //获取指定key的value的操作对象；
        BoundValueOperations<Object, Object> boundValueOps =
                redisTemplate.boundValueOps(Constants.ALL_BID_MONEY);

        //获取key的value;
        Double allBidMoney = (Double) boundValueOps.get();
        //判断是否有值；
        if (null == allBidMoney) {
            //去数据库查询；
            allBidMoney =  bidInfoMapper.selectAllBidMoney();
            //存放到redis中
            boundValueOps.set(allBidMoney,15, TimeUnit.MINUTES);

        }


        return allBidMoney;
    }

    @Override
    public List<BidInfoVO> queryBidInfoListByLoanId(Integer loanId) {


        return bidInfoMapper.selectBidInfoListByLoanId(loanId);
    }

    @Override
    public ResultObjectVO invest(Map<String, Object> paramMap) {
        ResultObjectVO resultObjectVO = new ResultObjectVO();
        resultObjectVO.setErrorCode(Constants.SUCCESS);
        //超卖现象，使用数据库乐观锁解决超卖
        //获取这个产品表乐观锁的版本号
        LoanInfo loanInfo = loanInfoMapper.selectByPrimaryKey((Integer) paramMap.get("loanId"));
        paramMap.put("version",loanInfo.getVersion());
        //更新产品剩余可投金额
        int updateLeftProductMoneyCount = loanInfoMapper.updateLeftProductMoneyByLoanId(paramMap);
        if( updateLeftProductMoneyCount > 0){
            //更新账号余额；
            int updateFinanceAccountCount = financeAccountMapper.updateFinanceAccountByBid(paramMap);
            //以后的充值，收益，也是通过这个方法更新；
            if (updateFinanceAccountCount > 0){
                //新增投资记录
                BidInfo bidInfo = new BidInfo();
                //用户标识
                bidInfo.setUid((Integer)paramMap.get("uid"));
                //产品标识
                bidInfo.setLoanId((Integer)paramMap.get("loanId"));
                //投资金额
                bidInfo.setBidMoney((Double) paramMap.get("bidMoney"));
                //投资时间
                bidInfo.setBidTime(new Date());
                //投资状态
                bidInfo.setBidStatus(1);//投资状态：0失败，1成功
                int insertBidCount = bidInfoMapper.insertSelective(bidInfo); //新增投资记录
                if(insertBidCount > 0){
                    //再次查看产品的剩余可投金额是否为0；不可重复读(虚读)：在同一个事务中，两次读取到的数据不一样。
                    LoanInfo loanDetail = loanInfoMapper.selectByPrimaryKey((Integer)paramMap.get("loanId"));

                    // 判断是否为0
                    //为0 ，更新产品的状态及满标时间，这里没version?
                    if(0 == loanDetail.getLeftProductMoney()){
                        //更新产品的状态及满标时间
                        LoanInfo updateLoanInfo = new LoanInfo();
                        updateLoanInfo.setId(loanDetail.getId());
                        updateLoanInfo.setProductFullTime(new Date());
                        updateLoanInfo.setProductStatus(1);  //0是未满标，1是已满标，2是满标且生成收益；
                        int  updateLoanInfoCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);   //因为只更新个别字段，所以选择，updateByPrimaryKeySelective；  //@
                        if(updateLoanInfoCount < 0){
                            resultObjectVO.setErrorCode(Constants.FAIL);

                        }
                    }

                    //将用户的投资金额存放到redis缓存中；
                    /*
                    incrementScore(K,V,delta)：元素分数增加，delta是增量
                    redis,zset集合使用； //@

                    每投资成功，则给指定的value，的分数添加本次bidMoney，然后这个zset集合，的value，
                    的分数不停增加；需要时，还可以根据分数排序查询出来；
                    以上，则是，关于使用redis,zset类型做排行榜的设计思想； //@
                     */
                    redisTemplate.opsForZSet().incrementScore(Constants.INVEST_TOP,
                            paramMap.get("phone"),
                            (Double)paramMap.get("bidMoney"));



                }else {
                    resultObjectVO.setErrorCode(Constants.FAIL);
                }



            }else {
                resultObjectVO.setErrorCode(Constants.FAIL);
            }

        }else{
            resultObjectVO.setErrorCode(Constants.FAIL);
        }



        //再次查看产品的剩余可投金额是否为0
        //为0，更新产品的状态，及满标时间；

        return resultObjectVO;
    }

    /**
     * 从Redis缓存中获取用户投资排行榜
     * @return
     */
    @Override
    public List<BidUserTopVO> queryBidUserTop() {
        List<BidUserTopVO> bidUserTopVOList = new ArrayList<>();
        //使用redisTemplate.opsForZSet获取redis 里 某个 key数据；
        Set<ZSetOperations.TypedTuple<Object>> typedTuples =
                redisTemplate.opsForZSet().reverseRangeWithScores(Constants.INVEST_TOP, 0, 9);
        //遍历获取 这个 INVEST_TOP key 的set集合 的数据；
        Iterator<ZSetOperations.TypedTuple<Object>> iterator = typedTuples.iterator();
        while (iterator.hasNext()){
            ZSetOperations.TypedTuple<Object> next = iterator.next();
            String phone = (String) next.getValue();
            Double score = next.getScore();
            //创建新增对象BidUserTopVO
            BidUserTopVO bidUserTopVO = new BidUserTopVO();
            bidUserTopVO.setPhone(phone);
            bidUserTopVO.setScore(score);
            bidUserTopVOList.add(bidUserTopVO);
        }

        return bidUserTopVOList;
    }


}

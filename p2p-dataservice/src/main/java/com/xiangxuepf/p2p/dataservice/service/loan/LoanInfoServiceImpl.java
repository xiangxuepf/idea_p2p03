package com.xiangxuepf.p2p.dataservice.service.loan;

import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.dataservice.mapper.loan.LoanInfoMapper;
import com.xiangxuepf.p2p.exterface.model.loan.LoanInfo;
import com.xiangxuepf.p2p.exterface.service.loan.LoanInfoService;
import com.xiangxuepf.p2p.exterface.vo.PaginationVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @author mhw
 */

/**
 * 代	Double historyAverageRate = (Double)
 并获取指定key的value值；
 09	代	//判断是否有值
 if()最好是反过来判断；//@
 idea	修改快捷键ifn模板
 if(null==$$)
 inn
 代	if(null == historyAverageRate)

 //没有值，去数据库查询；
 historyAverageRate = loanInfoMapper.selectHistoryAverageRate();

 //将该值存在Redis中
 redisTemplate.opsForValue().set(key,his,15,timeuint)
 记得要设失效时间；
 */
//@Service("loanInfoServiceImpl") loanInfoServiceImpl
//    @Service("loanInfoServiceImpl")
    @Service("loanInfoServiceImpl")
public class LoanInfoServiceImpl implements LoanInfoService {

    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private RedisTemplate<Object,Object> redisTemplate;


    @Override
    public Double queryHistoryAverageRate() {

        // 先去redis中获取该值，有则获取，无则查询并放入redis中；
        //更改redis中key值的序列化方式；
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        //获取操作kv数据类型的redis对象；
        Double historyAverageRate = (Double)redisTemplate.opsForValue().get(Constants.HISTORY_AVERAGE_RATE);
        //判断是否有值；
        if (null == historyAverageRate) {
            //没有值就去数据库查询；
            //将该值存在redis中，记得设置有效时间；
            historyAverageRate = loanInfoMapper.selectHistoryAverageRate();
            redisTemplate.opsForValue().set(Constants.HISTORY_AVERAGE_RATE,historyAverageRate,15, TimeUnit.SECONDS);
        }

        return historyAverageRate;
    }

    @Override
    public List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap) {


        return loanInfoMapper.selectLoanInfoByPage(paramMap);
    }

    @Override
    public PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap) {
        PaginationVO<LoanInfo> paginationVO = new PaginationVO<LoanInfo>();
        Long total = loanInfoMapper.selectTotal(paramMap);
        //查询总记录数；
        paginationVO.setTotal(total);
        //查询显示当页数据；
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByPage(paramMap);
//        直接使用indexController的分页查询方法就可以了
        paginationVO.setDataList(loanInfoList);

        return paginationVO;
    }

    @Override
    public LoanInfo queryLoanInfoById(Integer id) {


        return loanInfoMapper.selectByPrimaryKey(id);
    }
}

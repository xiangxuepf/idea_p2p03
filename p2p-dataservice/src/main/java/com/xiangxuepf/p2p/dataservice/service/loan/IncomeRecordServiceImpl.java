package com.xiangxuepf.p2p.dataservice.service.loan;

import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.common.util.DateUtils;
import com.xiangxuepf.p2p.dataservice.mapper.loan.BidInfoMapper;
import com.xiangxuepf.p2p.dataservice.mapper.loan.IncomeRecordMapper;
import com.xiangxuepf.p2p.dataservice.mapper.loan.LoanInfoMapper;
import com.xiangxuepf.p2p.dataservice.mapper.user.FinanceAccountMapper;
import com.xiangxuepf.p2p.exterface.model.loan.BidInfo;
import com.xiangxuepf.p2p.exterface.model.loan.IncomeRecord;
import com.xiangxuepf.p2p.exterface.model.loan.LoanInfo;
import com.xiangxuepf.p2p.exterface.service.loan.IncomeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author mhw
 */
@Service("incomeRecordServiceImpl")
public class IncomeRecordServiceImpl implements IncomeRecordService {
    
    private org.apache.log4j.Logger logger = org.apache.log4j.LogManager.getLogger(IncomeRecordServiceImpl.class);
    @Autowired
    private LoanInfoMapper loanInfoMapper;
    @Autowired
    private BidInfoMapper bidInfoMapper;
    @Autowired
    private IncomeRecordMapper incomeRecordMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 生成收益计划
     */
    @Override
    public void generateIncomePlan() {

        // 查询产品状态为1已满标的产品  返回List<已满标产品>
        List<LoanInfo> loanInfoList = loanInfoMapper.selectLoanInfoByProductStatus(1);
        if(loanInfoList.size() > 0){
            // 循环遍历，获取到每一个已满标产品
            for(LoanInfo loanInfo : loanInfoList){
                //产品的类型
                Integer productType = loanInfo.getProductType();
                //产品满标时间
                Date productFullTime = loanInfo.getProductFullTime();
                //产品的周期
                Integer cycle = loanInfo.getCycle();
                //产品利率
                Double rate = loanInfo.getRate();

                //获取当前已满标产品的所有投资记录  返回List<投资记录>
                List<BidInfo> bidInfoList = bidInfoMapper.selectBidInfoByLoanId(loanInfo.getId());
                // 循环遍历投资记录List,获取到每一条的记录；
                for (BidInfo bidInfo : bidInfoList){
                    //投资金额
                    Double bidMoney = bidInfo.getBidMoney();
                    IncomeRecord incomeRecord = new IncomeRecord();
                    //用户标识
                    incomeRecord.setUid(bidInfo.getUid());
                    //产品标识
                    incomeRecord.setLoanId(bidInfo.getLoanId());
                    //投资记录标识
                    incomeRecord.setBidId(bidInfo.getId());
                    //投资金额
                    incomeRecord.setBidMoney(bidInfo.getBidMoney());
                    //收益状态,0是未返还；1已返还；
                    incomeRecord.setIncomeStatus(0);
                    //收益时间 = 产品满标时间+产品的周期
                    Date incomeDate = null;
                    // 收益金额 = 投资金额*日利率*投资天数  也要根据不同产品来计算,因为不同产品，收益金额单位不同；
                    // 新手宝 元/天  散标 元/月
                    Double incomeMoney = null;

                    //判断产品类型
                    if(Constants.PRODUCT_TYPE_X == productType){
                        //新手宝产品 Date = productFullTime(Date) + cycle(Integer)天
                        incomeDate = DateUtils.getDateByAddDays(productFullTime,cycle);
                        incomeMoney = bidMoney * (rate/100/365) * cycle;
                    }else{
                        //优选和散标
                        // Date = productFullTime(Date) + cycle(Integer)月；

                        incomeDate = DateUtils.getDateByAddMonths(productFullTime,cycle);
                        incomeMoney = bidMoney * (rate/100/365) * cycle*30;
                    }
                    incomeMoney = Math.round(incomeMoney * Math.pow(10,2))/Math.pow(10,2);
                    //收益日期，收益金额
                    incomeRecord.setIncomeDate(incomeDate);
                    incomeRecord.setIncomeMoney(incomeMoney);

                    //将当前投资记录生成对应的收益记录；
                    int insertCount = incomeRecordMapper.insertSelective(incomeRecord);
                    //验证结果；

                    if(insertCount > 0){
                        logger.info("用户标识为"+bidInfo.getUid()+"，投资记录标识为"+bidInfo.getId()+",生成收益计划成功");
                    }else{
                        logger.info("用户标识为"+bidInfo.getUid()+"，投资记录标识为"+bidInfo.getId()+",生成收益计划失效");

                    }

                }
                //将当前循环遍历的产品状态更新为2
                LoanInfo updateLoanInfo = new LoanInfo();
                updateLoanInfo.setId(loanInfo.getId());
                updateLoanInfo.setProductStatus(2);
                int updateLoanInfoCount = loanInfoMapper.updateByPrimaryKeySelective(updateLoanInfo);
                if(updateLoanInfoCount > 0){
                    logger.info(("产品标识为" + loanInfo.getId() + "修改状态为2 ，满标且生成收益计划成功"));
                }else {
                    logger.info(("产品标识为" + loanInfo.getId() + "修改状态为2 ，满标且生成收益计划失败"));

                }


            }
        }else {
            logger.info("当前没有满标的产品，生成收益计划失败");
        }

    }

    /**
     * 生成收益返还
     */
    @Override
    public void generateIncomeBack() {
        //收益记录状态为0，且收益时间与当前时间相同的收益记录，返会List<收益记录>； 收益状态,0是未返还；1已返还；
        //incomeRecordList应该要验证，如果没有，则打印“无收益记录返还”
        List<IncomeRecord> incomeRecordList = incomeRecordMapper.selectIncomeRecordByIncomeStatus(0);
        if(incomeRecordList.size() > 0){
            //循环遍历收益记录
            for(IncomeRecord incomeRecord : incomeRecordList){
                //准备参数
                Map<String,Object> paramMap = new HashMap<String,Object>();
                paramMap.put("uid",incomeRecord.getUid());
                paramMap.put("bidMoney",incomeRecord.getBidMoney());
                paramMap.put("incomeMoney",incomeRecord.getIncomeMoney());
                //将当前收益记录的投资金额和收益金额返还给当前的用户；更新当前账户的可用余额；
                int updateFinaceAccountCount = financeAccountMapper.updateFinanceAccountByIncomeBack(paramMap);

                if(updateFinaceAccountCount > 0){
                    //将当前的收益记录的状态更新为1，已返还
                    IncomeRecord updateIncomeRecord = new IncomeRecord();
                    updateIncomeRecord.setId(incomeRecord.getId());
                    updateIncomeRecord.setIncomeStatus(1);
                    int updateIncomeCount = incomeRecordMapper.updateByPrimaryKeySelective(updateIncomeRecord);
                    if(updateIncomeCount > 0){
                        logger.info("用户标识为" + incomeRecord.getUid() + "收益记录标识为" + incomeRecord.getId() + "收益返还成功");

                    }else{
                        logger.info("用户标识为" + incomeRecord.getUid() + "收益记录标识为" + incomeRecord.getId() + "收益返还失败");

                    }


                }else {
                    logger.info("用户标识为" + incomeRecord.getUid() + "收益记录标识为" + incomeRecord.getId() + "收益返还失败");
                }


            }
        }else {
            logger.info("当前没有收益记录，生成收益返还失败");
        }


    }


}

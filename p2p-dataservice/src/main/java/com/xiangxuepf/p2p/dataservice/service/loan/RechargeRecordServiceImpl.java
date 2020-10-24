package com.xiangxuepf.p2p.dataservice.service.loan;

import com.xiangxuepf.p2p.dataservice.mapper.loan.RechargeRecordMapper;
import com.xiangxuepf.p2p.dataservice.mapper.user.FinanceAccountMapper;
import com.xiangxuepf.p2p.exterface.model.loan.RechargeRecord;
import com.xiangxuepf.p2p.exterface.service.loan.RechargeRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * @author mhw
 */
@Service("rechargeRecordServiceImpl")
public class RechargeRecordServiceImpl implements RechargeRecordService {
    @Autowired
    private RechargeRecordMapper rechargeRecordMapper;
    @Autowired
    private FinanceAccountMapper financeAccountMapper;

    /**
     * 新增充值记录
     * @param rechargeRecord
     * @return
     */
    @Override
    public int addRechargeRecord(RechargeRecord rechargeRecord) {

        return rechargeRecordMapper.insertSelective(rechargeRecord);
    }

    /**
     * 根据充值订单号更新充值记录；
     * @param rechargeRecord
     * @return
     */
    @Override
    public int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord) {
        return rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
    }

    /**
     * 用户充值；
     * @param paramMap
     * @return
     */
    @Override
    public int recharge(Map<String, Object> paramMap) {
        //更新账户可用余额
        int updateFinanceCount = financeAccountMapper.updateFinanceAccountByRecharge(paramMap);
        if(updateFinanceCount > 0){
        //更新充值记录的状态
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setRechargeStatus("1");
        rechargeRecord.setRechargeNo((String) paramMap.get("rechargeNo"));
            int rechargeRecordByRechargeNo = rechargeRecordMapper.updateRechargeRecordByRechargeNo(rechargeRecord);
            if(rechargeRecordByRechargeNo < 0){
                return 0;
            }
        }else {
            return 0;
        }

        return 1;
    }
}

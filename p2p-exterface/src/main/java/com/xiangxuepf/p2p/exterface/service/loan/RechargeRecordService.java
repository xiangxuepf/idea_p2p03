package com.xiangxuepf.p2p.exterface.service.loan;

import com.xiangxuepf.p2p.exterface.model.loan.RechargeRecord;

import java.util.Map;

/**
 * @author mhw
 */
public interface RechargeRecordService {
    /**
     * 新增充值记录
     * @param rechargeRecord
     * @return
     */
    int addRechargeRecord(RechargeRecord rechargeRecord);

    /**
     * 根据充值订单号更新充值记录；
     * @param rechargeRecord
     * @return
     */
    int modifyRechargeRecordByRechargeNo(RechargeRecord rechargeRecord);


    /**
     * 用户充值；
     * @param paramMap
     * @return
     */
    int recharge(Map<String, Object> paramMap);
}

package com.xiangxuepf.p2p.exterface.service.user;

import com.xiangxuepf.p2p.exterface.model.user.FinanceAccount;

/**
 * @author mhw
 */
public interface FinanceAccountService {

    /**
     * 根据用户标识获取账户信息
     * @param uid
     * @return
     */
    FinanceAccount queryFinanceAcountByUid(Integer uid);
}

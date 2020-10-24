package com.xiangxuepf.p2p.dataservice.service.user;

import com.xiangxuepf.p2p.dataservice.mapper.user.FinanceAccountMapper;
import com.xiangxuepf.p2p.exterface.model.user.FinanceAccount;
import com.xiangxuepf.p2p.exterface.service.user.FinanceAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author mhw
 */
@Service("financeAccountServiceImpl")
public class FinanceAccountServiceImpl implements FinanceAccountService {
    @Autowired
    private FinanceAccountMapper financeAccountMapper;
    @Override
    public FinanceAccount queryFinanceAcountByUid(Integer uid) {
        /*
        实现类
	@service
	注入mapper
	selectFinanceAccountByUid(uid )
         */


        return financeAccountMapper.selectFinanceAccountByUid(uid);
    }
}

package com.xiangxuepf.p2p.dataservice.mapper.user;

import com.xiangxuepf.p2p.exterface.model.user.FinanceAccount;

import java.util.Map;

public interface FinanceAccountMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(FinanceAccount record);

    int insertSelective(FinanceAccount record);

    FinanceAccount selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(FinanceAccount record);

    int updateByPrimaryKey(FinanceAccount record);

    /**
     * 根据用户标识获取账户信息；
     * @param uid
     * @return
     */
    FinanceAccount selectFinanceAccountByUid(Integer uid);


    /**
     * 更新用户余额；
     * @param paramMap
     * @return
     */
    int updateFinanceAccountByBid(Map<String, Object> paramMap);

    /**
     * 收益返还时，更新账户可用余额；
     * @param paramMap
     * @return
     */
    int updateFinanceAccountByIncomeBack(Map<String, Object> paramMap);

    /**
     * 用户充值，更新账户可用余额；
     * @param paramMap
     * @return
     */
    int updateFinanceAccountByRecharge(Map<String, Object> paramMap);
}
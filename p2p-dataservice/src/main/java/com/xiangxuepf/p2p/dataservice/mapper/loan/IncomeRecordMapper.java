package com.xiangxuepf.p2p.dataservice.mapper.loan;

import com.xiangxuepf.p2p.exterface.model.loan.IncomeRecord;

import java.util.List;

public interface IncomeRecordMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(IncomeRecord record);

    int insertSelective(IncomeRecord record);

    IncomeRecord selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(IncomeRecord record);

    int updateByPrimaryKey(IncomeRecord record);

    /**
     * 根据收益状态及收益时间获得收益记录
      * @param incomeStatus
     * @return
     */
    List<IncomeRecord> selectIncomeRecordByIncomeStatus(Integer incomeStatus);
}
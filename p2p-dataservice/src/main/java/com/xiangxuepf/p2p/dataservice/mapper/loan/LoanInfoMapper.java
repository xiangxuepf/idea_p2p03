package com.xiangxuepf.p2p.dataservice.mapper.loan;

import com.xiangxuepf.p2p.exterface.model.loan.LoanInfo;

import java.util.List;
import java.util.Map;


public interface LoanInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(LoanInfo record);

    int insertSelective(LoanInfo record);

    LoanInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(LoanInfo record);

    int updateByPrimaryKeyWithBLOBs(LoanInfo record);

    int updateByPrimaryKey(LoanInfo record);

    /**
     * 历史平均年化收益率；
     * @return
     */
    Double selectHistoryAverageRate();

    /**
     * 分页查询产品信息列表（而不是像业务层说 “根据产品类型查询产品信息列表”）
     * @param paramMap
     * @return
     */
    List<LoanInfo> selectLoanInfoByPage(Map<String, Object> paramMap);

    /**
     * 根据产品类型获取产品的总记录数；
     * @param paramMap
     * @return
     */
    Long selectTotal(Map<String, Object> paramMap);

    /**
     *更新产品剩余可投金额；
     * @param paramMap
     * @return
     */
    int updateLeftProductMoneyByLoanId(Map<String, Object> paramMap);

    /**
     * 根据产品状态获取产品信息；
     * @param productStatus
     * @return
     */
    List<LoanInfo> selectLoanInfoByProductStatus(Integer productStatus);
}
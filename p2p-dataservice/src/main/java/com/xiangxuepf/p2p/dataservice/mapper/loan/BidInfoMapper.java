package com.xiangxuepf.p2p.dataservice.mapper.loan;

import com.xiangxuepf.p2p.exterface.model.loan.BidInfo;
import com.xiangxuepf.p2p.exterface.vo.BidInfoVO;

import java.util.List;

public interface BidInfoMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(BidInfo record);

    int insertSelective(BidInfo record);

    BidInfo selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BidInfo record);

    int updateByPrimaryKey(BidInfo record);

    /**
     * 获取平台累计投资金额
     * @return
     */
    Double selectAllBidMoney();

    /**
     * 根据产品标识获取产品的所有投资记录,包含用户信息；
     * @param loanId
     * @return
     */
    List<BidInfoVO> selectBidInfoListByLoanId(Integer loanId);

    /**
     * 根据产品标识获取产品的所有投资记录
     * @param id
     * @return
     */
    List<BidInfo> selectBidInfoByLoanId(Integer id);
}
package com.xiangxuepf.p2p.exterface.service.loan;

import com.xiangxuepf.p2p.exterface.vo.BidInfoVO;
import com.xiangxuepf.p2p.exterface.vo.BidUserTopVO;
import com.xiangxuepf.p2p.exterface.vo.ResultObjectVO;

import java.util.List;
import java.util.Map;

/**
 * @author mhw
 */
public interface BidInfoService {
    /**
     * 获取平台累计投资金额；
     * @return
     */
    Double queryAllBidMoney();

    /**
     * 根据产品标识获取产品的所有投资记录；
     * @param loanId
     * @return
     */
    List<BidInfoVO> queryBidInfoListByLoanId(Integer loanId);

    /**
     * 用户投资
     * @param paramMap
     * @return
     */
    ResultObjectVO invest(Map<String, Object> paramMap);

    /**
     * 从Redis缓存中获取用户投资排行榜
     * @return
     */
    List<BidUserTopVO> queryBidUserTop();
}

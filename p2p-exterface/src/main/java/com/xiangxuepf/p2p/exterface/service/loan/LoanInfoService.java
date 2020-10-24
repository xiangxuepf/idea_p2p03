package com.xiangxuepf.p2p.exterface.service.loan;

/**
 * @author mhw
 */
//快捷键：Ctrl +Shift+ /
//        输入/** ,点击“Enter”，自动根据参数和返回值生成注释模板

import com.xiangxuepf.p2p.exterface.model.loan.LoanInfo;
import com.xiangxuepf.p2p.exterface.vo.PaginationVO;

import java.util.List;
import java.util.Map;

public interface LoanInfoService {
    /**
     *@获取年化收益率；
     *
     */
    Double queryHistoryAverageRate();

    /**
     * 根据产品类型获取产品信息列表；
     * @param paramMap
     * @return
     */
    List<LoanInfo> queryLoanInfoListByProductType(Map<String, Object> paramMap);

    /**
     * 分页查询产品信息列表；

     * @param paramMap
     * @return
     */
    PaginationVO<LoanInfo> queryLoanInfoByPage(Map<String, Object> paramMap);

    /**
     * 根据产品标识获取产品详情；
     * @param id
     * @return
     */
    LoanInfo queryLoanInfoById(Integer id);
}

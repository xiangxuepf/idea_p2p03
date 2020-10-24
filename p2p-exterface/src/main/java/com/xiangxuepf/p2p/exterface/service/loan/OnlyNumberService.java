package com.xiangxuepf.p2p.exterface.service.loan;

/**
 * @author mhw
 */
public interface OnlyNumberService {
    /**
     * 获取redis的全局唯一数字；
     * @return
     */
    Long getOnlyNumber();
}

package com.xiangxuepf.p2p.exterface.vo;

import java.io.Serializable;
import java.util.List;

/**
 * @author mhw
 */
public class PaginationVO<T> implements Serializable {
    /**
     * 总记录条数
     */
    private Long total;
    /**
     * 显示数据
     */
    private List<T> dataList;

    public void setTotal(Long total) {
        this.total = total;
    }

    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
    }

    public Long getTotal() {
        return total;
    }

    public List<T> getDataList() {
        return dataList;
    }
}

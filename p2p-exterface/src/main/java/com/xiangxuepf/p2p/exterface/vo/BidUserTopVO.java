package com.xiangxuepf.p2p.exterface.vo;

import java.io.Serializable;

/**
 * @author mhw
 */
public class BidUserTopVO implements Serializable{
    /**
     * 用户手机号
     */
    private String phone;
    /**
     * 分数：累计投资金额  转成redis描述,即是分数；
     */
    private Double score;

    public String getPhone() {
        return phone;
    }

    public Double getScore() {
        return score;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setScore(Double score) {
        this.score = score;
    }
}

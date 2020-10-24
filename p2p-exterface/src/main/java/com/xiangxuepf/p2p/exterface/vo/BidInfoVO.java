package com.xiangxuepf.p2p.exterface.vo;

import com.xiangxuepf.p2p.exterface.model.loan.BidInfo;
import com.xiangxuepf.p2p.exterface.model.user.User;

import java.io.Serializable;

/**
 * @author mhw
 */
public class BidInfoVO extends BidInfo implements Serializable {
    private User user;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}

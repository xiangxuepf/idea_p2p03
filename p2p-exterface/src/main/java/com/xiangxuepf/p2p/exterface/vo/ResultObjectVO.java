package com.xiangxuepf.p2p.exterface.vo;

import java.io.Serializable;

/**
 * @author mhw
 */
public class ResultObjectVO implements Serializable {
    /**
     * 错误码
     */
    private String errorCode;

//    public static void main(String[] args) {
//        System.out.println(errorCode);
//    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}

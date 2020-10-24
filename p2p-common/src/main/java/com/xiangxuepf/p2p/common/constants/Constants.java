package com.xiangxuepf.p2p.common.constants;

/**
 * @author mhw
 */
/*故这Constants,有些做key,有些做值；
    1.  做model的key
        1.  redis的key
    2.  做retMap的key
        1.  String ERROR_MESSAGE = "errorMessage";
    3.  redis的key
        1.  tring HISTORY_AVERAGE_RATE = "historyAverageRate";
        2.  String ALL_USER_COUNT = "allUserCount"
        3.  String ALL_BID_MONEY = "allBidMoney"
    4.  session的key
        1.  final String CAPTCHA = "captcha";
        2.  final String SESSION_USER = "USER";
    5.  做paramMap 的值
        1.  final Integer PRODUCT_TYPE_X = 0;
        2.  Integer PRODUCT_TYPE_Y = 1;
        3.  Integer PRODUCT_TYPE_S = 2;
    6.  做ERROR_MESSAGE,SUCCESS_MESSAGE 的值
    7.  做resultVO的值
        1.  final String OK = "OK";
        2.  final String SUCCESS = "SUCCESS";
        3.  final String FAIL = "FAIL";
    */

public class Constants {
    /**
     * 历史平均年化收益率； redis key
     */
    public static final String HISTORY_AVERAGE_RATE = "historyAverageRate";
    /**
     * 获取平台注册总人数； redis key
     */
    public static String ALL_USER_COUNT = "allUserCount";
    /**
     * 获取平台累计投资金额 redis key
     */
    public static String ALL_BID_MONEY = "allBidMoney";

    /**
     * 新手宝 做paramMap的值； map value
     */
    public static final Integer PRODUCT_TYPE_X = 0;

    /**
     * 优选产品
     */
    public static final Integer PRODUCT_TYPE_Y = 1;

    /**
     * 散标产品
     */
    public static final Integer PRODUCT_TYPE_S = 2;

    /**
     * 图形验证码 做session的key
     */
    public static final String CAPTCHA = "captcha";
    /**
     * 错误消息；retMap  key
     */
    public static final String ERROR_MESSAGE = "errorMessage";
    /**
     * 验证OK; 用来做ERROR_MESSAGE的值；而验证失败的 ERROR_MESSAGE再自定义；
     */
    public static final String OK = "OK";
    /**
     * SUCCESS; 用来set ResultVO ,验证业务方法结果；或其他map的值；
     */
    public static final String SUCCESS = "SUCCESS";

    /**
     * FAIL  用来set ResultVO 的 errorCode的值 ,验证业务方法结果；或其他map的值；
     */
    public static final String FAIL = "FAIL";

    /**
     * 用户信息；
     */
    public static final String SESSION_USER = "USER";

    /**
     * 用户投资排行榜；用做redis 的 zset集合的key,确定指定某个zset集合;
     */
    public static final String INVEST_TOP = "INVEST_TOP";

    /**
     * redis唯一数字  做redis key;
     */
    public static final String ONLY_NUMBER = "ONLY_NUMBER";

    /**
     * 交易消息，model key;
     */
    public static final String TRADE_MSG = "trade_msg";
}

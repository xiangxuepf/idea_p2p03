package com.xiangxuepf.p2p.pay.config;
/**
 * 支付宝配置对象
 * ClassName:AlipayConfig
 *
 */
public class AlipayConfig {
	
	/**
	 * 支付宝网关:向支付宝发起请求的网关。沙箱与线上不同，请更换代码中配置
	 */
	private String getway_url;
	/**
	 * 发起请求的应用ID。沙箱与线上不同，请更换代码中配置
	 */
	private String app_id;
	/**
	 * 仅支持JSON
	 */
	private String format;
	/**
	 * 应用私钥
	 */
	private String private_key;
	
	/**
	 * 请求使用的编码格式
	 */
	private String charset;
	/**
	 * 支付宝公钥（由应用公钥配置生成）
	 */
	private String alipay_public_key;
	/**
	 * 户生成签名字符串所使用的签名算法类型，目前支持RSA2和RSA，推荐使用RSA2
	 */
	private String sign_type;
	/**
	 * 支付宝同步返回的URL
	 */
	private String alipay_return_url;
	/**
	 * 支付宝异步通知返回的URL
	 */
	private String alipay_notify_url;
	/**
	 * pay同步返回p2p工程的URL
	 */
	private String pay_p2p_alipay_back_url;
	
	public String getPay_p2p_alipay_back_url() {
		return pay_p2p_alipay_back_url;
	}
	public void setPay_p2p_alipay_back_url(String pay_p2p_alipay_back_url) {
		this.pay_p2p_alipay_back_url = pay_p2p_alipay_back_url;
	}
	public String getAlipay_return_url() {
		return alipay_return_url;
	}
	public void setAlipay_return_url(String alipay_return_url) {
		this.alipay_return_url = alipay_return_url;
	}
	public String getAlipay_notify_url() {
		return alipay_notify_url;
	}
	public void setAlipay_notify_url(String alipay_notify_url) {
		this.alipay_notify_url = alipay_notify_url;
	}
	public String getPrivate_key() {
		return private_key;
	}
	public void setPrivate_key(String private_key) {
		this.private_key = private_key;
	}
	public String getGetway_url() {
		return getway_url;
	}
	public void setGetway_url(String getway_url) {
		this.getway_url = getway_url;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getFormat() {
		return format;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public String getCharset() {
		return charset;
	}
	public void setCharset(String charset) {
		this.charset = charset;
	}
	public String getAlipay_public_key() {
		return alipay_public_key;
	}
	public void setAlipay_public_key(String alipay_public_key) {
		this.alipay_public_key = alipay_public_key;
	}
	public String getSign_type() {
		return sign_type;
	}
	public void setSign_type(String sign_type) {
		this.sign_type = sign_type;
	}
	
}

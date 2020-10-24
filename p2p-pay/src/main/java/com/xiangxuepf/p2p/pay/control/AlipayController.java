package com.xiangxuepf.p2p.pay.control;

import com.alipay.api.AlipayApiException;
import com.alipay.api.AlipayClient;
import com.alipay.api.DefaultAlipayClient;
import com.alipay.api.internal.util.AlipaySignature;
import com.alipay.api.request.AlipayTradePagePayRequest;
import com.alipay.api.request.AlipayTradeQueryRequest;
import com.xiangxuepf.p2p.pay.config.AlipayConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * @author mhw
 */
@Controller
public class AlipayController {
    @Autowired
    private AlipayConfig alipayConfig;

    /**
     * 充值功能，请求支付宝alipay.trade.page.pay接口；代码复制SDK demo alipay.trade.page.pay.jsp 的Java脚本
     * @param request
     * @param model
     * @param out_trade_no
     * @param total_amount
     * @param subject
     * @param body
     * @return
     * @throws AlipayApiException
     */
    @RequestMapping("/api/alipay")
    public String alipay(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "out_trade_no",required = true) String out_trade_no, //商户订单号
            @RequestParam(value = "total_amount",required = true) Double total_amount, //付款金额
            @RequestParam(value = "subject",required = true) String subject, //订单名称
            @RequestParam(value = "body",required = true) String body //订单名称
    ) throws AlipayApiException {
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getGetway_url(),
                alipayConfig.getApp_id(),
                alipayConfig.getPrivate_key(),
                alipayConfig.getFormat(),
                alipayConfig.getCharset(),
                alipayConfig.getAlipay_public_key(),
                alipayConfig.getSign_type()
                );

        //设置请求参数
        AlipayTradePagePayRequest alipayRequest = new AlipayTradePagePayRequest();
        alipayRequest.setReturnUrl(alipayConfig.getAlipay_return_url());
        alipayRequest.setNotifyUrl(alipayConfig.getAlipay_notify_url());

//
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
                + "\"total_amount\":\""+ total_amount +"\","
                + "\"subject\":\""+ subject +"\","
                + "\"body\":\""+ body +"\","
                + "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
//
//        //若想给BizContent增加其他可选请求参数，以增加自定义超时时间参数timeout_express来举例说明
//        //alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\","
//        //		+ "\"total_amount\":\""+ total_amount +"\","
//        //		+ "\"subject\":\""+ subject +"\","
//        //		+ "\"body\":\""+ body +"\","
//        //		+ "\"timeout_express\":\"10m\","
//        //		+ "\"product_code\":\"FAST_INSTANT_TRADE_PAY\"}");
//        //请求参数可查阅【电脑网站支付的API文档-alipay.trade.page.pay-请求参数】章节
//
//        //请求响应result
        String result = alipayClient.pageExecute(alipayRequest).getBody();
        model.addAttribute("result",result);
        return "toAlipay";
    }

    /**
     * 充值功能，接收支付宝toAlipay.jsp中的form表单的url请求，
     * （alipay.trade.page.pay.jsp 接口的返回form表单，的return_url的请求；）
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/api/alipayBack")
    public String alipayBack(
            HttpServletRequest request,
            Model model
    ) throws AlipayApiException, UnsupportedEncodingException {
        Map<String,String> params = new HashMap<String,String>();
        //获取支付宝GET过来反馈信息
        Map<String,String[]> requestParams = request.getParameterMap();
        for (Iterator<String> iter = requestParams.keySet().iterator(); iter.hasNext();) {
            String name = (String) iter.next();
            String[] values = (String[]) requestParams.get(name);
            String valueStr = "";
            for (int i = 0; i < values.length; i++) {
                valueStr = (i == values.length - 1) ? valueStr + values[i]
                        : valueStr + values[i] + ",";
            }
            //乱码解决，这段代码在出现乱码时使用
            valueStr = new String(valueStr.getBytes("ISO-8859-1"), "utf-8");
            params.put(name, valueStr);
        }

        boolean signVerified = AlipaySignature.rsaCheckV1(
                params,
                alipayConfig.getAlipay_public_key(),
                alipayConfig.getCharset(),
                alipayConfig.getSign_type()); //调用SDK验证签名

        //——请在这里编写您的程序（以下代码仅作参考）——
        if(signVerified) {
            //商户订单号，参考代码
            String out_trade_no = new String(request.getParameter("out_trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //支付宝交易号，参考代码
            String trade_no = new String(request.getParameter("trade_no").getBytes("ISO-8859-1"),"UTF-8");

            //付款金额，餐代码
            String total_amount = new String(request.getParameter("total_amount").getBytes("ISO-8859-1"),"UTF-8");
            //以下是自己代码；
            model.addAttribute("signVerified", "SUCCESS");
            model.addAttribute("params",params);
        }else {
            model.addAttribute("signVerified","FAIL");
        }
            model.addAttribute("pay_p2p_return_url","http://localhost:8080/p2p/loan/alipayBack"); //前台form表单需要这个地址；
        //——请在这里编写您的程序（以上代码仅作参考）——


        return "toP2P";
    }



    /**
     * 接收web工程请求，然调用alipay.trade.query接口
     * 代码复制SDK demo alipay.trade.query.jsp 的Java脚本，返回json;
     * @param request
     * @param out_trade_no
     * @return
     * @throws AlipayApiException
     */
    @RequestMapping("/api/alipayQuery")
    public @ResponseBody Object alipayQuery(
            HttpServletRequest request,
            @RequestParam(value = "out_trade_no",required = true) String out_trade_no
            ) throws AlipayApiException {
//    复制 alipay.trade.query 代码
        //获得初始化的AlipayClient
        AlipayClient alipayClient = new DefaultAlipayClient(
                alipayConfig.getGetway_url(),
                alipayConfig.getApp_id(),
                alipayConfig.getPrivate_key(),
                alipayConfig.getFormat(),
                alipayConfig.getCharset(),
                alipayConfig.getAlipay_public_key(),
                alipayConfig.getSign_type()
        );

        //设置请求参数,请求对象；
        AlipayTradeQueryRequest alipayRequest = new AlipayTradeQueryRequest();
        alipayRequest.setBizContent("{\"out_trade_no\":\""+ out_trade_no +"\"}");

        //请求
        String result = alipayClient.execute(alipayRequest).getBody();

        return result;
    }


}

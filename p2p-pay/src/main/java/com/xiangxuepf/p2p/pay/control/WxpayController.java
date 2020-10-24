package com.xiangxuepf.p2p.pay.control;

import com.github.wxpay.sdk.WXPayUtil;
import com.xiangxuepf.p2p.common.util.HttpClientUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mhw
 */
@Controller
public class WxpayController {

    //统一下单接口
    @RequestMapping("/api/wxpay")
    public @ResponseBody
    Object wxpay(
            HttpServletRequest request,
            @RequestParam(value = "body", required = true) String body,
            @RequestParam(value = "out_trade_no", required = true) String out_trade_no,
            @RequestParam(value = "total_fee", required = true) Double total_fee
    ) throws Exception {

        //微信接口返回是xml格式   //@
        //然后我们再转成json响应给web工程
        //准备Map集合的请求参数
        Map<String, String> requestDataMap = new HashMap<>();
        requestDataMap.put("appid", "wx8a3fcf509313fd74");//公众账号id;
        requestDataMap.put("mch_id", "1361137902"); //商户号；
        requestDataMap.put("nonce_str", WXPayUtil.generateNonceStr());//随机字符串；
        requestDataMap.put("body", body);//商品描述；
        requestDataMap.put("out_trade_no", out_trade_no);//商品订单号；
        BigDecimal bigDecimal = new BigDecimal(total_fee);
//        专门处理金额的数据类型；  //@
        BigDecimal multiply = bigDecimal.multiply(new BigDecimal(100));
        requestDataMap.put("total_fee", multiply.toString()); //标价金额
        InetAddress localHost = InetAddress.getLocalHost();
        requestDataMap.put("spbill_create_ip", localHost.getHostAddress());//终端ip;
        requestDataMap.put("notify_url", "http://localhost:9090/api/wxpayNotify");
        requestDataMap.put("trade_type", "NATIVE");
        requestDataMap.put("product_id", out_trade_no);//可以自定义，故就用商户订单号吧；
        //生成签名值
        String signature = WXPayUtil.generateSignature(requestDataMap, "367151c5fd0d50f1e34a68a802d6bbca");
        requestDataMap.put("sign", signature);
        //以上则参数准备好
        //将Map集合的请求参数转换为xml格式的请求参数；
        String requestDataXml = WXPayUtil.mapToXml(requestDataMap);
        System.out.println("接口map类型的参数转换为xml-----> " + requestDataXml);
//        调用微信统一下单API接口，返回xml格式的字符串；
        String responseDataXml = HttpClientUtils.doPostByXml(
                "https://api.mch.weixin.qq.com/pay/unifiedorder",
                requestDataXml);

        System.out.println("调用微信统一下单接口的响应结果: " + new String(responseDataXml.getBytes("ISO-8859-1"),"UTF-8"));
//        将Xml格式的字符串转换成Map集合；
        Map<String, String> responseDataMap = WXPayUtil.xmlToMap(responseDataXml);
        //判断返回结果
//        String return_code = responseMap.get("return_code");
//        String return_msg = responseMap.get("return_msg");

        return responseDataMap;
    }
}

package com.xiangxuepf.p2p.web.web;

import com.alibaba.fastjson.JSONObject;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.common.util.DateUtils;
import com.xiangxuepf.p2p.common.util.HttpClientUtils;
import com.xiangxuepf.p2p.exterface.model.loan.RechargeRecord;
import com.xiangxuepf.p2p.exterface.model.user.User;
import com.xiangxuepf.p2p.exterface.service.loan.OnlyNumberService;
import com.xiangxuepf.p2p.exterface.service.loan.RechargeRecordService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mhw
 */
@Controller
public class RechargeRecordController {
    @Autowired
    private OnlyNumberService onlyNumberService;
    @Autowired
    private RechargeRecordService rechargeRecordService;


    /**
     * to阿里支付，调用pay工程的alipay.trade.page.pay 接口；
     * @param request
     * @param model
     * @param rechargeMoney
     * @return
     */
    @RequestMapping("/loan/toAlipayRecharge")
    public String toAlipayRecharge
       (HttpServletRequest request,
          Model model,
           @RequestParam(value = "rechargeMoney",required = true) Double rechargeMoney
      ){
        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //生成一个全局唯一充值订单号 = 时间戳 + redis全局唯一数字
        String rechargeNo = DateUtils.getTimeStamp()+ onlyNumberService.getOnlyNumber();
        //生成充值记录，状态为充值中
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeTime(new java.util.Date());
        rechargeRecord.setRechargeStatus("1");
        rechargeRecord.setRechargeDesc("支付宝充值");
        int addRechargeCount = rechargeRecordService.addRechargeRecord(rechargeRecord);
        if(addRechargeCount > 0){
            //向pay工程的支付方法传递参数
            model.addAttribute("p2p_pay_alipay_url","http://localhost:9090/pay/api/alipay");
            model.addAttribute("rechargeNo",rechargeNo);
            model.addAttribute("rechargeMoney",rechargeMoney);
            model.addAttribute("subject", "支付宝充值");


        }else {
            model.addAttribute(Constants.TRADE_MSG,"充值人数过多，请稍后重试...");
            return "toRechargeBack";
        }

        return "toAlipay";
    }


    /**
     * 接收pay工程alipay.trade.page.pay接口返回的支付数据，通过return_url请求
     * 支付宝返回 --> form表单，跳转地址是return_url -->
     * pay工程 /api/alipayBack (没在公网哦；不用在公网) -->toP2P,form表单 --> web工程 /loan/alipayBack
     * @param request
     * @param model
     * @param out_trade_no
     * @param signVerified
     * @param total_amount
     * @return
     */
    @RequestMapping("/loan/alipayBack")
    public String alipayBack(
            HttpServletRequest request,
            Model model,
            @RequestParam(value = "out_trade_no",required = true) String out_trade_no,
            @RequestParam(value = "signVerified",required = true) String signVerified,
            @RequestParam(value = "total_amount",required = true) Double total_amount
    ){
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //判断验证前面是否成功
        if(StringUtils.equals(Constants.SUCCESS,signVerified)){
            //成功
            Map<String, Object> paramMap = new HashMap<>();
            paramMap.put("out_trade_no",out_trade_no);
            //调用pay工程的订单查询接口，返回订单状态
            String jsonString = HttpClientUtils.doPost("http://localhost:9090/pay/api/alipayQuery", paramMap);
            //fastjson
            //json对象
            JSONObject jsonObject = JSONObject.parseObject(jsonString);
            //获取指定key的value值
            JSONObject tradeQueryResponse = jsonObject.getJSONObject("alipay_trade_query_response");
            //获取通信标识
            String code = tradeQueryResponse.getString("code");
            //判断通信是否成功
            if(StringUtils.equals("10000",code)){
                //获取业务处理结果
                String trade_status = tradeQueryResponse.getString("trade_status");
                /*
                WAIT_BUYER_PAY(交易创建，等待买家付款)
	            TRADE_CLOSED（未付款交易超时关闭，或支付完成后全额退款）
	            TRADE_SUCCESS（交易支付成功）
	            TRADE_FINISHED（交易结束，不可退款）
                 */

                if("TRADE_CLOSED".equals(trade_status)){
                    //更新充值记录状态为2，充值失败
                    RechargeRecord updateRechargeRecord = new RechargeRecord();
                    updateRechargeRecord.setRechargeNo(out_trade_no);
                    updateRechargeRecord.setRechargeStatus("2");
                    int modifyRechargeCount = rechargeRecordService.modifyRechargeRecordByRechargeNo(updateRechargeRecord);
                    if(modifyRechargeCount < 0){
                        model.addAttribute("trade_msg","充值人数过多，请稍后重试..");
                        return "toRechargeBack";
                    }
                }

                if("TRADE_SUCCES".equals(trade_status)){
                    //支付宝扣款成功，给用户充值
                  /*  1.	更新账户余额；
                    2.	更新充值记录的状态为1，充值成功；
                    3.	参数；用户标识，充值订单号，充值金额*/
                  paramMap.put("uid",sessionUser.getId());
                  paramMap.put("rechargeNo",out_trade_no);
                  paramMap.put("rechargeMoney",total_amount);
                  int rechargeCount = rechargeRecordService.recharge(paramMap);
                  if(rechargeCount < 0){
                      //但是已经扣款了呀？
                      model.addAttribute("trade_msg","充值人数过多，请稍后重试..");
                      return "toRechargeBack";
                  }

                }

                
            }else{
                model.addAttribute("trade_msg","通信失败，请稍后重试..");
                return "toRechargeBack";
            }


        }else {
            model.addAttribute("trade_msg","充值人数过多，请稍后重试..");
            return "toRechargeBack";
        }

        return "redirect:/loan/myCenter";
    }

    /**
     * to微信支付
     * @param request
     * @param model
     * @param rechargeMoney
     * @return
     */
    @RequestMapping("/loan/toWxpayRecharge")
    public String toWxpayRecharge
    (HttpServletRequest request,
     Model model,
     @RequestParam(value = "rechargeMoney",required = true) Double rechargeMoney
    ){
        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //生成一个全局唯一充值订单号 = 时间戳 + redis全局唯一数字
        String rechargeNo = DateUtils.getTimeStamp()+ onlyNumberService.getOnlyNumber();
        //生成充值记录，状态为充值中
        RechargeRecord rechargeRecord = new RechargeRecord();
        rechargeRecord.setUid(sessionUser.getId());
        rechargeRecord.setRechargeNo(rechargeNo);
        rechargeRecord.setRechargeMoney(rechargeMoney);
        rechargeRecord.setRechargeTime(new java.util.Date());
        rechargeRecord.setRechargeStatus("1");
        rechargeRecord.setRechargeDesc("微信充值");
        int addRechargeCount = rechargeRecordService.addRechargeRecord(rechargeRecord);
        if(addRechargeCount > 0){
            //跳转至展示二维码页面
            model.addAttribute("rechargeNo",rechargeNo);
            model.addAttribute("rechargeMoney",rechargeMoney);
            model.addAttribute("rechargeTime",new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        }else {
            model.addAttribute("trade_msg","充值人数过多，请稍后重试..");
            return "toRechargeBack";
        }




        return "generateQRCode";
    }

    /**
     * 微信支付生成二维码；
     * 接收生成二维码请求；
     * 调用pay工程统一下单接口；
     * @param request
     * @param response
     */
    @RequestMapping("/loan/generateQRCode")
    public void generateQRCode(
            HttpServletRequest request,
            HttpServletResponse response,
            @RequestParam(value = "rechargeNo",required = true) String rechargeNo,
            @RequestParam(value = "rechargeMoney",required = true) Double rechargeMoney
    ) throws IOException, WriterException {
        //参数准备
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("out_trade_no",rechargeNo);
        paramMap.put("total_fee",rechargeMoney);
        paramMap.put("body","微信充值");
        //调用pay工程的统一下单接口，返回参数，包含code_url
        String jsonString = HttpClientUtils.doPost("http://localhost:9090/pay/api/wxpay", paramMap);
        //使用fastJson解析json格式的字符串；
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //获取通信标识return_code
        String returnCode = jsonObject.getString("return_code");
        //判断通信是否成功
        if(StringUtils.equals(Constants.SUCCESS,returnCode)){
            //获取业务处理结果
            String resultCode = jsonObject.getString("result_code");
            //判断业务处理结果
            if (StringUtils.equals(Constants.SUCCESS,resultCode)){
                //将code_url转为二维码；
                String codeUrl = jsonObject.getString("code_url");//微信支付就是为了获得这个url，然把这url转成二维码；
                //创建一个矩阵对象；
                Map<com.google.zxing.EncodeHintType, Object> hintMap = new HashMap<>();
                hintMap.put(com.google.zxing.EncodeHintType.CHARACTER_SET,"UTF-8");
                BitMatrix bitMatrix = new
                        MultiFormatWriter().encode(codeUrl, BarcodeFormat.QR_CODE,200,200,hintMap);
                //把二维码输出到页面；
                ServletOutputStream outputStream = response.getOutputStream();
                MatrixToImageWriter.writeToStream(bitMatrix,"png",outputStream);

            }else{
                response.sendRedirect(request.getContextPath() + "/toRechargeBack.jsp");

            }


        }else{
            response.sendRedirect(request.getContextPath() + "/toRechargeBack.jsp");
        }

    }


}

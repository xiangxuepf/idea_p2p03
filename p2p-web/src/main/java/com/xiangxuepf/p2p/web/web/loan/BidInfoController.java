package com.xiangxuepf.p2p.web.web.loan;

import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.exterface.model.user.User;
import com.xiangxuepf.p2p.exterface.service.loan.BidInfoService;
import com.xiangxuepf.p2p.exterface.vo.ResultObjectVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * @author mhw
 */
@Controller
public class BidInfoController {
    @Autowired
    private BidInfoService bidInfoService;

    //用户投资
    @RequestMapping("/loan/invest")
    public @ResponseBody Object invest(
            HttpServletRequest request,
            @RequestParam(value = "loanId" , required = true) Integer loanId,
            @RequestParam(value = "bidMoney" , required = true) Double bidMoney
    ){
        Map<String ,Object> retMap = new HashMap<String,Object>();
        //从session获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //准备请求参数，service方法的参数
        Map<String,Object> paramMap = new HashMap<String,Object>();
        //用户标识
        paramMap.put("uid",sessionUser.getId());
        //产品标识
        paramMap.put("loanId",loanId);
        //投资金额
        paramMap.put("bidMoney",bidMoney);
        //用户手机号
        paramMap.put("phone",sessionUser.getPhone());

        //用户投资（用户标识，产品标识，投资金额），返回ResultObject
        ResultObjectVO resultObjectVO = bidInfoService.invest(paramMap);
        //判断是否成功
        if(StringUtils.equals(Constants.SUCCESS,resultObjectVO.getErrorCode())){
            retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        }else{
            retMap.put(Constants.ERROR_MESSAGE,"投资人数过多，请稍后重试..");
            return retMap;
        }
        return retMap;

    }


}

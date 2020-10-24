package com.xiangxuepf.p2p.web.web.user;

import com.alibaba.fastjson.JSONObject;
import com.xiangxuepf.p2p.common.constants.Constants;
import com.xiangxuepf.p2p.exterface.model.user.FinanceAccount;
import com.xiangxuepf.p2p.exterface.model.user.User;
import com.xiangxuepf.p2p.exterface.service.loan.BidInfoService;
import com.xiangxuepf.p2p.exterface.service.loan.LoanInfoService;
import com.xiangxuepf.p2p.exterface.service.user.FinanceAccountService;
import com.xiangxuepf.p2p.exterface.service.user.UserService;
import com.xiangxuepf.p2p.exterface.vo.ResultObjectVO;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * @author mhw
 * @create 2019-11-16 14:24
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private FinanceAccountService financeAccountService;
    @Autowired
    private LoanInfoService loanInfoService;
    @Autowired
    private BidInfoService bidInfoService;


    @RequestMapping("/loan/checkPhone")
    public @ResponseBody Object checkPhone(
            HttpServletRequest request,
            @RequestParam(value = "phone",required = true) String phone
    ){
        Map<String,Object> retMap = new HashMap<String,Object>();
        /*为什么用String 呢  //@
	1. 如用来做计算则用Interge，否则用String;*/
        //查手机号是否重复；
        /*
        * userService.queryUserByPhone(phone);
	返回布尔或User或int;
	User较好；
	因为可以换一个说法，是根据手机号查询用户信息；
	故可以一个方法，完成2个业务需求；
        * */
        User user = userService.queryUserByPhone(phone);
        //判断用户是否存在；
        if (null != user) {
            retMap.put(Constants.ERROR_MESSAGE,"该手机号码已被注册，请更换手机号码");
            return retMap;
        }
        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);

        return retMap;
    }

    @RequestMapping("/loan/checkCaptcha")
    public @ResponseBody Map<String,Object> checkCaptcha(
            HttpServletRequest request,
            @RequestParam(value = "captcha",required = true) String captcha
    ) {
        Map<String,Object> retMap = new HashMap<String,Object>();
        //获取Session中的图形验证码
        String sessionCaptcha = (String) request.getSession().getAttribute(Constants.CAPTCHA);
        //验证图形验证码是否一致；
        if (!StringUtils.equalsIgnoreCase(sessionCaptcha,captcha)) {
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的图形验证码");
            return retMap;
        }
        retMap.put(Constants.ERROR_MESSAGE, Constants.OK);
        return retMap;
    }


    /**
     * 处理用户注册请求；
     */
    @RequestMapping("/loan/register")
    public @ResponseBody  Object register(
            HttpServletRequest request,
            @RequestParam(value = "phone",required = true) String phone,
            @RequestParam(value = "loginPassword",required = true) String loginPassword,
            @RequestParam(value = "replayLoginPassword",required = true) String replayLoginPassword
    ){
        Map<String,Object> retMap = new HashMap<String,Object>();
        //验证参数,用到了java.util包下工具类；  //@
        if(!Pattern.matches("^1[1-9]\\d{9}$",phone)){
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的手机号码");
            return retMap;
        }

        if(!StringUtils.equals(loginPassword,replayLoginPassword)){
            retMap.put(Constants.ERROR_MESSAGE,"两次输入的密码不一致");
            return retMap;
        }
        //用户的注册
       /*
        1.	新增用户信息；
        2.	要领红包，就是开立账号
        3.	返回布尔或int或结果对象；
        4.	参数，手机号，密码；
        VO里创建这个reusltObject，这个Object就封装一个错误码；//@
	关于业务方法布尔结果集的封装；  //@
	string errorCode;
        */
        ResultObjectVO resultObject = userService.register(phone,loginPassword);

        //判断是否注册成功；
        /*if(!stringUtils.equals(constants.success,re.getEc))
            put 注册失败，请稍后重试；

        注册成功；则要将用户信息存放到session中；
        request.getSession
        put KV
        K session_user
        V user;*/
        if(!StringUtils.equals(Constants.SUCCESS,resultObject.getErrorCode())){
            retMap.put(Constants.ERROR_MESSAGE,"注册失败，请稍后重试。");
            return retMap;
        }
        //注册成功；则要将用户信息存放到session中,key是大写USER；
        request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(phone));
        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);
        return retMap;
    }

    /*
        usercontroller
	/loan/verifyRealName

	Object responceBody
	request
	string realName
	string idCard
	String replayIdCard

	Map
	//验证参数
	if(!Patten.matches(姓名正则，realName))
	put(errormessage,"真实姓名只支持中文")
	return retMap
     */
    @RequestMapping("/loan/verifyRealName")
    public @ResponseBody Object verifyRealName(
            HttpServletRequest request,
            @RequestParam(value = "realName",required = true) String realName,
            @RequestParam(value = "idCard",required = true) String idCard,
            @RequestParam(value = "replayIdCard",required = true) String replayIdCard
    ){
        Map<String,Object> retMap = new HashMap<String,Object>();
        //验证参数
        if(!Pattern.matches("^[\\u4e00-\\u9fa5]{0,}$",realName)){
            retMap.put(Constants.ERROR_MESSAGE,"真实姓名只支持中文");
            return retMap;
        }

        //验证身份证号
        /*
            身份证号
	if(p.身份证正则，idCard)
	身份证号是不是个人信息的主键；一定不是主键；//@
	设置主键的规则，就是一个自然数，且跟你的业务没有任何关系；  //@
	关于主键的设定，及身份证；
	主键是不能变的；
	身份证可以设定成主键那个规范，但它不是主键；

35	继续
	put 请输入正确的身份证号
	return

36	if(!strtingutils.eq())
	put 两次输入身份证号码不一致
	return

         */
        if(!Pattern.matches("(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)",idCard)){
            retMap.put(Constants.ERROR_MESSAGE,"请输入正确的身份证号码");
            return retMap;
        }
        if(!StringUtils.equals(replayIdCard,idCard)){
            retMap.put(Constants.ERROR_MESSAGE,"两次输入身份证号码不一致");
            return retMap;
        }

        //发送请求验证用户真实信息；
        /*
            HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test",paramMap);

	//准备参数
	Map paramMap
	//实名认证appkey
	put "appkey",""
	//真实姓名
	put "realName" ,realName
	put "cardNo",idCard

	响应json
         */
        //准备参数
        Map<String,Object> paramMap = new HashMap<String,Object>();
        //实名认证appkey
        paramMap.put("appkey","");
        //真实姓名，身份证
        paramMap.put("realName",realName);
        paramMap.put("cardNo",idCard);

        //发送请求验证用户真实信息；
//        String jsonString = HttpClientUtils.doPost("https://way.jd.com/youhuoBeijing/test", paramMap);
        String jsonString = "{\n" +
                "    \"code\": \"10000\",\n" +
                "    \"charge\": false,\n" +
                "    \"remain\": 1305,\n" +
                "    \"msg\": \"查询成功\",\n" +
                "    \"result\": {\n" +
                "        \"code\": \"000000\",\n" +
                "        \"serialNo\": \"201707110956216762709752427036\",\n" +
                "        \"success\": \"true\",\n" +
                "        \"message\": \"一致\",\n" +
                "        \"comfirm\": \"jd_credit_two\"\n" +
                "    }\n" +
                "}\n";
        //解析jsonString
        /*
            解析jsonString；
        使用fastjson
        //将json转成json对象；
        JSONObject.parseObject(jsonString)
        jsonObject
        //获取指定key对应的value
        jsonObject.getString("code");
        string code
        //判断通信是否成功；
        if(1000)
        //通信成功
        //获取isok是否配置标识
    00	jsonObject.getJSONObject("result").getJSONObect("result").getBoolean("isok")
        //通信失败，请稍后重试..
         */

        //将json转成json对象
        JSONObject jsonObject = JSONObject.parseObject(jsonString);
        //获取指定key对应的value；
        String code = jsonObject.getString("code");
        //判断通信是否成功；
        if(StringUtils.equals("10000",code)){
            //通信成功；
            //获取isok是否配置标识；
            Boolean isok = jsonObject.getJSONObject("result").getBoolean("success");
            //判断真实姓名与身份证号码是否一致；
            if(isok){
                //更新用户信息
                /*
                    更新用户信息
                userService.modifyRealNameAndIdCardByUid()
                这样定义，只能说明这方法只能改姓名，身份证号；
                关于service层方法命名，很关键；  //@
                故，可定义更范围更大些，
                modifyUserById(user);

                new User
                updateUser.setId()

                //从session中获取用户信息
                SESSION_USER
                 */
                //从session中获取用户信息
                User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
                //更新用户信息
                User updateUser = new User();
                updateUser.setId(sessionUser.getId());
                updateUser.setName(realName);
                updateUser.setIdCard(idCard);
                int modifyUserCount = userService.modifyUserById(updateUser);
                if(modifyUserCount > 0){
                    //更新session里的用户信息
                    request.getSession().setAttribute(Constants.SESSION_USER,userService.queryUserByPhone(sessionUser.getPhone()));

                    retMap.put(Constants.ERROR_MESSAGE,Constants.OK);

                }else {
                    retMap.put(Constants.ERROR_MESSAGE,"系统繁忙请稍后重试");
                    return retMap;
                }

            }else{
                retMap.put(Constants.ERROR_MESSAGE,"真实姓名与身份证信息不配置");
                return retMap;
            }

        }else{
            retMap.put(Constants.ERROR_MESSAGE,"通信失败，请稍后重试..");
            return retMap;
        }

        return retMap;
    }

    @RequestMapping("/loan/myFinanceAccount")
    public @ResponseBody FinanceAccount myFinanceAccount(HttpServletRequest request){
        //从session中获取用户信息
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);

        return financeAccountService.queryFinanceAcountByUid(sessionUser.getId());
    }

    //登录处理
    /*
        /loan/login
	public Object login
	request
	phone
	loginPassword

	//验证手机号格式

	//用户登录
	1.	查询用户
	2.	更新最近登录时间
	3.	参数；手机号，密码
	4.	返回，

	以上，则是control方法，快速梳理思路的步骤；
     */

    @RequestMapping("/loan/login")
    public @ResponseBody Object login(
            HttpServletRequest request,
            @RequestParam(value = "phone",required = true) String phone,
            @RequestParam(value = "loginPassword" , required = true) String loginPassword
    ){
        Map<String,Object> retMap = new HashMap<String,Object>();
        //验证手机号格式

        //用户登录
        /*
        返回值，User,Boolean int map
	上一次登录时间，要拿到；故map会更好；
	现在就先用user
         */
        User user = userService.login(phone,loginPassword);
        //判断用户是否存在
        /*
        判断用户是否存在
	if null
	put 用户名或密码输入有误，请重新输入；
	retmap
	//将用户信息存放在session

	set user  //这里不能再查询了，如查询的话则是最新的登录时间；

	put ok；
         */
        if (null == user) {
            retMap.put(Constants.ERROR_MESSAGE,"用户名或密码输入有误，请重新登录");
            return retMap;
        }
        //将用户信息存放在session；
        request.getSession().setAttribute(Constants.SESSION_USER,user);
        retMap.put(Constants.ERROR_MESSAGE,Constants.OK);



        return retMap;
    }

    //退出登录
    /*
    /loan/logout
	logout
	string
	request

	//让session失效，或者清除指定key的值
	request.getSession().invalidate();
	或者
	request.getSession().removeAttribute(Constants.SESSION_USER);

	return "index" 这是响应index.jsp
	return "redirect:/index"  这是重定向一个index请求；
	//@
     */
    @RequestMapping("/loan/logout")
    public String logout(HttpServletRequest request){
        //让session失效，或者清除指定key的值；
        request.getSession().invalidate();
        return "redirect:/index";
    }

    //登录页面的三个动态数据加载
    /*
    public object loadStat
	request

	//历史平均年化收益率
	his
	//平台注册总人数
	//累计投资金额
	map
     */
    @RequestMapping("/loan/loadStat")
    public @ResponseBody Object loadStat(){
        Map<String,Object> retMap = new HashMap<String, Object>();
        //历史平均年化收益率
        Double historyAverageRate = loanInfoService.queryHistoryAverageRate();
        //平台注册总人数
        Long allUserCount = userService.queryAllUerCount();
        //累计投资金额
        Double allBidMoney = bidInfoService.queryAllBidMoney();
        retMap.put(Constants.HISTORY_AVERAGE_RATE,historyAverageRate);
        retMap.put(Constants.ALL_USER_COUNT,allUserCount);
        retMap.put(Constants.ALL_BID_MONEY,allBidMoney);
        return retMap;
    }

    //进入用户中心
    @RequestMapping("/loan/myCenter")
    public String myCenter(
            HttpServletRequest request,
            Model model
    ){
        //从session中获取用户标识
        User sessionUser = (User) request.getSession().getAttribute(Constants.SESSION_USER);
        //根据用户标识获取账户可用余额；
        FinanceAccount financeAccount = financeAccountService.queryFinanceAcountByUid(sessionUser.getId());
        model.addAttribute("financeAccount",financeAccount);
        return "myCenter";
    }

}






var referrer = "";//登录后返回页面
referrer = document.referrer; //获取到当前页面之前页面的URL；
if (!referrer) {
	try {
		if (window.opener) {    //判断是否支持opener;返回父窗口，opener 属性是一个可读可写的属性，可返回对创建该窗口的 Window 对象的引用。
			// IE下如果跨域则抛出权限异常，Safari和Chrome下window.opener.location没有任何属性              
			referrer = window.opener.location.href;//获取到当前页面之前页面的URL；;使用window对象实现窗口跳转；
			/*
			 当使用window.open()打开一个窗口，你可以使用此opener属性返回来自目标窗口，源窗口的详细信息；
			 以上则是获取源窗口的url;
			 */
		}  
	} catch (e) {
	}
}

//按键盘Enter键即可登录
$(document).keyup(function(event){
	if(event.keyCode == 13){
		login();
	}
});



//验证手机号
/*
 //获取用户的手机号码
 var phone = $.trim($("#phone").val());
 if("" == phone)
 showId.html 请输入手机号；
 return false;

 else if
 手机号正则
 showId.html 请输入正确的手机号；
 false
 else
 showId.html ""

 true
 */
function checkPhone() {
    var phone = $.trim($("#phone").val());
    //获取用户的手机号码
    if("" == phone){
        $("#showId").html("请输入手机号码");
        return false;
    }else if (!/^1[1-9]\d{9}$/.test(phone)){
        $("#showId").html("请输入正确的手机号码");
        return false;
    }else {
        $("#showId").html("");
    }
    return true;
}

//验证密码
/*
 密码
 checkLoginPassword
 //获取登录密码
 var loginPassword =
 if("")
 showId html 请输入登录密码
 false
 html ""
 true
 */
function checkLoginPassword() {
    //获取登录密码
    var loginPassword = $.trim($("#loginPassword").val());
    if("" == loginPassword){
        $("#showId").html("请输入登录密码");
        return false;
    }else {
        $("#showId").html("");
    }
    return true;

}

//图形验证码
/*
 复制代码，基本一样；
 checkCaptcha
 showId.html
 */
function checkCaptcha() {
    // alert("开始验证图形验证码。。。");
    // 获取图形验证码
    var captcha = $.trim($("#captcha").val());
    var flag = true;
    if("" == captcha ) {
        $("#showId").html("请输入图形验证码");
        return false;
    } else{
        // alert("图形验证码前台通过，开始发到后台验证");
        $.ajax({
            url: "loan/checkCaptcha",
            type: "post",
            data: "captcha="+captcha,
            async: false,
            success:function(jsonObject){
                if(jsonObject.errorMessage == "OK"){
                    $("#showId").html("");
                    flag = true ;
                }else{
                    $("#showId").html(jsonObject.errorMessage);
                    flag = false ;
                }
            },
            error:function(){
                $("#showId").html("系统繁忙，请稍后重试...");
                flag = false;
            }



        });
        if(!flag) {
            return false;
        }
        return true;
    }
}

//登录操作
/*
 login
 alert(check())
 //获取用户登录的信息
 phone
 loginPassword

 //加密密码，再赋值给dom对象
 $().val($.md5(loginPassword))
 alert(val)
 */
function login() {
    alert("-denglu--");
    //获取用户登录的信息
    var phone = $.trim($("#phone").val());
    var loginPassword = $.trim($("#loginPassword").val());

    if(checkPhone()&&checkLoginPassword()&&checkCaptcha()){
        $("#loginPassword").val($.md5(loginPassword));
        alert($("#loginPassword").val());
		/*
		 ajax
		 url loan/login
		 type post
		 data phone,loginPassword
		 success
		 //验证成功之后，从哪来回哪去；
		 error
		 */
		$.ajax({
			url: "loan/login",
			type: "post",
			data: "phone="+phone+"&loginPassword="+$("#loginPassword").val(),
			success: function (jsonObject) {
				//验证成功后，从哪来会哪去；
				/*
				 if ok
				 window.location.href = referrer;
				 else
				 html 用户名或密码有误，请重新登录

				 系统繁忙
				 */
				if(jsonObject.errorMessage == "OK"){
					window.location.href = referrer;  //跳转上个请求路径
				}else {
					$("#showId").html("用户名或密码有误，请重新登录");
				}

            },
			error: function () {
                $("#showId").html("系统繁忙，请稍后重试...");

            }
		});
    }

}

//页面加载完，后加载login页面的3个动态数据
/*
 login.js
 //加载平台信息
 loadStat();

 ajax
 url /loan/loadStat
 get
 <span class="historyAverageRate"></span>
 <span allUserCount
 < allBidMoney
 关于如何把ajax的响应数据渲染到当前页面；//@

 $(".his").html(json.his);
 allUser
 allBid;
 */
$(function () {
    // alert("00");
    loadStart();
});

function loadStart() {
    $.ajax({
        url:"loan/loadStat",
        type:"get",
        success:function (jsonObejcet) {
            $(".historyAverageRate").html(jsonObejcet.historyAverageRate);
            $("#allBidMoney").html(jsonObejcet.allBidMoney);
            $("#allUserCount").html(jsonObejcet.allUserCount);
        }
    });
}
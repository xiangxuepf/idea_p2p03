
//验证手机号
function checkPhone(){
    // var nihao = "nihaoa"
    // alert("开始验证手机号码。。。");
	var flag = true;
    //获取用户的手机号；
    var phone = $.trim($("#phone").val());
    //trim是去掉两端多余的空格；
    if(""==phone){
        showError("phone","请输入正确的手机号码");
        return false;
        //终止并返回一个布尔值；
    }else if(!(/^1[1-9]\d{9}$/.test(phone))){
        showError("phone","请输入正确的手机号码");
        return false;
    }else{
    	// 隐藏之前输入错误手机号，而发起的错误
        hideError("phone");
        // alert("发送ajax请求验证是否重复");
        // alert("手机号码前台验证通过，开始发送给后台验证");
        $.ajax({
            url:"loan/checkPhone",
            type:"post",  //get是获取数据，post是提交数据；
            // data:"phone="+phone, // 1.拼接KV格式；2. 使用json格式；本例提交是phone=17012341223
            data:{"phone":phone},
            async:false,
            success:function(jsonObject){
                if(jsonObject.errorMessage=="OK"){
                    showSuccess("phone");
                    flag = true;
                }else{
                    showError("phone",jsonObject.errorMessage);
                    flag = false;
                }
            },
            error:function(){
                showError("phone","系统繁忙，请稍后重试。。。");
                flag = false;
            }

        });
        return flag;
    }

}


//验证密码
function checkLoginPassword() {
    // alert("---");
    //获取用户输入的密码；
    var loginPassword = $.trim($("#loginPassword").val());
    var replayLoginPassword = $.trim($("#replayLoginPassword").val());

    if("" == loginPassword){
        showError("loginPassword","请输入登录密码");
        return false;
    }else if(!/^[0-9a-zA-Z]+$/.test(loginPassword)){
        //密码字符只可使用数字和大小写英文字母^[0-9a-zA-Z]+$

        showError("loginPassword","密码只支持数字和大小英文字母");
        return false;

    }else if(!/^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*/.test(loginPassword)){
        //密码应同时包含英文或数字^(([a-zA-Z]+[0-9]+)|([0-9]+[a-zA-Z]+))[a-zA-Z0-9]*

        showError("loginPassword","密码应同时包含英文和数字");
        return false;

    }else if(loginPassword.length < 6 || loginPassword.length > 20){
        showError("loginPassword","密码长度应为6-20位");
        return false;
    }
    else{
        showSuccess("loginPassword");
    }
    //友好提示用户不要忘记再次输入密码；

    if(replayLoginPassword != loginPassword){
        showError("replayLoginPassword","两次密码输入不一致");
    }
    // 只要走到showSuccess分支，都返回true
    return true;

}

// 验证确认密码
function checkLoginPasswordEqu(){

    //获取用户输入的密码
    //有人会先输入确认密码；
    var loginPassword = $.trim($("#loginPassword").val());
    var replayLoginPassword = $.trim($("#replayLoginPassword").val());
    if("" == loginPassword){
        showError("loginPassword","请输入登录密码");
        return false;
    }else if("" == replayLoginPassword){
        showError("replayLoginPassword","请输入确认登录密码");
        return false;
    }else if(replayLoginPassword != loginPassword){
        showError("replayLoginPassword","两次输入密码不一致");
        return false;
    }else{
        showSuccess("replayLoginPassword");
    }
    return true;

}

// 验证图形验证码
function checkCaptcha() {
    // alert("开始验证图形验证码。。。");
    // 获取图形验证码
    var captcha = $.trim($("#captcha").val());
    var flag = true;
    if("" == captcha ) {
        showError("captcha","请输入图形验证码");
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
                    showSuccess("captcha");
                    flag = true ;
                }else{
                    showError("captcha",jsonObject.errorMessage);
                    flag = false ;
                }
            },
            error:function(){
                showError("captcha","系统繁忙，请稍后重试..");
                flag = false;
            }



        });
        if(!flag) {
            return false;
        }
        return true;
    }
}

//注册
function register() {
    //获取用户注册的参数
    var phone = $.trim($("#phone").val());
        var loginPassword = $.trim($("#loginPassword").val());
    var replayLoginPassword = $.trim($("#replayLoginPassword").val());
     //验证每个函数都得通过；
    if(checkPhone()&&checkLoginPassword()&&checkLoginPasswordEqu()&&checkCaptcha() ){
        $("#loginPassword").val($.md5(loginPassword));
        $("#replayLoginPassword").val($.md5(replayLoginPassword));
        $.ajax({
            url: "loan/register",
		type: "post",
            data: {
            "phone" :phone,
                "loginPassword" : $.md5(loginPassword),
                "replayLoginPassword" : $.md5(replayLoginPassword)

        } ,
        success: function(jsonObject){
                if(jsonObject.errorMessage == "OK"){
                    //跳转到实名认证页面；
                    window.location.href = "realName.jsp";

                }else{
                    showError("captcha",jsonObject.errorMessage);
                }

        },
        error: function(){
            showError("captcha","系统繁忙，请稍后重试。。");
        }

    });
    }
}


//错误提示
function showError(id,msg) {
	// 把ok的id隐藏
	$("#"+id+"Ok").hide();
	//把Err的id追加一些信息；
	$("#"+id+"Err").html("<i></i><p>"+msg+"</p>");
	//把Err的id显示
	$("#"+id+"Err").show();
	//在父id追加样式；
	$("#"+id).addClass("input-red");
}
//错误隐藏
function hideError(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id).removeClass("input-red");
}
//显示成功
function showSuccess(id) {
	$("#"+id+"Err").hide();
	$("#"+id+"Err").html("");
	$("#"+id+"Ok").show();
	$("#"+id).removeClass("input-red");
}

//注册协议确认
$(function() {
	$("#agree").click(function(){
		var ischeck = document.getElementById("agree").checked;
		if (ischeck) {
			$("#btnRegist").attr("disabled", false);
			$("#btnRegist").removeClass("fail");
		} else {
			$("#btnRegist").attr("disabled","disabled");
			$("#btnRegist").addClass("fail");
		}
	});
});

//打开注册协议弹层
function alertBox(maskid,bosid){
	$("#"+maskid).show();
	$("#"+bosid).show();
}
//关闭注册协议弹层
function closeBox(maskid,bosid){
	$("#"+maskid).hide();
	$("#"+bosid).hide();
}
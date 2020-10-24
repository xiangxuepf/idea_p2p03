//微信二维码关闭界面
function closeEwm(divId) {
	document.getElementById(divId).style.display = 'none';
	document.getElementById('dialog').style.display = 'none';
}
//微信二维码弹出界面
function displayEwm(divId) {
	document.getElementById('dialog').style.display = 'block';
	document.getElementById(divId).style.display = 'block';
}
/**
 * 媒体,公告，关于我们，联系我们等左侧菜单
 * @param cc 需要到div的id
 * @param selected 选中的菜单id
 */
function getLeftMenuxmlHttpAjax(cc,selected) {
	var xmlHttp = getRequest();
	xmlHttp.open("POST","../index/leftmenu.jsp",false);//没有参数且不会发生改变的用GET，否则用POST
	/*
	 false就是等待有返回数据的时候再继续往下走，还没有得到数据的时候就会卡在那里，直到获取数据为止。
	 true就是不等待,直接返回，这就是所谓的异步获取数据！
	 */
	xmlHttp.setRequestHeader("content-type","application/x-www-form-urlencoded");
	xmlHttp.onreadystatechange = function(){  //每当 readyState 改变时，就会触发 onreadystatechange 事件
		if(xmlHttp.readyState == 4) { // 4 请求已完成，且响应已就绪;
			if(xmlHttp.status == 200) {
			    var responseText = xmlHttp.responseText; //将响应信息作为字符串返回.只读
				var finance = document.getElementById(cc);
				if(finance != null && responseText.length != 0){
					//responseText替换html代码
					finance.innerHTML = responseText;  // finance.innerHTML 获取dom对象finance的html代码；
					$("#"+selected).addClass("current"); //addClass() 方法向被选元素添加一个或多个类;
				}else{

				}
			}
		}
	};
	xmlHttp.send(null);
	/*

	 （1）xmlhttp的send是传递参数用的,但是只有在使用post方式提交请求的时候才有用
	 如下:
	 xmlhttp.open("post",url,true);
	 xmlhttp.send(url);
	 （2）用get的话一般就是：
	 xmlhttp.open("get",url,true);

	 */
}

function getMassge(url,name,code){
	var showmsg = "";
	$.ajax({
			 url:""+url,
		     dataType: "text",
		     async: false,
		     data:"name="+name+"&code="+code,
			 success: function(msg){
				 showmsg = msg;
			 },
			 error:function(){
				showmsg = "错误";
			 }
	    });
	return showmsg;
}
//登录后个人信息提示
function firstLogin(){
    var userphone = $(".user-phone").text();
    //RegExp 对象表示正则表达式，它是对字符串执行模式匹配的强大工具
    var reg=new RegExp("(^| )isConfrimed_"+userphone+"=([^;]*)(;|$)");
    /*
	 使用 JavaScript 创建Cookie
	 document.cookie="username=John Doe; expires=Thu, 18 Dec 2043 12:00:00 GMT; path=/";
	 username=John Doe是cookie的kv; username就是cookie的名；
	 expires过期时间；
	 path cookie路径；

	 使用 JavaScript 读取 Cookie
	 var x = document.cookie;
     */
    var arr = document.cookie.match(reg);
    var arr2 = null;
    if(window.localStorage){ //if(! window.localStorage)我们需要判断浏览器是否支持 localStorage 这个属性：
    	arr2 = window.localStorage.getItem("isConfrimed_"+userphone);
	}
    if( (arr==null || arr[2] != 'isConfrimed') && arr2!='isConfrimed'  ){
    	$('body').addClass('personal-prompt');
        $('.personal-prompt .userinfo-up').show();
    }
    $('body').click(function(){
		$('body').removeClass('personal-prompt');
		document.cookie="isConfrimed_"+userphone+"=isConfrimed;path=/;domain=jinxin99.cn;expires=Fri, 31 Dec 9999 23:59:59 GMT";
		if(window.localStorage){
			window.localStorage.setItem("isConfrimed_"+userphone,"isConfrimed");
		}
    });
}
//头部账户效果
$(function(){
	if($(".user-phone").text() != null && $(".user-phone").text() != ''){
		firstLogin();
	}
	//getAccount();
	//页头页脚用到的js,程序写页面时无需在此页面加载下面的js，只需在页头页脚独立文件加载即可
	//手机客户端下拉层
	$(".phone-ewm").hover(function(){
		$(this).addClass('phone-ewm-hover');
	},function(){
		$(this).removeClass('phone-ewm-hover');
	});
	//个人信息下拉
	/*
	 发送ajax
	 url /loan/myFinanceAccount
	 type get
	 success:
	 frame_top.html()  //html()使用；  //@
	 jsonObject.availableMoney;
	 */
	$(".logged").hover(function(){
		$.ajax({
			url: "/p2p/loan/myFinanceAccount",
			type: "get",
			success: function (jsonObject) {
				$("#frame_top").html(jsonObject.availableMoney);
            }
		});
		$(this).addClass("logged-hover");
		// $("#box").stop();停止当前正在运行的动画：
		// $(selector).animate(styles,speed,easing,callback) 改变 "div" 元素的高度：
		$(".userinfo-drop-down",this).stop().animate({ height: '205px'},300);
	},function(){
		$(".userinfo-drop-down",this).stop().animate({ height: '0px'},300,function(){$(".logged").removeClass("logged-hover");});
	});
	//二维码弹出层显示隐藏
	$(".head-weixin").click(function(){
		$("#dialog,#ewm").fadeIn();
	});
	$("#ewm .close").click(function(){
		$(this).parent().fadeOut(); //fadeOut();使用淡出效果来隐藏一个 <p> 元素：
		$("#dialog").fadeOut();
	});
	//页脚友情链接
	var linksHeight=$(".links-list").innerHeight();
	$(".links-list").css({height:'30px'}); //jQuery css() 方法css() 方法设置或返回被选元素的一个或多个样式属性;
	$("#links-down").click(function(){
		if($(this).hasClass('links-up')){
			$(this).removeClass("links-up");
			$(this).prev().stop().animate({ height: '30px'},500);
		}else{
			$(this).addClass("links-up");
			$(this).prev().stop().animate({ height: linksHeight},500);
		}
	});
	//关闭温馨提示
	$(".footer-hint .hint-close").click(function(){
		$(this).parent().fadeOut();
	});
	//返回顶部
	$(window).scroll(function(){//滚动监听器，滚一次触发一次；
		if ($(window).scrollTop()>100){
			$("#back-to-top").fadeIn(1500);//使用淡入效果来显示一个隐藏的 <p> 元素：
		}
		else
		{
			$("#back-to-top").fadeOut(1500);
		}
	});
	//当点击跳转链接后，回到页面顶部位置
	$("#back-to-top").click(function(){
		$('body,html').animate({scrollTop:0},100);
		return false;
	});

	//getMessageNum();
	if($('.user_name,.user-phone').html() != null && $('.user_name,.user-phone').html() != "" && (location.href.indexOf("/account/") == 0 || location.href.indexOf("/cashbox/") == 0)){
		  var url = $(".urlPath").val();
		  $.ajax({
				 type:"POST",
				 url:url+"/user/getMember.do?myTime=" + new Date(), //
			     dataType: "text",
			     async: false,
				 success: function(msg){
					 if(msg != null && msg != ''){
					 	/*
						 eval() 函数可计算某个字符串，并执行其中的的 JavaScript 代码。
						 通过计算 string 得到的值（如果有的话）。
						 eval("x=10;y=20;document.write(x*y)") 输出200
					 	 */
						 var json = eval('(' + msg + ')'); //封装成json对象
						 if(json.level == "0"){
							 $('#member,#member_over').html("普通会员");
						 }else if(json.level == "1"){
							 $('#member,#member_over').html("VIP1");
						 }else if(json.level == "2"){
							 $('#member,#member_over').html("VIP2");
						 }else if(json.level == "3"){
							 $('#member,#member_over').html("VIP3");
						 }else if(json.level == "4"){
							 $('#member,#member_over').html("VIP4");
						 }
					 }else{
						 $('#member,#member_over').html("普通会员");
					 }
		 		  },
				error:function(){
					//alert("页面加载失败，请重试！");
				  }
		   });
	  }
	
});
/*function getAccount(){
	var url = $(".urlPath").val();
	$.post(url+"/account/getAvailableMoney.do",function(data){
		$("#frame_top").html(data);
		$("#frame_top").show();
		$("#frame_top").next('img').hide();
	},"text");
}*/
function getMessageNum(){
	var url = $(".urlPath").val();
	if(url == undefined){
		return;
	}
	$.post(url+"/account/getMessageNum.do",function(data){
		if(0<data){
			if(data>99)data=9+"+";
			$(".head_message").removeClass("no_message");
			$(".messageNum").html(data);
			$(".messageNum").show();
		}
	},"text");
}
//实名认证  交易密码  start
function popupComplete(path,conpath,type){
	$.ajax({
		 type:"POST",
		 url:conpath+"/ssl/completePopup.do?type="+type, //查看用户欲往操作   0投 1充 2提  3绑卡 4好易联充值
	     dataType: "text",
	     async: false,
		 success: function(data){
			 if(data == 'complete' ){
				 if(type == '4'){
				 }else{
					 window.location.href =path;
				 }
			    }else if( data == 'login'){
			    	window.location.href = "../../webPage/ssl/login.html";
			    }else{
			    	popupCompleteMsg(data,0,conpath,type);
			    }
		  },
		error:function(){
			//alert("页面加载失败，请重试！");
		  }
  });
}

//实名认证  交易密码  start
function popupCompleteindex(path,conpath,type){
	$.ajax({
		 type:"POST",
		 url:conpath+"/ssl/completePopup.do?type="+type, //查看用户欲往操作   0投 1充 2提  3绑卡 4好易联充值
	     dataType: "text",
	     async: false,
		 success: function(data){
			 if(data != null && data == 'complete'){
				 if(type == '4'){
				 }else{
					 window.location.href =path;
				 }
			 }else if(data != null && data == 'login'){
				 window.location.href = "../ssl/login.html";
			 }else{
				popupCompleteMsg(data,0,conpath,type);
			 }
		  },
		error:function(){
			//alert("页面加载失败，请重试！");
		  }
  });
}

function popupCompleteMsg(type,status,conpath,msg) {
	//type 是实名认证还是交易密码    status 成功还是尚未开始   msg 0投 1充 2提 3绑卡  4好易联充值
	//dateObject.getTime()getTime() 方法可返回距 1970 年 1 月 1 日之间的毫秒数。
	var myTime = new Date().getTime();
	$.ajax({
		 type:"POST",
		 url:conpath+"/ssl/completeMsg.do?msg="+msg, //
	     dataType: "text",
	     async: false,
		 success: function(data){
		 	// $("p").append(" <b>Hello world!</b>");在每个 p 元素结尾插入内容：
			 $('body').append(data);
			 $("#earnMaskMsg").show();
			 $("#"+type).show();
			 
			 var vHeight=$("#"+type).innerHeight();//返回 <div> 元素的内部高度：
			 $("#"+type).css({"marginTop":-vHeight/2});
			 $(".queding").click(function(){
			 	$("#earnMaskMsg").remove();
			 	$("#earnMsg").remove();
			 	if(msg == "4" && status != "1"){
			 		//重新跳转到充值页面
			 		window.location.href ="../../ssl/cashbox/getRecharge.do?mytime="+myTime;
			 	}else if(status == "1"){
			 		window.location.href ="../../ssl/cashbox/AccountSet.do?mytime="+myTime;
			 	}
			 });	
			 if(msg == "0"){
			 	// $("img").attr("width","180");改变图像的 width 属性：
				 $(".dobeforething").attr("href","../../webPage/invest/wytz.html");
			 }else if(msg == "1"){
				 $(".dobeforething").attr("href","../../ssl/cashbox/getRecharge.do?mytime="+myTime);
			 }else if(msg == "2"){
				 $(".dobeforething").attr("href","../../ssl/cashbox/getBankAccountlist.do?mytime="+myTime);
			 }else if(msg == "3"){
				 $(".dobeforething").attr("href","../../ssl/cashbox/getBankAccountlist.do?mybank=true&mytime="+myTime);
			 }else if(msg == "4"){
				 $(".dobeforething").attr("href","../../ssl/cashbox/getRecharge.do?mytime="+myTime);
			 }else if(msg == "5"){
				 $(".dobeforething").attr("href","../../webPage/goldtip/jinzhi.html");
			 }else if(msg == "6"){//散标投资
				 $(".dobeforething").attr("href","../../webPage/invest/standard_list.html");
			 }else if(msg == "7"){
	//			 $(".dobeforething").attr("href","../../");
				 $(".dobeforething").attr("href","../../webPage/index/index.html");
			 }else{
				 $("#earnMaskMsg").remove();
				 $("#earnMsg").remove();
			 }
		 },
		error:function(){
			//alert("页面加载失败，请重试！");
		  }
 });
}
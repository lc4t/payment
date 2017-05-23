<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String appid = (String) request.getSession().getAttribute("appid");
	String account = (String) request.getSession().getAttribute(
			"account");
	String uid = (String) request.getSession().getAttribute("uid");
	String packagename = (String) request.getSession().getAttribute(
			"packagename");
	String productid = (String) request.getSession().getAttribute(
			"appid");
	String productname = (String) request.getSession().getAttribute(
			"productname");
	String amount = (String) request.getSession()
			.getAttribute("amount");
	String channel = (String) request.getSession().getAttribute(
			"channel");
	String productdesc = (String) request.getSession().getAttribute(
			"productdesc");
	String cbgameurl = (String) request.getSession().getAttribute(
			"cbgameurl");
	String cburl = (String) request.getSession().getAttribute("cburl");
	String serverName = (String) request.getSession().getAttribute(
			"serverName");
	String money = String.valueOf(request.getSession().getAttribute(
			"money"));
%>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport"
	content="width=device-width, initial-scale=1.0, maximum-scale=1">
<title>《空中网》</title>
<link rel="icon" href="http://www.kongzhong.com/favicon.ico"
	type="image/x-icon">
<script type="text/javascript" src="jquery-1.8.2.min.js"></script>
<script>
	(function() {
		var phoneWidth = parseInt(window.screen.width), phoneScale = phoneWidth / 640, ua = navigator.userAgent;

		if (/Android (\d+\.\d+)/.test(ua)) {
			var version = parseFloat(RegExp.$1);
			if (version > 2.3) {
				document
						.write('<meta name="viewport" content="width=640, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
			} else {
				document
						.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
			}

		} else {
			document
					.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
		}
	})();
</script>
<style type="text/css">
@charset "utf-8";

* {
	margin: 0;
	padding: 0;
	border: none;
}

body,div,dl,dt,dd,ul,ol,li,h1,h2,h3,h4,h5,h6,pre,form,fieldset,input,textarea,p,blockquote
	{
	margin: 0;
	padding: 0;
}

ul,ul li {
	list-style: none
}

a {
	font-family: "微软雅黑", Arial, Helvetica, sans-serif;
	text-decoration: none;
	color: #272727;
}

table {
	border-collapse: collapse;
}

em {
	font-style: normal
}

img {
	border: 0;
	vertical-align: top;
}

body {
	font-family: "微软雅黑", Verdana, Arial, Helvetica, sans-serif;
	font-size: 20px;
	color: #474646;
	line-height: 18px;
}

.l {
	float: left;
}

.r {
	float: right;
}

.re {
	position: relative;
}

.ab {
	position: absolute;
}

.r0 {
	margin-right: 0;
}

.clearfix {
	clear: both;
}

.container {
	max-width: 600px;
	margin: 0px auto;
}

.title {
	border-radius: 10px 10px 0px 0px;
	height: 83px;
	background: #ff8e00;
	color: white;
	font-size: 33px;
	line-height: 83px;
	text-align: center;
	position: relative;
}

.title span {
	position: absolute;
	left: 16px;
	top: 24px;
}

.title span a {
	display: block;
}

.main {
	line-height: 38px;
	font-size: 22px;
	padding: 15px 20px;
}

.mainleft {
	float: left;
}

.mainright {
	float: right;
	text-align: center;
}

.conner {
	background: #f1f1f1;
	border-radius: 0px 0px 10px 10px;
	padding: 0px 20px 20px 20px;
}

.conner p {
	padding: 20px 0 15px 0;
}

.conner li {
	height: 72px;
}

.conner li a {
	display: block;
	height: 61px;
	background: white;
	padding: 5px 20px 0 20px;
}

.conner li a span {
	float: right;
	padding-top: 15px;
}

.service {
	padding: 20px 0 5px 0;
}
</style>

</head>

<body>

	<br>
	<div class="container">
		<div class="title">
			<span><a href="#"><img src="images/recharge_01.png"></a></span>
			支付中心
		</div>
		<div class="main">
			<div class="mainleft">
				当前账号 :<br> 所在服务器 :<br> 商品名称 :<br> 支付金额 :
			</div>
			<div class="mainright">
				${account}<br> ${serverName}<br> ${productname}<br>
				${money}
			</div>
			<div class="clearfix"></div>
		</div>
		<div class="conner">
			<p>请选择支付方式：</p>
			<ul>
				<li><a onclick="pay('now')"><span><img
							src="images/recharge_02.png"></span><img
						src="images/recharge_w.png"></a></li>
				<li><a onclick="pay('zfb')" href="javascript:void(0)"><span><img
							src="images/recharge_02.png"></span><img
						src="images/recharge_z.png"></a></li>
			</ul>
			<div class="service">充值遇到问题，请联系客服QQ：88888888</div>
		</div>
		<div class="info">
			<input type="hidden" id="appid" value="${appid}" /> <input
				type="hidden" id="uid" value="${uid}" /> <input type="hidden"
				id="packagename" value="${packagename}" /> <input type="hidden"
				id="amount" value="${amount}" /> <input type="hidden" id="channel"
				value="${channel}" /> <input type="hidden" id="productdesc"
				value="${productdesc}" /> <input type="hidden" id="cbgameurl"
				value="${cbgameurl}" /> <input type="hidden" id="cburl"
				value="${cburl}" /> <input type="hidden" id="productid"
				value="${productid}" /> <input type="hidden" id="productname"
				value="${productname}" />

		</div>
	</div>
	<script type="text/javascript">
		// 支付宝支付
		function pay(type) {
			var appid = $("#appid").val();
			var uid = $("#uid").val();
			var packagename = $("#packagename").val();
			var amount = $("#amount").val();
			var channel = $("#channel").val();
			var productdesc = $("#productdesc").val();
			var cbgameurl = $("#cbgameurl").val();
			var cburl = $("#cburl").val();
			var productid = $("#productid").val();
			var productname = $("#productname").val();
			var url = "/PaymentKong/pay/submit.jsp?appid=" + appid + "&uid="
					+ uid + "&packagename=" + packagename + "&amount=" + amount
					+ "&channel=" + channel + "&productdesc=" + productdesc
					+ "&cbgameurl=" + cbgameurl + "cburl=" + cburl
					+ "&productid=" + productid + "&productname=" + productname
					+ "&type=" + type;
			alert(type);
			window.location.href = url;

		}
	</script>
</body>

</html>

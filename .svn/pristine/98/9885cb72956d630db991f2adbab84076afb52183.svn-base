<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String payurl = String.valueOf(request.getSession().getAttribute(
			"payurl"));
	String mhtOrderNo =String.valueOf(request.getSession().getAttribute(
			"mhtOrderNo"));
	String url =String.valueOf(request.getSession().getAttribute(
			"url"));
	System.out.println(";;;;;;;;;;;;;;;;;;;;" + payurl);
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
<script type="text/javascript" src="qrcode.js"></script>
<script type="text/javascript" src="jquery.qrcode.js"></script>
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

.payment {
	background: white;
	text-align: center;
	padding-bottom: 60px;
	border-radius: 0px 0px 10px 10px;
	color: #393938;
	line-height: 290px;
	font-size: 42px;
}
</style>

</head>

<body>

	<div class="container">
		<div class="title">
			<span><a href="#"><img src="images/recharge_01.png"></a></span>
			支付中心
		</div>
		<div class="payment">支付处理中，请稍候 ... ...</div>
	</div>
	<div id="code"></div>
	<script type="text/javascript">
		$(document).ready(function() {
		window.open("${payurl}");     
		    window.setInterval(showalert, 8000); 
    var time=0;
function showalert() 
{ 
 $.ajax({
                    url:"${url}/now", 
                    data:{"model":"4",
                    "orderId":"${mhtOrderNo}"
                    },    
                    type:"POST", 
                    dataType:"json",   
    
 			success:function(result){
                        if(result.status=="fail"){
                        window.location.href="${url}/pay/payfail.jsp";
                        } 
                        if (result.status=="success"){
                          window.location.href="${url}/pay/phoneResult.jsp";
                        }
  					if (result.status=="wait"){
                         time=time+1; 
                         if(time>=30){
 							window.location.href="${url}/pay/payfail.jsp";
                         }
                        }
                        }            
        });
} 
		});
	</script>
</body>

</html>

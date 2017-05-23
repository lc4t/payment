<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html>
<html lang="zh-cn">
<head>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1">
<title>《空中网》</title>
<link rel="icon" href="http://www.kongzhong.com/favicon.ico" type="image/x-icon">
<script>
		(function(){
			var phoneWidth = parseInt(window.screen.width),
				phoneScale = phoneWidth/640,
				ua = navigator.userAgent;

			if (/Android (\d+\.\d+)/.test(ua)){
				var version = parseFloat(RegExp.$1);
				if(version > 2.3){
					document.write('<meta name="viewport" content="width=640, minimum-scale = '+phoneScale+', maximum-scale = '+phoneScale+', target-densitydpi=device-dpi">');
				}else{
					document.write('<meta name="viewport" content="width=640, target-densitydpi=device-dpi">');
				}

			} else {
				document.write('<meta name="viewport" content="width=640, user-scalable=no, target-densitydpi=device-dpi">');
			}
		})();
		</script>
<style type="text/css">
@charset "utf-8";
*{margin:0;padding:0;border:none;}
body,div,dl, dt, dd, ul, ol, li, h1, h2, h3, h4, h5, h6, pre, form, fieldset, input, textarea, p, blockquote {margin:0;padding:0;}
ul,ul li{ list-style:none}
a {font-family: "微软雅黑",Arial,Helvetica,sans-serif;text-decoration: none;color:#272727;}
table{border-collapse:collapse;}
em{font-style:normal}
img{border:0;vertical-align: top;}
body {font-family:"微软雅黑", Verdana, Arial, Helvetica, sans-serif;font-size:20px;color:#474646; line-height:18px;}
.l{float: left;}
.r{float: right;}
.re{position: relative;}
.ab{position: absolute;}
.r0{margin-right: 0;}
.clearfix{
	 clear:both;
}
.container{
 max-width:600px;margin:0px auto;
}
.title{border-radius:10px 10px 0px 0px;
	height:83px; background:#ff8e00;color:white; font-size:33px;line-height:83px; text-align:center;position:relative;
}
.title span{
	position:absolute; left:16px;top:24px;
}
.title span a{
	display:block;
}
.state{ background:white; text-align:center; padding-bottom:60px;
	border-radius:0px 0px 10px 10px; color:#393938;
}
.state p{
	line-height:290px; font-size:72px;
}
.input a{
	width:277px;height:80px;background:url('images/recharge_in.png'); display:inline-block; color:white; font-size:28px;line-height:80px;
}
.input a:hover{
	background:url('images/recharge_intwo.png');
}
</style>
		
</head>

<body>
<div class="container">
	<div class="title">
		<span><a href="#"><img src="images/recharge_01.png"></a></span>
		支付中心
	</div>
	<div class="state">
		<p>支付成功</p>
		<div class="input">
			<a href="#">返回游戏</a>
		</div>
	</div>		
</div>
</body>

</html>

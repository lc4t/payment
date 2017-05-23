<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
  <head>
    <base href="<%=basePath%>">
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->

  </head>
  
  <body>
    <a href="http://localhost:8080/PaymentKong/pay">进入支付中心</a>
    <form action="/PaymentKong/pay" method="POST">
    appid:<input type="text" name="appid" value="appid"/><br/>
    uid:<input type="text" name="uid" value="123123"/><br/>
    packagename:<input type="text" name="packagename" value="123123"/><br/>
    amount:<input type="text" name="amount" value="0.1"/><br/>
    channel:<input type="text" name="channel" value="123123"/><br/>
    productdesc:<input type="text" name="productdesc" value="123123"/><br/>
    cbgameurl:<input type="text" name="cbgameurl" value="http://www.baidu.com"/><br/>
    cburl:<input type="text" name="cburl" value=""/><br/>
    productid:<input type="text" name="productid" value="123123"/><br/>
    productname:<input type="text" name="productname" value="123123"/><br/>
    <button type="submit" value="">提交</button>
    </form>
  </body>
</html>

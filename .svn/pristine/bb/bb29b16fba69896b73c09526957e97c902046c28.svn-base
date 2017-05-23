<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.mycardtw.MyCardTWCharge" %>
<%@ page import="noumena.payment.mycardtw.MyCardTWParams" %>

<%
/*String ua = request.getHeader("User-Agent");
if (ua != null)
{
	String mua = ua.toLowerCase();
	System.out.println("ua-->" + mua);
	if (mua.indexOf("ipad") >= 0 || mua.indexOf("iphone") >= 0 || mua.indexOf("android") >= 0)
	{
		//mobile
		response.sendRedirect("phone.jsp");
	}
}*/
	String url = "";
	if (MyCardTWCharge.isTestmode() == true)
	{
		url = MyCardTWParams.INGAME_WEB_CHARGE_SERVICE_TEST;
	}
	else
	{
		url = MyCardTWParams.INGAME_WEB_CHARGE_SERVICE_RELEASE;
	}
	url += "?model=10";
	url += "&orderid=" + request.getParameter("orderid");
	url += "&facId=" + request.getParameter("facId");
	url += "&authCode=" + request.getParameter("authCode");
	url += "&facMemId=" + request.getParameter("facMemId");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MyCard Ingame</title>
</head>
<body>
	<form id="form" action="<%=url%>" method="post">
		卡號：<input id="cardId" name="cardId" size="20"><p>
		密碼：<input type="password" id="cardPwd" name="cardPwd" size="20"><p>
		<input type="submit" value="提交">
	</form>
</body>
</html>

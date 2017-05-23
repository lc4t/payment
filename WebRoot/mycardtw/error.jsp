<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.net.URLDecoder"%>
<%@page import="java.util.Vector"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.mycardtw.MyCardTWCharge" %>
<%@ page import="noumena.payment.mycardtw.MyCardTWParams" %>

<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	String errormsg = URLDecoder.decode(request.getParameter("errormsg"), "utf-8");
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MyCard Billing Error</title>
</head>

<body>
	<%=errormsg %>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.vo.PayGameVO" %>
<%@ page import="noumena.payment.vo.WebConvertVO" %>
<%@ page import="noumena.payment.model.PayServer" %>
<%@ page import="java.util.List" %>

<%
PayGameBean bean = new PayGameBean();
List<PayGameVO> list = bean.getPayGameVOs();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>测试回调</title>
</head>

<body>

<%
	String uid = request.getParameter("uid");
	String sid = request.getParameter("sid");
	
	System.out.println("uid->" + uid);
	System.out.println("sid->" + sid);
%>

Call Back TEST!!!!<br>
UID---><%=uid%><br>
SIP---><%=sid%>

</body>
</html>

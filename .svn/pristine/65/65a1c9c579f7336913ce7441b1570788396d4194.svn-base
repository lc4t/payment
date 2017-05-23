<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="noumena.payment.gash.GASHCharge"%>
<%@page import="noumena.payment.gash.GASHParams"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.vo.PayGameVO" %>
<%@ page import="noumena.payment.vo.WebConvertVO" %>
<%@ page import="noumena.payment.model.PayServer" %>
<%@ page import="noumena.payment.util.Constants" %>
<%@ page import="java.util.List" %>
<%@ page import="java.net.URLEncoder" %>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>空中网GASH充值</title>

<script type="text/javascript">
<!--
function submitform()
{
	var form = document.getElementById("form1");
	form.submit();
}
-->
</script>

</head>

<body onload="submitform()">
	
    <%
    	String senddata = request.getParameter("senddata");
		System.out.println("GASH web pay senddata->" + senddata);
    	String tradeurl = "";
    	
    	if (GASHCharge.isTestmode() == true)
    	{
    		tradeurl = GASHParams.GASH_TRADE_URL_TEST;
    	}
    	else
    	{
    		tradeurl = GASHParams.GASH_TRADE_URL_RELEASE;
    	}
    %>
    
    ......
	<form id="form1" action="<%=tradeurl %>" method="post">
		<input type="hidden" name="data" value="<%=senddata %>">
	</form>
</body>
</html>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
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
<title>空中网充值</title>

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
    	String payTypeId = request.getParameter("payTypeId");
		String payId = request.getParameter("payId");
		String paytime = request.getParameter("paytime");
		String gameid = request.getParameter("gameid");
		String areaid = request.getParameter("areaid");
		String serverid = request.getParameter("serverid");
		String imei = request.getParameter("imei");
		String accountid = request.getParameter("accountid");
		String roleid = request.getParameter("roleid");
		String payprice = request.getParameter("payprice");
		String paymemo = request.getParameter("paymemo");
		String sign = request.getParameter("sign");
    %>
    
    ......
	<form id="form1" action="<%=Constants.PAY_PHONE_URL %>" method="post">
		<input type="hidden" name="payTypeId" value=<%=payTypeId %>></input><br>
		<input type="hidden" name="payId" value=<%=payId %>></input><br>
		<input type="hidden" name="paytime" value=<%=paytime %>></input><br>
		<input type="hidden" name="gameid" value=<%=gameid %>></input><br>
		<input type="hidden" name="areaid" value=<%=areaid %>></input><br>
		<input type="hidden" name="serverid" value=<%=serverid %>></input><br>
		<input type="hidden" name="imei" value=<%=imei %>></input><br>
		<input type="hidden" name="accountid" value=<%=accountid %>></input><br>
		<input type="hidden" name="roleid" value=<%=roleid %>></input><br>
		<input type="hidden" name="payprice" value=<%=payprice %>></input><br>
		<input type="hidden" name="paymemo" value=<%=URLEncoder.encode(paymemo, "utf-8") %>></input><br>
		<input type="hidden" name="sign" value=<%=sign %>></input><br>
	</form>
</body>
</html>

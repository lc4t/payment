<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayServerBean" %>
<%@ page import="noumena.payment.model.PayServer" %>

<%
String serverid = request.getParameter("serverid");
PayServerBean bean = new PayServerBean();
PayServer vo = bean.get(serverid);
if (vo == null) {
	response.sendRedirect("index.jsp");
	return;
}
%>
<html>
  <head>
    
    <title>My JSP 'server.jsp' starting page</title>
    
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
    <form action="updateserver.jsp" method="post">
    	<input type="hidden" name="serverid" value="<%=serverid%>">
    	<table border="0" align="center" cellpadding="0" cellspacing="0">
    		<tr><td>&#160服务器名称：</td><td><input type="text" name="servername" value="<%=vo.getServerName() %>"/></td></tr>
    		<tr><td>&#160回调URL：</td><td><input type="text" name="serverurl" value="<%=vo.getCallbackUrl() %>"/></td></tr>
    		<tr><td>&#160验证URL：</td><td><input type="text" name="url" value="<%=vo.getCheckUrl() %>"/></td></tr>
    		<tr><td>&#160通知URL：</td><td><input type="text" name="paynotify" value="<%=vo.getPayNotify() %>"/></td></tr>
    		<tr><td colspan="2" align="center"><button>提交</button></td></tr>
    	</table>
    </form>
  </body>
</html>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.vo.PayGameVO" %>
<%@ page import="noumena.payment.vo.PayServerVO" %>
<%@ page import="noumena.payment.vo.WebConvertVO" %>
<%@ page import="noumena.payment.model.PayServer" %>
<%@ page import="java.util.List" %>

<%
PayGameBean bean = new PayGameBean();
String gameid = request.getParameter("gameid");
if (gameid == null) {
	response.sendRedirect("index.jsp");
	return;
}
PayGameVO vo = bean.getGameVO(gameid);
List<PayServerVO> list = vo.getServers();
List<WebConvertVO> list2 = vo.getConverts();
%>

<html>
  <head>
    
    <title>My JSP 'game.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
	var serverDiv = [];
	var convertDiv = [];
	var select = null;
	var convert = null;
	function selectServer() {
		for (var i = 0; i < serverDiv.length; i++) {
			serverDiv[i].style.backgroundColor = "#ffffff";
		}
		this.style.backgroundColor = "red";
		select = this.serverid;
		//alert(this.serverid);
	}
	function selectConvert() {
		for (var i = 0; i < convertDiv.length; i++) {
			convertDiv[i].style.backgroundColor = "#ffffff";
		}
		this.style.backgroundColor = "red";
		convert = this;
		//alert(this.serverid);
	}
	function addServer () {
		var div = document.getElementById("addserver");
		div.style.display = "";
	}
	function addConvert () {
		var div = document.getElementById("addconvert");
		div.style.display = "";
	}
	function updateServer () {
		if (select == null) {
			alert("没有选中服务器");
			return;
		}
		window.location = "server.jsp?serverid=" + select;
	}
	function delConvert () {
		if (convert == null) {
			alert("没有选中汇率");
			return;
		}
		var url = "delconvert.jsp";
		url += "?codename=" + convert.codename;
		url += "&gameid=" + convert.gameid;
		window.location = url;
	}
	function delServer () {
		if (select == null) {
			alert("没有选中服务器");
			return;
		}
		window.location = "delserver.jsp?serverid=" + select + "&gameid=<%=gameid%>";
	}
</script>
  </head>
  
  <body>
  	<form action="updategame.jsp" method="post">
  	<table border="0" align="center" cellpadding="0" cellspacing="0">
  	<input name="gameid" type="hidden" value="<%=vo.getGameId()%>"/>
	  	<tr>
	  		<td>游戏Id：</td><td><%=vo.getGameId()%></td>
	  	</tr>
	  	<tr>
	  		<td>游戏名称：</td><td><input name="gamename" type="text" value="<%=vo.getGameName()%>"/><button type="submit">提交</button></td>
	  	</tr>
    </table>
    </form>
	<br/>
	<br/>
	<table border="0" align="center" cellpadding="0" cellspacing="0">
	<tr><td colspan="6" align="center">服务器列表</td></tr>
<%
for (int i = 0; i < list.size(); i++) {
	String serverid = list.get(i).getServerId().split("_")[0];
	
%>
	<tr id="server<%=i%>" >
		<td>服务器Id：</td><td><%=serverid%></td>
		<td>&#160服务器名称：</td><td><%=list.get(i).getServerName()%></td>
	</tr>
<script type="text/javascript">
	serverDiv[<%=i%>] = document.getElementById("server"+<%=i%>);
	serverDiv[<%=i%>].onclick = selectServer;
	serverDiv[<%=i%>].serverid = "<%=list.get(i).getServerId()%>";
</script>
<%
}
%>
	</table>
	<form action="addserver.jsp" method="post">
		<input name="gameid" type="hidden" value="<%=vo.getGameId()%>"/>
		<div id="addserver" style="text-align:center;display:none;">
			服务器Id：<input type="text" name="serverid"/>
			服务器名称：<input type="text" name="servername"/>
			回调URL：<input type="text" name="serverurl"/>
			验证URL：<input type="text" name="url"/>
			通知URL：<input type="text" name="paynotify"/>
			<button type="submit">提交</button>
		</div>
	</form>
	<div style="text-align:center;">
		<button onclick="addServer()">添加服务器</button><button onclick="updateServer()">修改服务器</button><button onclick="delServer()">删除服务器</button>
	</div>
	<br/>
	<br/>
	<table border="0" align="center" cellpadding="0" cellspacing="0">
	<tr><td colspan="8" align="center">兑换列表</td></tr>
<%
for (int i = 0; i < list2.size(); i++) {
%>
	<tr id="convert<%=i%>" >
		<td>CodeName：</td><td><%=list2.get(i).getCodeName()%></td>
		<td>&#160现金数量：</td><td><%=list2.get(i).getMoney()%></td>
		<td>&#160现金种类：</td><td><%=list2.get(i).getUint().equals("CN") ? "人民币":"美元"%></td>
		<td>&#160虚拟货币数量：</td><td><%=list2.get(i).getPoint()%></td>
		<td>&#160虚拟货币名称：</td><td><%=list2.get(i).getPointName()%></td>
	</tr>
<script type="text/javascript">
	convertDiv[<%=i%>] = document.getElementById("convert"+<%=i%>);
	convertDiv[<%=i%>].onclick = selectConvert;
	convertDiv[<%=i%>].gameid = "<%=vo.getGameId()%>";
	convertDiv[<%=i%>].codename = "<%=list2.get(i).getCodeName()%>";
</script>
<%
}
%>
	</table>
	<table border="0" align="center" cellpadding="0" cellspacing="0">
	<form action="addconvert.jsp" method="post">
		<input name="gameid" type="hidden" value="<%=vo.getGameId()%>"/>
		<tr>
			<td>
				<div id="addconvert" style="text-align:left;display:none;">
					CodeName：<input type="text" name="codename"/><br/>
					现金数量：<input type="text" name="money"/><br/>
					现金种类：<select name="unit">
								<option value="CN">人民币</option>
								<option value="US">美元</option>
							 </select><br/>
					虚拟货币数量：<input type="text" name="point"/><br/>
					虚拟货币名称：<input type="text" name="pointname"/>
					<button type="submit">提交</button>
				</div>
			</td>
		</tr>
	</form>
	</table>
	<div style="text-align:center;">
		<button onclick="addConvert()">添加兑换</button><button onclick="delConvert()">删除兑换</button>
	</div>
  </body>
</html>

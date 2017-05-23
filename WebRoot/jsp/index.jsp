<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.model.PayGame" %>
<%@ page import="java.util.List" %>


<%
String yy = request.getParameter("yuange");
if (!"Icanpay".equals(yy)) {
	response.sendRedirect("../index.jsp");
	return;
}
PayGameBean bean = new PayGameBean();
List<PayGame> list = bean.getPayGames();
%>
<html>
  <head>
    
    <title>My JSP 'index.jsp' starting page</title>
    
	<meta http-equiv="pragma" content="no-cache">
	<meta http-equiv="cache-control" content="no-cache">
	<meta http-equiv="expires" content="0">    
	<meta http-equiv="keywords" content="keyword1,keyword2,keyword3">
	<meta http-equiv="description" content="This is my page">
	<!--
	<link rel="stylesheet" type="text/css" href="styles.css">
	-->
<script type="text/javascript">
	var gameDiv = [];
	var select = null;
	function selectGame() {
		for (var i = 0; i < gameDiv.length; i++) {
			gameDiv[i].style.backgroundColor = "#ffffff";
		}
		this.style.backgroundColor = "red";
		select = this.gameid;
		//alert(this.gameid);
	}
	function addGame () {
		var div = document.getElementById("addgame");
		div.style.display = "";
	}
	function updateGame () {
		if (select == null) {
			alert("没有选中游戏");
			return;
		}
		window.location = "game.jsp?gameid=" + select;
	}
	function delGame () {
		if (select == null) {
			alert("没有选中游戏");
			return;
		}
		window.location = "delgame.jsp?gameid=" + select;
	}
</script>
  </head>
  
  <body>
	<table border="0" align="center" cellpadding="0" cellspacing="0">
	<tr><td colspan="4" align="center">游戏列表</td></tr>
<%
for (int i = 0; i < list.size(); i++) {
%>
	<tr id="game<%=i%>">
		<td>游戏Id：</td><td><%=list.get(i).getGameId()%></td>
		<td>游戏名称：</td><td><%=list.get(i).getGameName()%></td>
	</tr>
<script type="text/javascript">
	gameDiv[<%=i%>] = document.getElementById("game"+<%=i%>);
	gameDiv[<%=i%>].onclick = selectGame;
	gameDiv[<%=i%>].gameid = "<%=list.get(i).getGameId()%>";
</script>
<%
}
%>
	</table>
	<form action="addgame.jsp" method="post">
		<div id="addgame" style="text-align:center;display:none;">
			游戏Id：<input type="text" name="gameid"/>
			游戏名称：<input type="text" name="gamename"/>
			<button type="submit">提交</button>
		</div>
	</form>
	<div style="text-align:center;">
		<button onclick="addGame()">添加游戏</button><button onclick="updateGame()">修改游戏</button><button onclick="delGame()">删除游戏</button>
	</div>
  </body>
</html>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayItemBean" %>
<%@ page import="noumena.payment.model.PayItems" %>
<%@ page import="java.util.List" %>


<%
/*String yy = request.getParameter("yuange");
if (!"Icanpay".equals(yy)) {
	response.sendRedirect("../index.jsp");
	return;
}*/
PayItemBean bean = new PayItemBean();
List<PayItems> list = null;
String gameid = request.getParameter("gameid");
if (gameid == null || gameid.equals("") || gameid.equals("0"))
{
	gameid = "";
	list = bean.select();
}
else
{
	list = bean.selectByGame(gameid);
}
%>
<html>
  <head>
    
    <title>My JSP 'items.jsp' starting page</title>
    
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
	var select = {gameid:0,serverid:0,itemid:0,itemprice:0};
	function selectItem() {
		for (var i = 0; i < gameDiv.length; i++) {
			gameDiv[i].style.backgroundColor = "#ffffff";
		}
		this.style.backgroundColor = "red";
		select.id = this.id;
		select.gameid = this.gameid;
		select.serverid = this.serverid;
		select.itemid = this.itemid;
		select.itemprice = this.itemprice;
		select.itemdesc = this.itemdesc;
		//alert(select.itemid);
	}
	function addItem () {
		var div = document.getElementById("additem");
		div.style.display = "";
		var obj = document.getElementById("defgameid");
		var gameid = obj.value;
		if (gameid != null)
		{
			obj = document.getElementById("gameid");
			obj.value = gameid;
		}
		obj = document.getElementById("serverid");
		obj.value = "0";
	}
	function updateItem () {
		if (select.gameid == 0) {
			alert("没有选中商品");
			return;
		}
		var div = document.getElementById("additem");
		div.style.display = "";
		var model = document.getElementById("model");
		model.value = "update";
		var obj = document.getElementById("id");
		obj.value = select.id;
		obj = document.getElementById("gameid");
		obj.value = select.gameid;
		obj = document.getElementById("serverid");
		obj.value = select.serverid;
		obj = document.getElementById("itemid");
		obj.value = select.itemid;
		obj = document.getElementById("itemprice");
		obj.value = select.itemprice;
		obj = document.getElementById("itemdesc");
		obj.value = select.itemdesc;
	}
	function delItem () {
		if (select.gameid == 0) {
			alert("没有选中商品");
			return;
		}
		var model = document.getElementById("model");
		model.value = "delete";
		var obj = document.getElementById("id");
		obj.value = select.id;
		obj = document.getElementById("gameid");
		obj.value = select.gameid;
		obj = document.getElementById("serverid");
		obj.value = select.serverid;
		obj = document.getElementById("itemid");
		obj.value = select.itemid;
		obj = document.getElementById("itemprice");
		obj.value = select.itemprice;
		obj = document.getElementById("itemdesc");
		obj.value = select.itemdesc;
		var form1 = document.getElementById("additemform");
		form1.submit();
	}
</script>
  </head>
  
  <body>
	<table border="0" align="center" cellpadding="0" cellspacing="0">
	<tr><td colspan="4" align="center">商品列表</td></tr>
	<tr>
		<table border="1" align="center" cellpadding="0" cellspacing="0">
			<tr>
				<td>游戏Id</td>
				<td>区服Id</td>
				<td>商品Id</td>
				<td>商品价格</td>
				<td>商品描述</td>
			</tr>
<%
for (int i = 0; i < list.size(); i++) {
%>
	<tr id="item<%=i%>">
		<td align="center"><%=list.get(i).getGameid()%></td>
		<td align="center"><%=list.get(i).getServerid()%></td>
		<td align="center"><%=list.get(i).getItemid()%></td>
		<td align="center"><%=list.get(i).getItemprice()%></td>
		<td align="center"><%=list.get(i).getItemdesc()%></td>
	</tr>
<script type="text/javascript">
	gameDiv[<%=i%>] = document.getElementById("item"+<%=i%>);
	gameDiv[<%=i%>].onclick = selectItem;
	gameDiv[<%=i%>].id = "<%=list.get(i).getId()%>";
	gameDiv[<%=i%>].gameid = "<%=list.get(i).getGameid()%>";
	gameDiv[<%=i%>].serverid = "<%=list.get(i).getServerid()%>";
	gameDiv[<%=i%>].itemid = "<%=list.get(i).getItemid()%>";
	gameDiv[<%=i%>].itemprice = "<%=list.get(i).getItemprice()%>";
	gameDiv[<%=i%>].itemdesc = "<%=list.get(i).getItemdesc()%>";
</script>
<%
}
%>
		</table>
	</tr>
	</table>
	<input type="hidden" id="defgameid" name="defgameid" value="<%=gameid%>"/>
	<form action="additem.jsp" id="additemform" method="post">
		<div id="additem" style="text-align:center;display:none;">
			游戏Id：<input type="text" id="gameid" name="gameid"/>
			区服Id：<input type="text" id="serverid" name="serverid"/>
			商品Id：<input type="text" id="itemid" name="itemid"/>
			商品价格：<input type="text" id="itemprice" name="itemprice"/>
			商品描述：<input type="text" id="itemdesc" name="itemdesc"/>
			<input type="hidden" id="id" name="id"/>
			<input type="hidden" id="model" name="model" value="insert"/>
			<button type="submit">提交</button>
		</div>
	</form>
	<div style="text-align:center;">
		<button onclick="addItem()">添加商品</button><button onclick="updateItem()">修改商品</button><button onclick="delItem()">删除商品</button>
	</div>
  </body>
</html>

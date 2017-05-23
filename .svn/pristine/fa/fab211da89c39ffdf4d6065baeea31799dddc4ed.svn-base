<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="noumena.payment.util.Constants"%>
<%@page import="noumena.payment.gash.GASHPaidParams"%>
<%@page import="noumena.payment.gash.GASHPaytypeParams"%>
<%@page import="noumena.payment.gash.GASHParams"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.vo.PayGameVO" %>
<%@ page import="noumena.payment.vo.PayServerVO" %>
<%@ page import="noumena.payment.vo.WebConvertVO" %>
<%@ page import="noumena.payment.model.PayServer" %>
<%@ page import="java.util.List" %>

<%
PayGameBean bean = new PayGameBean();
List<PayGameVO> list = bean.getGashPayGameVOs();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>空中網GASH充值</title>
<link href="../style/vss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	var payTypes = [];
	var payType = {};
	
	var PayGames = [];
	var PayGame = {};
	var payServer = {};
	var payConvert = {};
	var checkUrl;
	var req;
	var flag = false;
	var flag1 = false;
	
<%
	for (GASHPaytypeParams paytype : GASHParams.getPaytypes())
	{
		for (GASHPaidParams paid : paytype.getPaids())
		{
%>

			payType = {};
			payType.type = "<%=paytype.getType()%>";
			payType.paid = "<%=paid.getPaid()%>";
			payType.name = "<%=paid.getName()%>";
			payTypes.push(payType);

<%
		}
	}
%>
	
	
<%
for (PayGameVO payGameVO : list) {
%>
	PayGame = {};
	PayGame.gameid = "<%=payGameVO.getGameId()%>";
	PayGame.gamename = "<%=payGameVO.getGameName()%>";
	PayGame.servers = [];
	PayGame.converts = [];
	<%
	for (PayServerVO payServer : payGameVO.getServers()) {
	%>
		payServer = {};
		payServer.serverid = "<%=payServer.getServerId()%>";
		payServer.servername = "<%=payServer.getServerName()%>";
		payServer.checkurl = "<%=payServer.getCheckUrl()%>";
		PayGame.servers.push(payServer);
	<%
	}
	%>
	<%
	for (WebConvertVO webConvertVO : payGameVO.getConverts()) {
	%>
		payConvert = {};
		payConvert.codename = "<%=webConvertVO.getCodeName()%>";
		payConvert.money = "<%=webConvertVO.getMoney()%>";
		payConvert.unit = "<%=webConvertVO.getUint()%>";
		payConvert.point = "<%=webConvertVO.getPoint()%>";
		payConvert.pointname = "<%=webConvertVO.getPointName()%>";
		PayGame.converts.push(payConvert);
	<%
	}
	%>
	PayGames.push(PayGame);
<%
}
%>
	
	function initPaidType (select) {
		var paidtypect = document.getElementById("paidtypect");
		if (select == null)
		{
			paidtypect.options.add(new Option("點數卡儲值", "<%=Constants.PAY_TYPE_GASH_POINT%>"));
			paidtypect.options.add(new Option("電信業者儲值", "<%=Constants.PAY_TYPE_GASH_TEL%>"));
			paidtypect.options.add(new Option("金融業者儲值", "<%=Constants.PAY_TYPE_GASH_BANK%>"));
		}
		createpaidselect();
		//initdiv();
	}
	function initGame (select) {
		var game = document.getElementById("game");
		if (select == null) {
			for (var i = 0; i < PayGames.length;i++) {
				game.options.add(new Option(PayGames[i].gamename,PayGames[i].gameid));
			}
			createselect(0);
			createconvert(0);
		} else {
			for (var i = 0; i < game.options.length;i++) {
				if (game.options[i].selected) {
					createselect(i);
					createconvert(i);
					break;
				}
			}
		}
		//alert(select.value);
		initdiv();
	}
	function createpaidselect()
	{
		var paidtypect = document.getElementById("paidtypect");
		var paidtype = 1;
		for (var i = 0; i < paidtypect.options.length;i++)
		{
			if (paidtypect.options[i].selected)
			{
				paidtype = paidtypect.options[i].value;
			}
		}
		var paidct = document.getElementById("paidct");
		while (paidct.options.length > 0)
		{
			paidct.options.remove(0);
		}
		for (var i = 0; i < payTypes.length; i++)
		{
			if (payTypes[i].type == paidtype)
			{
				var option = new Option(payTypes[i].name, payTypes[i].paid);
				paidct.options.add(option);
			}
		}
	}
	function createselect(num) {
		PayGames[num];
		var server = document.getElementById("server");
		server.gameNum = num;
		while (server.options.length > 0) {
			server.options.remove(0);
		}
		for (var i = 0; i < PayGames[num].servers.length; i++) {
			var option = new Option(PayGames[num].servers[i].servername,PayGames[num].servers[i].serverid);
			
			if (i == 0) {
				option.select = true;
				checkUrl = PayGames[num].servers[i].checkurl;
			}
			server.options.add(option);
		}
	}
	function createconvert(num) {
		PayGames[num];
		var bankamount = document.getElementById("bankamount");
		while (bankamount.firstChild) {
              bankamount.removeChild(bankamount.firstChild);
		}
		
		var tr;
		var td;
		var input;
		var s = "";
		s += "<table width=\"100%\" border=\"0\" class=\"two\">";
  		
		for (var i = 0; i < PayGames[num].converts.length; i++) {
			if (i % 2 == 0) {
				s += "<tr>";
			}
			s += "<td style=\"text-align:right;vertical-align:text-top\">";
			if (i == 0) {
				s += "<input class=\"radio\" checked=\"checked\" type=\"radio\" name=\"amount\" value=\"" + PayGames[num].converts[i].codename  + "#" + PayGames[num].converts[i].money  + "#" + PayGames[num].converts[i].unit + "#" + PayGames[num].converts[i].point + "\">";
			} else {
				s += "<input class=\"radio\" type=\"radio\" name=\"amount\" value=\"" + PayGames[num].converts[i].codename  + "#" + PayGames[num].converts[i].money  + "#" + PayGames[num].converts[i].unit + "#" + PayGames[num].converts[i].point + "\">";
			}
			
			s += "</td>";
			s += "<td style=\"text-align:left\">";
			var unit;
			if (PayGames[num].converts[i].unit == "CN")
			{
				unit = "人民幣";
			}
			else if (PayGames[num].converts[i].unit == "TW")
			{
				unit = "新台幣";
			}
			else
			{
				unit = "美元";
			}
			s += PayGames[num].converts[i].money + unit + "=" + PayGames[num].converts[i].point + PayGames[num].converts[i].pointname;
			s += "</td>";
			if (i % 2 == 1 || i == PayGames[num].converts.length-1) {
				s += "</tr>";
			}
//			if (i % 2== 0) {
//				tr = document.createElement("tr");
//				bankamount.appendChild(tr);
//			}
//			td = document.createElement("td");
//			tr.appendChild(td);
//			td.width = "25%";
//			td.style.textAlign = "right";
//			input = document.createElement("input");
//			td.appendChild(input);
//			input.type = "radio";
//			input.name = "amount";
//			input.value = PayGames[num].converts[i].codename  + "#" + PayGames[num].converts[i].money  + "#" + PayGames[num].converts[i].unit + "#" + PayGames[num].converts[i].point;
//			if (i == 0) {
//				input.checked = "checked";
//			}
//			td = document.createElement("td");
//			tr.appendChild(td);
//			td.width = "25%";
//			td.style.textAlign = "left";
//			var unit;
//			if (PayGames[num].converts[i].unit == "CN") {
//				unit = "人民幣";
//			} else {
//				unit = "美元";
//			}
//			td.innerHTML = PayGames[num].converts[i].money + unit + "=" + PayGames[num].converts[i].point + PayGames[num].converts[i].pointname;
		}
		if (PayGames[num].gameid == 2300000) {
			s += "<tr><td colspan=\"4\" style=\"color:red\"></td></tr>";
		} else if (PayGames[num].gameid == 3000000) {
			s += "<tr><td colspan=\"4\" style=\"color:red\">注：<br/>1.遊戲id框請輸入您的角色名。如角色名中包含特殊字元無法輸入可聯繫客服尋求幫助。<br/>2.魔界客服QQ：1613848196 / 2674008676 / 2231741256<br/>3. 1000元 和 2000元充值選項不參加“首充翻倍”活動。</td></tr>";
		} else if (PayGames[num].gameid == 1800000) {
			//s += "<tr><td colspan=\"4\" style=\"color:red\"></td></tr>";
		} else if (PayGames[num].gameid == 2400000) {
			s += "<tr><td colspan=\"4\" style=\"color:red\">【活動】陰霾籠罩 黑暗降臨<br/>【日期】1月17日-1月23日<br/>【內容】來自未知α星系彌散星人，為了征服這片宇宙，向銀河聯盟投放了成千噸的輻射污染物，嚴重破壞影響了聯盟的環境和秩序！各位，指揮官們！拿起你手中的武器，行動吧！<br/>活動地址：http://play.ko.cn/pay/<br/><br/>兌換1000鑽贈送：人口x3000、主宰星片x200<br/>兌換2600鑽贈送：人口x8000、主宰星片x500<br/>兌換15000鑽贈送：水晶x500萬、鋁材x500萬、強化•正離子磁軌炮x1<br/>兌換32000鑽贈送：暗物質x1、元帥勳章x99、強化•收割者無人機x1<br/>注：<br/>1.獎勵可重複獲得，獎勵將在滿足條件後第二個工作日發放。<br/>2.請在領取獎勵前檢查倉庫，避免造成不必要的損失。<br/><br/>額外驚喜：<br/>活動期間，累計兌換滿100000鑽，即可額外獲得：末日級指揮艦lv5x1、暗物質x1<br/>活動期間，累計兌換滿200000鑽，即可額外獲得：末日級指揮艦lv10x1、暗物質x1<br/>注：<br/>1.以最高累計充值為准發獎，獎勵將在活動結束後統一發放。<br/>2.獎勵僅限獲得一次。</td></tr>";
		}
		PayGames[num]
		s += "<tr><td colspan=\"4\"><input id=\"next\" type=\"button\" class=\"but\" value=\"下一步\" style=\"display:none\" onclick=\"submitform()\"/></td></tr>";
		s += "</table>";
		bankamount.innerHTML = s;
	}

	function submitform() {
		var form = document.getElementById("form");
		if (form.username.value == form.usernamer.value && form.username.value != "") {
			form.submit();
		} else {
			alert("二次用戶名不匹配或用戶名為空");
		}
	}
	function checkUserByServer(user) {
		//alert(user.value + checkUrl);
		var usernamedui = document.getElementById("usernamedui");
		var usernamecuowu = document.getElementById("usernamecuowu");
		var usernameload = document.getElementById("usernameload");
		usernamedui.style.display = "none";
		usernamecuowu.style.display = "none";
		usernameload.style.display = "";
		var form = document.getElementById("form");
		if (form.username.value == null || form.username.value == "") {
			return;
		}
		var url = checkUrl;
		
		if(window.ActiveXObject){
			req = new ActiveXObject("Microsoft.XMLHTTP");
		}else if(window.XMLHttpRequest){
			req = new XMLHttpRequest();
		}
		var post = "&gameid=" + form.game.value;
		post += "&serverid=" + form.server.value;
		post += "&userid=" + form.username.value;
		req.onreadystatechange = display;
	    
		req.open("POST", "../checkuser.jsp", true);
	    
		req.setRequestHeader(
		 "Content-Type",
		 "application/x-www-form-urlencoded"
		);
		
		req.send("url="+url+post);
		
	}
	function checkUser() {
		var form = document.getElementById("form");
		var usernamerdui = document.getElementById("usernamerdui");
		var usernamercuowu = document.getElementById("usernamercuowu");
		if (form.username.value == form.usernamer.value) {
			var flag1 = true;
			usernamerdui.style.display = "";
			usernamercuowu.style.display = "none";
		} else {
			var flag1 = false;
			usernamercuowu.style.display = "";
			usernamerdui.style.display = "none";
		}
		if (flag1 && flag) {
			var next = document.getElementById("next");
			next.style.display = "";
		} else {
			var next = document.getElementById("next");
			next.style.display = "none";
		}
	}
	function initCheckUrl(select) {
		var num = select.gameNum;
		for (var i = 0; i < select.options.length;i++) {
			if (select.options[i].selected) {
				for (var j = 0; j < PayGames[num].servers.length; j++) {
					if (select.options[i].value == PayGames[num].servers[i].serverid) {
						checkUrl = PayGames[num].servers[i].checkurl;
						break;
					}
				}
				break;
			}
		}
		initdiv();
		//alert(checkUrl);
	}
	function display () {
		var usernamedui = document.getElementById("usernamedui");
		var usernamecuowu = document.getElementById("usernamecuowu");
		if (req.readyState == 4 && req.status == 200)
		{
			if (req.responseText == 0) {
				usernamedui.style.display = "";
				usernamecuowu.style.display = "none";
				flag = true;
			} else {
				usernamedui.style.display = "none";
				usernamecuowu.style.display = "";
				flag = false;
			}
				//usernamedui.style.display = "";
				//usernamecuowu.style.display = "none";
				//flag = true;
				
			var usernameload = document.getElementById("usernameload");
			usernameload.style.display = "none";
			var form = document.getElementById("form");
			if (form.username.value == form.usernamer.value) {
				var flag1 = true;
			} else {
				var flag1 = false;
			}
			if (flag1 && flag) {
				var next = document.getElementById("next");
				next.style.display = "";
			} else {
				var next = document.getElementById("next");
				next.style.display = "none";
			}
		}
	}
	function initdiv () {
		var usernamedui = document.getElementById("usernamedui");
		var usernamecuowu = document.getElementById("usernamecuowu");
		var usernamerdui = document.getElementById("usernamerdui");
		var usernamercuowu = document.getElementById("usernamercuowu");
		var usernameload = document.getElementById("usernameload");
		var next = document.getElementById("next");
		next.style.display = "none";
		usernamedui.style.display = "none";
		usernamecuowu.style.display = "none";
		usernamerdui.style.display = "none";
		usernamercuowu.style.display = "none";
		usernameload.style.display = "none";
	}
</script>
</head>

<body>
<!-- <script src="http://resource.kongzhong.com/comImg/title.js" type="text/javascript"></script>  -->
<div class="main">
	<!--頭圖-->
    <!-- <div class="pic_box"><img src="../Images/tou.jpg" /></div> -->
    <div>
		<table width="100%" border="0">
			<tr>
				<td width="20%"><img src="http://stc.kongzhong.com/kongzhong/images/foot_logo.png" /></td>
				<td width="80%"><h2>空中網手機遊戲充值頁面</h2></td>
			</tr>
		</table>
    </div>
    <!--請確認要充值遊戲:-->
	<div class="m_box">
    	<h2>請確認要充值遊戲:</h2>
        <form id="form" action="gashwebpay" method="post">
            <table width="100%" border="0" class="one">
              <tr>
                <td width="26%" class="forg">選擇遊戲：</td>
                <td width="24%"><select id="game" name="game" onchange="initGame(this)" class="select"></select></td>
                <td width="50%">&nbsp;</td>
              </tr>
              <tr>
                <td class="forg">選擇遊戲伺服器：</td>
                <td><select id="server" name="server" class="select" onchange="initCheckUrl(this)"></select></td>
                <td>&nbsp;</td>
              </tr>
              <tr>
                <td width="26%" class="forg">選擇行動支付類型：</td>
                <td width="24%"><select id="paidtypect" name="paidtypect" onchange="initPaidType(this)" class="select"></select></td>
                <td width="50%">&nbsp;</td>
              </tr>
              <tr>
                <td width="26%" class="forg">選擇行動支付：</td>
                <td width="24%"><select id="paidct" name="paidct" class="select"></select></td>
                <td width="50%">&nbsp;</td>
              </tr>
              <tr>
                <td class="forg">輸入遊戲ID：</td>
                <td><input name="username" id="username" type="text" onblur="checkUserByServer(this)"/></td>
                <td><div id="usernamedui" class="dui" style="display:none;">遊戲帳號存在</div><div id="usernamecuowu" class="cuowu" style="display:none;">遊戲帳號不存在</div><div id="usernameload" class="cuowu" style="display:none;">遊戲帳號驗證中</div></td>
              </tr>
              <tr>
                <td class="forg">確認遊戲ID：</td>
                <td><input name="usernamer" id="usernamer" type="text" onblur="checkUser()"/></td>
                <td><div id="usernamerdui" class="dui" style="display:none;">兩次帳號一致</div><div id="usernamercuowu" class="cuowu" style="display:none;">兩次帳號不一致</div></td>
              </tr>
            </table>
      <h2>請選擇充值金額:</h2>
            <div id="bankamount"></div>
	  </form>
   	<div class="cl"></div>
    </div>
    <!--請確認要充值遊戲:-->
    <div class="help">
    	<dl>
        	<dt>遊戲幫助：</dt>
        	<!-- <dd style="color:red">1 、王國創世錄,合金要塞,銀河創世錄,蒼穹啟示錄,遊戲ID通過遊戲內設置查看。 </dd> -->
        	<!-- <dd style="color:red">2 、魔界勇士請使用自己昵稱ID。 </dd> -->
			<dd>1 、由於銀行只支持 IE 流覽器，建議使用 IE6.0 和 IE6.0 以上流覽器。 </dd>
			<dd>2 、網銀支付頁面會在新的一頁打開，如未能彈出，請點擊 ie 的功能表 " 工具 "—"internet 選項 "—" 安全 "— 將安全中的各項設置恢復到默認級別。</dd>
			<dd>3 、銀行卡充值可能存在 24 小時網路延遲，請在扣費成功後，耐心等待，不要反復操作，否則將無法退還您的費用。 </dd>
			<dd>4 、如在充值時遇到問題，請您聯繫客服。</dd>
        </dl>
        <div class="cl"></div>
    </div>
    <script type="text/javascript">
    	initPaidType(null);
    	initGame(null);
    </script>
    <!--底部資訊-->
    <!-- <script src="http://stc.kongzhong.com/js/foot.js" type="text/javascript"></script>-->
	<div class="cl"></div>
</div>
</body>
</html>


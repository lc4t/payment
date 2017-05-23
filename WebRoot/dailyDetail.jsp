<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="noumena.payment.vo.PayInfoVO"%>
<%@page import="noumena.payment.bean.PayinfoBean"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

//日期处理
Calendar cal = Calendar.getInstance();
cal.add(Calendar.DAY_OF_MONTH, 0);
SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
String date = sdf.format(cal.getTime());

//查询条件返回值
String gameid = "0";
String bTime = "";
String eTime = "";
String sType = "";
String sMethod = "";
String sUserId = "";
String webPayIds = "10032,10033,10034,10035";

//查询结果集
List<PayInfoVO> list = new ArrayList<PayInfoVO>();

//查询开始
String flag = request.getParameter("flag");
//接收参数
String beginTime = null;
String endTime = null;
String searchType = null;
String searchMethod = null;
String payTypeId = "x";
float num = 0;
float num1 = 0;
float num2 = 0;
float num3 = 0;
float num4 = 0;
float num5 = 0;
float num6 = 0;
float num7 = 0;
float num8 = 0;
float num9 = 0;
float num10 = 0;
float num11 = 0;
float num12 = 0;
float num13 = 0;
float num14 = 0;
float num15 = 0;

if(flag!=null){
	//接收参数
	gameid = request.getParameter("gameid");
	beginTime = request.getParameter("searchCondBegin");
	endTime = request.getParameter("searchCondEnd");
	payTypeId = request.getParameter("payTypeId");
	if (payTypeId == null)
	{
		payTypeId = "";
	}
	PayinfoBean baen = new PayinfoBean();
	list = baen.getDetails(beginTime,endTime,payTypeId);
	for(PayInfoVO vo : list){
		if (vo.getPayState() == 1) {
			num += vo.getPayPrice();
			if("1800000".equals(vo.getPayGameId())) {
				num1 += vo.getPayPrice();
			} else if ("1900000".equals(vo.getPayGameId())) {
				num2 += vo.getPayPrice();
			} else if ("2000000".equals(vo.getPayGameId())) {
				num3 += vo.getPayPrice();
			} else if ("2300000".equals(vo.getPayGameId())) {
				num4 += vo.getPayPrice();
			} else if ("2900000".equals(vo.getPayGameId())) {
				num5 += vo.getPayPrice();
			} else if ("2400000".equals(vo.getPayGameId())) {
				num6 += vo.getPayPrice();
			} else if ("4200000".equals(vo.getPayGameId())) {
				num7 += vo.getPayPrice();
			} else if ("4300000".equals(vo.getPayGameId())) {
				num8 += vo.getPayPrice();
			} else if ("3000000".equals(vo.getPayGameId())) {
				num9 += vo.getPayPrice();
			} else if ("3300000".equals(vo.getPayGameId())) {
				num10 += vo.getPayPrice();
			} else if ("5800000".equals(vo.getPayGameId())) {
				num11 += vo.getPayPrice();
			} else if ("6500000".equals(vo.getPayGameId())) {
				num12 += vo.getPayPrice();
			} else if ("6000000".equals(vo.getPayGameId())) {
				num13 += vo.getPayPrice();
			} else if ("6100000".equals(vo.getPayGameId())) {
				num14 += vo.getPayPrice();
			} else if ("6600000".equals(vo.getPayGameId())) {
				num15 += vo.getPayPrice();
			}
		}
	}
}
%>

<html>
  <head>
    <title>大承支付查询</title>
	<link href="CSS/common.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" type="text/css" href="CSS/date.css" />
    <link rel="stylesheet" type="text/css" href="CSS/base.css"/>
	<link rel="stylesheet" type="text/css" href="CSS/cal_style.css"/>
	<link rel="stylesheet" type="text/css" href="CSS/query.css">
    <style type="text/css">
	    body {
		background-image: url(<%=basePath%>Images/back_load.gif);
		}
		.STYLE1 {
		color: #FFFFFF;
		font-weight: bold;
		}
    </style>
    <script type="text/javascript">
	<!--
	var ie =navigator.appName=="Microsoft Internet Explorer"?true:false;
	function $(objID){
	    return document.getElementById(objID);
	}
	//-->
	
	function myOnload(){
	}	
	function changeFuntion(){
		var a = document.getElementById("searchMethodId").value;
		if(a=='userId'){ 
			document.getElementById("userId").style.display = "";
		}
		
		if(a=='allUserId'){
			document.getElementById("userId").style.display = "none";
		}	
	}	
</script>
  </head>
  
  <body onLoad="myOnload()">
  <script src="script/WebCalendar.js"></script>
	  <TABLE width="88%" border="0" align="center" cellpadding="0" cellspacing="0">
	  <TBODY>
		<TR>
		  <TD vAlign=top width=90%><TABLE cellSpacing=0 cellPadding=0 width=90% align=center border=0>
			  <TBODY>
				<TR>
				  <TD width=7 height=22 valign="bottom" ><IMG src="Images/form_tab_start_on.gif" width="5" height="22" border=0></TD>
				  <TD background="Images/tbl_haut.gif"><TABLE cellSpacing=0 cellPadding=0 border=0>
					  <TBODY>
						<TR>
						  <TD background="Images/form_tab_bg_on.gif" class=group_title >大承支付查询</TD>
						  <TD><IMG height=22 src="Images/form_tab_btw_on_on.gif" width=25 border=0></TD>
						</TR>
					  </TBODY>
					</TABLE></TD>
				  <TD width=2 height=22><IMG height=22 src="Images/tbl_haut_d.gif" width=5 border=0></TD>
				</TR>
				<TR>
				  <TD width=5 background="Images/tbl_g.gif" height=5 rowSpan=2></TD>
				  <TD class=form_bgcolor style="PADDING-RIGHT: 20px; PADDING-LEFT: 20px; PADDING-BOTTOM: 5px; PADDING-TOP: 20px" width=796></TD>
				  <TD width=2 background="Images/tbl_d.gif" height=5 rowSpan=2></TD>
				</TR>
				<TR>
				  <TD class=form_bgcolor ></TD>
				</TR>
				<TR>
				  <TD width=7 background="Images/tbl_g.gif" height=5></TD>
				  <TD width="100%" class=form_bgcolor style="PADDING-RIGHT: 20px; PADDING-LEFT: 20px; PADDING-BOTTOM: 7px; PADDING-TOP: 0px" align=right >
				  <br/>
				  <form action="<%=basePath%>dailyDetail.jsp" method="post">
				  	  <input type="hidden" name="flag" value="serarchByTime">
					  <div class="desc_cell" align="center">
						  <select name="gameid">
					  	  	<option value="0" <%="0".equals(gameid) ? "selected=\"selected\"" : ""%>>全部</option>
					  	  	<option value="1800000" <%="1800000".equals(gameid) ? "selected=\"selected\"" : ""%>>王国</option>
					  	  	<option value="1900000" <%="1900000".equals(gameid) ? "selected=\"selected\"" : ""%>>坦克</option>
					  	  	<option value="2000000" <%="2000000".equals(gameid) ? "selected=\"selected\"" : ""%>>怒战</option>
					  	  	<option value="2300000" <%="2300000".equals(gameid) ? "selected=\"selected\"" : ""%>>合金</option>
					  	  	<option value="2900000" <%="2900000".equals(gameid) ? "selected=\"selected\"" : ""%>>苍穹</option>
					  	  	<option value="2400000" <%="2400000".equals(gameid) ? "selected=\"selected\"" : ""%>>银河</option>
					  	  	<option value="4200000" <%="4200000".equals(gameid) ? "selected=\"selected\"" : ""%>>大领主</option>
					  	  	<option value="4300000" <%="4300000".equals(gameid) ? "selected=\"selected\"" : ""%>>口袋战争</option>
					  	  	<option value="3000000" <%="3000000".equals(gameid) ? "selected=\"selected\"" : ""%>>魔界勇士</option>
					  	  	<option value="3300000" <%="3300000".equals(gameid) ? "selected=\"selected\"" : ""%>>蒸汽时代</option>
					  	  	<option value="3900000" <%="3900000".equals(gameid) ? "selected=\"selected\"" : ""%>>三国群英OL</option>
					  	  	<option value="4400000" <%="4400000".equals(gameid) ? "selected=\"selected\"" : ""%>>搞怪三国</option>
					  	  	<option value="5800000" <%="5800000".equals(gameid) ? "selected=\"selected\"" : ""%>>吸血鬼日记</option>
					  	  	<option value="6500000" <%="6500000".equals(gameid) ? "selected=\"selected\"" : ""%>>群英Q传</option>
					  	  	<option value="6000000" <%="6000000".equals(gameid) ? "selected=\"selected\"" : ""%>>超萌三国大战</option>
					  	  	<option value="6100000" <%="6100000".equals(gameid) ? "selected=\"selected\"" : ""%>>正义红师</option>
					  	  	<option value="6600000" <%="6600000".equals(gameid) ? "selected=\"selected\"" : ""%>>我家公主最可爱</option>
					  	  	<option value="5300000" <%="5300000".equals(gameid) ? "selected=\"selected\"" : ""%>>手游（无线部门）</option>
						  </select>
						  <select name="payTypeId">
					  	  	<option value="10029" <%="10029".equals(payTypeId) ? "selected=\"selected\"" : ""%>>支付宝</option>
					  	  	<option value="10030" <%="10030".equals(payTypeId) ? "selected=\"selected\"" : ""%>>神州行</option>
					  	  	<option value="<%=webPayIds %>" <%=(webPayIds.indexOf(payTypeId) >= 0) ? "selected=\"selected\"" : ""%>>网页充值</option>
					  	  </select>
						 开始日期：<input type="text" name="searchCondBegin" onClick="showcalendar(event, this);" onFocus="showcalendar(event, this);if(this.value=='0000-00-00')this.value=''" value="<%if(beginTime==null || beginTime.equals("")) out.print(date); else out.print(beginTime);%>" readonly/>
						 结束日期：<input type="text" name="searchCondEnd" onClick="showcalendar(event, this);" onFocus="showcalendar(event, this);if(this.value=='0000-00-00')this.value=''" value="<%if(endTime==null || endTime.equals("")) out.print(date); else out.print(endTime);%>" readonly/>
						 &nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
						 <input type="submit" value="查询" />
					  </div>
				  </form>
					
				  <table align="center" cellpadding="0" cellspacing="0" border="1" width="100%" height="18%" id="dataTable">
						<thead>
							<tr><td align="center" colspan="3" >总金额</td><td align="center" colspan="2" ><%=num%></td></tr>
							<tr><td align="center" colspan="3" >王国总金额</td><td align="center" colspan="2" ><%=num1%></td></tr>
							<tr><td align="center" colspan="3" >坦克总金额</td><td align="center" colspan="2" ><%=num2%></td></tr>
							<tr><td align="center" colspan="3" >怒战总金额</td><td align="center" colspan="2" ><%=num3%></td></tr>
							<tr><td align="center" colspan="3" >合金总金额</td><td align="center" colspan="2" ><%=num4%></td></tr>
							<tr><td align="center" colspan="3" >苍穹总金额</td><td align="center" colspan="2" ><%=num5%></td></tr>
							<tr><td align="center" colspan="3" >银河总金额</td><td align="center" colspan="2" ><%=num6%></td></tr>
							<tr><td align="center" colspan="3" >大领主总金额</td><td align="center" colspan="2" ><%=num7%></td></tr>
							<tr><td align="center" colspan="3" >口袋战争总金额</td><td align="center" colspan="2" ><%=num8%></td></tr>
							<tr><td align="center" colspan="3" >魔界勇士总金额</td><td align="center" colspan="2" ><%=num9%></td></tr>
							<tr><td align="center" colspan="3" >蒸汽时代总金额</td><td align="center" colspan="2" ><%=num10%></td></tr>
							<tr><td align="center" colspan="3" >吸血鬼日记总金额</td><td align="center" colspan="2" ><%=num11%></td></tr>
							<tr><td align="center" colspan="3" >群英Q传总金额</td><td align="center" colspan="2" ><%=num12%></td></tr>
							<tr><td align="center" colspan="3" >超萌三国大战总金额</td><td align="center" colspan="2" ><%=num13%></td></tr>
							<tr><td align="center" colspan="3" >正义红师总金额</td><td align="center" colspan="2" ><%=num14%></td></tr>
							<tr><td align="center" colspan="3" >我家公主最可爱总金额</td><td align="center" colspan="2" ><%=num15%></td></tr>
							<tr>
								<td align="center" nowrap >payid</td>
								<td align="center" nowrap >游戏</td>
								<td align="center" nowrap >交易号/卡号</td>
								<td align="center" nowrap >金额</td>
								<td align="center" nowrap >时间</td>
							</tr>
						</thead>
						<tbody>
								<% 
									for(PayInfoVO vo : list){
										if (vo.getPayState() == 1 && (gameid.equals("0") || gameid.equals(vo.getPayGameId()))) {
								%>
								<tr>
									<td align="center" nowrap ><a href="webOrderDetail.jsp?orderid=<%=vo.getGamePayid()%>"><%=vo.getGamePayid()%></a></td>
									<td align="center" nowrap >
									<%
									if ("1800000".equals(vo.getPayGameId())) {
									%>王国<%
									} else if ("1900000".equals(vo.getPayGameId())) {
									%>坦克<%
									} else if ("2000000".equals(vo.getPayGameId())) {
									%>怒战<%
									} else if ("2300000".equals(vo.getPayGameId())) {
									%>合金<%
									} else if ("2900000".equals(vo.getPayGameId())) {
									%>苍穹<%
									} else if ("2400000".equals(vo.getPayGameId())) {
									%>银河<%
									} else if ("4200000".equals(vo.getPayGameId())) {
									%>大领主<%
									} else if ("4300000".equals(vo.getPayGameId())) {
									%>口袋战争<%
									} else if ("3000000".equals(vo.getPayGameId())) {
									%>魔界勇士<%
									} else if ("3300000".equals(vo.getPayGameId())) {
									%>蒸汽时代<%
									} else if ("3900000".equals(vo.getPayGameId())) {
									%>三国群英OL<%
									} else if ("4400000".equals(vo.getPayGameId())) {
									%>搞怪三国<%
									} else if ("5800000".equals(vo.getPayGameId())) {
									%>吸血鬼日记<%
									} else if ("6500000".equals(vo.getPayGameId())) {
									%>群英Q传<%
									} else if ("6000000".equals(vo.getPayGameId())) {
									%>超萌三国大战<%
									} else if ("6100000".equals(vo.getPayGameId())) {
									%>正义红师<%
									} else if ("6600000".equals(vo.getPayGameId())) {
									%>我家公主最可爱<%
									} else if ("3700000".equals(vo.getPayGameId())) {
									%>空中小说（非手游）<%
									} else if ("5300000".equals(vo.getPayGameId())) {
									%>三国群英OL（无线部门）<%
									} else {
									%><%=vo.getPayGameId()%><%
									}
									%>
									</td>
									<td align="center" nowrap >
									<%
									if ("10029".equals(payTypeId)) {
										if (vo.getCashierCode() == null || "".equals(vo.getCashierCode())) {
									%><%=vo.getTradeNo() %><%
										} else {
									%>(<%=vo.getCashierCode() %>)<%=vo.getTradeNo() %><%
										}
									} else if ("10030".equals(payTypeId)){
									%><%=vo.getBuyerEmail() %><%
									} else {
									%><%=vo.getGamePayid()%><%
									}
									%>
									</td>
									<td align="center" nowrap ><%=vo.getPayPrice() %></td>
									<td align="center" nowrap ><%=vo.getPayTime() %></td>
								</tr>
								<%
										}
									}
								%>
						</tbody>
					</table>
					
					</TD>
				  <TD width=2 background="Images/tbl_d.gif"height=5 ></TD>
				</TR>
				<TR>
				  <TD width=5 height=5><IMG height=5 alt="" src="Images/tbl_bas_g.gif" width=5 border=0></TD>
				  <TD background="Images/tbl_bas.gif" height=5></TD>
				  <TD width=2 height=5><IMG height=5 alt="" src="Images/tbl_bas_d.gif" width=5 border=0></TD>
				</TR>
			  </TBODY>
			</TABLE></TD>
		</TR>
	  </TBODY>
	</TABLE>
  </body>
</html>

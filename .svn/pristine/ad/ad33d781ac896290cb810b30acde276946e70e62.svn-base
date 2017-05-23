<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page import="java.util.*"%>
<%@ page import="org.springframework.web.context.WebApplicationContext"%>
<%@ page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="noumena.payment.model.Orders"%>
<%@page import="noumena.payment.model.Callback"%>
<%@page import="noumena.payment.bean.OrdersBean"%>
<%@page import="noumena.payment.bean.CallbackBean"%>
<%@page import="noumena.payment.bean.PayGameBean"%>
<%@page import="noumena.payment.bean.PayServerBean"%>
<%@page import="noumena.payment.util.Constants"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";

//接收参数
String orderid = request.getParameter("orderid");
if (orderid == null || "".equals(orderid)) {
	response.sendRedirect("index.jsp");
	return;
}
OrdersBean ordersBean = new OrdersBean();
CallbackBean callbackBean = new CallbackBean();
Orders order = ordersBean.qureyOrder(orderid);
Callback callback = callbackBean.qureyCallback(orderid);
if (order == null || callback == null) {
	response.sendRedirect("index.jsp");
	return;
}
String gamename;
String servername = "未知";
if (order.getPayType().equals(Constants.PAY_TYPE_DACHENG_WEB)) {
	String gameid = order.getAppId();
	String serverid = order.getChannel()+"_"+gameid;
	PayGameBean payGameBean = new PayGameBean();
	PayServerBean payServerBean = new PayServerBean();
	gamename = payGameBean.getGame(gameid).getGameName();
	//servername = payServerBean.get(serverid).getServerName();
} else {
	gamename = order.getAppId();
}
%>

<html>
  <head>
    <title>订单查询</title>
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
  </head>
  
  <body>
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
				  <table align="center" cellpadding="0" cellspacing="0" border="1" width="100%" height="18%" id="dataTable">
						<thead>
							<tr>
								<td align="center" nowrap >payid</td>
								<td align="center" nowrap >用户id</td>
								<td align="center" nowrap >游戏</td>
								<td align="center" nowrap >区服</td>
								<td align="center" nowrap >金额</td>
								<td align="center" nowrap >订单创建时间</td>
								<td align="center" nowrap >支付是否成功</td>
								<td align="center" nowrap >支付成功时间</td>
								<td align="center" nowrap >是否需要回调</td>
								<td align="center" nowrap >回调是否成功</td>
								<td align="center" nowrap >回调支付状态</td>
								<td align="center" nowrap >回调时间</td>
							</tr>
						</thead>
						<tbody>
								<tr>
									<td align="center" nowrap ><%=orderid%></td>
									<td align="center" nowrap ><%=order.getUId()%></td>
									<td align="center" nowrap ><%=gamename%></td>
									<td align="center" nowrap ><%=servername%></td>
									<td align="center" nowrap ><%=order.getAmount()%></td>
									<td align="center" nowrap ><%=order.getCreateTime()%></td>
									<td align="center" nowrap ><%=order.getKStatus() == Constants.K_STSTUS_SUCCESS ? "是":"否"%></td>
									<td align="center" nowrap ><%=order.getUpdateTime()%></td>
									<td align="center" nowrap ><%=order.getIscallback() == Constants.CALLBACK_ON ? "是":"否"%></td>
									<td align="center" nowrap ><%=callback.getCallbackStatus() == Constants.CALLBACK_STSTUS_COMPLETE ? "是":"否"%></td>
									<td align="center" nowrap ><%=callback.getKStatus() == Constants.K_STSTUS_SUCCESS ? "成功":"失败"%></td>
									<td align="center" nowrap ><%=callback.getCallbackTime()%></td>
								</tr>
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

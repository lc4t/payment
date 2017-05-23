<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page contentType="text/html;charset=utf-8"%>

<%@ page import="java.sql.*"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="noumena.payment.model.Orders"%>
<%@ page import="noumena.payment.model.Callback"%>
<%@ page import="noumena.payment.util.DBUtil"%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<html>
	<head>
		<title>MIS for Payments</title>

		<!-- link calendar files  -->
		<script language="JavaScript" src="calendar_db.js"></script>
		<link rel="stylesheet" href="calendar.css">
	</head>
	
<%
	String action = request.getParameter("action");
	if (action == null)
	{
		action = "0";
	}
	String paytype = "";
	String appid = "";
	String uid = "";
	
	Vector<Orders> orders = new Vector<Orders>();
	HashMap<String, Callback> cbs = new HashMap<String, Callback>();
	
	if (action.equals("1"))
	{
		paytype = request.getParameter("paytype");
		appid = request.getParameter("appid");
		uid = request.getParameter("uid");
		
		if (paytype == null)
		{
			paytype = "";
		}
		if (appid == null)
		{
			appid = "";
		}
		if (uid == null)
		{
			uid = "";
		}
		
		Connection conn = null;
		PreparedStatement st = null;
		ResultSet rs = null;
	
		conn = DBUtil.getDBConn("java:comp/env/jdbc/PaymentDb");
		//String url1 = "jdbc:mysql://db.slave.cnxol.mgspublic.com:3306/cn1_xol?user=xolcn1&password=xxxx&autoReconnect=true&useUnicode=true&characterEncoding=utf-8";
		//Class.forName("com.mysql.jdbc.Driver").newInstance();
		//conn = DriverManager.getConnection(url1);
	
		String orderids = "";
		String sqlselect = "SELECT order_id,u_id,app_id,channel,item_id,amount,k_status,create_time,pay_type,pay_id,money FROM orders WHERE app_id LIKE ? AND pay_type LIKE ? AND u_id LIKE ? ORDER BY create_time DESC LIMIT 20";
	
		st = conn.prepareStatement(sqlselect);
		st.setString(1, "%" + appid + "%");
		st.setString(2, "%" + paytype + "%");
		st.setString(3, "%" + uid + "%");
		rs = st.executeQuery();
		try
		{
			while (rs.next())
			{
				Orders order = new Orders();
				order.setOrderId(rs.getString("order_id"));
				order.setUId(rs.getString("u_id"));
				order.setAppId(rs.getString("app_id"));
				order.setChannel(rs.getString("channel"));
				order.setItemId(rs.getString("item_id"));
				order.setAmount(rs.getFloat("amount"));
				order.setKStatus(rs.getInt("k_status"));
				order.setCreateTime(rs.getString("create_time"));
				order.setPayType(rs.getString("pay_type"));
				order.setPayId(rs.getString("pay_id"));
				order.setMoney(rs.getString("money"));
				
				orders.add(order);
				
				orderids += "'" + order.getOrderId() + "',";
			}
			
			orderids = orderids.substring(0, orderids.length() - 1);
			String sqlcbs = "SELECT order_id,callback_url,callback_status,server_status,k_status,create_time,callback_time FROM callback WHERE order_id IN (" + orderids + ")";
		
			st = conn.prepareStatement(sqlcbs);
			rs = st.executeQuery();

			while (rs.next())
			{
				Callback cb = new Callback();
				cb.setOrderId(rs.getString("order_id"));
				cb.setCallbackUrl(rs.getString("callback_url"));
				cb.setCallbackStatus(rs.getInt("callback_status"));
				cb.setServerStatus(rs.getInt("server_status"));
				cb.setKStatus(rs.getInt("k_status"));
				cb.setCreateTime(rs.getString("create_time"));
				cb.setCallbackTime(rs.getString("callback_time"));
				
				cbs.put(cb.getOrderId(), cb);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		finally
		{
			rs.close();
			st.close();
		}
	}

%>
		
	<body>
	<form name="testform" id="form1" action="pay.jsp" method="POST">
		AppId:<input type="text" name="appid">
		UId:<input type="text" name="uid">
		支付类型:<input type="text" name="paytype">
		<input type="submit" value="OK">
		<table border="1"><tbody>
			<tr>
				<td>订单号</td>
				<td>UID</td>
				<td>AppId</td>
				<td>渠道号</td>
				<td>ItemId</td>
				<td>请求金额</td>
				<td>支付状态</td>
				<td>下单时间</td>
				<td>支付方式</td>
				<td>渠道订单号</td>
				<td>实际金额</td>
				<td>回调地址</td>
				<td>回调状态</td>
				<td>服务器状态</td>
				<td>订单状态</td>
				<td>回调创建时间</td>
				<td>回调时间</td>
			</tr>

<%
	for (int i = 0 ; i < orders.size() ; i++)
	{
		Orders order = orders.get(i);
		Callback cb = cbs.get(order.getOrderId());
		if (cb == null)
		{
			cb = new Callback();
		}
%>

			<tr>
				<td><%=order.getOrderId()%></td>
				<td><%=order.getUId()%></td>
				<td><%=order.getAppId()%></td>
				<td><%=order.getChannel()%></td>
				<td><%=order.getItemId()%></td>
				<td><%=order.getAmount()%></td>
				<td><%=order.getKStatus()%></td>
				<td><%=order.getCreateTime()%></td>
				<td><%=order.getPayType()%></td>
				<td><%=order.getPayId()%></td>
				<td><%=order.getMoney()%></td>
				<td><%=cb.getCallbackUrl()%></td>
				<td><%=cb.getCallbackStatus()%></td>
				<td><%=cb.getServerStatus()%></td>
				<td><%=cb.getKStatus()%></td>
				<td><%=cb.getCreateTime()%></td>
				<td><%=cb.getCallbackTime()%></td>
			</tr>

<%
	}
%>

		</tbody></table>
		<input type="hidden" name="action" value="1">

	</form>
	</body>
</html>

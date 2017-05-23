<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="java.util.Vector"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.mycardtw.MyCardTWCharge" %>
<%@ page import="noumena.payment.mycardtw.MyCardTWParams" %>
<%@ page import="noumena.payment.bean.OrdersBean" %>

<%
/*String ua = request.getHeader("User-Agent");
if (ua != null)
{
	String mua = ua.toLowerCase();
	System.out.println("ua-->" + mua);
	if (mua.indexOf("ipad") >= 0 || mua.indexOf("iphone") >= 0 || mua.indexOf("android") >= 0)
	{
		//mobile
		response.sendRedirect("phone.jsp");
	}
}*/
	request.setCharacterEncoding("utf-8");
	String orderid = request.getParameter("orderid");
	String uid = request.getParameter("uid");
	String amount = request.getParameter("amount");
	String submit = request.getParameter("submit");
	String authcode = "";
	String actionurl = "";
	String serviceids = "";
	Vector<String> serviceid = new Vector<String>();
	Vector<String> servicename = new Vector<String>();
	String sid = request.getParameter("serviceid");
	
	if (submit == null || submit.equals(""))
	{
		String productid = "kz" + (int) Double.parseDouble(amount);
		System.out.println("mycardtw get productid->" + productid);
		serviceids = MyCardTWCharge.listBillingPayments(orderid, productid);
		if (serviceids == null || serviceids.equals(""))
		{
			
		}
		else
		{
			String[] str = serviceids.split(",");
			for (int i = 0 ; i < str.length ; i++)
			{
				String[] ps = str[i].split("[|]");
				if (ps[0] != null && ps[1] != null)
				{
					if (productid.equals("kz3000") && ps[0].equals("SPS0168102"))
					{
						//魔界勇士3000元，遠傳手機帳單付費，不包含
					}
					else
					{
						serviceid.add(ps[0]);
						servicename.add(ps[1]);
					}
				}
			}
			if (serviceid.size() == 0)
			{
				serviceids = "";
			}
		}
	}
	else
	{
		authcode = MyCardTWCharge.getTradeSeqFromMyCardBilling(sid, (int) Double.parseDouble(amount), Integer.parseInt(orderid), uid);
		String[] ps = authcode.split("[|]");
		if (ps[0].equals("1"))
		{
			OrdersBean bean = new OrdersBean();
			System.out.println("s3->" + ps[1]);
			bean.updateOrderExInfo(orderid, ps[1]);
			if (MyCardTWCharge.isTestmode() == true)
			{
				actionurl = MyCardTWParams.BILLING_CHARGE_URL_TEST;
			}
			else
			{
				actionurl = MyCardTWParams.BILLING_CHARGE_URL_RELEASE;
			}
			
			response.sendRedirect(actionurl + "?AuthCode=" + ps[1]);
		}
		else
		{
			if (MyCardTWCharge.isTestmode() == true)
			{
				actionurl = MyCardTWParams.BILLING_ERROR_PAGE_URL_TEST;
			}
			else
			{
				actionurl = MyCardTWParams.BILLING_ERROR_PAGE_URL_RELEASE;
			}
			
			response.sendRedirect(actionurl + "?errormsg=" + ps[1]);
		}
	}
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>MyCard Billing</title>
</head>

<body>
	<form id="form" action="" method="post">
	<input type="hidden" id="actionurl" name="actionurl" value="<%=actionurl %>">
	<input type="hidden" id="authcode" name="authcode" value="<%=authcode %>">
	<input type="hidden" id="submit" name="submit" value="1">
	
<%
	if (serviceids.equals(""))
	{
%>

		該品項下沒有可用金流

<%
	}
	else
	{
%>
	
		請選擇金流：<select id="serviceid" name="serviceid">
	
<%
		for (int i = 0 ; i < serviceid.size() ; i++)
		{
%>
	
			<option value=<%=serviceid.get(i) %>><%=servicename.get(i) %></option>
		
<%
		}
%>
		
		</select>
		<input type="submit" value="提交">
<%
	}
%>
	</form>
</body>
</html>

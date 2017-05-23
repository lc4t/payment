<%@page import="noumena.payment.util.Constants"%>
<%@page import="noumena.payment.mycardtw.MyCardTWParams"%>
<%@ page import="noumena.payment.util.DateUtil"%>
<%@ page import="noumena.payment.mycardtw.MyCardTWCharge" %>

<%
	request.setCharacterEncoding("utf-8");
	String startdate = request.getParameter("StartDate");
	String enddate = request.getParameter("EndDate");
	String mycardid = request.getParameter("MyCardID");
	String gameid = request.getParameter("GameID");
	String ret = "";
	if (startdate == null || startdate.equals(""))
	{
		startdate = DateUtil.getCurDateStr();
	}
	else
	{
		startdate = DateUtil.formatDate2(startdate);
	}
	if (enddate == null || enddate.equals(""))
	{
		enddate = DateUtil.getCurDateStr();
	}
	else
	{
		enddate = DateUtil.formatDate2(enddate);
	}
	if (mycardid == null || mycardid.equals(""))
	{
		mycardid = "0";
	}
	if (gameid == null || gameid.equals(""))
	{
		gameid = MyCardTWParams.INGAME_FACTORY_ID_2;
	}
	if (gameid.equals(MyCardTWParams.INGAME_FACTORY_ID_2))
	{
		gameid = Constants.PAY_TYPE_MYCARD_TW_INGAME_TW;
	}
	else if (gameid.equals(MyCardTWParams.INGAME_FACTORY_ID_3))
	{
		gameid = Constants.PAY_TYPE_MYCARD_TW_INGAME_HK;
	}
	else
	{
		gameid = Constants.PAY_TYPE_MYCARD_TW_INGAME_TW;
	}
		
	System.out.println("startdate->" + startdate);
	System.out.println("enddate->" + enddate);
	ret = MyCardTWCharge.getInGameServicesList(startdate, enddate, mycardid, gameid);
%>

<%=ret %>

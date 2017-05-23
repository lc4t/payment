<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.CallBackGameServBean" %>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="net.sf.json.JSONObject"%>
<%@page import="noumena.payment.vo.Status"%>

<%
	request.setCharacterEncoding("utf-8");
	response.setCharacterEncoding("utf-8");
	
	String url = request.getParameter("url");
	String gameid = request.getParameter("gameid");
	String serverid = request.getParameter("serverid");
	String server = serverid.split("_")[0];
	String userid = request.getParameter("userid");
	if (url.indexOf("?") == -1) {
		url += "?gameid=" + gameid;
	} else {
		url += "&gameid=" + gameid;
	}
	url += "&action=0";
	url += "&serverid=" + server;
	url += "&userid=" + URLEncoder.encode(userid, "utf-8");
	
	PrintWriter writer = response.getWriter();
	if (!gameid.equals("6400000"))
	{
		CallBackGameServBean cbean = new CallBackGameServBean();
		//System.out.println("check user url->" + url);
		String res = cbean.doGet(url);
		//System.out.println("check user ret->" + res);
		Status s = null;
		try
		{
			JSONObject json = JSONObject.fromObject(res);
			s = (Status) JSONObject.toBean(json,Status.class);
		}
		catch (Exception e)
		{
			//e.printStackTrace();
			s = new Status();
			s.setStatus(1);
		}
		if (s != null)
		{
			String ret = "";
			if (s.getUid() == null || s.getUid().equals(""))
			{
				ret = s.getStatus() + "";
			}
			else
			{
				ret = s.getStatus() + "," + s.getUid() + "," + s.getSid();
			}
			writer.write(ret);
		}
		else
		{
			writer.write("gameid(" + gameid + ")serverid(" + server + ")userid(" + URLEncoder.encode(userid, "utf-8") + ") can't get user status");
		}
	}
	else
	{
		writer.write("0");
	}
	writer.flush();
	writer.close();
%>

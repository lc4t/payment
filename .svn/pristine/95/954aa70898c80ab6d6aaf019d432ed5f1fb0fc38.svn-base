<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayServerBean" %>
<%@ page import="noumena.payment.model.PayServer" %>
<%@ page import="noumena.payment.util.DateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");

String gameid = request.getParameter("gameid");
String serverid = request.getParameter("serverid");
PayServer vo = new PayServer();
PayServerBean bean = new PayServerBean();
vo.setGameId(gameid);
vo.setServerId(serverid);
vo.setCreateTime(DateUtil.getCurrentTime());
bean.delete(vo);

response.sendRedirect("game.jsp?gameid=" + gameid);
%>

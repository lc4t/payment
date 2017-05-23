<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.model.PayGame" %>
<%@ page import="noumena.payment.vo.WebConvertVO" %>
<%@ page import="noumena.payment.util.DateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");

String gameid = request.getParameter("gameid");
String money = request.getParameter("money");
String unit = request.getParameter("unit");
String point = request.getParameter("point");
String pointname = request.getParameter("pointname");
String codename = request.getParameter("codename");

PayGameBean bean = new PayGameBean();
WebConvertVO vo = new WebConvertVO();

vo.setMoney(Double.parseDouble(money));
vo.setUint(unit);
vo.setPoint(Integer.parseInt(point));
vo.setPointName(pointname);
vo.setCodeName(codename);

bean.addConvert(vo, gameid);

response.sendRedirect("game.jsp?gameid=" + gameid);
%>
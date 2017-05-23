<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.bean.PayServerBean" %>
<%@ page import="noumena.payment.model.PayGame" %>
<%@ page import="noumena.payment.model.PayServer" %>
<%@ page import="noumena.payment.vo.WebConvertVO" %>
<%@ page import="noumena.payment.util.DateUtil" %>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>

<%
request.setCharacterEncoding("utf-8");
response.setCharacterEncoding("utf-8");

String gameid = request.getParameter("gameid");
PayServerBean bean = new PayServerBean();
List<PayServer> list = bean.selectByGame(gameid);
for (PayServer payServer : list) {
	bean.delete(payServer);
}

PayGame vo = new PayGame();
PayGameBean bean1 = new PayGameBean();
vo.setGameId(gameid);
bean1.delete(vo);

response.sendRedirect("index.jsp");
%>

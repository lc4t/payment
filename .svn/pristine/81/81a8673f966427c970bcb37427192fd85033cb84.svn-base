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
String gamename = request.getParameter("gamename");
PayGame vo = new PayGame();
PayGameBean bean = new PayGameBean();
vo.setGameId(gameid);
vo.setGameName(gamename);
List<WebConvertVO> list = new ArrayList<WebConvertVO>();
vo.setInfo(bean.toConvertString(list));
vo.setCreateTime(DateUtil.getCurrentTime());
bean.save(vo);

response.sendRedirect("game.jsp?gameid=" + gameid);
%>

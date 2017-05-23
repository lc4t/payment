<%@page import="java.net.URLDecoder"%>
<%@page import="noumena.payment.googleplay.GooglePlayCharge"%>
<%@page language="java" import="java.util.*" pageEncoding="GB18030"%>
<%@page import="java.io.PrintWriter"%>
<%@page import="java.net.URL"%>
<%@page import="java.net.HttpURLConnection"%>
<%@page import="java.io.BufferedReader"%>
<%@page import="java.io.InputStreamReader"%>
<%@page import="net.sf.json.JSONObject"%>
<%
String signedData = request.getParameter("signedData");
String signature = request.getParameter("signature");
String appId = request.getParameter("appId");

//signature = URLDecoder.decode(signature, "utf-8");
//signedData = URLDecoder.decode(signedData, "utf-8");

GooglePlayCharge.purchase(appId, signature, signedData);

String ret = "OK";
PrintWriter o = response.getWriter();
o.print(ret);
o.flush();
o.close();
%>



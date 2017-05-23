<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>

<%@ page import="noumena.mgsplus.logs.model.GameLogs" %>
<%@ page import="noumena.mgsplus.logs.bean.GameLogsBean" %>
<%@ page import="noumena.mgsplus.logs.util.DateUtil" %>
<%@ page import="noumena.mgsplus.logs.util.Constants" %>

<%
response.setHeader("Pragma", "No-cache");
response.setHeader("Cache-Control", "no-cache");
response.setDateHeader("Expires", 0);

String imei = request.getParameter("imei"); //imei
String appId = request.getParameter("appId"); //应用名字
String uId = request.getParameter("uId");//用户id
String charge = request.getParameter("charge");//消费金额
String unit = request.getParameter("unit");//币种（CN/US）
String screen = request.getParameter("screen"); //屏幕尺寸
String model = request.getParameter("model"); //设备类型
String ip = request.getParameter("ip");//（服务器需要传。客户端没有这个参数）
String platform = request.getParameter("platform"); //设备使用平台
String osversion = request.getParameter("osversion"); //平台版本
String gversion = request.getParameter("gversion"); //游戏版本
String channel = request.getParameter("channel"); //渠道
String itemId = request.getParameter("itemId"); //道具id
String itemInfo = request.getParameter("itemInfo"); //道具介绍
String itemNum = request.getParameter("itemNum"); //道具数量
String orderid = request.getParameter("orderid");//订单id
String callback = request.getParameter("callback");//回调url
String ischeckrepeat = request.getParameter("ischeckrepeat");//是否查询orderid 重复 （0 不需要 1需要）


System.out.println("===========================");
System.out.println("imei: "+imei);
System.out.println("appId: "+appId);
System.out.println("uId: "+uId);
System.out.println("charge: "+charge);
System.out.println("unit: "+unit);
System.out.println("model: "+model);
System.out.println("ip: "+ip);
System.out.println("platform: "+platform);
System.out.println("osversion: "+osversion);
System.out.println("gversion: "+gversion);
System.out.println("channel: "+channel);
System.out.println("itemId: "+itemId);
System.out.println("itemInfo: "+itemInfo);
System.out.println("itemNum: "+itemNum);
System.out.println("orderid: "+orderid);
System.out.println("callback: "+callback);
System.out.println("ischeckrepeat: "+ischeckrepeat);
System.out.println("===========================");

GameLogs log = new GameLogs();
log.setImei(imei);
log.setAppId(appId);
log.setuId(uId);
try{
	log.setCharge(Float.valueOf(charge));
} catch (Exception e) {
	log.setCharge(0);
}
log.setUnit(unit);
log.setScreen(screen);
log.setModel(model);
if (ip == null) {
	log.setIp(request.getRemoteAddr());
} else {
	log.setIp(ip);
}
log.setPlatform(platform);
log.setOsversion(osversion);
log.setGversion(gversion);
log.setChannel(channel);
log.setItemId(itemId);
log.setItemInfo(itemInfo + "#" + Constants.SERVER_HK);

try{
	log.setItemNum(Integer.valueOf(itemNum));
} catch (Exception e) {
	log.setItemNum(0);
}
System.out.println("begin save log");
if(log.getCharge()!=0){
	GameLogsBean.purchase(log, unit, Constants.SERVER_HK);
}

System.out.println("end save log");
%>

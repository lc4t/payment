<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="noumena.payment.util.Constants"%>
<%@page import="noumena.payment.gash.GASHPaidParams"%>
<%@page import="noumena.payment.gash.GASHPaytypeParams"%>
<%@page import="noumena.payment.gash.GASHParams"%>
<%@ page language="java" pageEncoding="UTF-8"%>

<%@ page import="noumena.payment.bean.PayGameBean" %>
<%@ page import="noumena.payment.vo.PayGameVO" %>
<%@ page import="noumena.payment.vo.PayServerVO" %>
<%@ page import="noumena.payment.vo.WebConvertVO" %>
<%@ page import="noumena.payment.model.PayServer" %>
<%@ page import="java.util.List" %>

<%
PayGameBean bean = new PayGameBean();
List<PayGameVO> list = bean.getGashPayGameVOs();
%>

<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title>空中網GASH充值</title>
<link href="../style/vss.css" rel="stylesheet" type="text/css" />
<script type="text/javascript">
	var payTypes = [];
	var payType = {};
	
	var PayGames = [];
	var PayGame = {};
	var payServer = {};
	var payConvert = {};
	var checkUrl;
	var req;
	var flag = false;
	var flag1 = false;
	
<%
	//必需的参数
	String stype = request.getParameter("model");			//请求的类型：1-得到交易id；2-查询订单状态
	String uid = request.getParameter("uid");				//
	String pkgid = request.getParameter("pkgid");			//
	String itemid = request.getParameter("itemid");			//
	String payprice = request.getParameter("price");		//
	String cburl = request.getParameter("cburl");			//单机游戏可以不需要回调地址
	
	//选填的参数
	String imei = request.getParameter("imei");				//
	String channel = request.getParameter("channel");		//
	String device_type = request.getParameter("device_type");	//
	String device_id = request.getParameter("device_id");	//
	String gversion = request.getParameter("gversion");		//
	String osversion = request.getParameter("osversion");	//
	
	for (GASHPaytypeParams paytype : GASHParams.getPaytypes())
	{
		for (GASHPaidParams paid : paytype.getPaids())
		{
%>

			payType = {};
			payType.type = "<%=paytype.getType()%>";
			payType.paid = "<%=paid.getPaid()%>";
			payType.name = "<%=paid.getName()%>";
			payTypes.push(payType);

<%
		}
	}
%>
	
	function initPaidType (select) {
		var paidtypect = document.getElementById("paidtypect");
		if (select == null)
		{
			paidtypect.options.add(new Option("點數卡儲值", "<%=Constants.PAY_TYPE_GASH_POINT%>"));
			paidtypect.options.add(new Option("電信業者儲值", "<%=Constants.PAY_TYPE_GASH_TEL%>"));
			paidtypect.options.add(new Option("金融業者儲值", "<%=Constants.PAY_TYPE_GASH_BANK%>"));
		}
		createpaidselect();
		//initdiv();
	}
	function createpaidselect()
	{
		var paidtypect = document.getElementById("paidtypect");
		var paidtype = 1;
		for (var i = 0; i < paidtypect.options.length;i++)
		{
			if (paidtypect.options[i].selected)
			{
				paidtype = paidtypect.options[i].value;
			}
		}
		var paidct = document.getElementById("paidct");
		while (paidct.options.length > 0)
		{
			paidct.options.remove(0);
		}
		for (var i = 0; i < payTypes.length; i++)
		{
			if (payTypes[i].type == paidtype)
			{
				var option = new Option(payTypes[i].name, payTypes[i].paid);
				paidct.options.add(option);
			}
		}
	}

	function submitform() {
		//var form = document.getElementById("form");
		var paidct = document.getElementById("paidct");
		var type = paidct.value;
		//GashPurchase.createOrder(type);
		//alert(type);
		form.submit();
	}
</script>
</head>

<body>
<div class="main">
	<div class="m_box">
        <form id="form" action="gashwebpay" method="post">
            <table width="100%" border="0" class="one">
              <tr>
                <td width="26%" class="forg">選擇行動支付類型：</td>
                <td width="24%"><select id="paidtypect" name="paidtypect" onchange="initPaidType(this)" class="select"></select></td>
                <td width="50%">&nbsp;</td>
              </tr>
              <tr>
                <td width="26%" class="forg">選擇行動支付：</td>
                <td width="24%"><select id="paidct" name="paidct" class="select"></select></td>
                <td width="50%">&nbsp;</td>
              </tr>
              <tr>
              	<td><input id="next" type="submit" value="下一步"/></td>
              </tr>
            </table>
            <input type="hidden" name="uid" value="<%=uid%>"/>
            <input type="hidden" name="pkgid" value="<%=pkgid%>"/>
            <input type="hidden" name="itemid" value="<%=itemid%>"/>
            <input type="hidden" name="price" value="<%=payprice%>"/>
            <input type="hidden" name="cburl" value="<%=cburl%>"/>
            <input type="hidden" name="imei" value="<%=imei%>"/>
            <input type="hidden" name="channel" value="<%=channel%>"/>
            <input type="hidden" name="device_type" value="<%=device_type%>"/>
            <input type="hidden" name="device_id" value="<%=device_id%>"/>
            <input type="hidden" name="gversion" value="<%=gversion%>"/>
            <input type="hidden" name="osversion" value="<%=osversion%>"/>
        </form>
    </div>
    <script type="text/javascript">
    	initPaidType(null);
    </script>
</div>
</body>
</html>


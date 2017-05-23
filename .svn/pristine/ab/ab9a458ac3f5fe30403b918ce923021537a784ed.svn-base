<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ page import="noumena.pay.ZFBSubmit"%>
<%@ page import="noumena.pay.NowSubmit"%>
<%@ page import="noumena.pay.util.MD5"%>
<%@ page import="noumena.pay.util.MD5Facade"%>
<%@ page import="java.util.HashMap"%>
<%@ page import="java.util.Map"%>
<%@ page import="java.util.Map"%>
<!DOCTYPE">
<html>
<head>

<script type="text/javascript" src="jquery-1.8.2.min.js"></script>
<link href="iOSdialogBox.css" rel="stylesheet">
<script type="text/javascript" src="iOSdialogBox.js"></script>

</head>
<body>
	<%
		Map<String, String> submitMap = new HashMap<String, String>();
	    		String sHtmlText = "";
	    		String type = request.getParameter("type");
	    		
	    		String appId = request.getParameter("appid");//Appid
	    		String productname = request.getParameter("productname");
	    		String amount = request.getParameter("amount");
	    		String cbgameurl = request.getParameter("cbgameurl");//返回游戏地址
	    		String productdesc = request.getParameter("productdesc");
	    		String channel = request.getParameter("channel");//渠道号
	    		String packagename = request.getParameter("packagename");//包名
	    		String uid = request.getParameter("uid");//角色ID
	    		String productid = request.getParameter("productid");//商品ID
	    		String cburl = request.getParameter("cburl");//服务器回调地址
	    		System.out.println(cbgameurl+"wewewewewewe))))))))))))))))))))))))))");
	    		if("zfb".equals(type)){
		    		submitMap.put("service", ZFBSubmit.service);
		            submitMap.put("partner", ZFBSubmit.partner);
		            submitMap.put("seller_id", ZFBSubmit.seller_id);
		            submitMap.put("_input_charset", ZFBSubmit.input_charset);
		    		submitMap.put("payment_type", ZFBSubmit.payment_type);
		    		submitMap.put("notify_url", ZFBSubmit.notify_url);
		    		submitMap.put("return_url", ZFBSubmit.return_url);
		    		submitMap.put("out_trade_no", ZFBSubmit.getOrderId(uid,packagename,productid,String.valueOf(Float.parseFloat(amount)/100f),cbgameurl,channel));

		    		submitMap.put("subject", productname);//商品名称
		    		submitMap.put("total_fee", String.valueOf(Float.parseFloat(amount)/100f));//金额
		    		submitMap.put("show_url", cburl);//游戏地址
		    		submitMap.put("app_pay","Y");//启用此参数可唤起钱包APP支付。
		    		submitMap.put("body", productdesc);
		    		sHtmlText = ZFBSubmit.buildRequest(submitMap,"get","确认");
	    		}else if("now".equals(type)){
			String mhtOrderNo = NowSubmit.getOrderId(uid,packagename,productid,amount,cbgameurl,channel);//获取支付中心订单号
			String ua = request.getHeader("User-Agent");
			String reString="1";
			// 做手机的判断
			if (ua != null) {
		String mua = ua.toLowerCase();
		System.out.println("ua-->" + mua);
		if (mua.indexOf("ipad") >= 0 || mua.indexOf("iphone") >= 0
				|| mua.indexOf("android") >= 0) {

		 reString="1";
		} else {
			 reString="0";
		}
			}
			//做MD5签名
			Map<String, String> dataMap = new HashMap<String, String>();
			dataMap.put("appId", NowSubmit.getAppIp(reString));
			dataMap.put("mhtOrderNo", mhtOrderNo);
			dataMap.put("mhtOrderName", productname);
			dataMap.put("mhtCurrencyType", NowSubmit.mhtCurrencyType);
			dataMap.put("mhtOrderAmt", amount);
			dataMap.put("mhtOrderDetail", productdesc);
			dataMap.put("mhtOrderType", NowSubmit.mhtOrderType);
			dataMap.put("mhtOrderStartTime", NowSubmit.dateFormat.format(new Date()));
			dataMap.put("notifyUrl", NowSubmit.notifyUrl);
			dataMap.put("frontNotifyUrl", NowSubmit.frontNotifyUrl);
			dataMap.put("mhtCharset", NowSubmit.input_charset);
			if("0".equals(reString)){
			dataMap.put("outputType", reString);
			}
			dataMap.put("payChannelType", NowSubmit.getPayChannelType(reString));
			//商户保留域， 可以不用填。 如果商户有需要对每笔交易记录一些自己的东西，可以放在这个里面
			//dataMap.put("mhtReserved", cbgameurl);
			String mhtSignature = MD5Facade.getFormDataParamMD5(dataMap, NowSubmit.getAppKey(reString), NowSubmit.input_charset);
			
			submitMap.put("appId", NowSubmit.getAppIp(reString));
			submitMap.put("mhtOrderNo", mhtOrderNo);
			submitMap.put("mhtOrderName", productname);
			submitMap.put("mhtCurrencyType", NowSubmit.mhtCurrencyType);
			submitMap.put("mhtOrderAmt", amount);
			submitMap.put("mhtOrderDetail", productdesc);
			submitMap.put("mhtOrderType", NowSubmit.mhtOrderType);
			submitMap.put("mhtOrderStartTime",NowSubmit.dateFormat.format(new Date()));
			submitMap.put("notifyUrl", NowSubmit.notifyUrl);
			submitMap.put("frontNotifyUrl", NowSubmit.frontNotifyUrl);
			submitMap.put("mhtCharset", NowSubmit.input_charset);
			
			submitMap.put("mhtSignType", NowSubmit.mhtSignType);
			submitMap.put("mhtSignature", mhtSignature);
			submitMap.put("funcode", NowSubmit.funcode);
			submitMap.put("deviceType", NowSubmit.getDeviceType(reString));
			if("0".equals(reString)){
			submitMap.put("outputType", reString);
			}
			submitMap.put("payChannelType", NowSubmit.getPayChannelType(reString));
		//	submitMap.put("mhtReserved", cbgameurl);
	    			sHtmlText = NowSubmit.buildRequest(submitMap,"get","确认");
	    			request.getSession().setAttribute("payurl", sHtmlText);
	    			request.getSession().setAttribute("mhtOrderNo", mhtOrderNo);
	    			String url = request.getContextPath();
	    			request.getSession().setAttribute("url", url);
	    			if("0".equals(reString)){
	    			response.sendRedirect(url + "/pay/notphonewait.jsp");
	    			}else{			
	    				response.sendRedirect(url + "/pay/phonewait.jsp");
	    			}
	    			
	    		out.println(sHtmlText);
	    		}
	%>
	<script type="text/javascript">
		function alterMsg() {
			var url = $("#urlvalue").val();
			alert(url);
			window.open(url);
			$(this).iOSdialogBoxConfirm({
				'title' : '素材家园',
				'message' : '是否支持素材家园？',
				'button1' : '确定',
				'button2' : '取消'
			}, respuesta);

		}
	</script>
</body>
</html>

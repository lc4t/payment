package noumena.payment.webomg;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.bean.PayServerBean;
import noumena.payment.model.Orders;
import noumena.payment.model.PayServer;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;

public class WebOMGCharge
{
	private static WebOMGParams params = new WebOMGParams();
	private static String GAME_ID = "6000000";
	private static boolean testmode = false;
	private static HashMap<String, Object> mutex = new HashMap<String, Object>();
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		WebOMGCharge.testmode = testmode;
	}
	
	private static Orders createOrder(WebOMGOrderVO ordervo)
	{
		String gameid = GAME_ID;
		PayServerBean payServerBean = new PayServerBean();
		String serverid = ordervo.getServerid();
		serverid = "webomg_" + serverid;
		PayServer payServer = payServerBean.get(serverid);
		if (payServer==null) {
			return null;
		}

		Orders vo = new Orders();
		
		vo.setImei("");
		vo.setUId(ordervo.getUserid());
		vo.setChannel(Constants.getGameIdByAppId(gameid)+ "A2AFU70A0000000");
		vo.setAppId(gameid);
		String itemprice = ordervo.getItemprice();
		float amount = 0;
		if (itemprice != null && !itemprice.equals("")) 
		{
			itemprice = itemprice.replace(",", "");
			amount = Float.parseFloat(itemprice);
		}
		vo.setAmount(amount);
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vo.setCreateTime(df1.format(new Date()));
		vo.setPayType(Constants.PAY_TYPE_WEBOMG);
		vo.setItemId(ordervo.getItemid());
		vo.setItemPrice("");
		vo.setItemNum(1);
		vo.setExInfo("");
		vo.setPayId(ordervo.getTradeno());
		vo.setMoney(itemprice);
		vo.setCurrency(Constants.CURRENCY_TWD);
		vo.setUnit(Constants.CURRENCY_UNIT_YUAN);
		

		OrdersBean bean = new OrdersBean();
		String cburl = "";
		String Parameter = "";
		if (payServer != null)
		{
			cburl = payServer.getCallbackUrl();
			Parameter += "userid=" + ordervo.getUserid();
			Parameter += "&gameid=" + gameid;
			Parameter += "&serverid=" + serverid;
			Parameter += "&itmeid=" + ordervo.getItemid();
			Parameter += "&amount=" + itemprice;
			Parameter += "&itemprice=" + -1;
		}
		String cbsign = gameid+serverid+ordervo.getAid()+ordervo.getItemid()+(-1)+ordervo.getItemprice();
		cbsign = StringEncrypt.Encrypt(cbsign+Constants.P_KEY);
		
		if (cburl == null || cburl.equals(""))
		{
			String orderid = bean.CreateOrder(vo);
			vo.setOrderId(orderid);
		}
		else
		{
			if (cburl.indexOf("?") == -1)
			{
				cburl += "?" + Parameter;
			}
			else
			{
				cburl += "&" + Parameter;
			}
			cburl += "&sign=" +cbsign;
			cburl += "&pt=" + Constants.PAY_TYPE_WEBOMG;
			cburl += "&currency=" + Constants.CURRENCY_TWD;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			String orderid = bean.CreateOrder(vo, cburl);
			vo.setOrderId(orderid);
		}
//		System.out.println(vo.toString());
		return vo;
	}



	public static String getCallbackFromWebomg(Map<String,String> WebOMGparams)
	{
		System.out.println("webomg cb info-->"+WebOMGparams.toString());
		String gamebarid = WebOMGparams.get("gamebarid");
		String code =  WebOMGparams.get("code");
		String msg =  WebOMGparams.get("msg");
		String hash =  WebOMGparams.get("hash");
		JSONObject json = null;
	
		String ret = "{\"code\":\"1\",\"msg\":\"success\"}";;
		Orders order = null;
		String orderid = "";
		try
		{
			hash = StringEncrypt.decryptAESIV(hash, params.getAppKeyById(gamebarid), params.getAppIVById(gamebarid));
			json = JSONObject.fromObject(hash);
			WebOMGOrderVO ordervo = (WebOMGOrderVO)JSONObject.toBean(json,WebOMGOrderVO.class);
			
			Object obj = mutex.get(ordervo.getTradeno());
			if (obj == null)
			{
				obj = new Object();
				mutex.put(ordervo.getTradeno(), obj);
			}
			synchronized (obj)
			{
			  //OMG的网页充值，并且是在OMG平台下，所以只有通知支付结果没有创建订单，需要手动创建一个订单
				order = createOrder(ordervo);
				orderid = order.getOrderId();
				
				if (ordervo.getCode().equals("100")) {
					
					OrdersBean bean = new OrdersBean();
					
					if (order != null)
					{
						//测试反查
						//docheck(ordervo);
						
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
							// 支付成功
							List<Orders> oo = bean.qureyOrdersByPayId(ordervo.getTradeno());
							if (oo.size() > 1) {
								// 重复订单，忽略
								System.out.println("webomg order (" + orderid+ ")(" + order.getPayId() + ") is existing");
							} else {
								bean.updateOrderKStatus(order.getOrderId(),Constants.K_STSTUS_SUCCESS);
							}
						} else {
							System.out.println("webomg order (" + orderid+ ")(" + order.getPayId() + ") had been succeed");
						}
					}
				}
			}
			mutex.clear();
		}
		catch (Exception e)
		{
			e.printStackTrace();
//			ret = "{\"code\":\"-1\",\"msg\":\"fail\"}";
		}
		
		System.out.println("webomg cb hash-->"+json.toString());
		String path = OSUtil.getRootPath() + "../../logs/WebOMGcb/"+ DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;

		OSUtil.saveFile(filename, json.toString());
		
		return ret;
	}

	public static void docheck(WebOMGOrderVO vo){		
		//反查订单
		int ts = (int)(Calendar.getInstance().getTimeInMillis()/1000);	
		String hashforcheck = "{\"gamebarid\":\""+vo.getGamebarid()+"\",\"tradeno\":\""+vo.getTradeno()+"\",\"ts\":\""+ts+"\"}";
		hashforcheck = StringEncrypt.encryptAESIV(hashforcheck, params.getAppKeyById(vo.getGamebarid()),params.getAppIVById(vo.getGamebarid()) );

		String urlstr = WebOMGParams.VERIFY_URL_RELEASE;
		String urlParameters = "gamebarid=" + vo.getGamebarid() + "&hash="+ hashforcheck;
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setUseCaches(false);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			connection.disconnect();

			System.out.println("webomg cb check order res->"+res);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
	}
	
	public static void init()
	{
		String key = "";
		String iv = "";
		if (testmode) 
		{
			key = WebOMGParams.KEY_TEST;
			iv = WebOMGParams.IV_TEST;
		}
		else 
		{
			key = WebOMGParams.KEY_RELEASE;
			iv = WebOMGParams.IV_RELEASE;
		}
		params.addApp("M5", "10037x2x5ju74xxm", key, iv); //ios
		params.addApp("M5", "10037x3x9ns343ht", key, iv); //google play
		params.addApp("M5", "10037x1x7amp65yk", key, iv); //web
	}
}

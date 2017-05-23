package noumena.payment.omg;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class OMGCharge {
	private static OMGParams params = new OMGParams();
	private static boolean testmode = false;
	private static HashMap<String, Object> mutex = new HashMap<String, Object>();

	public static boolean isTestmode() {
		return testmode;
	}

	public static void setTestmode(boolean testmode) {
		OMGCharge.testmode = testmode;
	}

	public static String getTransactionId(Orders order) {
		order.setCurrency(Constants.CURRENCY_TWD);
		order.setUnit(Constants.CURRENCY_UNIT_YUAN);
		
		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + Constants.PAY_TYPE_OMG;
			} else {
				cburl += "&pt=" + Constants.PAY_TYPE_OMG;
			}
			cburl += "&currency=" + Constants.CURRENCY_TWD;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);

		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	public static String checkOrdersStatus(String payIds) {
		String[] orderIds = payIds.split(",");

		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		OrdersBean bean = new OrdersBean();
		try
		{
	//		List<Orders> orders = bean.qureyOrders(orderIds);
			List<Orders> orders = bean.qureyOrders2(orderIds);
			for (int i = 0; i < orders.size(); i++) {
				Orders order = orders.get(i);
				int status = order.getKStatus();
				OrderStatusVO st = new OrderStatusVO();
				st.setPayId(order.getOrderId());
				if (status == Constants.K_STSTUS_DEFAULT|| status == Constants.K_CON_ERROR) {
					// 如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
					Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
					Calendar cal2 = Calendar.getInstance();
					if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT) {
						st.setStatus(4);
					} else {
						st.setStatus(3);
					}
				} else if (status == Constants.K_STSTUS_SUCCESS) {
					// 如果订单已经成功，直接返回订单状态
					st.setStatus(1);
				} else {
					// 订单已经失败，直接返回订单状态
					st.setStatus(2);
				}
				statusret.add(st);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		JSONArray arr = JSONArray.fromObject(statusret);

		return arr.toString();
	}

	public static String getCallbackFromOMG(Map<String,String> OMGparams) 
	{
		System.out.println("OMG cb info-->" +OMGparams.toString() );
		
		String ret = "{\"code\":\"1\",\"msg\":\"success\"}";
		
		JSONObject json=null;
		String orderid="";
		try {
			String gamebarid = OMGparams.get("gamebarid"); //OMG提供APP上架识别ID
			String code = OMGparams.get("code");  //回传代码（100：付款成功, 200：付款失敗）
			String msg = OMGparams.get("msg");  //回传讯息
			String hash = OMGparams.get("hash"); //加密串
			
			hash = StringEncrypt.decryptAESIV(hash, params.getAppKeyById(gamebarid), params.getAppIVById(gamebarid));
			json = JSONObject.fromObject(hash);
			OMGOrderVO vo = (OMGOrderVO)JSONObject.toBean(json,OMGOrderVO.class);
			orderid = vo.getStoretradeno();
			String money = vo.getItemprice();
			if (money != null && !money.equals("")) 
			{
				money = money.replace(",", "");
			}
			
			//测试反查功能
//			JSONObject result = docheck(vo);
//			System.out.println("反查结果------------>>>"+result.toString());
			Object obj = mutex.get(vo.getTradeno());
			if (obj == null)
			{
				obj = new Object();
				mutex.put(vo.getTradeno(), obj);
			}
			synchronized (obj)
			{
				if (vo.getCode().equals("100"))
				{
					OrdersBean bean = new OrdersBean();
					Orders order = bean.qureyOrder(orderid);
					if (order != null) 
					{
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, vo.getTradeno(), money, msg);
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("OMG order ("+ order.getOrderId() + ") had been succeed");
						}
					}
				}
			}
			mutex.clear();
		} 
		catch (Exception e) {
			e.printStackTrace();
			//非json格式的返回，OMG才会重新回调
			ret = "\"code\":\"-1\",\"msg\":\"fail\"";
		}

		System.out.println("OMG cb ->" +json.toString() );
		System.out.println("OMG cb ret->" + ret);

		String path = OSUtil.getRootPath() + "../../logs/OMGcb/"+ DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;

		OSUtil.saveFile(filename, json.toString());

		return ret;
	}
	
	public static JSONObject docheck(OMGOrderVO vo){
		JSONObject json = null;
		
		//反查订单
		int ts = (int)(Calendar.getInstance().getTimeInMillis()/1000);	
		String hashforcheck = "{\"gamebarid\":\""+vo.getGamebarid()+"\",\"storetradeno\":\""+vo.getStoretradeno()+"\",\"ts\":\""+ts+"\"}";
		hashforcheck = StringEncrypt.encryptAESIV(hashforcheck, params.getAppKeyById(vo.getGamebarid()),params.getAppIVById(vo.getGamebarid()) );

		String urlstr = "";
		if (testmode) 
		{
			urlstr = OMGParams.VERIFY_URL_TEST;
		}
		else
		{
			urlstr = OMGParams.VERIFY_URL_RELEASE;
		}
		String urlParameters = "gamebarid=" + vo.getGamebarid() + "&hash="+ hashforcheck;
		System.out.println("request  urlParameters--------->"+urlParameters);
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
			json = JSONObject.fromObject(res);
			
		} catch (Exception e) {
			e.printStackTrace();
		} 
		return json;
	}
	
	
	public static void init()
	{
		String key = "";
		String iv = "";
		if (testmode) 
		{
			key = OMGParams.KEY_TEST;
			iv = OMGParams.IV_TEST;
		}
		else 
		{
			key = OMGParams.KEY_RELEASE;
			iv = OMGParams.IV_RELEASE;
		}
		params.addApp("M5", "10037x2x5ju74xxm", key, iv); //ios
		params.addApp("M5", "10037x3x9ns343ht", key, iv); //google play
		params.addApp("M5", "10037x1x7amp65yk", key, iv); //ios + android
	}
}

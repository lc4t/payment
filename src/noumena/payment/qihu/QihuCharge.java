package noumena.payment.qihu;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.util.TrustAnyTrustManager;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class QihuCharge
{
	private static QihuParams params = new QihuParams();
	private static boolean testmode = false;
	private static HashMap<String, Object> mutex = new HashMap<String, Object>();

	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		QihuCharge.testmode = testmode;
	}

	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_YUAN);
		
		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals(""))
		{
			payId = bean.CreateOrder(order);
		}
		else
		{
			if (cburl.indexOf("?") == -1)
			{
				cburl += "?pt=" + Constants.PAY_TYPE_QIHU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_QIHU;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	public static String getTransactionInfo(String code, String appkey)
	{
		String token = getTokenFrom360(code, appkey);
		if (token == null || token.equals(""))
		{
			return "";
		}
		
		String id = getIdFrom360(token);
		
		QihuTransVO orderIdVO = new QihuTransVO(token, id);
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	public static String getTransactionInfoByToken(String token)
	{
		String id = getIdFrom360(token);
		
		QihuTransVO orderIdVO = new QihuTransVO(token, id);
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	public static String getTokenFrom360(String code, String appkey)
	{
		String token = "";
		String urlstr = QihuParams.GET_TOKEN_URL;
		String secretkey = params.getParams(appkey).getAppkey();
		if (secretkey == null || secretkey.equals(""))
		{
			return "";
		}
		urlstr = String.format(urlstr, code, appkey, secretkey);
		System.out.println("qh360token:"+urlstr);
		try
		{
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[]{ new TrustAnyTrustManager() }, new java.security.SecureRandom());   
			HttpsURLConnection connection = (HttpsURLConnection)new URL(urlstr).openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
			(
				new HostnameVerifier()
				{
					@Override
					public boolean verify(String arg0, SSLSession arg1)
					{
						//return true; //不验证
						return false; //验证
					}
				}
			);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			
			connection.disconnect();
			
			JSONObject json = JSONObject.fromObject(res);
			QihuOrderVO ordervo = (QihuOrderVO) JSONObject.toBean(json, QihuOrderVO.class);
			
			token = ordervo.getAccess_token();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return token;
	}

	public static String getIdFrom360(String token)
	{
		String id = "";
		String urlstr = QihuParams.GET_USER_INFO_URL;
		urlstr = String.format(urlstr, token);
		System.out.println(DateUtil.getCurTimeStr()+"qh360user:"+urlstr);
		try
		{
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[]{ new TrustAnyTrustManager() }, new java.security.SecureRandom());   
			HttpsURLConnection connection = (HttpsURLConnection)new URL(urlstr).openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
			(
				new HostnameVerifier()
				{
					@Override
					public boolean verify(String arg0, SSLSession arg1)
					{
						//return true; //不验证
						return false; //验证
					}
				}
			);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			
			connection.disconnect();
			
			JSONObject json = JSONObject.fromObject(res);
			QihuOrderVO ordervo = (QihuOrderVO) JSONObject.toBean(json, QihuOrderVO.class);
			System.out.println(DateUtil.getCurTimeStr()+"qh360order:"+ordervo.toString());
			id = ordervo.getId();
			System.out.println(DateUtil.getCurTimeStr()+"qh360id:"+id);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return id;
	}
	public static void main(String args[]){
		//{"id":"903082990","name":"季寂风","avatar":"http://quc.qhimg.com/dm/48_48_100/t00df551a583a87f4e9.jpg?f=60e3f1b5a6ee08559b84669b6c8e5f10","sex":"未知","area":""}
		JSONObject json = JSONObject.fromObject("{\"id\":\"903082990\",\"name\":\"季寂风\",\"avatar\":\"http://quc.qhimg.com/dm/48_48_100/t00df551a583a87f4e9.jpg?f=60e3f1b5a6ee08559b84669b6c8e5f10\",\"sex\":\"未知\",\"area\":\"\"}");
		QihuOrderVO ordervo = (QihuOrderVO) JSONObject.toBean(json, QihuOrderVO.class);
		System.out.println(ordervo.getId());
	}
	
	public static String checkOrdersStatus(String payIds)
	{
		String[] orderIds = payIds.split(",");
		
		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0 ; i < orders.size() ; i++)
		{
			Orders order = orders.get(i);
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					status = Constants.K_STSTUS_TIMEOUT;
					st.setStatus(2);
				}
			}
			else if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(1);
			}
			else
			{
				//订单已经失败，直接返回订单状态
				st.setStatus(2);
			}
			statusret.add(st);
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}
	
	public static String getOrderCBFrom360(QihuCBOrderVO cbvo, String signstr)
	{
		try {
			String orderid = cbvo.getApp_order_id();
			
			Object obj = mutex.get(cbvo.getOrder_id());
			if (obj == null)
			{
				obj = new Object();
				mutex.put(cbvo.getOrder_id(), obj);
			}
			synchronized (obj) 
			{
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
				if (order == null)
				{
					return "";
				}
				int status = order.getKStatus();
				if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
				{
					//cbvo.getAmount()目前单位是分，要改成元
					String amount = cbvo.getAmount();
					long lamount = Long.parseLong(amount);
					amount = (lamount / 100) + "";
					bean.updateOrderAmountPayIdExinfo(cbvo.getApp_order_id(), cbvo.getOrder_id(), amount, cbvo.getUser_id());
					if (cbvo.getGateway_flag().equals("success"))
					{
						String appsecret = params.getParams(cbvo.getApp_key()).getAppkey();
						signstr += "#" + appsecret;
						String sign = StringEncrypt.Encrypt(signstr);
//						System.out.println("360 cb my sign ->" + sign);
						if (sign.equals(cbvo.getSign()))
						{
//							if (verifyOrderFrom360(cbvo).equals("1"))
							{
								bean.updateOrderKStatus(cbvo.getApp_order_id(), Constants.K_STSTUS_SUCCESS);
							}
//							else
//							{
//								bean.updateOrderKStatus(cbvo.getApp_order_id(), Constants.K_STSTUS_ERROR);
//							}
						}
						else
						{
							bean.updateOrderKStatus(cbvo.getApp_order_id(), Constants.K_STSTUS_ERROR);
						}
					}
					else
					{
						bean.updateOrderKStatus(cbvo.getApp_order_id(), Constants.K_STSTUS_ERROR);
					}
				}
			}
			mutex.clear();
			
			return "{\"status\":\"ok\",\"delivery\":\"success\",\"msg\":\"\"}";
			
		} catch (Exception e) {
			e.printStackTrace();
			return "{\"status\":\"error\",\"delivery\":\"error\",\"msg\":\"\"}";
		}
		
	}

	public static String verifyOrderFrom360(QihuCBOrderVO cbvo)
	{
		String ret = "";
		String urlstr = QihuParams.VERIFY_URL;
		String secretkey = params.getParams(cbvo.getApp_key()).getAppkey();
		urlstr = String.format(urlstr, cbvo.getApp_key(), cbvo.getProduct_id(), cbvo.getAmount(), cbvo.getApp_uid(), 
								cbvo.getOrder_id(), cbvo.getApp_order_id(), cbvo.getSign_type(), cbvo.getSign_return(), 
								cbvo.getApp_key(), secretkey);
		System.out.println("360 verify order url ->" + urlstr);

		try
		{
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[]{ new TrustAnyTrustManager() }, new java.security.SecureRandom());   
			HttpsURLConnection connection = (HttpsURLConnection)new URL(urlstr).openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
			(
				new HostnameVerifier()
				{
					@Override
					public boolean verify(String arg0, SSLSession arg1)
					{
						//return true; //不验证
						return false; //验证
					}
				}
			);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			
			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/qihuorder/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + cbvo.getApp_order_id();
			OSUtil.saveFile(filename, res);
			
			System.out.println("360 verify order ret ->" + res);
			
			if (res.indexOf("\"ret\":\"verified\"") >= 0)
			{
				return "1";
			}
			else
			{
				return "0";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = "0";
		}
		
		return ret;
	}
	
	public static void init()
	{
		params.initParams(QihuParams.CHANNEL_ID, new QihuParamsVO());
//		params.addQihuApp("TinyWar", "586d0609d40b1e014248a9558c29763c", "c16e06f895ed0109f5811f758781e93f");
//		params.addQihuApp("Galaxy2", "716a2ac2d5062da3f364a4c845984f4e", "277496fece2ae04518ad1d7aeec5b6e6");
//		params.addQihuApp("BattleLand", "15448dce69c76e5fcd657c4f7a141631", "5259f12fda7d0cc990ba04b9b0b6f504");	//Appid: 200266196
//		params.addQihuApp("M1", "73ea650556a7bd5a05633388c1de15a7", "5bf6722d5022a24f161d800d3584f30a");			//Appid: 200542601
//		params.addQihuApp("T6", "1391ed3caaf74f60efe420d6b10e50fd", "cf6306985cde4537cef13f5af4d1eb1d");			//Appid: 200626241
//		params.addQihuApp("Mingjiang", "60e3f1b5a6ee08559b84669b6c8e5f10", "ec7a7dc906d1e4e49c1de2a34da7d80f");		//Appid: 201023021
//		params.addQihuApp("Gaoguai", "c1262e00fe079a8a8bbed448a26c36df", "ff1453dd0ebc2b30e032acb1d8de20d1");		//Appid: 201078481
//		params.addQihuApp("T6", "f3a11f58c3f68bef3239e907accebb4c", "c0ca2518950be07f16ec1a3f53b3a2a1");			//Appid: 201117096
//		params.addQihuApp("T6", "a6f904a4f06fe76212f7e761dc06f7b7", "0dc2a35ed72c26a853ac5fd49195b4d3");			//Appid: 201168356
//		params.addQihuApp("xiexuegui", "10804801513f20868b16e1b1731f52c1", "402aeb73b9a3425b1172993bd92ed0c4");		//Appid: 201774721
//		params.addQihuApp("M5", "97e9cf4c13c691f0d1e1e24fe5b5feab", "091cd3d50cc39e24354ecb93fd1d9efb");			//Appid: 201828271
//		params.addQihuApp("qingwa", "9c11195f64315044e8409ac73ba6a5f2", "a316dbadf43d819ac9dd4ed1a363af78");		//Appid: 201875901
//		params.addQihuApp("Qzhuan", "280dd7b40f9576b81b175a1bd0bee989", "a6b84b41c10572fe6c8c74e6147927b3");		//Appid: 202083501
//		params.addQihuApp("M3", "f739cfbb9f9510ef039a3c22e2756e01", "b6c5ec4a0ae5da926170f7c95b447282");			//Appid: 202121406
	}
}

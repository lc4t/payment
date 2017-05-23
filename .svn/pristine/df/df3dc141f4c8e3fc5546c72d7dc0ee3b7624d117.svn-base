package noumena.payment.tencent;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;
import sun.misc.BASE64Encoder;

public class TencentCharge
{
	private static TencentParams params = new TencentParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		TencentCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		OrderIdVO orderIdVO = getTransactionIdVO(order);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static OrderIdVO getTransactionIdVO(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_TENCENT);
		order.setUnit(Constants.CURRENCY_UNIT_JIAO);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_TENCENT;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_TENCENT;
			}
			cburl += "&currency=" + Constants.CURRENCY_TENCENT;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_JIAO;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		return orderIdVO;
	}
	
	public static String checkOrdersStatus(String payIds, TencentOrderVO tencentvo)
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
				int ret = purchase(order, tencentvo);
				
				if (ret == 1)
				{
					//扣款成功
					st.setStatus(1);
				}
				else
				{
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
	
	
	/*
	 * 腾讯的扣除游戏币接口
	 */
	public static int purchase(Orders order, TencentOrderVO ordervo)
	{
		int ret = 2;
		
		StringBuffer sb = new StringBuffer();
		sb.append("openid=").append(ordervo.getOpenid());
		sb.append("&openkey=").append(ordervo.getOpenkey());
		sb.append("&pay_token=").append(ordervo.getPay_token());
		sb.append("&appid=").append(ordervo.getAppid());
		sb.append("&type=").append(ordervo.getType());
		sb.append("&zoneid=").append(order.getSign());
		String exinfo = sb.toString();
		sb.append("&pf=").append(ordervo.getPf());
		sb.append("&pfkey=").append(ordervo.getPfkey());
		
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent purchase params)->" + sb.toString());
		
		try {
			String appid = ordervo.getAppid();
			long ts = Calendar.getInstance().getTimeInMillis()/1000;
			String amt = String.valueOf(order.getAmount().intValue());
			String session_id = "";
			String session_type = "";
			String pay_token = "";
			if (ordervo.getType().equals("qq"))
			{
				session_id = TencentParams.QQ_SESSION_ID;
				session_type = TencentParams.QQ_SESSION_TYPE;
				pay_token = ordervo.getPay_token();
			}
			else
			{
				session_id = TencentParams.WEIXIN_SESSION_ID;
				session_type = TencentParams.WEIXIN_SESSION_TYPE;
				pay_token = null;
			}
			
			String encryptKey = params.getParams(appid).getAppkey() + "&";
			
			String paramstr = "";
			paramstr += "amt=" + amt;
			paramstr += "&appid=" + appid;
			paramstr += "&openid=" + ordervo.getOpenid();
			paramstr += "&openkey=" + ordervo.getOpenkey();
			paramstr += "&pay_token=" + pay_token;
			paramstr += "&pf=" + ordervo.getPf();
			paramstr += "&pfkey=" + ordervo.getPfkey();
			paramstr += "&ts=" + ts;
			paramstr += "&zoneid=" + order.getSign();
			
			String minwen = "";
			minwen += TencentParams.METHOD.toUpperCase();
			minwen += "&" + URLEncoder.encode(TencentParams.PURCHASE_URI, "utf-8").replace("*", "%2A").replace("+", "%20");
			minwen += "&" + URLEncoder.encode(paramstr, "utf-8").replace("*", "%2A").replace("+", "%20");
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent purchase minwen)->" + minwen);
			
			byte[] sigbyte = TencentSignUtils.HmacSHA1Encrypt(minwen, encryptKey);
			BASE64Encoder base64 = new BASE64Encoder();
			String sig = base64.encode(sigbyte);
			sig = URLEncoder.encode(sig, "utf-8").replace("*", "%2A").replace("+", "%20");
			
			String cookiestr = "";
			cookiestr += "session_id=" + URLEncoder.encode(session_id,"utf-8");
			cookiestr += "; session_type=" + URLEncoder.encode(session_type,"utf-8");
			cookiestr += "; org_loc=" + URLEncoder.encode(TencentParams.PURCHASE_URI,"utf-8");
			
			String urlstr = "";
			if (testmode) 
			{
				urlstr = TencentParams.PURCHASE_URL_TEST;
			}
			else
			{
				urlstr = TencentParams.PURCHASE_URL_RELEASE;
			}
			urlstr += "?";
			urlstr += paramstr;
			urlstr += "&sig=" + sig;
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent purchase urlstr)->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Cookie", cookiestr);
			
			connection.connect();
			
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
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent purchase ret)->" + res);
			
			OrdersBean bean = new OrdersBean();
			
			//{"ret" : 0,"billno" : ""}
			JSONObject json = JSONObject.fromObject(res);
			if (json.getInt("ret")==0 && json.getInt("balance")>=0)
			{
				ret = 1;
				bean.updateOrderAmountPayIdExinfo(order.getOrderId(), json.getString("billno"), amt, exinfo + "#" + json.getString("balance"));
				bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
				
				if (appid.equals("1104539253") || appid.equals("wx6292d91d2ddfcf4c"))
				{
					if (json.getInt("balance")>0)
					{
						clearBalance(ordervo, json.getString("balance"), order.getSign());
					}
				}
			}
			else
			{
				bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return ret;
	}
	
	/*
	 * 清空余额
	 */
	public static void clearBalance(TencentOrderVO ordervo, String balance, String zoneid)
	{
		StringBuffer sb = new StringBuffer();
		sb.append("openid=").append(ordervo.getOpenid());
		sb.append("&openkey=").append(ordervo.getOpenkey());
		sb.append("&pay_token=").append(ordervo.getPay_token());
		sb.append("&appid=").append(ordervo.getAppid());
		sb.append("&type=").append(ordervo.getType());
		sb.append("&zoneid=").append(zoneid);
		sb.append("&pf=").append(ordervo.getPf());
		sb.append("&pfkey=").append(ordervo.getPfkey());
		
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent clearBalance params)->" + sb.toString());
		
		try {
			String appid = ordervo.getAppid();
			long ts = Calendar.getInstance().getTimeInMillis()/1000;
			String amt = balance;
			String session_id = "";
			String session_type = "";
			String pay_token = "";
			if (ordervo.getType().equals("qq"))
			{
				session_id = TencentParams.QQ_SESSION_ID;
				session_type = TencentParams.QQ_SESSION_TYPE;
				pay_token = ordervo.getPay_token();
			}
			else
			{
				session_id = TencentParams.WEIXIN_SESSION_ID;
				session_type = TencentParams.WEIXIN_SESSION_TYPE;
				pay_token = null;
			}
			
			String encryptKey = params.getParams(appid).getAppkey() + "&";
			
			String paramstr = "";
			paramstr += "amt=" + amt;
			paramstr += "&appid=" + appid;
			paramstr += "&openid=" + ordervo.getOpenid();
			paramstr += "&openkey=" + ordervo.getOpenkey();
			paramstr += "&pay_token=" + pay_token;
			paramstr += "&pf=" + ordervo.getPf();
			paramstr += "&pfkey=" + ordervo.getPfkey();
			paramstr += "&ts=" + ts;
			paramstr += "&zoneid=" + zoneid;
			
			String minwen = "";
			minwen += TencentParams.METHOD.toUpperCase();
			minwen += "&" + URLEncoder.encode(TencentParams.PURCHASE_URI, "utf-8").replace("*", "%2A").replace("+", "%20");
			minwen += "&" + URLEncoder.encode(paramstr, "utf-8").replace("*", "%2A").replace("+", "%20");
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent clearBalance minwen)->" + minwen);
			
			byte[] sigbyte = TencentSignUtils.HmacSHA1Encrypt(minwen, encryptKey);
			BASE64Encoder base64 = new BASE64Encoder();
			String sig = base64.encode(sigbyte);
			sig = URLEncoder.encode(sig, "utf-8").replace("*", "%2A").replace("+", "%20");
			
			String cookiestr = "";
			cookiestr += "session_id=" + URLEncoder.encode(session_id,"utf-8");
			cookiestr += "; session_type=" + URLEncoder.encode(session_type,"utf-8");
			cookiestr += "; org_loc=" + URLEncoder.encode(TencentParams.PURCHASE_URI,"utf-8");
			
			String urlstr = "";
			if (testmode) 
			{
				urlstr = TencentParams.PURCHASE_URL_TEST;
			}
			else
			{
				urlstr = TencentParams.PURCHASE_URL_RELEASE;
			}
			urlstr += "?";
			urlstr += paramstr;
			urlstr += "&sig=" + sig;
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent clearBalance urlstr)->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Cookie", cookiestr);
			
			connection.connect();
			
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
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(tencent clearBalance ret)->" + res);
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/*
	 * 腾讯的查询余额接口
	 */
	public static int getBalance(String appid, TencentOrderVO ordervo)
	{
		int balance = 0;
		
		try {
			long ts = Calendar.getInstance().getTimeInMillis()/1000;
			String session_id = "";
			String session_type = "";
			String pay_token = "";
			if (ordervo.getType().equals("qq"))
			{
				session_id = TencentParams.QQ_SESSION_ID;
				session_type = TencentParams.QQ_SESSION_TYPE;
				pay_token = ordervo.getPay_token();
			}
			else
			{
				session_id = TencentParams.WEIXIN_SESSION_ID;
				session_type = TencentParams.WEIXIN_SESSION_TYPE;
				pay_token = null;
			}
			
			String encryptKey = params.getParams(appid).getAppkey() + "&";
			
			String paramstr = "";;
			paramstr += "appid=" + appid;
			paramstr += "&openid=" + ordervo.getOpenid();
			paramstr += "&openkey=" + ordervo.getOpenkey();
			paramstr += "&pay_token=" + pay_token;
			paramstr += "&pf=" + ordervo.getPf();
			paramstr += "&pfkey=" + ordervo.getPfkey();
			paramstr += "&ts=" + ts;
			paramstr += "&zoneid=" + ordervo.getZoneid();
			
			String minwen = "";
			minwen += TencentParams.METHOD.toUpperCase();
			minwen += "&" + URLEncoder.encode(TencentParams.GETBALANCE_URI, "utf-8").replace("*", "%2A").replace("+", "%20");
			minwen += "&" + URLEncoder.encode(paramstr, "utf-8").replace("*", "%2A").replace("+", "%20");
			
			System.out.println("tencent getBalance minwen-->"+ minwen);
			
			byte[] sigbyte = TencentSignUtils.HmacSHA1Encrypt(minwen, encryptKey);
			BASE64Encoder base64 = new BASE64Encoder();
			String sig = base64.encode(sigbyte);
			sig = URLEncoder.encode(sig, "utf-8").replace("*", "%2A").replace("+", "%20");
			
			String cookiestr = "";
			cookiestr += "session_id=" + URLEncoder.encode(session_id,"utf-8");
			cookiestr += "; session_type=" + URLEncoder.encode(session_type,"utf-8");
			cookiestr += "; org_loc=" + URLEncoder.encode(TencentParams.GETBALANCE_URI,"utf-8");
			
			String urlstr = "";
			if (testmode) 
			{
				urlstr = TencentParams.GETBALANCE_URL_TEST;
			}
			else
			{
				urlstr = TencentParams.GETBALANCE_URL_RELEASE;
			}
			urlstr += "?";
			urlstr += paramstr;
			urlstr += "&sig=" + sig;
			
			System.out.println("tencent getBalance urlstr-->"+ urlstr);			
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Cookie", cookiestr);
			
			connection.connect();
			
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
			
			System.out.println("tencent getBalance ret-->"+ res);
			
			//{"ret" : 0,"balance" : ""}
			JSONObject json = JSONObject.fromObject(res);
			if (json.getInt("ret")==0)
			{
				balance = json.getInt("balance");
			}
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return balance;
	}
	
	public static String getTransactionIdAndStatus(Orders vo,TencentOrderVO tencentvo)
	{
		OrderIdVO orderIdVO = getTransactionIdVO(vo);
		String payIds = "";
		if (orderIdVO != null)
		{
			payIds = orderIdVO.getPayId();
		}
		return checkOrdersStatus(payIds,tencentvo);
	}

	public static void init()
	{
		params.initParams(TencentParams.CHANNEL_ID, new TencentParamsVO());
	}
}

/*package noumena.payment.weixin;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.userverify.util.TrustAnyTrustManager;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class CopyOfWeixinCharge
{
	private static String accessToken = "";		//微信支付的accesstoken
	private static long getTokenTime = 0l;		//获取accesstoken的时间，单位秒
	private static int tokenExpire 	= 7000;		//accesstoken持续时间，实际7200秒
	
	private static WeixinParams params = new WeixinParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		CopyOfWeixinCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order, String body, String remoteip)
	{
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_FEN);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_WEIXIN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_WEIXIN;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		String prepayid = getPrepayID(order, body, remoteip);
		orderIdVO.setMsg(prepayid);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	private static String getPackage_1(Orders order, String body, String remoteip)
	{
		String packagestr = "";
		packagestr += "bank_type=WX";
		packagestr += "&body=" + body;
		packagestr += "&fee_type=1";
		packagestr += "&input_charset=UTF-8";
		packagestr += "&notify_url=" + WeixinParams.NOTIFY_URL;
		packagestr += "&out_trade_no=" + order.getOrderId();
		packagestr += "&partner=" + WeixinParams.PARTNER_ID;
		packagestr += "&spbill_create_ip=" + remoteip;
		packagestr += "&total_fee=" + (int) (order.getAmount() * 100);
		
		return packagestr;
	}
	
//	private static String getPackage_2(String packagestr)
//	{
//		packagestr += "&key=" + WeixinParams.PARTNER_KEY;
//		packagestr = StringEncrypt.Encrypt(packagestr);
//		packagestr = packagestr.toUpperCase();
//
//		return packagestr;
//	}
	
	private static String getPackage_3(Orders order, String body, String remoteip)
	{
		String packagestr = "";
		try
		{
			packagestr += "bank_type=WX";
			packagestr += "&body=" + URLEncoder.encode(body, "utf-8");
			packagestr += "&fee_type=1";
			packagestr += "&input_charset=UTF-8";
			packagestr += "&notify_url=" + URLEncoder.encode(WeixinParams.NOTIFY_URL, "utf-8");
			packagestr += "&out_trade_no=" + order.getOrderId();
			packagestr += "&partner=" + WeixinParams.PARTNER_ID;
			packagestr += "&spbill_create_ip=" + remoteip;
			packagestr += "&total_fee=" + (int) (order.getAmount() * 100);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return packagestr;
	}
	
	private static String getPackage_4(String packagestr, String sign)
	{
		packagestr += "&sign=" + sign;

		return packagestr;
	}
	
	private static String getAppSign_1(String appid, String noncestr, String packagestr, String timestamp, String traceid)
	{
		String appsignstr = "";
		appsignstr += "appid=" + appid;
		appsignstr += "&appkey=" + params.getAppSignKeyById(appid);
		appsignstr += "&noncestr=" + noncestr;
		appsignstr += "&package=" + packagestr;
		appsignstr += "&timestamp=" + timestamp;
		appsignstr += "&traceid=" + traceid;
		
		return appsignstr;
	}
	
	private static String getPrepayID(Orders order, String body, String remoteip)
	{
		String accesstoken = getAccessToken(order);
		String prepayid = "";
		long now = System.currentTimeMillis() / 1000;
		try
		{
			String packagestr = "", sign = "";
			packagestr = getPackage_1(order, body, remoteip);
			sign = getPackage_2(packagestr);
			packagestr = getPackage_3(order, body, remoteip);
			packagestr = getPackage_4(packagestr, sign);
			
			String appsign = getAppSign_1(order.getSign(), order.getOrderId(), packagestr, now + "", order.getOrderId());
			appsign = Sha1Util.getSha1(appsign);
			
			String urlstr = "https://api.weixin.qq.com/pay/genprepay?access_token=" + accesstoken;
			JSONObject json = new JSONObject();
			json.accumulate("appid", order.getSign());
			json.accumulate("traceid", order.getOrderId());
			json.accumulate("noncestr", order.getOrderId());
			json.accumulate("timestamp", now);
			json.accumulate("package", packagestr);
			json.accumulate("sign_method", "sha1");
			json.accumulate("app_signature", appsign);
			String content = json.toString();

			System.out.println("weixin prepay url ->" + urlstr);
			System.out.println("weixin prepay content ->" + content);

			SSLContext context = SSLContext.getInstance("SSL");
			context.init
				(
					null,
					new TrustManager[]
					{
						new TrustAnyTrustManager()
					},
					new java.security.SecureRandom()
				);
			HttpsURLConnection connection = (HttpsURLConnection) new URL(urlstr).openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
				(
					new HostnameVerifier()
					{
						@Override
						public boolean verify(String arg0, SSLSession arg1)
						{
							return true; //不验证
							//return false; // 验证
						}
					}
				);

			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream(), "utf8")
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
	
			connection.disconnect();
	
			System.out.println("weixin accesstoken ret ->" + res);
//			{"prepayid":"1201000000141126148dbd023a544744","errcode":0,"errmsg":"Success"}
			JSONObject json1 = JSONObject.fromObject(res);
			prepayid = json1.getString("prepayid");
			
			System.out.println("weixin prepay prepayid ->" + prepayid);
			
			json.clear();
			json.accumulate("noncestr", order.getOrderId());
			json.accumulate("prepayid", prepayid);
			json.accumulate("timestamp", now);
			prepayid = json.toString();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return prepayid;
	}
	
	private static String getAccessToken(Orders order)
	{
		long now = (System.currentTimeMillis() / 1000);
		if (accessToken.equals("") || (now - getTokenTime) > tokenExpire)
		{
			try
			{
				String urlstr = "https://api.weixin.qq.com/cgi-bin/token";
				urlstr += "?grant_type=client_credential";
				urlstr += "&appid=" + order.getSign();
				urlstr += "&secret=" + params.getAppSecretKeyById(order.getSign());
				
				System.out.println("weixin accesstoken url ->" + urlstr);

				SSLContext context = SSLContext.getInstance("SSL");
				context.init
					(
						null,
						new TrustManager[]
						{
							new TrustAnyTrustManager()
						},
						new java.security.SecureRandom()
					);
				HttpsURLConnection connection = (HttpsURLConnection) new URL(urlstr).openConnection();
				connection.setSSLSocketFactory(context.getSocketFactory());
				connection.setHostnameVerifier
					(
						new HostnameVerifier()
						{
							@Override
							public boolean verify(String arg0, SSLSession arg1)
							{
								return true; //不验证
								//return false; // 验证
							}
						}
					);

//				connection.setDoOutput(true);
				connection.setUseCaches(false);
				connection.setDoInput(true);
//				connection.setRequestProperty("Content-type", "application/json");
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestMethod("GET");
				connection.connect();
				
//				OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
//				outs.write(content);
//				
//				outs.flush();
//				outs.close();
				
				BufferedReader in = new BufferedReader
					(
						new InputStreamReader(connection.getInputStream(), "utf8")
					);
				String res = "", line = null;
				while ((line = in.readLine()) != null)
				{
					res += line;
				}
		
				connection.disconnect();
		
				System.out.println("weixin accesstoken ret ->" + res);
//				{"access_token":"ACCESS_TOKEN","expires_in":7200}
//				{"errcode":40013,"errmsg":"invalid appid"}
				JSONObject json = JSONObject.fromObject(res);
				accessToken = json.getString("access_token");
				tokenExpire = json.getInt("expires_in");
				getTokenTime = now;
			}
			catch (Exception e)
			{
				e.printStackTrace();
				accessToken = "";
				tokenExpire = 0;
			}
		}
		return accessToken;
	}
	
	public static String checkOrdersStatus(String payIds)
	{
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0 ; i < orders.size() ; i++)
		{
			try
			{
				Orders order = orders.get(i);
				int status = order.getKStatus();
				OrderStatusVO st = new OrderStatusVO();
				st.setPayId(order.getOrderId());
				if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
				{
					//如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
					Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
					Calendar cal2 = Calendar.getInstance();
					if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
					{
						st.setStatus(4);
					}
					else
					{
						st.setStatus(3);
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
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}
	
	public static String getCallbackFromWeixin(Map<String,Object> weixinparams)
	{
		System.out.println("weixin cb->" + weixinparams.toString());
		String ret = "success";
		String orderid = "";
		String sporderid = "";
		String orderAmount = "";
		String sign = "";
		String orderstatus = "";
		
		try
		{
			orderid = (String) weixinparams.get("out_trade_no");
			sporderid = (String) weixinparams.get("transaction_id");
			orderAmount = (String) weixinparams.get("total_fee");
			sign = (String) weixinparams.get("sign");
			orderstatus = (String) weixinparams.get("trade_state");

			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
//				if (sign.equals(miwen))
				{
					if (orderstatus.equals("0"))
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							String exinfo = "";
							exinfo += "bank_type:" + (String) weixinparams.get("bank_type") + "|";
							exinfo += "bank_billno:" + (String) weixinparams.get("bank_billno") + "|";
							exinfo += "notify_id:" + (String) weixinparams.get("notify_id") + "|";
							exinfo += "discount:" + (String) weixinparams.get("discount") + "|";
							bean.updateOrderAmountPayIdExinfo(orderid, sporderid, orderAmount, exinfo);
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("weixin order (" + order.getOrderId() + ") had been succeed");
						}
					}
					else
					{
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
					}
				}
//				else
//				{
//					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
//				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("weixin cb->" + weixinparams.toString());
		
		String path = OSUtil.getRootPath() + "../../logs/weixincb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, weixinparams.toString());
		
		return ret;
	}
	
	public static OrderIdVO getTransactionIdVO(Orders order)
	{
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
				cburl += "?pt=" + order.getPayType();
			}
			else
			{
				cburl += "&pt=" + order.getPayType();
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}
	
	public static void init()
	{
		//accessToken = "ZcKPcsmSdXTNMOeHED5q-Ss6B0z3qY7ERFBlpoia3WaluylxNIv75tZHRpymGZWDj9jJ-gcG0SRvy6CBwqc3IC05hVwwlLBLGBsALGd6xQM";
		params.addApp("m1", "wx8a0c851cd4d1278d", "c1f066a5718d5dc76993e8fbc0f3c9c3", "oq32W8jIiQX7gpo60CrIOeKRlaSMQzxHuOF9Yvom4re1bwIj5f12czMwM2EY6PkpWsGxkMBZRVRmP56tL79Le73uhX18P6tNNS7dIDqVqsqetpH29QzKMKrglSMwH6mS");	//口袋战争
//		params.addApp("m1", "wxd930ea5d5a258f4f", "c1f066a5718d5dc76993e8fbc0f3c9c3", "L8LrMqqeGRxST5reouB0K66CaYAWpqhAVsq7ggKkxHCOastWksvuX1uvmvQclxaHoYd3ElNBrNO2DHnnzgfVG9Qs473M3DTOZug5er46FhuGofumV8H2FVR9qkjSlC5K");	//口袋战争
	}
	
	public static String getSha1(String str) {
		if (str == null || str.length() == 0) {
			return null;
		}
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };

		try {
			MessageDigest mdTemp = MessageDigest.getInstance("SHA1");
			mdTemp.update(str.getBytes("GBK"));

			byte[] md = mdTemp.digest();
			int j = md.length;
			char buf[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = md[i];
				buf[k++] = hexDigits[byte0 >>> 4 & 0xf];
				buf[k++] = hexDigits[byte0 & 0xf];
			}
			return new String(buf);
		} catch (Exception e) {
			return null;
		}
	}
	
	private static String getCbSign(Orders order, String body, String remoteip)
	{
		String packagestr = "";
		packagestr += "bank_type=WX";
		packagestr += "&body=" + body;
		packagestr += "&fee_type=1";
		packagestr += "&input_charset=UTF-8";
		packagestr += "&notify_url=" + WeixinParams.NOTIFY_URL;
		packagestr += "&out_trade_no=" + order.getOrderId();
		packagestr += "&partner=" + WeixinParams.PARTNER_ID;
		packagestr += "&spbill_create_ip=" + remoteip;
		packagestr += "&total_fee=" + (int) (order.getAmount() * 100);
		
		return packagestr;
	}
	
	private static String getPackage_2(String packagestr)
	{
		packagestr += "&key=" + WeixinParams.PARTNER_KEY;
		packagestr = StringEncrypt.Encrypt(packagestr);
		packagestr = packagestr.toUpperCase();

		return packagestr;
	}
	
	public static void main(String[] args)
	{
		init();
		Orders order = new Orders();
		order.setSign("wx8a0c851cd4d1278d");
		order.setAmount((float) 0.01);
		order.setOrderId("13");
//		getAccessToken(order);
//		getAccessToken(order);
//		getAccessToken(order);
//		getAccessToken(order);
//		getAccessToken(order);
//		JSONObject json = new JSONObject();
//		json.accumulate("appid", "xxx");
//		System.out.println("json->" + json.toString());
//		
		accessToken = "SOPyHHK_WEM8XN8ke1iYkDwS0MvUMFK8tpRT4RYyZGqJG5xqBuSCz47NH4ZXB_G7F-KNjiSyq8sSBTdeW4ig3ufgdEeYt41aRZHpjh_eLM4";
		String packagestr = "", sign = "";
		packagestr = getPackage_1(order, "安卓签名测试", "196.168.1.1");
		System.out.println("packagestr1->" + packagestr);
		sign = getPackage_2(packagestr);
		System.out.println("packagestr2->" + sign);
		packagestr = getPackage_3(order, "安卓签名测试", "196.168.1.1");
		System.out.println("packagestr3->" + packagestr);
		packagestr = getPackage_4(packagestr, sign);
		System.out.println("packagestr4->" + packagestr);
		String appsign = getAppSign_1("wx8a0c851cd4d1278d", "139042a4157a773f209847829d80894d", "", "1456736960", "Sign=Wxpay");
		String appsign1 = Sha1Util.getSha1(appsign);
//		appsign = StringEncrypt.EncryptSHA256(appsign);
		try
		{
//			appsign = URLDecoder.decode(appsign, "utf-8");
			appsign = getSha1(appsign);
			System.out.println("appsign->" + appsign);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}
*/
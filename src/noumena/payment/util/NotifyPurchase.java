package noumena.payment.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;

public class NotifyPurchase 
{
	private static boolean notifymode = false;
	
	public static boolean isNotifymode() 
	{
		return notifymode;
	}
	public static void setNotifymode(boolean notifymode) 
	{
		NotifyPurchase.notifymode = notifymode;
	}

	/**
	 * 利用渠道号的第六位开始的连续三位判断
	 * 代理公司，根据需要进行通知
	 */
	public static void notify(String orderid)
	{
		try {
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			//AHM - incross(韩国)
			if (order.getChannel() != null && order.getChannel().length() >= 9)
			{
				if (order.getChannel().substring(5,8).equals("AHM"))
				{
					//通知kakao成功的订单
					notifykakao(order);
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private static void notifykakao(Orders ordervo)
	{
		try {
			String urlstr = "https://gameapi.kakao.com/payment_v1/payments.json";
			String client_id = "";
			String secret_key = "";
			//126 - M5
			if (ordervo.getChannel().substring(0, 3).equals("126")) {
				client_id = "93111343632426851";
				secret_key = "ae87e06551cd1224a52e801530772c53ec7cb2c3ca7ef73fdc871a854454c203";
			}
			String platform = "";
			String os = "";
			String currency = "";
			int payType = Integer.parseInt(ordervo.getPayType());
			switch (payType) 
			{
				case 5000:
					platform = "apple";
					os = "ios";
					currency = "USD";
					break;
				case 5015:
					platform = "tstore";
					os = "android";
					currency = "KRW";
					break;
				case 5070:
					platform = "nstore";
					os = "android";
					currency = "KRW";
					break;
				case 5113:
					platform = "google";
					os = "android";
					currency = "KRW";
					break;
			}
			
			String urlParameters = "";
			urlParameters += "client_id=";
			urlParameters += client_id;
			urlParameters += "&secret_key=";
			urlParameters += secret_key;
			urlParameters += "&service_user_id=";
			urlParameters += ordervo.getUId();
			urlParameters += "&platform=";
			urlParameters += platform;
			urlParameters += "&os=";
			urlParameters += os;
			urlParameters += "&price=";
			urlParameters += ordervo.getMoney();
			urlParameters += "&currency="; //KRW - 韩元; USD - 美元; JPY - 日元
			urlParameters += currency;
			urlParameters += "&buy_no=";
			urlParameters += ordervo.getOrderId();
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kakao)-> send purchase info to kakao urlstr "+urlstr);
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kakao)-> send purchase info to kakao urlParameters "+urlParameters);
			
			if (!notifymode) 
			{
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
				connection.setInstanceFollowRedirects(false);
				connection.setRequestMethod("POST");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
				connection.setRequestProperty("charset", "utf-8");
				
				connection.connect();
				
				OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
				outs.write(urlParameters);
				
				outs.flush();
				outs.close();
				
				
				BufferedReader in = new BufferedReader
					(
						new InputStreamReader(connection.getInputStream())
					);
				String res = "", line = null;
				while ((line = in.readLine()) != null)
				{
					res += line;
				}
				in.close();
				connection.disconnect();
				
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kakao)-> send purchase info to kakao ret "+res);
			}			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}
}

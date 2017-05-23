package noumena.payment.anqu;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.caishen.CaishenOrderVO;
import noumena.payment.lenovo.util.Base64;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.util.TrustAnyTrustManager;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class AnquCharge
{
	private static AnquParams params = new AnquParams();
	
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
				cburl += "?pt=" + Constants.PAY_TYPE_ANQU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_ANQU;
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
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}
	
	public static String getCallbackFromAnqu(Map<String,String> anquparams)
	{
		String ret = "success";
		System.out.println("anqu cb ->" + anquparams.toString());
		AnquOrderVO covo = new AnquOrderVO();
			covo.setCpappid(anquparams.get("cpappid"));
			covo.setCporder(anquparams.get("cporder"));
			covo.setMoney(anquparams.get("money"));
			covo.setOrder(anquparams.get("order"));
			covo.setUid(anquparams.get("uid"));
		String miwen = "";
		miwen+=covo.getUid()+covo.getCporder()+covo.getMoney()+covo.getOrder()+params.getParams(covo.getCpappid()).getAppkey();
		String sign = StringEncrypt.Encrypt(miwen);
		OrdersBean bean = new OrdersBean();
			try {
				if(sign.equals(anquparams.get("sign"))){
					// 支付成功
					Orders order = bean.qureyOrder(covo.getCporder());
					if (order != null) {
						//if(validateSign(covo)){
							if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
								bean.updateOrderAmountPayIdExinfo(covo.getCporder(), covo.getOrder(), covo.getMoney(), "");
								bean.updateOrderKStatus(covo.getCporder(),Constants.K_STSTUS_SUCCESS);
							} else {
								System.out.println("anqu order ("+ order.getOrderId() + ") had been succeed");
							}
						//}else{
						//	System.out.println("anqu order ("+ order.getOrderId() + ") search is error");
						//}
					}
				}else{
					System.out.println("anqu order ("+ covo.getCporder() + ") sign is error");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("anqu cb ret->" + ret);

			String path = OSUtil.getRootPath() + "../../logs/anqucb/"
					+ DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + covo.getCporder();

			OSUtil.saveFile(filename, anquparams.toString());
		return ret;
	}
	public static boolean validateSign(AnquOrderVO covo){
		String res = "";
		StringBuffer sb = new StringBuffer();
		sb.append("cpappid=").append(covo.getCpappid()).append("&");
		sb.append("uid=").append(covo.getUid()).append("&");
		sb.append("money=").append(covo.getMoney()).append("&");
		sb.append("anquorderid=").append(covo.getOrder()).append("&");
		sb.append("sign=").append(StringEncrypt.Encrypt(covo.getUid()+covo.getCpappid()+covo.getMoney()+covo.getOrder()+params.getParams(covo.getCpappid()).getAppkey()));
		try {
		URL	url = new URL("http://i.anqu.com/index.php/searchOrder/index?"+sb.toString());
		System.out.println(url);
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setInstanceFollowRedirects(false);
		connection.setUseCaches(false);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestMethod("GET");
		connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
		connection.setRequestProperty("charset", "utf-8");
		
		connection.connect();
		
		DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
		wr.flush();
		wr.close();
		
		BufferedReader in = new BufferedReader
			(
				new InputStreamReader(connection.getInputStream())
			);
		String line = null;
		while ((line = in.readLine()) != null)
		{
			res += line;
		}
		connection.disconnect();
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(res);
		if("success".equals(res.trim())){
			System.out.println("anqu true"+res.trim());
			return true;
		}else{
			System.out.println("anqu false"+res.trim());
			return false;
		}
		
	}
	
	public static void init()
	{
		params.initParams(AnquParams.CHANNEL_ID, new AnquParamsVO());
	}
	
	public static void main(String args[]){//0c8865930fc94d8d61b1bdcc8312a65d
		String res="";
		try {
			URL	url = new URL("http://i.anqu.com/index.php/searchOrder/index?cpappid=G100243&uid=747628&money=6&anquorderid=774590000317568&sign=a1cb1165b6640104f96501a47cf5b4ae");
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setUseCaches(false);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream())
				);
			String line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			connection.disconnect();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(res);
			if("success".equals(res.trim())){
				System.out.println(1);
			}else{
				System.out.println(2);
			}
	}

}

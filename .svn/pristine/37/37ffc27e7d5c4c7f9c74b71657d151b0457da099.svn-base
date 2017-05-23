package noumena.payment.duoku;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
import sun.misc.BASE64Decoder;

public class DuokuCharge
{
	private static DuokuParams params = new DuokuParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		DuokuCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_DUOKU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_DUOKU;
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
	
	public static String getCallbackFromDuoku(DuokuOrderVO duokuvo)
	{
		System.out.println("duoku cb info ---->"+duokuvo.toString());
    	String minwen = "";
    	String miwen = "";
    	String orderid = duokuvo.getCooperatorOrderSerial();
    	String content = duokuvo.getContent();
    	BASE64Decoder base = new BASE64Decoder();
    	try {
			content = new String(base.decodeBuffer(content));
		} catch (IOException e1) {
			e1.printStackTrace();
		}
    	String appid = duokuvo.getAppID();
    	JSONObject json = JSONObject.fromObject(content);  	
    	
    	minwen = appid;
    	minwen += duokuvo.getOrderSerial();
    	minwen += orderid;
    	minwen += duokuvo.getContent();
    	minwen += params.getParams(appid).getAppkey();
    	
    	miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
    	
    	String ret = "";
    	String resultCode = "1";
    	String resultMsg = "success";
    	
    	if (miwen.equals(duokuvo.getSign()))
		{
			//服务器签名验证成功
			
			OrdersBean bean = new OrdersBean();
			try
			{
//				checkOrder(orderid,appid);
				if (json.getString("OrderStatus").equals("1")) {
					//支付成功
					Orders order = bean.qureyOrder(orderid);
					if (order != null)
					{
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(orderid, duokuvo.getOrderSerial(), json.getString("OrderMoney"), json.getString("UID"));
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("duoku order (" + order.getOrderId() + ") had been succeed");
						}
					}
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			// 服务器签名验证失败
			resultCode = "0";
			resultMsg = "fail";
		}
    	
    	String retSign = "";
    	retSign  = duokuvo.getAppID();
    	retSign += resultCode;
    	retSign += params.getParams(appid).getAppkey();
    	retSign = StringEncrypt.Encrypt(retSign).toLowerCase();
    	
    	ret = "{\"AppID\":\""+appid+"\",\"ResultCode\":\""+resultCode+"\",\"ResultMsg\":\""+resultMsg+"\",\"Sign\":\""+retSign+"\"}";

		System.out.println("duoku cb content->" + content);
		System.out.println("duoku cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/duokucb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, duokuvo.toString());
		
		return ret;
	}
	public static void checkOrder(String orderid,String appid){
		JSONObject json = null;
		String sign ="";
		sign = appid;
		sign += orderid;
		sign += params.getParams(appid).getAppkey();
		sign = StringEncrypt.Encrypt(sign);

		String urlstr = "http://querysdkapi.91.com/ CpOrderQuery.ashx";
		String urlParameters = "AppID=" + appid + "&=CooperatorOrderSerial="+orderid+"&OrderType=1&Sign="+sign+"&Action=10002";
		System.out.println("duoku request  urlParameters--------->" + urlParameters);
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setUseCaches(false);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type","application/x-www-form-urlencoded");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", ""+ Integer.toString(urlParameters.getBytes().length));

			connection.connect();

			DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			connection.disconnect();
			json = JSONObject.fromObject(res);
			System.out.println("duoku get info res---->"+json.toString());

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void init()
	{
		params.initParams(DuokuParams.CHANNEL_ID, new DuokuParamsVO());
//		params.addApp("t6", "1452", "4aba057a5ec082fc2ade563d04ba1bf2");
//		params.addApp("hero", "1700", "3eb2850086599b27468a02a00a142801");
//		params.addApp("gaoguai", "1885", "358183db50d9998b2dae088ac11ff287");
//		params.addApp("t8", "2981059", "c694356990bbb0e51c7edc3b01a6fbce");
//		params.addApp("m5", "3488535", "XSZlWP4Z4GLnUanGd9QfWClgrMjsGwNO");
//		params.addApp("qingwa", "4435670", "2OQw5LHEEeZVqni3QWAeDAPUKVg3xqDp");
//		params.addApp("qzhuan", "4759859", "UzZtqdGpbL9FHKEEa8KwWuNPEnGd24FN");	//群英Q传
//		params.addApp("m3", "4820921", "DbCgokHGtRSzGP72Qh0kcKpOVxwQni4Q");		//M3
	}
}

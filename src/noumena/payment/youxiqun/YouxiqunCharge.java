package noumena.payment.youxiqun;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
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

public class YouxiqunCharge
{
	private static YouxiqunParams params = new YouxiqunParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		YouxiqunCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_YOUXIQUN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_YOUXIQUN;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		String prepayid = getPrepayid(order);
		orderIdVO.setMsg(prepayid);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	private static String getPrepayid(Orders order)
	{
		String prepayid = "";
		try 
		{
			long ts = Calendar.getInstance().getTimeInMillis()/1000;
			String appid = order.getSign();
			String callback_url = "";
			if (testmode) 
			{
				callback_url = YouxiqunParams.CALLBACK_URL_TEST;
			}
			else
			{
				callback_url = YouxiqunParams.CALLBACK_URL_RELEASE;
			}
			
			String minwen = "";
			minwen += "amount=";
			minwen += order.getAmount().intValue();
			minwen += "&app_order_id=";
			minwen += order.getOrderId();
			minwen += "&app_user_id=";
			minwen += order.getUId();
			minwen += "&notify_url=";
			minwen += callback_url;
			minwen += "&timestamp=";
			minwen += ts;
			minwen += "&client_secret=";
			minwen += params.getParams(appid).getSecretkey();
			
			String sign = StringEncrypt.Encrypt(minwen).toLowerCase();
			
			String urlstr = YouxiqunParams.CREATE_ORDER_URL;
			
			String urlParameters = minwen;
			urlParameters += "&access_token=";
			urlParameters += order.getExInfo();
			urlParameters += "&client_id=";
			urlParameters += appid;
			urlParameters += "&sign=";
			urlParameters += sign;
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> create order urlParameters -->" + urlParameters);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
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
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> create order ret -->" + res);
			
			JSONObject json = JSONObject.fromObject(res);			
			prepayid = json.getString("serial");
			
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
		return prepayid;
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
	
	public static String getCallbackFromYouxiqun(Map<String,String> youxiqunparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun cb params)->" + youxiqunparams.toString());
		
    	String ret = "success";
    	
    	try {
    		JSONObject json = JSONObject.fromObject(youxiqunparams);
        	YouxiqunOrderVO ordervo = (YouxiqunOrderVO)JSONObject.toBean(json,YouxiqunOrderVO.class);
        	String orderid = ordervo.getApp_order_id();
        	
        	if (ordervo.getStatus().equals("success"))
        	{	
        		//订单支付完成
        		OrdersBean bean = new OrdersBean();
    			Orders order = bean.qureyOrder(orderid);
    			
    			if (order != null) 
    			{
    				String appid = order.getSign();
    				
    				//检验订单
    				String status = checkOrder(ordervo, appid);
    				
    				if (status.equals("success")) 
    				{
    					String content = "";
        				content += "amount=";
        				content += ordervo.getAmount();
        				content += "&app_order_id=";
        				content += orderid;
        				content += "&app_user_id=";
        				content += ordervo.getApp_user_id();
        				content += "&serial=";
        				content += ordervo.getSerial();
        				content += "&status=";
        				content += ordervo.getStatus();
        				content += "&client_secret=";
        				content += params.getParams(appid).getSecretkey();
        				
        				String sign = StringEncrypt.Encrypt(content).toLowerCase();
        				
        				if (sign.equals(ordervo.getSign())) 
        				{
        					//验签成功
        					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
        					{
        						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getSerial(), ordervo.getAmount(), "");
        						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
        					}
        					else
        					{
        						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> order (" + orderid + ") had been succeed");
        					}
        				}
        				else
        				{
        					//验签失败
        					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> sign is error");
        					bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getSerial(), ordervo.getAmount(), "");
    						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
        					ret = "fail";
        				}
					}
    				else
    				{
    					//订单未支付
    	        		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> order (" + orderid + ") status is unpaid");
    					ret = "fail";
					}
    				
    				String path = OSUtil.getRootPath() + "../../logs/youxiquncb/" + DateUtil.getCurTimeStr().substring(0, 8);
    				OSUtil.makeDirs(path);
    				String filename = path + "/" + orderid;
    				
    				OSUtil.saveFile(filename, youxiqunparams.toString());
    			}
			}
        	else
        	{	
        		//订单未支付
        		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> order (" + orderid + ") status is unpaid");
				ret = "fail";
			}
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun cb ret)->" + ret);

		return ret;
	}
	
	public static String checkOrder(YouxiqunOrderVO ordervo, String appid)
	{
		String status = "";
		try
		{
			String content = "";
			content += "amount=";
			content += ordervo.getAmount();
			content += "&app_order_id=";
			content += ordervo.getApp_order_id();
			content += "&serial=";
			content += ordervo.getSerial();
			content += "&client_secret=";
			content += params.getParams(appid).getSecretkey();
			
			String sign = StringEncrypt.Encrypt(content).toLowerCase();
			
			String urlstr = YouxiqunParams.CHECK_ORDER_URL;
			String urlParameters = "serial=%s&client_id=%s&client_secret=%s&amount=%s&app_order_id=%s&sign=%s";
			urlParameters = String.format(urlParameters, ordervo.getSerial(), appid, params.getParams(appid).getSecretkey(), ordervo.getAmount(), ordervo.getApp_order_id(), sign);
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> check order urlParameters -->" + urlParameters);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
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
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> check order ret -->" + res);
			
//			{"status":"success"}
			JSONObject json = JSONObject.fromObject(res);
			status = json.getString("status");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return status;
	}
	
	public static String getAccessToken(String appid, String code )
	{
		JSONObject json = null;
		try
		{
			String urlstr = YouxiqunParams.GET_TOKEN_URL;
			String urlParameters = "client_id=%s&client_secret=%s&grant_type=%s&code=%s";
			urlParameters = String.format(urlParameters, appid, params.getParams(appid).getSecretkey(), YouxiqunParams.GRANT_TYPE, code);
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> get access_token urlParameters -->" + urlParameters);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
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
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(youxiqun)-> get access_token ret -->" + res);
			
//			{"access_token":"","refresh_token":"","expires_in":""}
			json = JSONObject.fromObject(res);
			String access_token = json.getString("access_token");
			json.clear();
			json.put("token", access_token);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return json.toString();
	}

	public static void init()
	{
		params.initParams(YouxiqunParams.CHANNEL_ID, new YouxiqunParamsVO());
	}
	
}

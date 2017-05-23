package noumena.payment.tag;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Set;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderStatusVO;

public class TAGCharge
{
	private static TAGParams params = new TAGParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		TAGCharge.testmode = testmode;
	}
	
//	public static String getTransactionId(Orders order)
//	{
//		OrdersBean bean = new OrdersBean();
//		String cburl = order.getCallbackUrl();
//		String payId;
//		if (cburl == null || cburl.equals(""))
//		{
//			payId = bean.CreateOrder(order);
//		}
//		else
//		{
//			if (cburl.indexOf("?") == -1)
//			{
//				cburl += "?pt=" + Constants.PAY_TYPE_TAG;
//			}
//			else
//			{
//				cburl += "&pt=" + Constants.PAY_TYPE_TAG;
//			}
//			payId = bean.CreateOrder(order, cburl);
//		}
//		order.setCallbackUrl(cburl);
//		String date = DateUtil.formatDate(order.getCreateTime());
//		OrderIdVO orderIdVO = new OrderIdVO(String.valueOf(payId), date);
//		
//		JSONObject json = JSONObject.fromObject(orderIdVO);
//		return json.toString();
//	}
//	
//	
//	public static String checkOrdersStatus(String payIds)
//	{
//		String[] orderIds = payIds.split(",");
//
//		OrdersBean bean = new OrdersBean();
//		List<Orders> orders = bean.qureyOrders(orderIds);
//		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
//		for (int i = 0 ; i < orders.size() ; i++)
//		{
//			Orders order = orders.get(i);
//			int status = order.getKStatus();
//			OrderStatusVO st = new OrderStatusVO();
//			st.setPayId(order.getOrderId());
//			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
//			{
//				//如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
//				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
//				Calendar cal2 = Calendar.getInstance();
//				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
//				{
//					st.setStatus(4);
//				}
//				else
//				{
//					st.setStatus(3);
//				}
//			}
//			else if (status == Constants.K_STSTUS_SUCCESS)
//			{
//				//如果订单已经成功，直接返回订单状态
//				st.setStatus(1);
//			}
//			else
//			{
//				//订单已经失败，直接返回订单状态
//				st.setStatus(2);
//			}
//			statusret.add(st);
//		}
//		JSONArray arr = JSONArray.fromObject(statusret);
//		
//		return arr.toString();
//	}
	
	public static String getTransactionIdAndStatus(Orders vo, String prid)
	{
		return checkOrderFromTAG(vo, prid);
	}
	
	public static String checkOrderFromTAG(Orders vo, String prid)
	{
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		try 
		{
			String urlstr = "";
			if (testmode) 
			{
				urlstr = TAGParams.TAG_VERIFY_URL_TEST;
			}
			else 
			{
				urlstr = TAGParams.TAG_VERIFY_URL_RELEASE;
			}
			urlstr = String.format(urlstr, prid, vo.getUId());
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(TAG)-> check order urlstr -->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
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
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(TAG)-> check order ret -->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			//{"resultCode":"0000","resultMsg":"OK","result":{"uid":"USER1","prid":"1","reward_item":"{GOLD:1000}","join_time":"2014-08-12 14:30:34","item_limit_time":"2014-09-30 00:00:00"}}
			if (json.getString("resultCode").equals("0000")) 
			{
				OrderStatusVO st = new OrderStatusVO();
				
				JSONObject jsob = JSONObject.fromObject(json.get("result"));
				JSONObject itemjson = JSONObject.fromObject(jsob.getString("reward_item"));
				Set<Object> set = itemjson.keySet();
				String orderinfo = "";
				for (Object itemid : set)
				{
					orderinfo += itemid;
					orderinfo += "[|]";
					orderinfo += itemjson.getInt(itemid+"");
					orderinfo += "[#]";
				}
				orderinfo = orderinfo.substring(0,orderinfo.length()-3);
				
				Orders order = getOrderInfo(vo, orderinfo);
				OrdersBean bean = new OrdersBean();
				String orderid = bean.CreateOrder(order, order.getCallbackUrl());
				st.setPayId(orderid);
				
				String itemLimitTime = jsob.getString("item_limit_time");
				long limitdate = DateUtil.str2Time(itemLimitTime);
				long ts = Calendar.getInstance().getTimeInMillis();
				
				if (ts>limitdate) 
				{
					//奖励过期，不发送
					st.setStatus(2);
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
					System.out.println("$$$$$(" + DateUtil.getCurTimeStr() + ")$$$$$channel(TAG)-> reward is timeout ");
				}
				else
				{
					//奖励未过期，发送奖励
					st.setStatus(1);
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
				}
				
				statusret.add(st);
				
				String path = OSUtil.getRootPath() + "../../logs/TAGcb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, res);
			}
			else
			{
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(TAG)-> resultCode=" + json.getString("resultCode"));
			}
		} 
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		JSONArray arr = JSONArray.fromObject(statusret);
		return arr.toString();
	}
	
	private static Orders getOrderInfo(Orders ordervo, String orderinfo)
	{
		Orders order = new Orders();
		
		order.setImei(ordervo.getImei());
		order.setUId(ordervo.getUId());
		order.setGversion(ordervo.getGversion());
		order.setOsversion(ordervo.getOsversion());
		order.setDeviceId(ordervo.getDeviceId());
		order.setDeviceType(ordervo.getDeviceType());
		order.setChannel(ordervo.getChannel());
		order.setAppId(ordervo.getAppId());
		order.setAmount(ordervo.getAmount());
		order.setCreateTime(ordervo.getCreateTime());
		order.setPayType(ordervo.getPayType());
		order.setExInfo(ordervo.getExInfo());
		order.setItemId(orderinfo);
		order.setItemNum(1);
		
		String cburl = ordervo.getCallbackUrl();
		if (cburl.indexOf("?") == -1)
		{
			cburl += "?pt=" + Constants.PAY_TYPE_TAG;
		}
		else
		{
			cburl += "&pt=" + Constants.PAY_TYPE_TAG;
		}
		cburl += "&orderinfo=" + orderinfo;
		cburl += "&price=0";
		order.setCallbackUrl(cburl);
		
		return order;
	}
	
	public static void init()
	{
//		params.initParams(TAGParams.CHANNEL_ID, new TAGParamsVO());
	}
}

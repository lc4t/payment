package noumena.payment.kongmp;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class KongMPCharge
{
	private static String smsDestId = "12345";
	private static KongMPParams params = new KongMPParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		KongMPCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
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
				cburl += "?pt=" + Constants.PAY_TYPE_KONGMP;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_KONGMP;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		String content = getUseableItems(order);
		orderIdVO.setMsg(content);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static String getUseableItems(Orders order)
	{
		String ret = "";
		try
		{
			String urlstr = KongMPParams.GET_USEABLE_ITEM_URL;
			urlstr += "imei=" + order.getImei();
			urlstr += "&bid=" + order.getItemPrice();
			urlstr += "&gid=" + order.getSign();
			urlstr += "&mob=" + order.getExInfo();
			urlstr += "&goid=" + order.getOrderId();
			System.out.println("kongmp request url->" + urlstr);
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			ret = in.readLine();

			connection.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return ret;
//		return "{\"active\":\"BA460031802398120\",\"active_code\":\"02123330768\",\"operator\":\"CNET\",\"messages\":[{\"message\":\"QJ\",\"message_code\":\"10663355\",\"price\":\"1000\",\"keyword\":\"确认请回复\",\"rcontent\":\"是\",\"serviceId\":\"0000000001\"},{\"message\":\"QJ\",\"message_code\":\"10663355\",\"price\":\"1500\",\"keyword\":\"确认请回复\",\"rcontent\":\"是\" ,\"serviceId\":\"0000000002\"}]}";
	}
	
	public static String getTransactionPhoneId(Orders order)
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
				cburl += "?pt=" + Constants.PAY_TYPE_KONGMPID;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_KONGMPID;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		String ret = KongMPParams.KEYPRE + payId + KongMPParams.KEYEND;
		OrderIdVO orderIdVO = new OrderIdVO(ret, date);
		orderIdVO.setMsg(smsDestId);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static String changeSMSDestId(String newid)
	{
		String ret = "";
		
		smsDestId = newid;
		
		return ret;
	}
	
	public static String getPhoneId(String phoneid, String content)
	{
		//phoneid指定电话号码，content指定短信内容（其中包含了订单号信息）
		String ret = "";
		
		int pos1=0, pos2=0;
		pos1 = content.indexOf(KongMPParams.KEYPRE) + KongMPParams.KEYPRE.length();
		pos2 = content.indexOf(KongMPParams.KEYEND);
		String orderid = content.substring(pos1, pos2);

		CallbackBean callbackBean = new CallbackBean();
		Callback cb = callbackBean.qureyCallback(orderid);
		String cburl = cb.getCallbackUrl();
		if (cburl.indexOf("?") >= 0)
		{
			cburl += "&";
		}
		else
		{
			cburl += "?";
		}
		cburl += "phoneid=" + phoneid;
		cb.setCallbackUrl(cburl);
		callbackBean.updateCallback(cb);
		
		OrdersBean bean = new OrdersBean();
		bean.updateOrderAmountPayIdExinfo(orderid, phoneid, "", content);
		bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
		
		return ret;
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

	public static String getCallbackFromKong(KongMPOrderVO ordervo, int status)
	{
		String ret = KongMPParams.SUCCESS;
		try
		{
			OrdersBean bean = new OrdersBean();
			if (status == 0)
			{
				//订购完成通知
				Orders order = bean.qureyOrder(ordervo.getOrderid());
				
				if (order != null)
				{
					StringBuffer orderstr = new StringBuffer();
					orderstr.append(ordervo.getOrderid());
					orderstr.append(ordervo.getMob());
					orderstr.append(ordervo.getServiceId());
					orderstr.append(ordervo.getPrice());
					orderstr.append(ordervo.getTime());
					orderstr.append(ordervo.getStatus());
					orderstr.append(KongMPParams.SIGN_KEY);
					
					String minwen = orderstr.toString();
					
					boolean isvlid = validMessage(minwen, ordervo.getSign());
		
					if (!isvlid)
					{
						return KongMPParams.ERR_SIGN;
					}
					else
					{
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							CallbackBean cbBean = new CallbackBean();
							Callback cb = cbBean.qureyCallback(ordervo.getOrderid());
							String cburl = cb.getCallbackUrl();
							if (cburl.indexOf("?") >= 0)
							{
								cburl += "&";
							}
							else
							{
								cburl += "?";
							}
							cburl += "action=" + status;
							cb.setCallbackUrl(cburl);
							cbBean.updateCallback(cb);
							
							bean.updateOrderAmountPayId(ordervo.getOrderid(), ordervo.getMob() + "#" + ordervo.getServiceId(), ordervo.getPrice());
							
							//支付成功
							bean.updateOrderKStatus(ordervo.getOrderid(), Constants.K_STSTUS_SUCCESS);
							ret = KongMPParams.SUCCESS;
						}
						else
						{
							System.out.println("kongmp order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
							ret = KongMPParams.ERR_REPEAT;
						}
					}
				}
				else
				{
					ret = KongMPParams.ERR_NOORDER;
				}
			}
			else if (status == 1)
			{
				//退订完成通知
				String[] orderids = ordervo.getOrderid().split(",");
				CallbackBean cbbean = new CallbackBean();
				for (int i = 0 ; i < orderids.length ; i++)
				{
					String orderid = orderids[i];
					Callback cb = cbbean.qureyCallback(orderid);
					String cburl = cb.getCallbackUrl();
					cburl = cburl.replaceAll("action=0", "action=-1");
					cb.setOrderId(cb.getOrderId() + "_t");
					cb.setCallbackUrl(cburl);
					cb.setKStatus(Constants.K_STSTUS_SUCCESS);
					cb.setCallbackStatus(-1);
					cbbean.createCallback(cb);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = KongMPParams.FAILURE;
		}
		return ret;
	}

	private static boolean validMessage(String minwen, String sign) throws Exception
	{
		String miwen = StringEncrypt.Encrypt(minwen);
		
		System.out.println("kongmp minwen->" + minwen);
		System.out.println("kongmp my sign->" + miwen);
		System.out.println("kongmp sign->" + sign);
		
		if (sign.equals(miwen))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static void init()
	{
		params.addApp("t6", "1452", "4aba057a5ec082fc2ade563d04ba1bf2");
		params.addApp("hero", "1700", "3eb2850086599b27468a02a00a142801");
	}
}

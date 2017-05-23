package noumena.payment.pps;

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

public class PPSCharge
{
	private static PPSParams params = new PPSParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		PPSCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_PPS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_PPS;
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

	public static String getCallbackFromPPS(PPSOrderVO vo)
	{
		String msg = "";
		String appid = "";
		msg += "user_id=" + vo.getUser_id();
		msg += "&role_id=" + vo.getRole_id();
		msg += "&order_id=" + vo.getOrder_id();
		msg += "&money=" + vo.getMoney();
		msg += "&time=" + vo.getTime();
		msg += "&appid=" + vo.getAppid();
		msg += "&userData=" + vo.getUserData();
		msg += "&sign=" + vo.getSign();
		System.out.println("pps cb ->" + msg);
		
		String ret = "{\"result\":%d,\"message\":\"%s\"}";
		String orderid = vo.getUserData();

		try
		{
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
				appid = order.getSign();
				if (appid == null || appid.equals(""))
				{
					appid = "535";	//如果没有appid就当做是三国群英
				}
				String mingwen = "";
				mingwen += vo.getUser_id();
				mingwen += vo.getRole_id();
				mingwen += vo.getOrder_id();
				mingwen += vo.getMoney();
				mingwen += vo.getTime();
				mingwen += params.getParams(appid).getAppkey();
				String miwen = StringEncrypt.Encrypt(mingwen);
				
				if (miwen.equals(vo.getSign()))
				{
					// 服务器签名验证成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayIdExinfo(orderid, vo.getOrder_id(), vo.getMoney(), vo.getUser_id());
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("pps order (" + order.getOrderId() + ") had been succeed");
					}
		
					ret = String.format(ret, 0, "success");
				}
				else
				{
					// 服务器签名验证失败
					ret = String.format(ret, -1, "sign error");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		System.out.println("pps cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/ppscb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, msg);
		
		return ret;
	}

	public static void init()
	{
		params.initParams(PPSParams.CHANNEL_ID, new PPSParamsVO());
		
//		params.addApp("qunying", "535", "SGBWDL%78*%Ppsgames535");
//		params.addApp("qzhuan", "2721", "QYQZ71ba28c08a75dc597067JND2721");
//		params.addApp("m5", "2722", "JJBSGd76f956bbed7942d65b18f3f2722");
	}
}

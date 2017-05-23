package noumena.payment.youxin;

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

public class YouxinCharge
{
	private static YouxinParams params = new YouxinParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		YouxinCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_YOUXIN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_YOUXIN;
			}
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
	
	public static String getCallbackFromYouxin(Map<String,Object> youxinparams)
	{
		String ret = "success";
		String orderid = "";
//		String sporderid = "";
		String orderAmount = "";
		String sign = "";
		String orderstatus = "";
		String appid = "";
		
		try
		{
			appid = (String) youxinparams.get("appid");
			orderid = (String) youxinparams.get("orderid");
//			sporderid = (String) youxinparams.get("oid");
			orderAmount = (String) youxinparams.get("ordermoney");
			sign = (String) youxinparams.get("sign");
			orderstatus = (String) youxinparams.get("orderstatus");

			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
			
				String minwen = "";
				String miwen = "";
				minwen += youxinparams.get("appid");
				minwen += youxinparams.get("ext1");
				minwen += youxinparams.get("ext2");
				minwen += youxinparams.get("ext3");
				minwen += youxinparams.get("notifytime");
				minwen += youxinparams.get("openid");
				minwen += youxinparams.get("orderid");
				minwen += youxinparams.get("ordermoney");
				minwen += youxinparams.get("orderstatus");
				minwen += youxinparams.get("ordertime");
				minwen += params.getAppKeyById(appid);
				miwen = StringEncrypt.Encrypt(minwen);
				
				if (sign.equals(miwen))
				{
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountExinfo(orderid, (String) youxinparams.get("openid"), orderAmount);
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("youxin order (" + order.getOrderId() + ") had been succeed");
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("youxin cb->" + youxinparams.toString());
		
		String path = OSUtil.getRootPath() + "../../logs/youxincb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, youxinparams.toString());
		
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
		params.addApp("xixuegui", "10134", "1AC409699F92554EF231D2D9DAD63A2B");	//吸血鬼
		params.addApp("qunying", "10137", "B1F3D5DF40AB265259995618962FB9B2");	//三国群英
	}
}

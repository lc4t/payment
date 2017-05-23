package noumena.payment.downjoy;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class DownjoyCharge
{
	private static DownjoyParams params = new DownjoyParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		DownjoyCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_DOWNJOY;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_DOWNJOY;
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
			order.setProductId(order.getItemId());
			order.setSubId(order.getExInfo());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				st.setStatus(0);
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

	public static String getCallbackFromDownjoy(DownjoycbOrderVO ordervo)
	{
		String ret = "success";
		String orderid = ordervo.getExt();
		if (orderid == null)
		{
			orderid = "";
		}
		
		OrdersBean bean = new OrdersBean();
		Orders order = bean.qureyOrder(orderid);
		if (order == null || order.getKStatus() == Constants.K_STSTUS_SUCCESS)
		{
			return ret;
		}
		
//		String appid = ordervo.getAppid();
//		if (appid == null || appid.equals(""))
//		{
//			appid = "506";	//缺省是口袋战争appid
//		}
		String appid = order.getSign();
		String appkey = params.getParamsVO(appid).getAppkey();
		
		if (ordervo.getResult().equals("1"))
		{
			String sign = ordervo.getSignature();
			String minwen = String.format("order=%s&money=%s&mid=%s&time=%s&result=%s&ext=%s&key=%s", 
										ordervo.getOrder(), ordervo.getMoney(), ordervo.getMid(),
										ordervo.getTime(), ordervo.getResult(), ordervo.getExt(), appkey);
			String miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
			bean.updateOrderAmountPayId(orderid, ordervo.getOrder(), ordervo.getMoney());
			if (sign.equals(miwen))
			{
				//MD5码验证通过,支付成功
				bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
			}
			else
			{
				bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
			}
		}
		return ret;
	}
	
	public static void init()
	{
		params.initParams(DownjoyParams.CHANNEL_ID, new DownjoyParamsVO());
//		params.addApp("qunyingandroid", "866", "mYgRNiiqnJUP");
//		params.addApp("qunyingios", "867", "9gLRI8U8P4Gc");
//		params.addApp("m1", "506", "b920pYsJX56c");
//		params.addApp("t8android", "1812", "31qFS3j5WI1t");
//		params.addApp("t8ios", "1813", "0deit0v98Hwv");
//		params.addApp("xixueguiios", "1811", "kX9gdvAgHluQ");
//		params.addApp("xixueguiandroid", "1810", "JA8yJw8sWDR6");
//		params.addApp("m5", "2317", "o8LCovThdgeJ");
//		params.addApp("longzhu", "2604", "HAO2TBFR3tF0");			//龙族
//		params.addApp("qzhuan", "2656", "8sjCX09wFdy8");			//群英Q传
	}
}

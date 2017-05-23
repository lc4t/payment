package noumena.payment.cmgc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class CMGCCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		CMGCCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_CMGC;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_CMGC;
			}
			if (cburl != null && !cburl.equals(""))
			{
				payId = bean.CreateOrder(order, cburl);
			}
			else
			{
				payId = bean.CreateOrder(order);
			}
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
			order.setSubId(order.getSign());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			st.setTid(order.getExInfo());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
				st.setStatus(3);
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

	public static String getCallbackCMGC(CMGCOrderDataVO vo, String xmlstr)
	{
		OrdersBean bean = new OrdersBean();
		String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		ret += "<response>";
		ret += "<transIDO>";
		ret += vo.getTransIDO();
		ret += "</transIDO>";
		ret += "<hRet>";
		ret += vo.gethRet();
		ret += "</hRet>";
		
		String model = vo.getCpParam().substring(0, 1);
		if (model.equals("0"))
		{
			//cbparam是第一位为0表示通过支付中心的短代支付
			String orderid = Integer.parseInt(vo.getCpParam()) + "";
			bean.updateOrderAmountPayId(orderid, vo.getTransIDO(), vo.getUserId());
			if (vo.gethRet().equals("0"))
			{
				//成功
				ret += "<message>Successful</message>";
				bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
			}
			else
			{
				ret += "<message>Failed</message>";
				bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
			}
		}
		else if (model.equals("1"))
		{
			//网游的短代支付，不通过支付中心支付，只是回调到支付中心
			ret += "<message>Successful</message>";
			
			pushToWuXian(model, xmlstr);
		}
		else if (model.equals("A"))
		{
			//口袋战争Mini
			String orderid = vo.getCpParam();
			orderid = orderid.substring(11);
			String str = orderid.substring(0, 1);
			while (str.equals("0"))
			{
				if (orderid.equals("0"))
				{
					break;
				}
				else
				{
					orderid = orderid.substring(1);
					str = orderid.substring(0, 1);
				}
			}
			orderid = NumbericUtils.anyToAny(orderid, 62, BigInteger.valueOf(10L));
			bean.updateOrderAmountPayId(orderid, vo.getTransIDO(), vo.getUserId());
			if (vo.gethRet().equals("0"))
			{
				//成功
				ret += "<message>Successful</message>";
				bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
			}
			else
			{
				ret += "<message>Failed</message>";
				bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
			}
			
			pushToWuXian(model, xmlstr);
		}
		
		ret += "</response>";
		System.out.println("model(" + model + ") cmgc pay response ->" + ret);
		
		return ret;
	}
	
	private static void pushToWuXian(String model, String xmlstr)
	{
		int type = 0;
		if (model.equals("1"))
		{
			type = 1;
		}
		else if (model.equals("A"))
		{
			type = 2;
		}
		String cburl = CMGCParams.CALL_BACK_URL[type];
		String cbpre = CMGCParams.CALL_BACK_PRE[type];
		
		CallbackBean callbackBean = new CallbackBean();
		String oid = cbpre + "_" + System.currentTimeMillis();
		Callback cb = callbackBean.qureyCallback(oid);
		if (cb == null)
		{
			Callback callbackvo = new Callback();
			callbackvo.setOrderId(oid);
			callbackvo.setCallbackUrl(cburl);
			callbackvo.setCallbackContent(xmlstr);
			callbackvo.setKStatus(Constants.K_STSTUS_SUCCESS);		//设置为成功，并且把回调状态设置成-1，让刷新进程自动回调
			callbackvo.setCallbackStatus(Constants.K_STSTUS_ERROR);
			callbackvo.setCreateTime(DateUtil.getCurrentTime());
			callbackBean.createCallback(callbackvo);
		}
	}
}

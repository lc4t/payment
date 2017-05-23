package noumena.payment.cctc;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.cmgc.NumbericUtils;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class CCTCCharge
{
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
				cburl += "?pt=" + Constants.PAY_TYPE_TELCOM;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_TELCOM;
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
			order.setSubId(order.getExInfo());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
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

	public static String getCallbackCCTC(String resultcode)
	{
		OrdersBean bean = new OrdersBean();
		String ret = "ok";
		String orderid = resultcode.substring(0, 7);
		String cpid = resultcode.substring(7, 12);
		String result = resultcode.substring(12, 14);
		
		orderid = NumbericUtils.anyToAny(orderid, 16, BigInteger.valueOf(10L));
		System.out.println("cctc cb orderid->" + orderid);
		cpid = NumbericUtils.anyToAny(cpid, 16, BigInteger.valueOf(10L));
		System.out.println("cctc cb cpid->" + cpid);
		System.out.println("cctc cb result->" + result);
		
		Orders order = bean.qureyOrder(orderid);
		if (order != null)
		{
			if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
			{
				bean.updateOrderAmountPayId(orderid, resultcode, cpid);
				if (result.equals("00"))
				{
					//成功
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
				}
				else
				{
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
				}
			}
		}
		else
		{
			ret = "failed";
		}
		
		return ret;
	}
}

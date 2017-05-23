package noumena.payment.adways;

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
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class AdwaysCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		AdwaysCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_ADWAYS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_ADWAYS;
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
			Orders order = orders.get(i);
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_SUCCESS)
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

	public static String getCallbackFromAdways(AdwaysOrderVO ordervo)
	{
		String ret = "1";
		try
		{
			{
				OrdersBean bean = new OrdersBean();
				int point = (int) Double.parseDouble(ordervo.getPoint());
				if (point >= 0)
				{
					//支付成功
					CallbackBean callbackBean = new CallbackBean();
					Callback cbvo = callbackBean.qureyCallback(ordervo.getIdentifier());
					if (cbvo != null)
					{
						cbvo.setCallbackUrl(cbvo.getCallbackUrl() + "&samount=" + ordervo.getPoint() + "&money=" + ordervo.getPoint());
						callbackBean.updateCallback(cbvo);
					}
					
//					Orders order = bean.qureyOrder(ordervo.getIdentifier());
//					if (order != null)
//					{
//						order.setItemPrice("" + point);
//						order.setUpdateTime(DateUtil.getCurrentTime());
//						order.setKStatus(Constants.K_STSTUS_SUCCESS);
//						bean.updateOrder(ordervo.getIdentifier(), order);
//					}
					bean.updateOrderAmountPayIdExinfo(ordervo.getIdentifier(), ordervo.getAchieve_id(), ordervo.getPoint(), ordervo.getUser());
					bean.updateOrderKStatus(ordervo.getIdentifier(), Constants.K_STSTUS_SUCCESS);
				}
				else
				{
					//支付失败
					bean.updateOrderKStatus(ordervo.getIdentifier(), Constants.K_STSTUS_ERROR);
				}
				
				String path = OSUtil.getRootPath() + "../../logs/adwayscb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + ordervo.getIdentifier();
				
				String res = "";
				res += "identifier=" + ordervo.getIdentifier();
				res += "&achieve_id=" + ordervo.getAchieve_id();
				res += "&point=" + ordervo.getPoint();
				res += "&user=" + ordervo.getUser();
				res += "&confirm_flag=" + ordervo.getConfirm_flag();
				res += "&type=" + ordervo.getType();
				OSUtil.saveFile(filename, res);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = "0";
		}
		return ret;
	}
}

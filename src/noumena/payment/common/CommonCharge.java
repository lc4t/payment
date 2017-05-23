package noumena.payment.common;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderStatusVO;

public class CommonCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		CommonCharge.testmode = testmode;
	}
	
//	public static String getTransactionId(Orders order)
//	{
//		OrdersBean bean = new OrdersBean();
//		String cburl = order.getCallbackUrl();
//		int payId;
//		if (cburl == null || cburl.equals(""))
//		{
//			payId = bean.CreateOrder(order);
//		}
//		else
//		{
//			if (cburl.indexOf("?") == -1)
//			{
//				cburl += "?pt=" + order.getPayType();
//			}
//			else
//			{
//				cburl += "&pt=" + order.getPayType();
//			}
//			payId = bean.CreateOrder(order, cburl);
//		}
//		order.setCallbackUrl(cburl);
//		String date = DateUtil.formatDate(order.getCreateTime());
//		OrderIdVO orderIdVO = new OrderIdVO(String.valueOf(payId), date);
//		
//		JSONObject json = JSONObject.fromObject(orderIdVO);
//		return json.toString();
//		return "";
//	}
	
	public static String checkOrdersStatus(String payIds)
	{
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		String[] ptype ={"5103","5077","5072","5061","5060","5059","5056","5055","5052","5051","5042","5038","5032","5029","5028","5027","5026","5019","5016","5015","5011","5010","5009","5000"}; 
		for (int i = 0 ; i < orders.size() ; i++)
		{
			Orders order = orders.get(i);
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			String payType = order.getPayType(); 
			boolean flag = false;
			for (int j = 0; j < ptype.length; j++) {
				if (payType.equals(ptype[j])) {
					flag = true;
				}
			}
			if (flag) {
				st.setStatus(2);
			}
			else {
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
			}
			statusret.add(st);
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}
	
	

	public static void init()
	{
		
	}
}

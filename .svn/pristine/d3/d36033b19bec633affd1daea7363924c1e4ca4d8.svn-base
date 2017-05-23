package noumena.payment.yinni;

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
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;


public class YinniCharge
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
				cburl += "?pt=" + Constants.PAY_TYPE_YINNI;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_YINNI;
			}
			order.setCurrency(Constants.CURRENCY_IDR);
			order.setUnit(Constants.CURRENCY_UNIT_YUAN);
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
	
	public static String getCallbackFromyinni(YinniOrderVO covo)
	{
		String ret = "0";
		System.out.println("yinni cb ->" + covo.toString());
		
			OrdersBean bean = new OrdersBean();
			try {
				// 支付成功
				Orders order = bean.qureyOrder(covo.getOrder_id());
				if (order != null) {
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
							if(order.getAmount().equals(Float.valueOf(covo.getPrice()))){
								bean.updateOrderAmountPayIdExinfo(covo.getOrder_id(), covo.getPay_id(), covo.getPrice(), "");
								bean.updateOrderKStatus(covo.getOrder_id(),Constants.K_STSTUS_SUCCESS);
							}else{
								System.out.println("yinni order ("+ order.getOrderId() + ") jin e bu pipei.order price:("+order.getAmount()+").sub price:("+covo.getPrice()+")");
							}
						} else {
							System.out.println("yinni order ("+ order.getOrderId() + ") had been succeed");
						}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			System.out.println("yinni cb ret->" + ret);

			String path = OSUtil.getRootPath() + "../../logs/yinnicb/"
					+ DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + covo.getOrder_id();

			OSUtil.saveFile(filename, covo.toString());
		return ret;
	}
	
	public static void init()
	{
		
	}
	public static void main(String args[]){
		Float a = 500.0f;
		String b = "500";
		System.out.println(a.equals(Float.valueOf(b)));
	}
}

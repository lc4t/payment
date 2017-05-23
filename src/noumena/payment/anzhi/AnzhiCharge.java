package noumena.payment.anzhi;

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

public class AnzhiCharge
{
	private static AnzhiParams params = new AnzhiParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		AnzhiCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_FEN);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_ANZHI;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_ANZHI;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
			
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

	public static String getCallbackFromAnzhi(String orderstr, String appid)
	{
		String ret = AnzhiParams.SUCCESS;
		
		try
		{
			String appkey = params.getParams(appid).getAppkey();
			System.out.println("anzhi cb appid(" + appid + ") appkey(" + appkey + ")" + " orderstr(" + orderstr + ")");
			orderstr = Des3Util.decrypt(orderstr, appkey);
			
			JSONObject json = JSONObject.fromObject(orderstr);
			String logstr = json.toString();
			System.out.println("anzhi cb ->" + logstr);

			AnzhiOrderVO ordervo = new AnzhiOrderVO();
			ordervo.setCode(json.getInt("code") + "");
			ordervo.setCpInfo(json.getString("cpInfo"));
			ordervo.setMemo(json.getString("memo"));
			ordervo.setMsg(json.getString("msg"));
			ordervo.setNotifyTime(json.getString("notifyTime"));
			ordervo.setOrderAccount(json.getString("orderAccount"));
			ordervo.setOrderAmount(json.getString("orderAmount"));
			ordervo.setOrderId(json.getString("orderId"));
			ordervo.setOrderTime(json.getString("orderTime"));
			ordervo.setPayAmount(json.getString("payAmount"));
			ordervo.setUid(json.getString("uid"));
			ordervo.setRedBagMoney(json.getString("redBagMoney"));
			
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(ordervo.getCpInfo());
			
			if (order != null)
			{
				if (ordervo.getCode().equals("1"))
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayIdExinfo(ordervo.getCpInfo(), ordervo.getOrderId(), ordervo.getOrderAmount(), ordervo.getUid() + "#" + ordervo.getOrderAccount());
						
						//支付成功
						bean.updateOrderKStatus(ordervo.getCpInfo(), Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("anzhi order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
					}
				}
				else
				{
					//支付失败
					bean.updateOrderKStatus(ordervo.getCpInfo(), Constants.K_STSTUS_ERROR);
				}
				
				String path = OSUtil.getRootPath() + "../../logs/anzhicb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + ordervo.getCpInfo();
				
				OSUtil.saveFile(filename, logstr);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void init()
	{
		params.initParams(AnzhiParams.CHANNEL_ID, new AnzhiParamsVO());
//		params.addApp("hero", "3900000", "PDQ23JgWy06Vz1Vx88c2kObp");		//13844966705G1ve6WcTFrDY6lsMEHS
//		params.addApp("m5", "6000000", "XwQfR459l64DHu6Di68uAPY8");			//1409126499W512dAnhYEq6PLkfbNqV
//		params.addApp("gaoguai", "4400000", "b5e5Qb14zjTde3do4F1DEo6x");	//1413878427BeP26uQloOl1tVkvu2a9
	}
}

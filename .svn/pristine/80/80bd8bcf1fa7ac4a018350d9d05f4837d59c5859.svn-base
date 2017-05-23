package noumena.payment.c4399;

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

public class C4399Charge
{
	private static C4399Params params = new C4399Params();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		C4399Charge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_4399;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_4399;
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
	
	public static String getCallbackFrom4399(Map<String,String> c4399params)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(4399 cb params)->" + c4399params.toString());
		String gamemoney = "";
		String money = "";
    	String ret = "{\"status\":%d,\"code\":%s,\"money\":\"%s\",\"game_money\":\"%s\",\"msg\":\"%s\"}";
    	
    	try {
			JSONObject json = JSONObject.fromObject(c4399params);
			C4399OrderVO ordervo = (C4399OrderVO)JSONObject.toBean(json,C4399OrderVO.class);
			gamemoney = ordervo.getGamemoney();
			money = ordervo.getMoney();
			String orderid = ordervo.getMark();
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				String appid = order.getExInfo();
				String minwen = "";
				String miwen = "";
				
				minwen += ordervo.getOrderid();
				minwen += ordervo.getUid();
				minwen += ordervo.getMoney();
				minwen += ordervo.getGamemoney();
				if (ordervo.getServerid()!=null) 
				{
					minwen += ordervo.getServerid();
				}
				minwen += params.getParams(appid).getSecretkey();
				minwen += orderid;
				if (ordervo.getRoleid()!=null) 
				{
					minwen += ordervo.getRoleid();
				}
				minwen += ordervo.getTime();
				
				miwen = StringEncrypt.Encrypt(minwen);
				
				if (miwen.equals(ordervo.getSign()))
				{
					//服务器签名验证成功
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
					{
						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getOrderid(), ordervo.getMoney(), ordervo.getP_type()+"#"+ordervo.getUid());
						bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						ret = String.format(ret, 2, null, money, gamemoney, "success");
					} 
					else 
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(4399 cb) order (" + order.getOrderId()+ ") had been succeed");
						ret = String.format(ret, 1, "orderid_exist", money, gamemoney, "orderid exist");
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = String.format(ret, 1, "sign_error", money, gamemoney, "sign error");
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(4399 cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
				}
				String path = OSUtil.getRootPath() + "../../logs/4399cb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, c4399params.toString());
			}
			else
			{
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(4399 cb) order (" + orderid+ ") is null");
				ret = String.format(ret, 1, "other_error", money, gamemoney, "order is null");
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
			ret = String.format(ret, 1, "other_error", money, gamemoney, "other error");
		}
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(4399 cb ret)->" + ret);		
    	
		return ret;
	}

	public static void init()
	{
		params.initParams(C4399Params.CHANNEL_ID, new C4399ParamsVO());
	}
}

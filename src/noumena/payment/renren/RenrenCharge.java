package noumena.payment.renren;

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

public class RenrenCharge
{
	private static RenrenParams params = new RenrenParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		RenrenCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_RENREN);
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
				cburl += "?pt=" + Constants.PAY_TYPE_RENREN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_RENREN;
			}
			cburl += "&currency=" + Constants.CURRENCY_RENREN;
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
	
	public static String getCallbackFromRenren(Map<String,String> renrenparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(renren cb info)->" + renrenparams.toString());
    	
    	String ret = "success";
    	
    	try {
			JSONObject json = JSONObject.fromObject(renrenparams);
			RenrenOrderVO ordervo = (RenrenOrderVO)JSONObject.toBean(json,RenrenOrderVO.class);
			
			if (ordervo.isXn_issuccess()) 
			{
				String orderid = ordervo.getXn_app_bid();
				String appid = ordervo.getXn_app_id();
				String minwen = "";
				String miwen = "";
				
				minwen += "xn_amount=";
				minwen += ordervo.getXn_amount();
				minwen += "&xn_app_bid=";
				minwen += orderid;
				minwen += "&xn_app_id=";
				minwen += appid;
				minwen += "&xn_channel=";
				minwen += ordervo.getXn_channel();
				minwen += "&xn_deal_time=";
				minwen += ordervo.getXn_deal_time();
				minwen += "&xn_goods_info=";
				minwen += ordervo.getXn_goods_info();
				minwen += "&xn_issuccess=";
				minwen += ordervo.isXn_issuccess();
				minwen += "&xn_istest=";
				minwen += ordervo.isXn_istest();
				minwen += "&xn_order_time=";
				minwen += ordervo.getXn_order_time();
				minwen += "&xn_platform_bid=";
				minwen += ordervo.getXn_platform_bid();
				minwen += "&xn_user_id=";
				minwen += ordervo.getXn_user_id();
				minwen += params.getParams(appid).getSecretkey();
				
				miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
				
				if (miwen.equals(ordervo.getXn_sig()))
				{
					//服务器签名验证成功
					if (ordervo.isXn_istest()) 
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(renren cb) xn_istest is true");
						return "success";
					}
					OrdersBean bean = new OrdersBean();
					Orders order = bean.qureyOrder(orderid);
					if (order != null)
					{//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getXn_platform_bid(), ordervo.getXn_amount(), ordervo.getXn_user_id()+"#"+ordervo.getXn_goods_info());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(renren cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
				
				}
				else
				{
					// 服务器签名验证失败
					ret = "fail";
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(renren cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
				}
				String path = OSUtil.getRootPath() + "../../logs/renrencb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, renrenparams.toString());
			}
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(renren cb ret)->" + ret);			
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public static void init()
	{
		params.initParams(RenrenParams.CHANNEL_ID, new RenrenParamsVO());
	}
}

package noumena.payment.douyu;

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

public class DouyuCharge
{
	private static DouyuParams params = new DouyuParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		DouyuCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_DOUYU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_DOUYU;
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
	
	public static String getCallbackFromDouyu(Map<String,String> douyuparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(douyu cb params)->" + douyuparams.toString());
		
    	String ret = "success";   
    	
    	try {
			JSONObject json = JSONObject.fromObject(douyuparams);
			DouyuOrderVO ordervo = (DouyuOrderVO)JSONObject.toBean(json,DouyuOrderVO.class);
			
			String orderid = ordervo.getCallbackInfo();
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				String appid = order.getExInfo();
				String minwen = "";
				String miwen = "";
				
				minwen += "amount=";
				minwen += ordervo.getAmount();
				minwen += "callbackInfo=";
				minwen += orderid;
				minwen += "openId=";
				minwen += ordervo.getOpenId();
				minwen += "orderId=";
				minwen += ordervo.getOrderId();
				minwen += "orderStatus=";
				minwen += ordervo.getOrderStatus();
				minwen += "payType=";
				minwen += ordervo.getPayType();
				minwen += "remark=";
				minwen += ordervo.getRemark();
				minwen += "serverId=";
				minwen += ordervo.getServerId();
				minwen += params.getParams(appid).getAppkey();
				
				miwen = StringEncrypt.Encrypt(minwen);
				
				if (miwen.equals(ordervo.getSign()))
				{
					//服务器签名验证成功
					if (ordervo.getOrderStatus().equals("1")) 
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getOrderId(), ordervo.getAmount(), ordervo.getOpenId());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(douyu cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					} 
					else 
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr()+ ")=====channel(douyu cb) orderStatus = "+ ordervo.getOrderStatus());
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = "failure";
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(douyu cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
				}
				
				String path = OSUtil.getRootPath() + "../../logs/douyucb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, douyuparams.toString());
			}	
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(douyu cb ret)->" + ret);		
    	
		return ret;
	}

	public static void init()
	{
		params.initParams(DouyuParams.CHANNEL_ID, new DouyuParamsVO());
	}
}

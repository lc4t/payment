package noumena.payment.kuaifa;

import java.net.URLEncoder;
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

public class KuaifaCharge
{
	private static KuaifaParams params = new KuaifaParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		KuaifaCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_KUAIFA;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_KUAIFA;
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
	
	public static String getCallbackFromKuaifa(Map<String,String> kuaifaparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kuaifa cb params)->" + kuaifaparams.toString());
    	
    	String ret = "{\"result\":\"0\",\"result_desc\":\"ok\"}";
    	
    	try {
			JSONObject json = JSONObject.fromObject(kuaifaparams);
			KuaifaOrderVO ordervo = (KuaifaOrderVO)JSONObject.toBean(json,KuaifaOrderVO.class);
			
			String orderid = ordervo.getGame_orderno();
			
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				String appid = ordervo.getExtend();
				
				String minwen = "";
				String miwen = "";
				
				minwen += "amount=";
				minwen += ordervo.getAmount();
				minwen += "&cp=";
				minwen += URLEncoder.encode(ordervo.getCp(),"utf-8");
				minwen += "&extend=";
				minwen += URLEncoder.encode(appid,"utf-8");
				minwen += "&game_orderno=";
				minwen += orderid;
				minwen += "&product_id=";
				minwen += URLEncoder.encode(ordervo.getProduct_id(),"utf-8");
				minwen += "&product_num=";
				minwen += ordervo.getProduct_num();
				minwen += "&result=";
				minwen += ordervo.getResult();
				minwen += "&serial_number=";
				minwen += URLEncoder.encode(ordervo.getSerial_number(),"utf-8");
				minwen += "&server=";
				minwen += ordervo.getServer();
				minwen += "&timestamp=";
				minwen += ordervo.getTimestamp();
				
				miwen = StringEncrypt.Encrypt(minwen);
				miwen = StringEncrypt.Encrypt(miwen + params.getParams(appid).getSecretkey());
				
				if (miwen.equals(ordervo.getSign()))
				{
					//服务器签名验证成功
					if (ordervo.getResult().equals("0")) 
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getSerial_number(), ordervo.getAmount(), ordervo.getCp() + "#" + ordervo.getServer());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kuaifa cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
					else
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kuaifa cb) result="+ordervo.getResult());
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = "{\"result\":\"1\",\"result_desc\":\"sign is error\"}";
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kuaifa cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
				}
				
				String path = OSUtil.getRootPath() + "../../logs/kuaifacb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, kuaifaparams.toString());
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(kuaifa cb ret)->" + ret);
		
		return ret;
	}
	
	public static void init()
	{
		params.initParams(KuaifaParams.CHANNEL_ID, new KuaifaParamsVO());
	}
}

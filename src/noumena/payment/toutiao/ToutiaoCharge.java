package noumena.payment.toutiao;

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
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class ToutiaoCharge
{
	private static ToutiaoParams params = new ToutiaoParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		ToutiaoCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_TOUTIAO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_TOUTIAO;
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
	
	public static String getCallbackFromToutiao(Map<String,String> toutiaoparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(toutiao cb params)->" + toutiaoparams.toString());
    	
    	String ret = "success";
    	
    	try {
			JSONObject json = JSONObject.fromObject(toutiaoparams);
			ToutiaoOrderVO ordervo = (ToutiaoOrderVO)JSONObject.toBean(json,ToutiaoOrderVO.class);
			
			String orderid = ordervo.getOut_trade_no();
			
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				String appid = ordervo.getClient_id();
				
				String content = "";
				
				content += "buyer_id=";
				content += ordervo.getBuyer_id();
				content += "&client_id=";
				content += appid;
				content += "&notify_id=";
				content += ordervo.getNotify_id();
				content += "&notify_time=";
				content += ordervo.getNotify_time();
				content += "&notify_type=";
				content += ordervo.getNotify_type();
				content += "&out_trade_no=";
				content += orderid;
				content += "&pay_time=";
				content += ordervo.getPay_time();
				content += "&total_fee=";
				content += ordervo.getTotal_fee();
				content += "&trade_no=";
				content += ordervo.getTrade_no();
				content += "&trade_status=";
				content += ordervo.getTrade_status();
				content += "&way=";
				content += ordervo.getWay();
				
				boolean flag = validMessage(content, ordervo.getTt_sign(), appid);
				
				if (flag)
				{
					//服务器签名验证成功
					String trade_status = ordervo.getTrade_status();
					if (trade_status.equals("0") || trade_status.equals("3")) 
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getTrade_no(), ordervo.getTotal_fee(), ordervo.getBuyer_id() + "#" + ordervo.getWay());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(toutiao cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
					else
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(toutiao cb) status="+ordervo.getTrade_status());
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = "fail";
				}
				
				String path = OSUtil.getRootPath() + "../../logs/toutiaocb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, toutiaoparams.toString());
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(toutiao cb ret)->" + ret);
		
		return ret;
	}

	private static boolean validMessage(String transdata, String sign, String appid) throws Exception
	{
		boolean flag;
		String publickey = params.getParams(appid).getPublickey();
		flag = RSASignature.validSign(transdata, sign, publickey,"utf-8");
		
		return flag;
	}
	
	public static void init()
	{
		params.initParams(ToutiaoParams.CHANNEL_ID, new ToutiaoParamsVO());
	}
}

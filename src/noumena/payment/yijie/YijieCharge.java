package noumena.payment.yijie;

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

public class YijieCharge
{
	private static YijieParams params = new YijieParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		YijieCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order, String paytype)
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
				cburl += "?pt=" + paytype;
			}
			else
			{
				cburl += "&pt=" + paytype;
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
	
	public static String getCallbackFromYijie(Map<String,String> yijieparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yijie cb params)->" + yijieparams.toString());
		
    	String ret = "SUCCESS";   
    	try {
			JSONObject json = JSONObject.fromObject(yijieparams);
			YijieOrderVO ordervo = (YijieOrderVO)JSONObject.toBean(json,YijieOrderVO.class);
			String orderid = ordervo.getCbi();
			String appid = ordervo.getApp();
			String sdk = ordervo.getSdk();
			
			String minwen = "";
			String miwen = "";
			
			minwen += "app=";
			minwen += appid;
			minwen += "&cbi=";
			minwen += orderid;
			minwen += "&ct=";
			minwen += ordervo.getCt();
			minwen += "&fee=";
			minwen += ordervo.getFee();
			minwen += "&pt=";
			minwen += ordervo.getPt();
			minwen += "&sdk=";
			minwen += ordervo.getSdk();
			minwen += "&ssid=";
			minwen += ordervo.getSsid();
			minwen += "&st=";
			minwen += ordervo.getSt();
			minwen += "&tcd=";
			minwen += ordervo.getTcd();
			minwen += "&uid=";
			minwen += ordervo.getUid();
			minwen += "&ver=";
			minwen += ordervo.getVer();
			if (appid.contains("{"))
			{
				appid = appid.replace("{", "").replace("-", "").replace("}", "");
				sdk = sdk.replace("{", "").replace("-", "").replace("}", "").toLowerCase();
			}
			minwen += params.getParams(appid.toUpperCase()).getSecretkey();
			miwen = StringEncrypt.Encrypt(minwen);
			
			if (miwen.equals(ordervo.getSign()))
			{
				//服务器签名验证成功
				//支付成功
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
				if (order!=null) 
				{
					String paytype = order.getPayType().substring(3);
					if (paytype.equals(sdk)) 
					{
						if (ordervo.getSt().equals("1")) 
						{
							if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
								{
									bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getTcd(), ordervo.getFee(), ordervo.getUid()+"#"+ordervo.getSsid());
									bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
								} 
								else 
								{
									System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yijie cb) order (" + order.getOrderId()+ ") had been succeed");
								}
						}
						else
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yijie cb) st="+ordervo.getSt());
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getTcd(), ordervo.getFee(), ordervo.getUid()+"#"+ordervo.getSsid());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_ERROR);
						}
					}
					else 
					{
						//支付渠道有冲突
						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getTcd(), ordervo.getFee(), ordervo.getUid()+"#"+ordervo.getSsid());
						bean.updateOrderKStatus(orderid,Constants.K_STSTUS_ERROR);
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yijie cb)->channelId is different (channelId=" + ordervo.getSdk()+", paytype="+paytype+")");
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/yijiecb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, yijieparams.toString());
			}
			else
			{
				ret = "failed";
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yijie cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yijie cb ret)->" + ret);		
    	
		return ret;
	}

	public static void init()
	{
		params.initParams(YijieParams.CHANNEL_ID, new YijieParamsVO());
	}
}

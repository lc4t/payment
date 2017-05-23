package noumena.payment.efun;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.bean.PayServerBean;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.model.PayServer;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.LogVO;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class EfunCharge
{
	private static EfunParams params = new EfunParams();
	private static boolean testmode = false;
	private static HashMap<String, Object> mutex = new HashMap<String, Object>();
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		EfunCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_USD);
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
				cburl += "?pt=" + Constants.PAY_TYPE_EFUN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_EFUN;
			}
			cburl += "&currency=" + Constants.CURRENCY_USD;
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
	
	public static String getCallbackFromEfun(Map<String,String> efunparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(efun cb params)->" + efunparams.toString());
    	
    	String ret = "{\"code\":\"0000\"}";
    	
    	try {
			JSONObject json = JSONObject.fromObject(efunparams);
			EfunOrderVO ordervo = (EfunOrderVO)JSONObject.toBean(json,EfunOrderVO.class);
			String remark = ordervo.getRemark(); //游戏内 - 订单号，web - platform
			Orders order = null;
			
			String appid = ordervo.getGameCode();
			if (appid == null || appid.equals(""))
			{
				return "{\"code\":\"1000\"}";
			}
			
			String minwen = "";
			String miwen = "";
			
			minwen += ordervo.getpOrderId();
			minwen += ordervo.getServerCode();
			minwen += ordervo.getCreditId();
			minwen += ordervo.getUserId();
			minwen += ordervo.getAmount();
			minwen += ordervo.getStone();
			minwen += ordervo.getTime();
			minwen += params.getParams(appid).getSecretkey().toUpperCase();
			
			miwen = StringEncrypt.Encrypt(minwen).toUpperCase();
			
			if (miwen.equals(ordervo.getMd5Str()))
			{
				//服务器签名验证成功
				OrdersBean bean = new OrdersBean();
				boolean flag = false; //true - web储值, false - 游戏内储值
				String orderid = "";
				
				Object obj = mutex.get(ordervo.getpOrderId());
				if (obj == null)
				{
					obj = new Object();
					mutex.put(ordervo.getpOrderId(), obj);
				}
				synchronized (obj)
				{
					if (remark == null) 
					{
						remark = "";
					}
					if (remark.equals("platform"))
					{
						//web储值
						order = createOrder(ordervo);
						flag = true;
					}
					else
					{
						//游戏内储值
						order = bean.qureyOrder(remark);
						flag = false;
						
						long callbacktime = System.currentTimeMillis();
						String gameid = params.getParams(ordervo.getGameCode()).getGameid();
						String serverid = ordervo.getServerCode();
						String Parameter = "";
						Parameter += "userid=" + ordervo.getCreditId();
						Parameter += "&gameid=" + gameid;
						Parameter += "&serverid=" + serverid;
						Parameter += "&itemid=" + ordervo.getProductId();
						Parameter += "&gem=" + ordervo.getStone();
						Parameter += "&activityextragem=" + ordervo.getActivityExtra();
						Parameter += "&callbacktime=" + callbacktime;
						
						String cbsign = gameid+serverid+ordervo.getCreditId()+ordervo.getProductId()+ordervo.getStone()+ordervo.getActivityExtra()+callbacktime;
						cbsign = StringEncrypt.Encrypt(cbsign+Constants.P_KEY);
						
						CallbackBean callbackBean = new CallbackBean();
						Callback callbackvo = callbackBean.qureyCallback(remark);
						String cburl = "";
						if (callbackvo != null)
						{
							cburl = callbackvo.getCallbackUrl();
						}
						if (cburl != null && !cburl.equals(""))
						{
							if (cburl.indexOf("?") == -1)
							{
								cburl += "?" + Parameter;
							}
							else
							{
								cburl += "&" + Parameter;
							}
							cburl += "&sign=" +cbsign;
							
							callbackvo.setCallbackUrl(cburl);
							callbackBean.updateCallback(callbackvo);
						}
					}
					if (order != null) 
					{
						orderid = order.getOrderId();
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							if(flag)
							{
								List<Orders> oo = bean.qureyOrdersByPayId(ordervo.getpOrderId());
								if (oo.size() > 1) 
								{
									// 重复订单，忽略
									System.out.println("efun order (" + order.getOrderId()+ ")(" + order.getPayId() + ") is existing");
								} 
								else 
								{
									bean.updateOrderKStatus(order.getOrderId(),Constants.K_STSTUS_SUCCESS);
									genEfunLog(ordervo, order.getOrderId());
								}
							}	
							else
							{
								bean.updateOrderAmountPayIdExinfo(order.getOrderId(), ordervo.getpOrderId(), ordervo.getAmount(), ordervo.getServerCode() + "#" + ordervo.getUserId());
								bean.updateOrderKStatus(order.getOrderId(),Constants.K_STSTUS_SUCCESS);
								genEfunLog(ordervo, order.getOrderId());
							}
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(efun cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
				}
				mutex.clear();
				
				String path = OSUtil.getRootPath() + "../../logs/efuncb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, efunparams.toString());
			}
			else
			{
				// 服务器签名验证失败
				ret = "{\"code\":\"0011\"}";
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(efun cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
//			ret = "{\"code\":\"0100\"}";
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(efun cb ret)->" + ret);
		
		return ret;
	}
	
	private static Orders createOrder(EfunOrderVO ordervo)
	{
		String gameid = params.getParams(ordervo.getGameCode()).getGameid();
		PayServerBean payServerBean = new PayServerBean();
		String serverid = ordervo.getServerCode();
		PayServer payServer = payServerBean.get("efun");
		if (payServer==null) {
			return null;
		}

		Orders vo = new Orders();
		
		vo.setImei("");
		vo.setUId(ordervo.getCreditId());
		vo.setChannel(Constants.getGameIdByAppId(gameid)+ "A0AL670A1000000");
		vo.setAppId(gameid);
		vo.setAmount(Float.parseFloat(ordervo.getAmount()));
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vo.setCreateTime(df1.format(new Date()));
		vo.setPayType(Constants.PAY_TYPE_EFUN);
		vo.setItemId(ordervo.getProductId());
		vo.setItemPrice(ordervo.getStone() + "#" + ordervo.getActivityExtra());
		vo.setItemNum(1);
		vo.setExInfo(ordervo.getServerCode() + "#" + ordervo.getUserId());
		vo.setPayId(ordervo.getpOrderId());
		vo.setMoney(ordervo.getAmount());
		vo.setCurrency(Constants.CURRENCY_USD);
		vo.setUnit(Constants.CURRENCY_UNIT_YUAN);

		OrdersBean bean = new OrdersBean();
		long callbacktime = System.currentTimeMillis();
		String cburl = "";
		String Parameter = "";
		if (payServer != null)
		{
			cburl = payServer.getCallbackUrl();
			Parameter += "userid=" + ordervo.getCreditId();
			Parameter += "&gameid=" + gameid;
			Parameter += "&serverid=" + serverid;
			Parameter += "&itemid=" + ordervo.getProductId();
			Parameter += "&gem=" + ordervo.getStone();
			Parameter += "&activityextragem=" + ordervo.getActivityExtra();
			Parameter += "&callbacktime=" + callbacktime;
		}
		//gameid + serverid + userid + itemid + gem + activityextragem + callbacktime
		String cbsign = gameid+serverid+ordervo.getCreditId()+ordervo.getProductId()+ordervo.getStone()+ordervo.getActivityExtra()+callbacktime;
		cbsign = StringEncrypt.Encrypt(cbsign+Constants.P_KEY);
		
		if (cburl == null || cburl.equals(""))
		{
			String orderid = bean.CreateOrder(vo);
			vo.setOrderId(orderid);
		}
		else
		{
			if (cburl.indexOf("?") == -1)
			{
				cburl += "?" + Parameter;
			}
			else
			{
				cburl += "&" + Parameter;
			}
			cburl += "&sign=" +cbsign;
			cburl += "&pt=" + Constants.PAY_TYPE_EFUN;
			cburl += "&currency=" + Constants.CURRENCY_USD;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			String orderid = bean.CreateOrder(vo, cburl);
			vo.setOrderId(orderid);
		}
		return vo;
	}
	
	/**
	 * 生成易幻对账日志
	 */
	private static void genEfunLog(EfunOrderVO ordervo, String orderid)
	{
		LogVO logvo = new LogVO();
		logvo.setItem1(DateUtil.date2Str(Long.parseLong(ordervo.getTime()), 0));
		logvo.setItem2(ordervo.getServerCode());
		logvo.setItem3(ordervo.getUserId());
		logvo.setItem4(ordervo.getpOrderId());
		logvo.setItem5(Constants.ORDERID_PRE + orderid);
		logvo.setItem6(ordervo.getCurrency());
		if (ordervo.getAmount() != null && !ordervo.getAmount().equals("") && ordervo.getAmount().matches("^(-?\\d+)(\\.\\d+)?$") == true)
		{
			logvo.setItem7(ordervo.getAmount());
		}
		logvo.setItem8(ordervo.getStone());
		logvo.setItem9(ordervo.getOrderStateMonth());
		logvo.setItem10(ordervo.getpOrderId().contains("SMJJIOS")?"IOS":"Android");
		OSUtil.genChannelLog(logvo, "efun");
	}
	
	public static void init()
	{
		params.initParams(EfunParams.CHANNEL_ID, new EfunParamsVO());
	}
}

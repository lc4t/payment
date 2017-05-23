package noumena.payment.kingnet;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.bean.PayGameBean;
import noumena.payment.bean.PayServerBean;
import noumena.payment.model.Orders;
import noumena.payment.model.PayGame;
import noumena.payment.model.PayServer;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class KingnetCharge
{
	private static KingnetParams params = new KingnetParams();
	private static String GAME_ID = "4200000";
	private static String GAME_ID_LAN = "1";
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		KingnetCharge.testmode = testmode;
	}
	
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
				cburl += "?pt=" + Constants.PAY_TYPE_BAIDU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_BAIDU;
			}
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
				st.setStatus(3);
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
	
	private static Orders createOrder(KingnetOrderVO ordervo)
	{
		String gameid = GAME_ID;		//目前kingnet参数没有指定游戏id，所以只支持大领主，所以写死gameid
		PayGameBean payGameBean = new PayGameBean();
		PayServerBean payServerBean = new PayServerBean();
		PayGame payGame = payGameBean.getGame(gameid);
		String serverid = ordervo.getSid();
		PayServer payServer = payServerBean.get(serverid);

		Orders vo = new Orders();
		
		vo.setImei("");
		vo.setUId(ordervo.getUser_id());
		vo.setChannel(Constants.getGameIdByAppId(gameid) + "A2ABE70A0000000");
		vo.setAppId(gameid);
		vo.setAmount(Float.parseFloat(ordervo.getAmount()));
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		vo.setCreateTime(df1.format(new Date()));
		vo.setPayType(Constants.PAY_TYPE_KINGNET);
		vo.setItemId(ordervo.getRole_id());
		vo.setItemPrice(ordervo.getNumber());
		vo.setItemNum(1);
		vo.setExInfo(ordervo.getKda());
		vo.setPayId(ordervo.getOrder_id());
		vo.setMoney(ordervo.getAmount());

		OrdersBean bean = new OrdersBean();
		String cburl = "";
		String Parameter = "";
		if (payServer != null)
		{
			cburl = payServer.getCallbackUrl();
			Parameter += "userid=" + ordervo.getUser_id();
			Parameter += "&gameid=" + gameid;
			Parameter += "&serverid=" + serverid;
			Parameter += "&itmeid=" + "";
			Parameter += "&amount=" + ordervo.getAmount();
			Parameter += "&itemprice=" + ordervo.getNumber();
			Parameter += "&money=" + ordervo.getNumber();
			Parameter += "&active1=" + ordervo.getActive1();
		}
		
		if (cburl == null || cburl.equals(""))
		{
			bean.CreateOrder(vo);
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
			cburl += "&pt=" + Constants.PAY_TYPE_KINGNET;
			bean.CreateOrder(vo, cburl);
		}
		
		return vo;
	}

	public static String getServerinfoFromKingnet()
	{
		KingnetRetVO ret = new KingnetRetVO();
		KingnetServerVO kingnetsvo = null;

		try
		{
			String gameid = GAME_ID;		//目前kingnet参数没有指定游戏id，所以只支持大领主，所以写死gameid
			PayServerBean payServerBean = new PayServerBean();
			List<PayServer> servers = payServerBean.select();
			for (int i = 0 ; i < servers.size() ; i++)
			{
				PayServer servervo = servers.get(i);
				if (servervo.getGameId() != null && servervo.getGameId().equals(gameid + GAME_ID_LAN))
				{
					kingnetsvo = new KingnetServerVO();
					kingnetsvo.setServer_name(servervo.getServerName());
					kingnetsvo.setServer_id(servervo.getServerId());
					kingnetsvo.setServer_status("1");
					
					ret.getMsg().getSlist().add(kingnetsvo);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		JSONObject json = JSONObject.fromObject(ret);
		return json.toString();
	}

	public static String getUserinfoFromKingnet(String uid, String sid)
	{
		String ret = "";
		try
		{
			PayServerBean payServerBean = new PayServerBean();
			PayServer payServer = payServerBean.get(sid);
			
			String checkurl = payServer.getCheckUrl();
			checkurl += "?action=1";
			checkurl += "&userid=" + uid;
			checkurl += "&serverid=" + sid;
			
			System.out.println("kingnet getuserinfo->" + checkurl);
			URL url = new URL(checkurl);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			ret = in.readLine();

			connection.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ret;
	}

	public static String getCallbackFromKingnet(KingnetOrderVO ordervo)
	{
		//kingnet目前是网页充值，并且是在kingnet平台下，所以只有通知支付结果没有创建订单，需要手动创建一个订单
		Orders order = createOrder(ordervo);
		
		int ret = KingnetParams.SUCCESS;
		try
		{
			OrdersBean bean = new OrdersBean();
			
			if (order != null)
			{
				boolean isvlid = validMessage(ordervo, GAME_ID);
	
				if (!isvlid)
				{
					ret = KingnetParams.ERR_SIGN;
				}
				else
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						//支付成功
						List<Orders> oo = bean.qureyOrdersByPayId(ordervo.getOrder_id());
						if (oo.size() > 1)
						{
							//重复订单，忽略
							ret = KingnetParams.ERR_REPEAT;
						}
						else
						{
							bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
							ret = KingnetParams.SUCCESS;
						}
					}
					else
					{
						System.out.println("kingnet order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
						ret = KingnetParams.ERR_REPEAT;
					}
				}
			}
			else
			{
				ret = KingnetParams.ERR_PARAMS;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = KingnetParams.ERR_OTHER;
		}
		String result = "{";
		result += "\"ret\":";
		result += ret;
		result += ",";
		result += "\"msg\":";
		result += "\"\"}";
		return result;
	}

	private static boolean validMessage(KingnetOrderVO ordervo, String appid) throws Exception
	{
		StringBuffer orderstr = new StringBuffer();
		String publickey = "kingnetpay.kingnet.com";
		String privatekey = params.getAppKeyById(appid);
		orderstr.append(publickey);
		orderstr.append(ordervo.getUser_id());
		orderstr.append(ordervo.getSid());
		orderstr.append(ordervo.getNumber());
		orderstr.append(ordervo.getAmount());
		orderstr.append(ordervo.getRole_id());
		orderstr.append(ordervo.getOrder_id());
		orderstr.append(ordervo.getTs());
		orderstr.append(privatekey);
		
		String minwen = orderstr.toString();
		String miwen = StringEncrypt.EncryptSHA256(minwen);
		
		System.out.println("kingnet minwen->" + minwen);
		System.out.println("kingnet my sign->" + miwen);
		System.out.println("kingnet sign->" + ordervo.getSig());
		
		if (ordervo.getSig().equals(miwen))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	public static void init()
	{
		params.addApp("t6", "4200000", "1109028");
	}
}

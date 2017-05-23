package noumena.payment.igs;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
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

public class IGSCharge
{
	private static IGSParams params = new IGSParams();
	private static int testmode = 0;	//0-design;1-test;2-release
	
	public static int getTestmode()
	{
		return testmode;
	}

	public static void setTestmode(int testmode)
	{
		IGSCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_IGS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_IGS;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static String checkOrdersStatus(Orders vo, String AccountID, String GameID, String AppID, String UserID, String UserName, String GroupNo, String ServerNo, String Environment)
	{
//		String[] orderIds = payIds.split(",");
//
//		OrdersBean bean = new OrdersBean();
//		List<Orders> orders = bean.qureyOrders(orderIds);
//		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
//		for (int i = 0 ; i < orders.size() ; i++)
//		{
//			Orders order = orders.get(i);
//			int status = order.getKStatus();
//			OrderStatusVO st = new OrderStatusVO();
//			st.setPayId(order.getOrderId());
//			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
//			{
//				//如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
//				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
//				Calendar cal2 = Calendar.getInstance();
//				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
//				{
//					st.setStatus(4);
//				}
//				else
//				{
//					int ret = checkOrderFromIGS(vo, AccountID, GameID, AppID, UserID, UserName, GroupNo, ServerNo, Environment);
//					st.setStatus(ret);
//				}
//			}
//			else if (status == Constants.K_STSTUS_SUCCESS)
//			{
//				//如果订单已经成功，直接返回订单状态
//				st.setStatus(1);
//			}
//			else
//			{
//				//订单已经失败，直接返回订单状态
//				st.setStatus(2);
//			}
//			statusret.add(st);
//		}
//		JSONArray arr = JSONArray.fromObject(statusret);
		
//		return arr.toString();
		return checkOrderFromIGS(vo, AccountID, GameID, AppID, UserID, UserName, GroupNo, ServerNo, Environment);
	}

	private static String json2str(IGSRequestVO requestvo)
	{
		//{"accountID":1,"appID":2,"gameID":3,"groupNo":4,"serverNo":5,"userID":6,"userName":"7"}
		String ret = "";
		
		ret += "{";
		ret += "\"AccountID\":" + requestvo.getAccountID() + ",";
		ret += "\"AppID\":" + requestvo.getAppID() + ",";
		ret += "\"GameID\":" + requestvo.getGameID() + ",";
		ret += "\"GroupNo\":" + requestvo.getGroupNo() + ",";
		ret += "\"ServerNo\":" + requestvo.getServerNo() + ",";
		ret += "\"UserID\":" + requestvo.getUserID() + ",";
		ret += "\"UserName\":\"" + requestvo.getUserName() + "\"";
		ret += "}";
		
		return ret;
	}
	
	private static String checkOrderFromIGS(Orders vo, String AccountID, String GameID, String AppID, String UserID, String UserName, String GroupNo, String ServerNo, String Environment)
	{
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		try
		{
			//{"AccountID":1,"GameID":1,"AppID":1,"GroupNo": 1,"ServerNo":1,"UserID":1,"UserName":"Test"}
			String urlstr = "";
			if (Environment != null && !Environment.equals(""))
			{
				testmode = Integer.parseInt(Environment);
			}
			switch (testmode)
			{
			case 0:
				//开发环境
				urlstr = IGSParams.IGS_VERIFY_URL_DESIGN;	//开发环境
				break;
			case 1:
				//测试环境
				urlstr = IGSParams.IGS_VERIFY_URL_TEST;		//测试环境
				break;
			default :
				//正式环境
				urlstr = IGSParams.IGS_VERIFY_URL_RELEASE;	//正式环境
				break;
			}
			
			IGSRequestVO requestvo = new IGSRequestVO();
			requestvo.setAccountID(Integer.parseInt(AccountID));
			requestvo.setAppID(Integer.parseInt(AppID));
			requestvo.setGameID(Integer.parseInt(GameID));
			requestvo.setGroupNo(Integer.parseInt(GroupNo));
			requestvo.setServerNo(Integer.parseInt(ServerNo));
			requestvo.setUserID(Integer.parseInt(UserID));
			requestvo.setUserName(UserName);
//			JSONObject json = JSONObject.fromObject(requestvo);
//			String content = json.toString();
			String content = json2str(requestvo);

			System.out.println("igs cb url ->" + urlstr);
			System.out.println("igs cb content ->" + content);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			connection.setRequestMethod("POST");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}

			connection.disconnect();

			System.out.println("igs cb ret ->" + res);
			
//			res = "[{\"PayChannelID\":1,\"PayChannelOrderID\":1234,\"AccountID\":1,\"GameID\":1,\"ItemNumber\":123,\"ItemAmount\":1,\"ItemMoney\":0,\"ItemPrice\":0,\"CreateTime\":\"Date(1398154243000+0800)\"}]";

			JSONArray ja = JSONArray.fromObject(res);
			int itemnumber = 0;
			for (Iterator iter = ja.iterator() ; iter.hasNext() ;)
			{
				int i = 0;
				Object obji = iter.next();
				if (obji instanceof JSONObject)
				{
					OrderStatusVO st = new OrderStatusVO();
					IGSOrderVO retvo = new IGSOrderVO();
					retvo.setAccountID(((JSONObject) obji).getString("AccountID"));
					retvo.setGameID(((JSONObject) obji).getString("GameID"));
					retvo.setPayChannelID(((JSONObject) obji).getString("PayChannelID"));
					retvo.setPayChannelOrderID(((JSONObject) obji).getString("PayChannelOrderID"));
					retvo.setItemNumber(((JSONObject) obji).getString("ItemNumber"));
					retvo.setItemAmount(((JSONObject) obji).getString("ItemAmount"));
					retvo.setItemPrice(((JSONObject) obji).getString("ItemPrice"));
					retvo.setItemMoney(((JSONObject) obji).getString("ItemMoney"));
					retvo.setItemCurrencyCode(((JSONObject) obji).getString("ItemCurrencyCode"));
					retvo.setCreateTime(((JSONObject) obji).getString("CreateTime"));
					
					st.setPayId(retvo.getPayChannelOrderID());
					st.setAmount(retvo.getItemMoney());
					st.setPid(retvo.getItemNumber());
					st.setTid(retvo.getItemCurrencyCode());
					st.setStatus(1);
					statusret.add(st);
					
					itemnumber = Integer.parseInt(retvo.getItemAmount());
					for (int j = 0 ; j < itemnumber ; j++)
					{
						Orders order = genOrderInfo(vo, retvo);
						OrdersBean bean = new OrdersBean();
						String payId = bean.CreateOrder(order, order.getCallbackUrl());
						bean.updateOrderKStatus(payId, Constants.K_STSTUS_SUCCESS);
					}
				}
				else
				{
					System.out.println("igs cb ret [" + i++ + "]->" + obji);
				}
			}
			
			if (itemnumber > 0)
			{
				String path = OSUtil.getRootPath() + "../../logs/igscb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + DateUtil.getCurTimeStr();
				
				OSUtil.saveFile(filename, res);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		return arr.toString();
	}
	
	private static Orders genOrderInfo(Orders ordervo, IGSOrderVO vo)
	{
		Orders order = new Orders();
		
		order.setImei(ordervo.getImei());
		order.setUId(ordervo.getUId());
		order.setGversion(ordervo.getGversion());
		order.setOsversion(ordervo.getOsversion());
		order.setDeviceId(ordervo.getDeviceId());
		order.setDeviceType(ordervo.getDeviceType());
		order.setChannel(ordervo.getChannel());
		order.setAppId(ordervo.getAppId());
		order.setAmount(ordervo.getAmount());
		order.setCreateTime(ordervo.getCreateTime());
		order.setPayType(ordervo.getPayType());
		order.setPayId(vo.getPayChannelOrderID());
		float money = Float.valueOf(vo.getItemMoney()) * 100;
//		if (vo.getItemCurrencyCode() != null)
//		{
//			if (vo.getItemCurrencyCode().equals("USD"))
//			{
//				money = money * 29;
//			}
//		}
		order.setMoney(money + "");
		order.setItemId(vo.getItemNumber());
		order.setCurrency(vo.getItemCurrencyCode());
		order.setUnit(Constants.CURRENCY_UNIT_FEN);
		
		String cburl = ordervo.getCallbackUrl();
		if (cburl.indexOf("?") == -1)
		{
			cburl += "?pt=" + Constants.PAY_TYPE_IGS;
		}
		else
		{
			cburl += "&pt=" + Constants.PAY_TYPE_IGS;
		}
		cburl += "&p=" + (int) money;
		cburl += "&it=" + vo.getItemNumber();
		cburl += "&currency=" + vo.getItemCurrencyCode();
		cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
		order.setCallbackUrl(cburl);
		
		return order;
	}
	
	public static OrderIdVO getTransactionIdVO(Orders order)
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
				cburl += "?pt=" + Constants.PAY_TYPE_IGS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_IGS;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}
	
	public static String getTransactionIdAndStatus(Orders vo, String AccountID, String GameID, String AppID, String UserID, String UserName, String GroupNo, String ServerNo, String Environment)
	{
//		OrderIdVO orderIdVO = getTransactionIdVO(vo);
//		String ids = "";
//		if (orderIdVO != null)
//		{
//			ids = orderIdVO.getPayId();
//		}
		return checkOrdersStatus(vo, AccountID, GameID, AppID, UserID, UserName, GroupNo, ServerNo, Environment);
	}
	
	public static void init()
	{
//		params.addApp("gaoguai", "4038", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPtxJwSjpyMd23NhJqVR3o7QvA/0ZXhNBxmZ29FBsH/DwjxuhcYYz08GIWRF6AT0Watxu2In9M7Rnbecyy4uyT9gJG4WWbBCPa592+N7i88/zpP18equvHUT+US833pjw4jBebFOEAgGATC4rTbqjNiH5TXbcZrfFHkxapNsuYiwIDAQAB");	//2ec36f47fb4714a5971112a2e80a54cf
	}
}

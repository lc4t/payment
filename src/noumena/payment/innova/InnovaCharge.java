package noumena.payment.innova;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
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
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;
import eu.inn.api.services.JsonConfiguration;
import eu.inn.api.services.Session;
import eu.inn.api.services.SessionFactory;
import eu.inn.api.services.delivery.DeliverOrderRequest;
import eu.inn.api.services.delivery.DeliverOrderResponse;
import eu.inn.api.services.delivery.DeliveryService;

public class InnovaCharge
{
	private static InnovaParams params = new InnovaParams();
	private static boolean testmode = false;
	private static Session session = null;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		InnovaCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order, String cardnum1, String cardnum2, String version_id, String merchant_id, String order_date, String currency)
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
				cburl += "?pt=" + Constants.PAY_TYPE_CARD19;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_CARD19;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static HashMap<String, String> getPcIdByCards(String card, String pwd)
	{
		HashMap<String, String> ret = new HashMap<String, String>();
		
		String pcid = "";
		String pmid = "";

		ret.put("pcid", pcid);
		ret.put("pmid", pmid);
		
		if (card == null || pwd == null)
		{
			return ret;
		}
		if (card.length() == 17 && pwd.length() == 18)
		{
			pcid = "CMJFK00010001"; // 移动全国卡
			pmid = "CMJFK";
		}
		else if (card.length() == 16 && pwd.length() == 21)
		{
			pcid = "CMJFK00010102"; // 移动辽宁本地卡
			pcid = "CMJFK00010001"; // 移动全国卡
			pmid = "CMJFK";
		}
		else if (card.length() == 10 && pwd.length() == 8)
		{
			pcid = "CMJFK00010112"; // 移动浙江本地卡
			pcid = "CMJFK00010001"; // 移动全国卡
			pmid = "CMJFK";
		}
		else if (card.length() == 16 && pwd.length() == 17)
		{
			if ((card.substring(0, 1).equals("2") || card.substring(0, 1).equals("3"))&& pwd.substring(0, 3).equals("110"))
			{
				pcid = "CMJFK00010111"; // 移动江苏本地卡
				pcid = "CMJFK00010001"; // 移动全国卡
				pmid = "CMJFK";
			}
			else
			{
				pcid = "CMJFK00010014"; // 移动福建本地卡
				pcid = "CMJFK00010001"; // 移动全国卡
				pmid = "CMJFK";
			}
		}
		else if (card.length() == 15 && pwd.length() == 19)
		{
			pcid = "LTJFK00020000";// 全国联通一卡充
			pmid = "LTJFK";
		}
		else if (card.length() == 19 && pwd.length() == 18)
		{
			pcid = "DXJFK00010001";// 中国电信充值付费卡
			pmid = "DXJFK";
		}

		ret.put("pcid", pcid);
		ret.put("pmid", pmid);
		
		return ret;
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
	
	public static DeliverOrderResponse getCallbackFromCard19(DeliverOrderRequest request) throws Exception
	{
//        Order order = gameDB.findBy4gameOrderId(request.getOrderId())
//        if (order == null) {
//            // deliver order to the character
//            order = gameDB.insertOrder(
//                request.getAccountId(),
//                request.getProductId(),
//                request.getQuantity(),
//                request.getTotalPrice(),
//                request.getExpiration(),
//                request.getAddress(),        // Game Server
//                request.getReceiverName(),   // Character
//            );

        DeliverOrderResponse response = new DeliverOrderResponse();
        System.out.println("innova cb->" + request.toString());
        
        String innovaorderid = request.getOrderId();

        String orderid = createOrder(request, response);
        
		OrdersBean bean = new OrdersBean();
		try
		{
			//支付成功
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
				if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
				{
					List<Orders> orders = bean.qureyOrdersByPayId(innovaorderid);
					if (orders.size() <= 1)
					{
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("innova order (" + order.getOrderId() + ") had been succeed");
					}
				}
				else
				{
					System.out.println("innova order (" + order.getOrderId() + ") had been succeed");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		String path = OSUtil.getRootPath() + "../../logs/innovacb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + innovaorderid;
		
		OSUtil.saveFile(filename, request.toString());

		return response;
	}
	
	private static String createOrder(DeliverOrderRequest request, DeliverOrderResponse response)
	{
		String payid = "0";
        String innovaorderid = request.getOrderId();
        String accountid = request.getAccountId();
        String productid = request.getProductId();
        String amount = request.getTotalPrice() + "";
        long num = request.getQuantity();

		Date date = new Date();
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		Orders vo = new Orders();
		
		vo.setImei("");
		vo.setUId(accountid);
		vo.setChannel("118" + "A04290");
		vo.setAppId("118");
		vo.setAmount(Float.parseFloat(amount));
		vo.setCreateTime(df1.format(date));
		vo.setPayType(Constants.PAY_TYPE_INNOVA);
		vo.setItemId(productid);
		vo.setItemPrice("");
		vo.setItemNum((int) num);
		vo.setDeviceId("");
		vo.setExInfo("");
		vo.setPayId(innovaorderid);
		vo.setMoney(amount);

		PayServerBean payServerBean = new PayServerBean();
		PayGameBean payGameBean = new PayGameBean();
		PayServer payServer = payServerBean.get(InnovaParams.SERVER_ID + "_" + InnovaParams.GAME_ID);
		PayGame payGame = payGameBean.getGame(InnovaParams.GAME_ID);
		if (payServer == null || payGame == null)
		{
			return "";
		}
		String cburl = payServer.getCallbackUrl();
		String Parameter = "";
		Parameter += "userid=" + accountid;
		Parameter += "&gameid=" + InnovaParams.GAME_ID;
		Parameter += "&serverid=" + InnovaParams.SERVER_ID;
		Parameter += "&itmeid=" + productid;
		Parameter += "&amount=" + amount;
		Parameter += "&itemprice=";
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
		}
		OrdersBean bean = new OrdersBean();
		if (cburl == null || cburl.equals(""))
		{
			payid = bean.CreateOrder(vo);
		}
		else
		{
			if (cburl.indexOf("?") == -1)
			{
				cburl += "?pt=" + Constants.PAY_TYPE_INNOVA;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_INNOVA;
			}
			payid = bean.CreateOrder(vo, cburl);
		}

        response.setApplicationOrderId(payid);
        response.setDeliveryDate(date);
        
		return payid;
	}

	public static void finish()
	{
		if (session != null)
		{
			session.close();
		}
	}

	public static void init()
	{
		try
		{
			String filename = InnovaCharge.class.getResource("/").getPath();
			filename += "config/payment/pocket-fort-4game.cfg";
			File file = new File(filename);
			
			JsonConfiguration jsconf = new JsonConfiguration(file);
			session = SessionFactory.createSession(jsconf);
			DeliveryService deliveryService = new DeliveryService(session);
			deliveryService.subscribeToDeliverOrder(new DeliveryService.DeliverOrderCallback()
			{
			    @Override
			    public DeliverOrderResponse onDeliverOrder(DeliverOrderRequest request) throws Exception
			    {
			    	return getCallbackFromCard19(request);
			        // Check if this order was already delivered
//				        Order order = gameDB.findBy4gameOrderId(request.getOrderId())
//				        if (order == null) {
//				            // deliver order to the character
//				            order = gameDB.insertOrder(
//				                request.getAccountId(),
//				                request.getProductId(),
//				                request.getQuantity(),
//				                request.getTotalPrice(),
//				                request.getExpiration(),
//				                request.getAddress(),        // Game Server
//				                request.getReceiverName(),   // Character
//				            );
//				        }
//				        // process order delivery
//				        if (data.isSuccessfull) {
//				            var response = new DeliverOrderResponse()
//				            response.setApplicationOrderId(data.getGameOrderId());
//				            response.setDeliveryDate(data.getOrderCreateDate());
//				            return response;
//				        } else {
//				            throw new Exception("Can't create an order")
//				        }
			    }
			});
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
}

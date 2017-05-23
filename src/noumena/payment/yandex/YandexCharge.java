package noumena.payment.yandex;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class YandexCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		YandexCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		OrderIdVO orderIdVO = getTransactionIdVO(order);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
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
				cburl += "?pt=" + Constants.PAY_TYPE_YANDEX;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_YANDEX;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}
	
	public static String checkOrdersStatus(String payIds,YandexOrderVO yandexvo)
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
			if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(1);
			}
			else if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//订单没有结果
				int ret = checkOrderFromYandex(yandexvo,order.getOrderId(),order.getUId());
				if (ret == 1)
				{
					//验证成功
					st.setStatus(1);
					bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
				}
				else if (ret == 3) {
					//需要重新验证
					st.setStatus(3);
				}
				else if (ret == 4) {
					st.setStatus(4);
					bean.updateOrderKStatusNoCB(order.getOrderId(), Constants.ORDER_ERROR);
				}
				else
				{
					//验证失败，销毁订单
					st.setStatus(2);
					bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
				}
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
	
	private static int checkOrderFromYandex(YandexOrderVO yandexvo,String orderid,String uid)
	{
		try
		{
			String urlstr = "https://api.developer.store.yandex.ru/androidpublisher/v1/applications/%s/subscriptions/%s/purchases/%s?access_token=%s";
			urlstr = String.format(yandexvo.getPkname(),yandexvo.getSubscriptionID(),yandexvo.getPmtoken(),YandexParams.ACCESS_TOKEN);
			System.out.println("request  url--------->"+urlstr);
	
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.connect();
			
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
			
			System.out.println("res from yandex------->"+res);
			if (res.equals("")) {
				//发生异常，需要重新验证
				System.out.println("yandex charge exception :" + orderid + "-" + uid);
				return 3;
			}
			System.out.println("get res from Yandex--------"+res);
			JSONObject json = JSONObject.fromObject(res);
			System.out.println("res to json----->"+json.toString());
			boolean autoRenewing = json.getBoolean("autoRenewing");
			Calendar cal = Calendar.getInstance();
			
			if (autoRenewing) {
				//验证成功，通知服务器
				System.out.println("yandex charge success :" + orderid + "-" + uid);
				return 1;
			}else {
				if (cal.getTimeInMillis()-json.getLong("initiationTimestampMsec") >= json.getLong("validUntilTimestampMsec")) {
					//验证失败，订单超时，销毁订单
					System.out.println("yandex charge timeout :" + orderid + "-" + uid);
					return 4;
				}else {
					//验证失败，以后重连
					System.out.println("yandex charge next connection :" + orderid + "-" + uid);
					return 3;
				}
			}
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 2;
		}
	}
	
	public static String getTransactionIdAndStatus(Orders vo,YandexOrderVO yandexvo)
	{
		String pmtoken = yandexvo.getPmtoken();
		if (pmtoken == null)
		{
			List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId("0");
			st.setStatus(2);
			statusret.add(st);
			JSONArray arr = JSONArray.fromObject(statusret);
			return arr.toString();
		}
		OrderIdVO orderIdVO = getTransactionIdVO(vo);
		String ids = "";
		if (orderIdVO != null)
		{
			ids = orderIdVO.getPayId();
		}
		return checkOrdersStatus(ids,yandexvo);
	}
}

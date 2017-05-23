package noumena.payment.xiaomi;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
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
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class xiaomiCharge
{
	private static xiaomiParams params = new xiaomiParams();
	
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
				cburl += "?pt=" + Constants.PAY_TYPE_XIAOMI;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_XIAOMI;
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
			order.setProductId(order.getItemId());
			order.setSubId(order.getExInfo());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					st.setStatus(4);
				}
				else
				{
//					xiaomiOrderVO retvo = checkOrderFromXiaomi(order);
//					bean.updateOrderAmountPayId(retvo.getCpOrderId(), retvo.getOrderId(), retvo.getPayFee());
//					if (retvo.getOrderStatus() == null || !retvo.getOrderStatus().equals("TRADE_SUCCESS"))
//					{
//						st.setStatus(2);
//						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
//					}
//					else
//					{
//						st.setStatus(1);
//						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
//					}
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

	public static void getCallbackFromXiaomi(xiaomiOrderVO ordervo)
	{
		try
		{
			OrdersBean bean = new OrdersBean();
			
			bean.updateOrderAmountPayId(ordervo.getCpOrderId(), ordervo.getOrderId(), ordervo.getPayFee());
			if (ordervo.getOrderStatus().toUpperCase().equals("TRADE_SUCCESS"))
			{
				//支付成功
				bean.updateOrderKStatus(ordervo.getCpOrderId(), Constants.K_STSTUS_SUCCESS);
			}
			else
			{
				//支付失败
				bean.updateOrderKStatus(ordervo.getCpOrderId(), Constants.K_STSTUS_ERROR);
			}
	
			String path = OSUtil.getRootPath() + "../../logs/xiaomicb/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + ordervo.getAppId() + "_" + ordervo.getUid() + "_" + ordervo.getCpOrderId();
			
			String res = "";
			res += "appId=" + ordervo.getAppId();
			res += "&cpOrderId=" + ordervo.getCpOrderId();
			res += "&cpUserInfo=" + ordervo.getCpUserInfo();
			res += "&uid=" + ordervo.getUid();
			res += "&orderId=" + ordervo.getOrderId();
			res += "&orderStatus=" + ordervo.getOrderStatus();
			res += "&payFee=" + ordervo.getPayFee();
			res += "&productCode=" + ordervo.getProductCode();
			res += "&productName=" + ordervo.getProductName();
			res += "&productCount=" + ordervo.getProductCount();
			res += "&payTime=" + ordervo.getPayTime();
			res += "&signature=" + ordervo.getSignature();
			OSUtil.saveFile(filename, res);
			
			System.out.println("xiaomi cb ->" + res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static xiaomiOrderVO checkOrderFromXiaomi(Orders order)
	{
		xiaomiOrderVO ordervo = new xiaomiOrderVO();
		ordervo.setErrcode("1");
		try
		{
			String urlstr = xiaomiParams.CHECK_ORDER_STATUS_URL;
			String content = "";
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			content += "appId=" + order.getAppId();
			content += "&cpOrderId=" + order.getOrderId();
			content += "&uid=" + order.getSubId();
			String key = params.getParams(order.getAppId()).getAppkey();
			String signature = SHAEncryption.SHAEncrypt(content, key);
			content += "&signature=" + signature;

			System.out.println("xiaomi check order params->" + content);
			
			outs.write(content);
			outs.flush();
			outs.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/xiaomi/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + order.getAppId() + "_" + order.getUId() + "_" + order.getOrderId();
			OSUtil.saveFile(filename, res);
			
			JSONObject json = JSONObject.fromObject(res);
			ordervo = (xiaomiOrderVO) JSONObject.toBean(json, xiaomiOrderVO.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ordervo;
	}
	
	public static void init()
	{
		params.initParams(xiaomiParams.CHANNEL_ID, new xiaomiParamsVO());
		
//		addXiaomiApp("Galaxy2", "3873", "4825bf27-371e-1358-2cad-50a08df44c4c");
//		addXiaomiApp("Tinywar", "5253", "ce3b5961-ff94-7786-f4c3-50f66c7d0693");
//		addXiaomiApp("Cangqiong", "5252", "7c12025e-133a-c470-d039-50f66975c5ed");
//		addXiaomiApp("Zhengqi", "5255", "50c1574f-e6e3-153f-b964-50f6700211b2");
//		addXiaomiApp("t6", "14806", "e6f20a27-b608-c17a-df19-51b3060291fc");
//		addXiaomiApp("m1", "15174", "cf3cc7d9-4318-6bf8-0b85-51bfd2a8e9ff");
//		addXiaomiApp("gaoguai", "22808", "1405709c-03bf-69e4-be60-52cb76266877");
//		addXiaomiApp("t6_nvshen", "24621", "146fe410-1189-2459-6530-533903438674");
//		addXiaomiApp("xixuegui", "2882303761517226542", "6EcHKATy3okGa7+ylcLrCA==");		//5871722625542
//		addXiaomiApp("t8", "2882303761517228332", "X8UJNmTvtB3lS2U+OUdmYw==");				//5431722841332
//		addXiaomiApp("m5", "2882303761517251821", "mFJTPxATsfPilfj5dWqxhg==");				//5841725117821
//		addXiaomiApp("com.noumena.android.qyqz.mi", "2882303761517265507", "hNdDIsXqfvNgYmR78LiUAA==");		//5221726522507
//		addXiaomiApp("com.kongzhong.publish.gz.mi", "2882303761517249123", "6q07jukOh2xaK2kHPkyusA==");		//5371724943123
//		addXiaomiApp("com.kongzhong.long8.mi", "2882303761517271598", "9dBZBPIkladzYNGs8uye9g==");			//5141727184598
//		addXiaomiApp("com.noumena.android.m3cn.mi", "2882303761517279495", "dJSay5mjwYOy6+ZCL/okVQ==");		//5761727985495
	}
}

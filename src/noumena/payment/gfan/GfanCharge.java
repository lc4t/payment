package noumena.payment.gfan;

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
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;
import sun.misc.BASE64Encoder;

public class GfanCharge
{
	private static GfanParams params = new GfanParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		GfanCharge.testmode = testmode;
	}

	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_GFAN);
		order.setUnit(Constants.CURRENCY_UNIT_JIAO);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_GFAN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_GFAN;
			}
			cburl += "&currency=" + Constants.CURRENCY_GFAN;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_JIAO;
			
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
				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					st.setStatus(2);
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
	
	public static String verifyOrderFromGfan(String orderid, String pkgname, String appid, String chlid)
	{
		String ret = "";
		String urlstr = GfanParams.VERIFY_URL;
		String appkey = params.getAppKeyById(appid);
		System.out.println("gfan verify order url ->" + urlstr);
		String ua = "packageName=%s,appName=%s,channelID=%s";
		System.out.println("gfan verify order header ->" + ua);

		try
		{
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setRequestMethod("POST");
			connection.setReadTimeout(60000);
			connection.setRequestProperty("User-Agent", ua);
			connection.setDoInput(true);
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");

			StringBuffer content = new StringBuffer();
			content.append("<request>");
			content.append("<order_id>");
			content.append(orderid);
			content.append("</order_id>");
			content.append("<app_key>");
			content.append(appkey);
			content.append("</app_key>");
			content.append("</request>");
			
			String requestStr = new BASE64Encoder().encode(new TEAEncryption().encrypt(content.toString().getBytes(), appkey.getBytes()));

			System.out.println("gfan request params->" + content.toString());
			System.out.println("gfan request appkey->" + appkey);
			System.out.println("gfan request miwen->" + requestStr);
			
			outs.write(requestStr);
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/gfan/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			OSUtil.saveFile(filename, res);
			
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = "0";
		}
		
		return ret;
	}

	public static String getCallbackFromGfan(String time, String sign, String postcnt)
	{
		String errorcode = "1";
		String errordesc = "Success";
		
		String mysign = StringEncrypt.Encrypt(GfanParams.GFAN_ACCOUNT_ID + time);
		if (!mysign.equals(sign))
		{
			//验证非法
			errorcode = "1";
			errordesc = "Invalid Sign";
		}
		else
		{
			String price = getValueByKey(postcnt, "cost");
			String orderid = getValueByKey(postcnt, "order_id");
			
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
				if (order.getKStatus() == Constants.K_STSTUS_SUCCESS)
				{
					//订单已经通知成功，不再通知
					errorcode = "1";
					errordesc = "Orderid Existed";
				}
				else
				{
					//其他状态再通知一次
					bean.updateOrderAmountExinfo(orderid, postcnt, price);
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					errorcode = "1";
					errordesc = "Success";
				}
			}
			else
			{
				//订单不存在
				errorcode = "1";
				errordesc = "Invalid Orderid";
			}
		}
		
		String ret = "<response><ErrorCode >" + errorcode + "</ErrorCode><ErrorDesc>" + errordesc + "</ErrorDesc></response>";
		return ret;
	}
	
	private static String getValueByKey(String content, String key)
	{
		String value = "";
		String key1 = "<" + key + ">";
		String key2 = "</" + key + ">";
		
		int pos1 = 0, pos2 = 0;
		pos1 = content.indexOf(key1);
		pos2 = content.indexOf(key2);
		value = content.substring(pos1 + key1.length(), pos2);
		
		return value;
	}
	
	public static void init()
	{
		params.addApp("gaoguai", "635904656", "d2604a64fc0f962d");
		params.addApp("sanguoqunying", "229895609", "5e362f92b38b9f86");
		params.addApp("dalingzhu", "1872277796", "1b8fb043b256546e");
		params.addApp("m1", "51153943", "36ac999293a199de");
		params.addApp("xixuegui", "2062164023", "f18357492f683c67");
	}
}

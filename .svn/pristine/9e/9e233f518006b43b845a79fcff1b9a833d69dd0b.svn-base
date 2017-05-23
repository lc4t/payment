package noumena.payment.app01;

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
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class App01Charge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		App01Charge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_APP01;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_APP01;
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
				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					status = Constants.K_STSTUS_TIMEOUT;
					st.setStatus(2);
				}
				else
				{
					String ret = verifyOrderFromApp01(order.getOrderId());
					if (ret.equals("1"))
					{
						st.setStatus(1);
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						st.setStatus(2);
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
					}
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
	
	public static String verifyOrderFromApp01(String orderid)
	{
		String ret = "0";
		String urlstr = "";
		String appkey = "";
		if (testmode == true)
		{
			appkey = App01Params.KEY_TEST;
			urlstr = App01Params.VERIFY_URL_TEST;
		}
		else
		{
			appkey = App01Params.KEY_RELEASE;
			urlstr = App01Params.VERIFY_URL_RELEASE;
		}
		String shopid = App01Params.SHOP_ID;
		
		String minwen = appkey + "*" + shopid + "*" + orderid + "*" + appkey;
		String checkcode = StringEncrypt.Encrypt(minwen);
		
		urlstr = String.format(urlstr, shopid, orderid, checkcode);
		System.out.println("app01 minwen ->" + minwen);
		System.out.println("app01 verify order url ->" + urlstr);

		try
		{
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoInput(true);

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/app01/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			OSUtil.saveFile(filename, res);
			
			//TRANSACTIONID=PPSK191702784, TRANSACTIONSTATUS=0, AMOUNT=30, TRANSACTIONMSG=付費成功
			String sep1 = "TRANSACTIONSTATUS=";
			String sep2 = ", AMOUNT=";
			int pos1 = res.indexOf(sep1);
			int pos2 = res.indexOf(sep2);
			
			ret = res.substring(pos1 + sep1.length(), pos2);
			System.out.println("app01 ret ->" + res + "-" + pos1 + "-" + pos2 + "-" + ret);
			if (ret.equals("0"))
			{
				ret = "1";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = "0";
		}
		
		return ret;
	}
}

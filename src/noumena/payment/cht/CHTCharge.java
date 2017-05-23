package noumena.payment.cht;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class CHTCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		CHTCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_CHT;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_CHT;
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
					status = CHTCharge.checkOrderFromCHT(order);
					if (status == 1)
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
	
	private static int checkOrderFromCHT(Orders order)
	{
		try
		{
			String urlstr = "";
			if (isTestmode() == true)
			{
				urlstr = CHTParams.CHT_CHECK_URL_TEST;
			}
			else
			{
				urlstr = CHTParams.CHT_CHECK_URL_RELEASE;
			}
			
			//vo.setExInfo(productid + "#" + transid + "#" + subid + "#" + ip);
			String exinfo = order.getExInfo();
			String[] ps = exinfo.split("#");
			urlstr = String.format(urlstr, ps[1], URLEncoder.encode(ps[2], "utf-8"));
			
			System.out.println("cht request ->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			outs.flush();
			outs.close();
			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());

			connection.disconnect();

			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/cht/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + order.getOrderId();
			OSUtil.saveXML(root, filename);
			
			List<?> childrens = root.getChildren("orderStatus");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				System.out.println("cht check order status->" + str);
				if (str.equals("1"))
				{
					return 1;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 0;
	}
}

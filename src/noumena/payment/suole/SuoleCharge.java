package noumena.payment.suole;

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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class SuoleCharge
{
	private static SuoleParams params = new SuoleParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		SuoleCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_SUOLE;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_SUOLE;
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
					status = Constants.K_STSTUS_TIMEOUT;
					st.setStatus(2);
				}
				else
				{
					SuoleOrderVO retvo = submitOrderFromSuole(order);
					if (retvo.getStatus() == null || !retvo.getStatus().equals("1"))
					{
						st.setStatus(2);
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
					}
					else
					{
						retvo = checkOrderFromSuole(order.getOrderId(), params.getAppKeyById(order.getAppId()), order.getAppId(), order.getSubId());
						if (retvo.getResult() == null || !retvo.getResult().equals("success"))
						{
							st.setStatus(2);
							bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
						}
						else
						{
							st.setStatus(1);
							bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
						}
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

	private static SuoleOrderVO submitOrderFromSuole(Orders order)
	{
		SuoleOrderVO ordervo = new SuoleOrderVO();
		ordervo.setStatus("0");
		try
		{
			System.out.println("sub id->" + order.getSubId());
			System.out.println("app id->" + order.getAppId());
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = SuoleParams.SUBMIT_ORDER_STATUS_URL_TEST;
			}
			else
			{
				urlstr = SuoleParams.SUBMIT_ORDER_STATUS_URL_RELEASE;
			}
			String content = "";
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());

			String mingwen = "";
			content += "<order>\n";
			content += "<usercode>";
			content += order.getSubId();
			mingwen += order.getSubId();
//			content += "1500000160791";
//			mingwen += "1500000160791";
			content += "</usercode>\n";
			content += "<gamecode>";
			content += order.getAppId();
			mingwen += order.getAppId();
			content += "</gamecode>\n";
			content += "<tradnum>";
			content += order.getOrderId();
			mingwen += order.getOrderId();
			content += "</tradnum>\n";
			content += "<account>";
			content += "account";
			mingwen += "account";
			content += "</account>\n";
			content += "<servername>";
			content += "servername";
			mingwen += "servername";
			content += "</servername>\n";
			content += "<pamt>";
			content += (int) order.getAmount().floatValue();
			mingwen += (int) order.getAmount().floatValue();
			content += "</pamt>\n";
			content += "<checking>";
			mingwen += params.getAppKeyById(order.getAppId());
			content += md5Encryption.toMD5String(mingwen);
			content += "</checking>\n";
			content += "</order>";

			System.out.println("mingwen->" + mingwen);
			System.out.println("params->" + content);
			
			outs.write(content);
			outs.flush();
			outs.close();

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			connection.disconnect();

			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/suoleorder/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + order.getAppId() + "_" + order.getUId() + "_" + order.getOrderId();
			OSUtil.saveXML(root, filename);

			List<?> childrens = root.getChildren("tradnum");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setTradnum(str);
			}

			childrens = root.getChildren("status");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setStatus(str);
			}

			childrens = root.getChildren("realamount");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setRealamount(str);
			}

			childrens = root.getChildren("checking");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setChecking(str);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ordervo;
	}

	private static SuoleOrderVO checkOrderFromSuole(String orderid, String key, String appid, String uid)
	{
		SuoleOrderVO ordervo = new SuoleOrderVO();
		ordervo.setStatus("0");
		try
		{
			System.out.println("check order->" + orderid);
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = SuoleParams.CHECK_ORDER_STATUS_URL_TEST;
			}
			else
			{
				urlstr = SuoleParams.CHECK_ORDER_STATUS_URL_RELEASE;
			}
			String content = "";
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());

			String mingwen = "";
			content += "<order>\n";
			content += "<tradnum>";
			content += orderid;
			mingwen += orderid;
			content += "</tradnum>\n";
			content += "<status>";
			content += "1";
			mingwen += "1";
			content += "</status>\n";
			content += "<checking>";
			mingwen += key;
			content += md5Encryption.toMD5String(mingwen);
			content += "</checking>\n";
			content += "</order>";

			System.out.println("mingwen->" + mingwen);
			System.out.println("params->" + content);
			
			outs.write(content);
			outs.flush();
			outs.close();

			SAXBuilder builder = new SAXBuilder();
			Document doc = null;
			try
			{
				doc = builder.build(connection.getInputStream());
			}
			catch (Exception e)
			{
				e.printStackTrace();
				connection.disconnect();
				
				return ordervo;
			}
			connection.disconnect();

			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/suoleordercheck/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + appid + "_" + uid + "_" + orderid;
			OSUtil.saveXML(root, filename);

			List<?> childrens = root.getChildren("tradnum");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setTradnum(str);
			}
			childrens = root.getChildren("result");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setResult(str);
			}
			childrens = root.getChildren("checking");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setChecking(str);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ordervo;
	}
	
	public static void init()
	{
		addApp("miniWar", "1352967807937", "5ojqk3sNt3CHUJxCSjK2dYxKrn8RRSKs");
	}
	
	public static void addApp(String appname, String appid, String appkey)
	{
		params.addApp(appname, appid, appkey);
	}
}

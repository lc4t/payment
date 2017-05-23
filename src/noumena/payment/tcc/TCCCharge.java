package noumena.payment.tcc;

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

public class TCCCharge
{
	public static String getTransactionId(Orders order)
	{
		OrderIdVO orderIdVO = new OrderIdVO();
		String date = DateUtil.formatDate(order.getCreateTime());
		orderIdVO.setTime(date);
		if (checkSubIdValid(order.getSubId()) == false)
		{
			orderIdVO.setPayId("-1");
		}
		else
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
					cburl += "?pt=" + Constants.PAY_TYPE_TCC;
				}
				else
				{
					cburl += "&pt=" + Constants.PAY_TYPE_TCC;
				}
				payId = bean.CreateOrder(order, cburl);
			}
			order.setCallbackUrl(cburl);
			orderIdVO.setPayId(payId);
		}
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
			order.setSubId(order.getExInfo());
			order.setProductId(order.getItemId());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (checkSubIdValid(order.getSubId()) == false)
			{
				st.setStatus(3);
			}
			else if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
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
					TCCOrderVO retvo = getTokenFromTCC(order);
					if (retvo.getReturn_code().equals("00"))
					{
						retvo = doBillingFromTCC(order, retvo);
						if (retvo.getReturn_code().equals("00"))
						{
							st.setStatus(1);
							bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							st.setStatus(2);
						}
					}
					else
					{
						st.setStatus(2);
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

	private static TCCOrderVO doBillingFromTCC(Orders order, TCCOrderVO tokenvo)
	{
		TCCOrderVO ordervo = new TCCOrderVO();
		try
		{
			System.out.println("sub id->" + order.getSubId());
			String urlstr = TCCParams.TCC_BILLING_URL;
			String content = "";
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			content += "callerID=" + TCCParams.TCC_CALLER_ID;
			content += "&callerPwd=" + TCCParams.TCC_CALLER_PWD;
			content += "&service_ID=" + order.getAppId();
			content += "&token=" + tokenvo.getToken();
			
			outs.write(content);
			outs.flush();
			outs.close();
			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());

			connection.disconnect();

			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/tccbilling/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + order.getAppId() + "_" + order.getUId() + "_" + order.getOrderId();
			OSUtil.saveXML(root, filename);
			
			List<?> childrens = root.getChildren("return-code");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setReturn_code(str);
			}
			
			childrens = root.getChildren("description");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setDescription(str);
			}
			
			childrens = root.getChildren("info");
			for (int i = 0 ; i < childrens.size() ; i++)
			{
				Element children = (Element) childrens.get(i);
				String str = children.getAttributeValue("name");
				if (str.equals("token"))
				{
					str = children.getText();
					ordervo.setToken(str);
				}
				else if (str.equals("service_ID"))
				{
					str = children.getText();
					ordervo.setService_ID(str);
				}
				else if (str.equals("serviceDescription"))
				{
					str = children.getText();
					ordervo.setServiceDescription(str);
				}
				else if (str.equals("ratingCode"))
				{
					str = children.getText();
					ordervo.setRatingCode(str);
				}
				else if (str.equals("payerSubID"))
				{
					str = children.getText();
					ordervo.setPayerSubID(str);
				}
				else if (str.equals("status"))
				{
					str = children.getText();
					ordervo.setStatus(str);
				}
				else if (str.equals("aaTime"))
				{
					str = children.getText();
					ordervo.setAaTime(str);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ordervo;
	}
	
	private static TCCOrderVO getTokenFromTCC(Orders order)
	{
		TCCOrderVO ordervo = new TCCOrderVO();
		try
		{
			System.out.println("sub id->" + order.getSubId());
			String urlstr = TCCParams.TCC_GET_TOKEN_URL;
			String content = "";
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			content += "callerID=" + TCCParams.TCC_CALLER_ID;
			content += "&callerPwd=" + TCCParams.TCC_CALLER_PWD;
			content += "&payerSubID=" + order.getSubId();
			content += "&service_ID=" + order.getAppId();
			content += "&ratingCode=" + order.getProductId();
			content += "&contentDescription=" + order.getAppId() + "_" + order.getProductId();
			//content += "&contentDescription=xxxx";
			//content += "&payerSubID=14010671";
			//content += "&service_ID=KON01001";
			//content += "&ratingCode=846833651";
			
			outs.write(content);
			outs.flush();
			outs.close();
			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());

			connection.disconnect();

			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/tcctoken/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + order.getAppId() + "_" + order.getUId() + "_" + order.getOrderId();
			OSUtil.saveXML(root, filename);
			
			List<?> childrens = root.getChildren("return-code");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setReturn_code(str);
			}
			
			childrens = root.getChildren("description");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				ordervo.setDescription(str);
			}
			
			childrens = root.getChildren("info");
			for (int i = 0 ; i < childrens.size() ; i++)
			{
				Element children = (Element) childrens.get(i);
				String str = children.getAttributeValue("name");
				if (str.equals("token"))
				{
					str = children.getText();
					ordervo.setToken(str);
				}
				else if (str.equals("service_ID"))
				{
					str = children.getText();
					ordervo.setService_ID(str);
				}
				else if (str.equals("serviceDescription"))
				{
					str = children.getText();
					ordervo.setServiceDescription(str);
				}
				else if (str.equals("ratingCode"))
				{
					str = children.getText();
					ordervo.setRatingCode(str);
				}
				else if (str.equals("payerSubID"))
				{
					str = children.getText();
					ordervo.setPayerSubID(str);
				}
				else if (str.equals("status"))
				{
					str = children.getText();
					ordervo.setStatus(str);
				}
				else if (str.equals("aaTime"))
				{
					str = children.getText();
					ordervo.setAaTime(str);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ordervo;
	}
	
	private static boolean checkSubIdValid(String subid)
	{
		boolean ret = true;
		
		if (subid.substring(0, 1).toLowerCase().equals("i"))
		{
			ret = false;
		}
		
		if (subid.equals("14009810"))
		{
			ret = false;
		}
		
		return ret;
	}
}

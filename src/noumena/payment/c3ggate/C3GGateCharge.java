package noumena.payment.c3ggate;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

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

public class C3GGateCharge
{
	private static C3GGateParams params = new C3GGateParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		C3GGateCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_3GGATE;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_3GGATE;
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

	public static String getCallbackFrom3GGate(String postdata, String sign)
	{
		String ret = "ok";

		System.out.println("c3ggate cb postdata->" + postdata);
		System.out.println("c3ggate cb sign->" + sign);
		
		if (postdata == null || postdata.equals(""))
		{
			return ret;
		}
		
		try
		{
			C3GGateOrderVO vo = new C3GGateOrderVO();

			StringReader read = new StringReader(postdata);
			InputSource source = new InputSource(read);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(source);
			Element root = doc.getRootElement();
			
			List<?> childrens = root.getChildren("orderid");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setOrderid(str);
			}
			
			childrens = root.getChildren("gameid");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setGameid(str);
			}
			
			childrens = root.getChildren("token");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setToken(str);
			}
			
			childrens = root.getChildren("cpid");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setCpid(str);
			}
			
			childrens = root.getChildren("access");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setAccess(str);
			}
			
			childrens = root.getChildren("paytotalfee");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setPaytotalfee(str);
			}
			
			childrens = root.getChildren("paytypeid");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setPaytypeid(str);
			}
			
			childrens = root.getChildren("cporderid");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setCporderid(str);
			}
			
			childrens = root.getChildren("stime");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setStime(str);
			}
			
			String minwen = params.getAppKeyById(vo.getGameid() + "_" + vo.getCpid());
			minwen += "_" + vo.getOrderid();
			minwen += "_" + vo.getGameid();
			minwen += "_" + vo.getToken();
			
			String miwen = StringEncrypt.Encrypt(minwen);
			
			System.out.println("c3ggate cb minwen->" + minwen);
			System.out.println("c3ggate cb miwen->" + miwen);
			System.out.println("c3ggate cb sign->" + sign);
			
			if (miwen.equals(sign))
			{
				try
				{
					OrdersBean bean = new OrdersBean();
					Orders order = bean.qureyOrder(vo.getCporderid());
					if (order != null)
					{
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(vo.getCporderid(), vo.getOrderid(), vo.getPaytotalfee(), vo.getPaytypeid());
							
							if (vo.getAccess().equals("1"))
							{
								//支付成功
								bean.updateOrderKStatus(vo.getCporderid(), Constants.K_STSTUS_SUCCESS);
							}
							else
							{
								//支付失败
								bean.updateOrderKStatus(vo.getCporderid(), Constants.K_STSTUS_ERROR);
							}
						}
						else
						{
							System.out.println("c3ggate order (" + order.getOrderId() + ") had been succeed");
						}
					}
					
					String path = OSUtil.getRootPath() + "../../logs/c3ggatecb/" + DateUtil.getCurTimeStr().substring(0, 8);
					OSUtil.makeDirs(path);
					String filename = path + "/" + vo.getCporderid();
					
					OSUtil.saveFile(filename, postdata);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
				ret = "FAILURE";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}
	
	public static void init()
	{
		params.addApp("gaoguai", "2496_2484", "2324gaoguaisanguo");	//alT96LfBmgTAGndfGkkR9MHgiStVcEKT
	}
}

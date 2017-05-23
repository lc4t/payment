package noumena.payment.tstore;

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
import noumena.payment.util.NotifyPurchase;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class TStoreCharge
{
	private static TStoreParams params = new TStoreParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		TStoreCharge.testmode = testmode;
	}

	public static void init()
	{
		addTStoreApp("CangQiong", "OA00307582", "0AABMttjyefVeVYf");
		addTStoreApp("KingdomStory", "OA00307583", "AVHHRZuBz8kfwSU4");
		addTStoreApp("TinyWar", "OA00307584", "mVDg7uDanFf1NBH7");
		addTStoreApp("Galaxy2", "OA00319111", "uJPraZaUTnQeD8if");
		addTStoreApp("M1", "OA00387578", "VQNOVziyF9C3v39X");
		//addTStoreApp("M5", "OA00697455", "");
	}
	
	public static void addTStoreApp(String appname, String appid, String appkey)
	{
		params.addTStoreApp(appname, appid, appkey);
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
				cburl += "?pt=" + Constants.PAY_TYPE_TSTORE;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_TSTORE;
			}
			order.setCurrency(Constants.CURRENCY_KRW);
			order.setUnit(Constants.CURRENCY_RMB);
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	/*public static String checkOrdersStatus(String payIds)
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
					TStoreOrderVO ordervo = TStoreCharge.checkOrderFromTStore(order);
					status = ordervo.getResult().getStatus();
					if (status == TStoreOrderResultVO.ORDER_STATUS_SUCCESS)
					{
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
					}
					st = parseOrder(bean, order, ordervo);
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
	}*/
	
	public static String checkOrdersStatusNew(String payIds)
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
					status = Constants.K_STSTUS_DEFAULT;
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
	
	public static String checkOrdersStatusNew(String txid, String appid, String receipt)
	{
		List<OrderStatusVO> statusret = TStoreCharge.checkOrderFromTStoreNew(txid, appid, receipt);
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}
	
	/*private static TStoreOrderVO checkOrderFromTStore(Orders order)
	{
		TStoreOrderVO ordervo = new TStoreOrderVO();
		String appid = order.getAppId();
		String orderid = order.getOrderId();
		System.out.println("appid->" + appid);
		System.out.println("orderid->" + orderid);
		
		String urlstr = params.getCheckUrl(orderid, appid, isTestmode());
		if (urlstr == null)
		{
			//no app
			ordervo.getResult().setStatus(TStoreOrderResultVO.ORDER_STATUS_NOAPPID);
		}
		
		System.out.println("url->" + urlstr);
		
		try
		{
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write("");
			outs.flush();
			outs.close();
			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			connection.disconnect();
			
			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/tstorebilling/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + appid + "_" + order.getUId() + "_" + orderid;
			OSUtil.saveXML(root, filename);
			List<?> childrens = root.getChildren("result");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				childrens = children.getChildren("status");
				if (childrens.size() > 0)
				{
					Element children2 = (Element) childrens.get(0);
					String str = children2.getText();
					ordervo.getResult().setStatus(Integer.parseInt(str));
				}
				childrens = children.getChildren("detail");
				if (childrens.size() > 0)
				{
					Element children2 = (Element) childrens.get(0);
					String str = children2.getText();
					ordervo.getResult().setDetail(str);
				}
				childrens = children.getChildren("message");
				if (childrens.size() > 0)
				{
					Element children2 = (Element) childrens.get(0);
					String str = children2.getText();
					ordervo.getResult().setMessage(str);
				}
				childrens = children.getChildren("appid");
				if (childrens.size() > 0)
				{
					Element children2 = (Element) childrens.get(0);
					String str = children2.getText();
					ordervo.getResult().setAppid(str);
				}
			}

			childrens = root.getChildren("billing_log");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				childrens = children.getChildren("item");
				if (childrens.size() > 0)
				{
					Element children2 = (Element) childrens.get(0);
					List<?> childrens2 = children2.getChildren("tid");
					if (childrens2.size() > 0)
					{
						Element children3 = (Element) childrens2.get(0);
						String str = children3.getText();
						ordervo.getBilling().getItem().setTid(str);
					}
					childrens2 = children2.getChildren("product_id");
					if (childrens2.size() > 0)
					{
						Element children3 = (Element) childrens2.get(0);
						String str = children3.getText();
						ordervo.getBilling().getItem().setProductId(str);
					}
					childrens2 = children2.getChildren("log_time");
					if (childrens2.size() > 0)
					{
						Element children3 = (Element) childrens2.get(0);
						String str = children3.getText();
						ordervo.getBilling().getItem().setLogTime(str);
					}
					childrens2 = children2.getChildren("charge_amount");
					if (childrens2.size() > 0)
					{
						Element children3 = (Element) childrens2.get(0);
						String str = children3.getText();
						ordervo.getBilling().getItem().setChargeAmount(str);
					}
					childrens2 = children2.getChildren("detail_pname");
					if (childrens2.size() > 0)
					{
						Element children3 = (Element) childrens2.get(0);
						String str = children3.getText();
						ordervo.getBilling().getItem().setDetailPname(str);
					}
					childrens2 = children2.getChildren("bp_info");
					if (childrens2.size() > 0)
					{
						Element children3 = (Element) childrens2.get(0);
						String str = children3.getText();
						ordervo.getBilling().getItem().setBpInfo(str);
					}
					childrens2 = children2.getChildren("<tcash_flag");
					if (childrens2.size() > 0)
					{
						Element children3 = (Element) childrens2.get(0);
						String str = children3.getText();
						ordervo.getBilling().getItem().setTcashFlag(str);
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ordervo;
	}*/
	
	private static List<OrderStatusVO> checkOrderFromTStoreNew(String txid, String appid, String receipt)
	{
		List<OrderStatusVO> rets = new ArrayList<OrderStatusVO>();
		System.out.println("txid: "+txid+" appid:"+appid+" receipt:"+receipt);
		TStoreEOrderVO ordervo = new TStoreEOrderVO();
		ordervo.setAppid(appid);
		ordervo.setSigndata(receipt);
		ordervo.setTxid(txid);
		
		String urlstr = "";
		if (isTestmode() == true)
		{
			urlstr = TStoreParams.TSTORE_E_CHECK_URL_TEST;
		}
		else
		{
			urlstr = TStoreParams.TSTORE_E_CHECK_URL_RELEASE;
		}
		
		try
		{
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-type", "application/json");
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			JSONObject json = JSONObject.fromObject(ordervo);
			outs.write(json.toString());
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
			System.out.println("res:"+res);
			json = JSONObject.fromObject(res);
			if (json != null)
			{
				System.out.println("tsssssssssssssssssss");
				System.out.println(json.getString("status").equals("0"));
				System.out.println(json.getString("detail").equals("0000"));
				System.out.println(json.getString("status").equals("0") && json.getString("detail").equals("0000"));
				if (json.getString("status").equals("0") && json.getString("detail").equals("0000"))
				{
					int count = json.getInt("count");
					String products = json.getString("product");
					JSONArray arr = JSONArray.fromObject(products);
					for (int i = 0 ; i < count ; i++)
					{
						JSONObject product = (JSONObject) arr.get(i);
						String orderid = product.getString("tid");
						String amount = product.getString("charge_amount");
						String exinfo = product.getString("product_id");
						OrderStatusVO ret = updateOrder(orderid, amount, exinfo, txid);
						rets.add(ret);
					}
				}
			}
			
			String path = OSUtil.getRootPath() + "../../logs/tstorebilling2/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + txid;
			OSUtil.saveFile(filename, res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return rets;
	}
	
	private static OrderStatusVO updateOrder(String orderid, String amount, String exinfo, String txid)
	{
		OrderStatusVO st = new OrderStatusVO();

		OrdersBean bean = new OrdersBean();
		Orders order = bean.qureyOrder(orderid);
		if (order != null)
		{
			int status = order.getKStatus();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					st.setStatus(Constants.K_STSTUS_TIMEOUT);
				}
				else
				{
					st.setStatus(Constants.K_STSTUS_SUCCESS);
					bean.updateOrderAmountPayIdExinfo(orderid, txid, amount, exinfo);
					bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
					
					//通知kakao成功的订单
					NotifyPurchase.notify(orderid);
				}
			}
			else if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(Constants.K_STSTUS_SUCCESS);
			}
			else
			{
				//订单已经失败，直接返回订单状态
				st.setStatus(Constants.K_STSTUS_ERROR);
			}
		}
		
		return st;
	}
	
	/*private static OrderStatusVO parseOrder(OrdersBean bean, Orders order, TStoreOrderVO ordervo)
	{
		OrderStatusVO ret = new OrderStatusVO();
		int status = 2;
		
		if (ordervo.getResult().getStatus() == 0)
		{
			status = 1;
		}
		else
		{
			status = 2;
		}
		
		ret.setPayId(order.getOrderId());
		ret.setStatus(status);

		System.out.println("orderid->" + order.getOrderId());
		System.out.println("appid->" + ordervo.getResult().getAppid());
		System.out.println("status->" + ordervo.getResult().getStatus());
		System.out.println("detail->" + ordervo.getResult().getDetail());
		System.out.println("message->" + ordervo.getResult().getMessage());
		
		bean.updateOrderCStatus(order.getOrderId());
		
		return ret;
	}*/
}

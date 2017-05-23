package noumena.payment.lguplus;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.TrustAnyTrustManager;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class LGUplusCharge
{
	private static LGUplusParams params = new LGUplusParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		LGUplusCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_LGUPLUS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_LGUPLUS;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static String checkOrdersStatus(String payIds, String pid, String appid, String ctn, String mac, String ukey)
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
					LGUplusOrderVO retvo = checkOrderFromLGUplus(order, pid, appid, ctn, mac, ukey);
					if (retvo.getStatus().equals("0"))
					{
						st.setStatus(1);
						bean.updateOrderAmountPayIdExinfo(order.getOrderId(), order.getPayId(), retvo.getPrice(), retvo.getTxid());
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

	private static LGUplusOrderVO checkOrderFromLGUplus(Orders order, String pid, String appid, String ctn, String mac, String ukey)
	{
		String ectn = getKey(ctn, order.getOrderId());
		return verify(order, pid, appid, ectn, mac, ukey);
	}
	
	private static String getContent(Orders order, String pid, String appid, String ctn, String mac, String ukey)
	{
//		<?xml version="1.0" encoding="UTF-8"?>
//		<request>
//		<service_name>BuyVerify</service_name>
//		<request_time>20110516131001</request_time>
//		<reponse_time></reponse_time>
//		<pid>402007607472</pid>
//		<appid>Q01010251864</appid>
//		<ctn>F765AB5D9E751D7D0CF4C84E3AF04799</ctn>
//		<devflag>SERVER</devflag>
//		<ukey>4686A1DA9E1621EFAE2906B7AFBFA353D137936CB59C92DA9CA56C6C29EFFAAE</ukey>
//		<txid>TXID_20131031171654631905</txid>
//		<comtype>L</comtype>
//		<deviceip>111.111.111.111</deviceip>
//		<networktype>W</networktype>
//		<create_id>S_CMS_CP</create_id>
//		<mac>f8d0.bd7c.5398</mac>
//		</request>
		
		String content = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
		content += "<request>";
		content += "<service_name>BuyVerify</service_name>";
		content += "<request_time>" + DateUtil.getCurTimeStr() + "</request_time>";
		content += "<reponse_time></reponse_time>";
		content += "<pid>" + pid + "</pid>";
		content += "<appid>" + appid + "</appid>";
		content += "<ctn>" + ctn + "</ctn>";
		content += "<devflag>SERVER</devflag>";
		content += "<ukey>" + ukey + "</ukey>";
		content += "<txid>" + order.getPayId() + "</txid>";
		content += "<comtype>L</comtype>";
		if (isTestmode() == true)
		{
			content += "<deviceip>" + LGUplusParams.LGUPLUS_REQUEST_IP_TEST + "</deviceip>";
		}
		else
		{
			content += "<deviceip>" + LGUplusParams.LGUPLUS_REQUEST_IP_RELEASE + "</deviceip>";
		}
		content += "<networktype>W</networktype>";
		content += "<create_id>S_" + params.getAppKeyById(order.getAppId()) + "</create_id>";
		content += "<mac>" + mac + "</mac>";
		content += "</request>";
		
		return content;
	}
	
	private static LGUplusOrderVO verify(Orders order, String pid, String appid, String ctn, String mac, String ukey)
	{
		LGUplusOrderVO vo = new LGUplusOrderVO();
		try
		{
			String urlstr = "";
			if (isTestmode() == true)
			{
				urlstr = LGUplusParams.LGUPLUS_VERIFY_URL_TEST;
			}
			else
			{
				urlstr = LGUplusParams.LGUPLUS_VERIFY_URL_RELEASE;
			}
			String content = getContent(order, pid, appid, ctn, mac, ukey);
			
			System.out.println("lgu+ verify request url ->" + urlstr);
			System.out.println("lgu+ verify request content ->" + content);

			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[]{ new TrustAnyTrustManager() }, new java.security.SecureRandom());   
			HttpsURLConnection connection = (HttpsURLConnection)new URL(urlstr).openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-type", "application/xml");
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
			(
				new HostnameVerifier()
				{
					@Override
					public boolean verify(String arg0, SSLSession arg1)
					{
						return true; //不验证
//						return false; //验证
					}
				}
			);

			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			
			outs.flush();
			outs.close();
			
//			<?xml version="1.0" encoding="UTF-8">
//			<response>
//				<service-name>BuyVerify</service-name>
//				<request-time>20110516131001</request-time>
//				<response-time>20110516131011</response-time>
//				<result_code>0</result_code>
//				<status>0</status>
//				<msg>xxxxxxx</msg>
//				<prouct>
//					<pid>0000049813</pid>
//					<productName>xxxx</productName>
//					<productType>PB0001</productType>
//					<productKind>PK0002</productKind>
//					<usePeriod>36</usePeriod>
//					<price>1100</price>
//				</product>
//				<ukey>xxxxx</ukey>
//				<txid>TXID_xxxx</txid>
//			</response>
			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			Element root = doc.getRootElement();
			
			List<?> childrens = root.getChildren("service-name");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify service-name->" + str);
					vo.setService_name(str);
			}
			
			childrens = root.getChildren("request-time");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify request-time->" + str);
					vo.setRequest_time(str);
			}
			
			childrens = root.getChildren("response-time");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify response-time->" + str);
					vo.setResponse_time(str);
			}
			
			childrens = root.getChildren("result_code");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify result_code->" + str);
					vo.setResult_code(str);
			}
			
			childrens = root.getChildren("status");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify status->" + str);
					vo.setStatus(str);
			}
			
			childrens = root.getChildren("msg");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify msg->" + str);
					vo.setMsg(str);
			}
			
			childrens = root.getChildren("ukey");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify ukey->" + str);
					vo.setUkey(str);
			}
			
			childrens = root.getChildren("txid");
			if (childrens.size() > 0)
			{
					Element key = (Element) childrens.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify txid->" + str);
					vo.setTxid(str);
			}
			
			childrens = root.getChildren("prouct");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				List<?> keys = children.getChildren("pid");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify pid->" + str);
					vo.setPid(str);
				}
				keys = children.getChildren("productName");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify productName->" + str);
					vo.setProductName(str);
				}
				keys = children.getChildren("productType");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify productType->" + str);
					vo.setProductType(str);
				}
				keys = children.getChildren("productKind");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify productKind->" + str);
					vo.setProductKind(str);
				}
				keys = children.getChildren("usePeriod");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify usePeriod->" + str);
					vo.setUsePeriod(str);
				}
				keys = children.getChildren("price");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("lgu+ verify price->" + str);
					vo.setPrice(str);
				}
			}

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/lguplusverify/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + order.getOrderId();

			OSUtil.saveXML(root, filename);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return vo;
	}

	public static String getKey(String ctn, String orderid)
	{
		String ectn = "";
		try
		{
			String urlstr = "";
			if (isTestmode() == true)
			{
				urlstr = LGUplusParams.LGUPLUS_GETKEY_URL_TEST;
			}
			else
			{
				urlstr = LGUplusParams.LGUPLUS_GETKEY_URL_RELEASE;
			}
			urlstr += ctn;
			
			System.out.println("lgu+ getkey request url ->" + urlstr);
			
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[]{ new TrustAnyTrustManager() }, new java.security.SecureRandom());   
			HttpsURLConnection connection = (HttpsURLConnection)new URL(urlstr).openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
			(
				new HostnameVerifier()
				{
					@Override
					public boolean verify(String arg0, SSLSession arg1)
					{
						return true; //不验证
//						return false; //验证
					}
				}
			);

			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			ectn = res;

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/lgupluskey/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			
			OSUtil.saveFile(filename, res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ectn;
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
				cburl += "?pt=" + Constants.PAY_TYPE_LGUPLUS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_LGUPLUS;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}
	
	public static String getTransactionIdAndStatus(Orders vo, String pid, String appid, String ctn, String mac, String ukey)
	{
		OrderIdVO orderIdVO = getTransactionIdVO(vo);
		String ids = "";
		if (orderIdVO != null)
		{
			ids = orderIdVO.getPayId();
		}
		return checkOrdersStatus(ids, pid, appid, ctn, mac, ukey);
	}
	
	public static void init()
	{
		params.addApp("gaoguai", "com.kongzhong.simlife.sanguokr_uplus", "TLSENT");
		params.addApp("gaoguai", "com.kongzhong.simlife.lib", "TLSENT");
		params.addApp("dalingzhu", "com.noumena.tjmammoth.android.t6kr.uplus", "SKNS");
	}
}

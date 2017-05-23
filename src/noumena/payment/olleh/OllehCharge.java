package noumena.payment.olleh;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
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

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class OllehCharge
{
	private static OllehParams params = new OllehParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		OllehCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_OLLEH;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_OLLEH;
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
					OllehOrderVO retvo = checkOrderFromOlleh(order.getOrderId(), order.getPayId());
					if (retvo.getCode().equals("0"))
					{
						st.setStatus(1);
						bean.updateOrderAmountPayIdExinfo(order.getOrderId(), order.getPayId(), retvo.getPrice_vat(), retvo.getDi_id());
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

	private static OllehOrderVO checkOrderFromOlleh(String orderid, String paymentSeq)
	{
		OllehOrderVO vo = getKey(orderid);
		vo = verify(vo, orderid, paymentSeq);
		return vo;
	}
	
	private static String AES_Encrypt(String keyStr, String plainText)
	{
		byte[] encrypt = null;
		try
		{
			Key key = generateKey(keyStr);
			Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, key);
			encrypt = cipher.doFinal(plainText.getBytes());
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		String ret = new String(Base64.encode(encrypt));
		return ret;
	}
	
	private static Key generateKey(String key) throws Exception
	{
		try
		{
			SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(), "AES");
			return keySpec;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			throw e;
		}
	}
	
	private static OllehOrderVO verify(OllehOrderVO vo, String orderid, String paymentSeq)
	{
		try
		{
			String minwen = OllehParams.OLLEH_VERIFY_TRANSID;
			String miwen = "";
			
			minwen = String.format(minwen, paymentSeq);
			miwen = AES_Encrypt(vo.getSymmetric_key(), minwen);
			miwen = miwen.replaceAll("/", "\\$");
			
			String urlstr = "";
			if (testmode == true)
			{
				urlstr = OllehParams.OLLEH_VERIFY_URL_TEST;
			}
			else
			{
				urlstr = OllehParams.OLLEH_VERIFY_URL_RELEASE;
			}
			urlstr += OllehParams.OLLEH_VERIFY_SERVICE;
			urlstr = String.format(urlstr, miwen, vo.getSeq_key());
			
			System.out.println("olleh verify request url ->" + urlstr);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
			connection.setRequestMethod("POST");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write("");
			
			outs.flush();
			outs.close();
			
//			<?xml version="1.0" encoding="utf-8" ?>
//			<response>
//			<result>
//				<tr_id>20100720093100101126</tr_id>
//				<code>0</code>
//				<reason> 과금처리 실패 [OI1200:CP/SP 권한 체크시 에러가 발생하였습니다.] </reason>
//			</result>
//			<value>
//				<app_id>81009FBC</app_id>
//				<di_id>81009FBC01</di_id>
//				<buy_time>20120716235959</buy_time>
//				<price_vat>110</price_vat>
//				<refund_date/>
//				<seq_key>81009FBC01</seq_key >
//			</value>
//			</response>
			
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			Element root = doc.getRootElement();
			
			List<?> childrens = root.getChildren("result");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				List<?> keys = children.getChildren("code");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("olleh verify code->" + str);
					vo.setCode(str);
				}
			}

			childrens = root.getChildren("value");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				List<?> keys = children.getChildren("app_id");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("olleh verify appid->" + str);
					vo.setApp_id(str);
				}
				keys = children.getChildren("di_id");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("olleh verify diid->" + str);
					vo.setDi_id(str);
				}
				keys = children.getChildren("price_vat");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("olleh verify price->" + str);
					vo.setPrice_vat(str);
				}
			}

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/ollehverify/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			OSUtil.saveXML(root, filename);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return vo;
	}

	private static OllehOrderVO getKey(String orderid)
	{
		OllehOrderVO vo = new OllehOrderVO();
		try
		{
			String urlstr = "";
			if (testmode == true)
			{
				urlstr = OllehParams.OLLEH_GETKEY_URL_TEST;
			}
			else
			{
				urlstr = OllehParams.OLLEH_GETKEY_URL_RELEASE;
			}
			urlstr += OllehParams.OLLEH_GETKEY_SERVICE;
			
			System.out.println("olleh getkey request url ->" + urlstr);
			
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

//			<?xml version="1.0" encoding="utf-8" ?>
//			<response>
//			<result>
//				<tr_id>20100720093100101126</tr_id>
//				<code>0</code>
//				<reason> 과금처리 실패 [OI1200:CP/SP권한 체크시 에러가 발생하였습니다.] </reason>
//			</result>
//			<value>
//				<symmetric_key>8E0IU2955MUOEE2J</symmetric_key>
//				<seq_key>KEY0131103130171</seq_key>
//			</value>
//			</response>

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			Element root = doc.getRootElement();
			
			List<?> childrens = root.getChildren("value");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				List<?> keys = children.getChildren("symmetric_key");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("olleh symmetric_key->" + str);
					vo.setSymmetric_key(str);
				}
				keys = children.getChildren("seq_key");
				if (keys.size() > 0)
				{
					Element key = (Element) keys.get(0);
					String str = key.getText();
					System.out.println("olleh seq_key->" + str);
					vo.setSeq_key(str);
				}
			}

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/ollehkey/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			OSUtil.saveXML(root, filename);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return vo;
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
				cburl += "?pt=" + Constants.PAY_TYPE_OLLEH;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_OLLEH;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}
	
	public static String getTransactionIdAndStatus(Orders vo)
	{
		OrderIdVO orderIdVO = getTransactionIdVO(vo);
		String ids = "";
		if (orderIdVO != null)
		{
			ids = orderIdVO.getPayId();
		}
		return checkOrdersStatus(ids);
	}
	
	public static void init()
	{
//		params.addApp("gaoguai", "4038", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPtxJwSjpyMd23NhJqVR3o7QvA/0ZXhNBxmZ29FBsH/DwjxuhcYYz08GIWRF6AT0Watxu2In9M7Rnbecyy4uyT9gJG4WWbBCPa592+N7i88/zpP18equvHUT+US833pjw4jBebFOEAgGATC4rTbqjNiH5TXbcZrfFHkxapNsuYiwIDAQAB");	//2ec36f47fb4714a5971112a2e80a54cf
	}
}

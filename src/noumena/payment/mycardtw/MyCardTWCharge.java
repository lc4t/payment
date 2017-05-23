package noumena.payment.mycardtw;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class MyCardTWCharge
{
	private static boolean testmode = false;
	private static Object ingamemutex = new Object();
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		MyCardTWCharge.testmode = testmode;
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
			if (cburl.indexOf("?") < 0)
			{
				cburl += "?pt=" + order.getPayType();
				cburl += "&amount=" + (int) Double.parseDouble("" + order.getAmount());
			}
			else
			{
				cburl += "&pt=" + order.getPayType();
				cburl += "&amount=" + (int) Double.parseDouble("" + order.getAmount());
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		
		//创建订单的时候，根据支付类型返回带有不同的订单交易授权码的URL
		String url = "";
		if (order.getPayType().equals(Constants.PAY_TYPE_MYCARD_TW_BILLING))
		{
			//Billing
			if (isTestmode() == true)
			{
				url = MyCardTWParams.BILLING_GET_PAYMENTS_PAGE_TEST;
			}
			else
			{
				url = MyCardTWParams.BILLING_GET_PAYMENTS_PAGE_RELEASE;
			}
			url += "?orderid=" + payId;
			url += "&uid=" + order.getUId();
			url += "&amount=" + order.getAmount();
		}
		else if (order.getPayType().equals(Constants.PAY_TYPE_MYCARD_TW_INGAME_TW))
		{
			//Ingame TW
			MyCardTWOrderDataVO vo = getTradeSeqFromMyCardIngame(MyCardTWParams.INGAME_FACTORY_ID_2, payId, order.getUId());
			if (vo.getTradeType().equals("2"))
			{
				if (isTestmode() == true)
				{
					url = MyCardTWParams.INGAME_CHARGE_URL_TEST;
				}
				else
				{
					url = MyCardTWParams.INGAME_CHARGE_URL_RELEASE;
				}
				url += "?facId=";
				url += MyCardTWParams.INGAME_FACTORY_ID_2;
				url += "&AuthCode=";
				url += vo.getAuthCode();
				url += "&facMemId=";
				url += order.getUId();
				String mingwen = MyCardTWParams.INGAME_KEY_1 + vo.getAuthCode() + MyCardTWParams.INGAME_FACTORY_ID_2 + order.getUId() + MyCardTWParams.INGAME_KEY_2;
				url += "&hash=";
				url += StringEncrypt.EncryptSHA256(mingwen);
			}
			else
			{
				if (isTestmode() == true)
				{
					url = MyCardTWParams.INGAME_WEB_CHARGE_PAGE_TEST;
				}
				else
				{
					url = MyCardTWParams.INGAME_WEB_CHARGE_PAGE_RELEASE;
				}
				url += "?model=10";
				url += "&orderid=" + order.getOrderId();
				url += "&facId=" + MyCardTWParams.INGAME_FACTORY_ID_2;
				url += "&authCode=" + vo.getAuthCode();
				url += "&facMemId=" + order.getUId();
			}
		}
		else if (order.getPayType().equals(Constants.PAY_TYPE_MYCARD_TW_INGAME_HK))
		{
			//Ingame HK
			MyCardTWOrderDataVO vo = getTradeSeqFromMyCardIngame(MyCardTWParams.INGAME_FACTORY_ID_3, payId, order.getUId());
			if (vo.getTradeType().equals("2"))
			{
				if (isTestmode() == true)
				{
					url = MyCardTWParams.INGAME_CHARGE_URL_TEST;
				}
				else
				{
					url = MyCardTWParams.INGAME_CHARGE_URL_RELEASE;
				}
				url += "?facId=";
				url += MyCardTWParams.INGAME_FACTORY_ID_3;
				url += "&AuthCode=";
				url += vo.getAuthCode();
				url += "&facMemId=";
				url += order.getUId();
//				String mingwen = MyCardTWParams.INGAME_KEY_1 + vo.getAuthCode() + MyCardTWParams.INGAME_FACTORY_ID_3 + order.getUId() + MyCardTWParams.INGAME_KEY_2;
				url += "&hash=";
				url += StringEncrypt.EncryptSHA256(MyCardTWParams.INGAME_KEY_1 + vo.getAuthCode() + MyCardTWParams.INGAME_FACTORY_ID_3 + order.getUId() + MyCardTWParams.INGAME_KEY_2);
			}
			else
			{
				if (isTestmode() == true)
				{
					url = MyCardTWParams.INGAME_WEB_CHARGE_PAGE_TEST;
				}
				else
				{
					url = MyCardTWParams.INGAME_WEB_CHARGE_PAGE_RELEASE;
				}
				url += "?model=10";
				url += "&orderid=" + order.getOrderId();
				url += "&facId=" + MyCardTWParams.INGAME_FACTORY_ID_3;
				url += "&authCode=" + vo.getAuthCode();
				url += "&facMemId=" + order.getUId();
			}
		}
		else if (order.getPayType().equals(Constants.PAY_TYPE_MYCARD_TW_POINT))
		{
			//Point
			String seq = getTradeSeqFromMyCardPoint(payId, order.getUId(), (int) Double.parseDouble("" + order.getAmount()), 0);
			if (isTestmode() == true)
			{
				url = MyCardTWParams.POINT_CHARGE_URL_TEST;
			}
			else
			{
				url = MyCardTWParams.POINT_CHARGE_URL_RELEASE;
			}
			url += "?AuthCode=";
			url += seq;
		}
		
		System.out.println("mycardtw url ->" + url);
		
		orderIdVO.setMsg(url);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static String listBillingPayments(String orderid, String productid)
	{
//		String ret = "";
//		String str = getBillingProducts(MyCardTWParams.BILLING_FACTORY_ID, orderid);
//		String products[] = str.split(",");
//		
//		for (int i = 0 ; i < products.length ; i++)
//		{
//			String[] pids = products[i].split("[|]");
//			String pid = pids[0];
//			
//			ret += "," + getBillingPayments(MyCardTWParams.BILLING_FACTORY_ID, pid, orderid);
//		}
//		if (ret.length() > 0)
//		{
//			ret = ret.substring(1);
//		}
//		return ret;
		
		return getBillingPayments(MyCardTWParams.BILLING_FACTORY_ID, productid, orderid);
	}

	public static String getBillingPayments2(String facid, String productid, String orderid)
	{
		String ret = "";
		String urlstr = "";
		
		try
		{
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.BILLING_GET_PAYMENTS2_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.BILLING_GET_PAYMENTS2_RELEASE;
			}
			urlstr = String.format(urlstr, facid, productid);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();
			
			System.out.println("mycardtw payments21->" + res);
			int pos = res.lastIndexOf("</string>");
			ret = res.substring(0, pos);
			pos = ret.lastIndexOf(">");
			ret = ret.substring(pos + 1);
			System.out.println("mycardtw payments22->" + ret);
	
			String path = OSUtil.getRootPath() + "../../logs/mycardtwbillingpayments2/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + productid + "_" + orderid;
			OSUtil.saveFile(filename, res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}

	public static String getBillingPayments(String facid, String productid, String orderid)
	{
		String ret = "";
		String urlstr = "";
		
		try
		{
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.BILLING_GET_PAYMENTS_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.BILLING_GET_PAYMENTS_RELEASE;
			}
			urlstr = String.format(urlstr, facid, productid);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();
			
			System.out.println("mycardtw payments1->" + res);
			int pos = res.lastIndexOf("</string>");
			if (pos >= 0)
			{
				ret = res.substring(0, pos);
				pos = ret.lastIndexOf(">");
				ret = ret.substring(pos + 1);
			}
			System.out.println("mycardtw payments2->" + ret);
	
			String path = OSUtil.getRootPath() + "../../logs/mycardtwbillingpayments/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + productid + "_" + orderid;
			OSUtil.saveFile(filename, res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ret;
	}

	public static String getBillingProducts(String facid, String orderid)
	{
		String ret = "";
		String urlstr = "";
		
		try
		{
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.BILLING_GET_PRODUCTS_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.BILLING_GET_PRODUCTS_RELEASE;
			}
			urlstr = String.format(urlstr, facid);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();
			
			System.out.println("mycardtw products1->" + res);
			int pos = res.lastIndexOf("</string>");
			if (pos >= 0)
			{
				ret = res.substring(0, pos);
				pos = ret.lastIndexOf(">");
				ret = ret.substring(pos + 1);
			}
			System.out.println("mycardtw products2->" + ret);
	
			String path = OSUtil.getRootPath() + "../../logs/mycardtwbillingproducts/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			OSUtil.saveFile(filename, res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return ret;
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
			//order.setSubId(order.getExInfo());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			st.setAmount("" + order.getAmount());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
				st.setStatus(3);
			}
			else if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				if (order.getCStatus() == 0)
				{
					//没有得到过成功状态订单，返回成功，并且设置为已经得到过成功订单
					st.setStatus(1);
					bean.updateOrderCStatus(order.getOrderId(), "1");
				}
				else
				{
					//已经得到过成功订单，返回重复订单状态
					st.setStatus(4);
				}
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

	public static String ingameCharge(String payid ,String facId ,String authCode ,String facMemId, String cardId, String cardPwd)
	{
		String urlstr = "";
		String ret = "支付成功";
		
		synchronized (ingamemutex)
		{
			try
			{
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(payid);
				if (order != null)
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						if (isTestmode() == true)
						{
							urlstr = MyCardTWParams.INGAME_CHARGE_INGAME_URL_TEST;
						}
						else
						{
							urlstr = MyCardTWParams.INGAME_CHARGE_INGAME_URL_RELEASE;
						}
						urlstr += "?facId=" + facId;
						urlstr += "&authCode=" + authCode;
						urlstr += "&facMemId=" + facMemId;
						urlstr += "&cardId=" + cardId;
						urlstr += "&cardPwd=" + cardPwd;
						String mingwen = MyCardTWParams.INGAME_KEY_1 + facId + authCode + facMemId + cardId + cardPwd + MyCardTWParams.INGAME_KEY_2;
						urlstr += "&hash=" + StringEncrypt.EncryptSHA256(mingwen);
				
						URL url = new URL(urlstr);
						HttpURLConnection connection = (HttpURLConnection) url.openConnection();
						
						BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
						String res = in.readLine();
						
						connection.disconnect();
			
						String path = OSUtil.getRootPath() + "../../logs/mycardtwingame/" + DateUtil.getCurTimeStr().substring(0, 8);
						OSUtil.makeDirs(path);
						String filename = path + "/" + facId + "_" + facMemId + "_" + payid;
						OSUtil.saveFile(filename, res);
						
			//			{"CardKind":0,"CardPoint":0,"SaveSeq":null,"facTradeSeq":null,"oProjNo":null,"ExtensionData":null,"ReturnMsg":"參數有誤","ReturnMsgNo":-901}
						MyCardTWOrderDataVO vo = new MyCardTWOrderDataVO();
						JSONObject json = JSONObject.fromObject(res);
						vo.setExtensionData(json.getJSONObject("ExtensionData"));
						vo.setCardPoint(json.getString("CardPoint"));
						vo.setSaveSeq(json.getString("SaveSeq"));
						vo.setFacTradeSeq(json.getString("facTradeSeq"));
						vo.setoProjNo(json.getString("oProjNo"));
						vo.setCardKind(json.getString("CardKind"));
						vo.setReturnMsg(json.getString("ReturnMsg"));
						vo.setReturnMsgNo(json.getString("ReturnMsgNo"));
				
						if (!vo.getReturnMsgNo().equals("1"))
						{
							ret = vo.getReturnMsg();
							bean.updateOrderKStatusNoCB(payid, Constants.K_STSTUS_ERROR);
						}
						else
						{
							String exinfo = cardId + ",";
							exinfo += facMemId + ",";
							if (vo.getSaveSeq() == null || vo.getSaveSeq().equals(""))
							{
								exinfo += cardId + ",";
							}
							else
							{
								exinfo += vo.getSaveSeq() + ",";
							}
							exinfo += vo.getFacTradeSeq() + ",";
							exinfo += DateUtil.getCurrentTime() + "<BR>";
							bean.updateOrderAmountExinfo(vo.getFacTradeSeq(), exinfo, vo.getCardPoint());
			//				updateCallbackUrl(vo.getFacTradeSeq(), vo.getCardPoint());
							
							bean.updateOrderKStatusNoCB(payid, Constants.K_STSTUS_SUCCESS);
						}
					}
					else
					{
						System.out.println("mycard ingame had successed -> " + payid);
					}
				}
				else
				{
					ret = "系統錯誤";
				}
			}
			catch (Exception e)
			{
				ret = "系統錯誤";
				e.printStackTrace();
			}
		}

		return ret;
	}
	
	private static String getTradeSeqFromMyCardPoint(String orderid, String uid, int paypoint, int bonuspoint)
	{
		String tradeseq = "";
		String authcode = "";
		try
		{
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.POINT_GET_AUTH_CODE_URL_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.POINT_GET_AUTH_CODE_URL_RELEASE;
			}
			urlstr += "?FactoryId=";
			urlstr += MyCardTWParams.POINT_FACTORY_ID;
			urlstr += "&FactoryServiceId=";
			urlstr += MyCardTWParams.POINT_SERVICE_ID;
			urlstr += "&FactorySeq=";
			urlstr += MyCardTWParams.POINT_SERVICE_ID + orderid;
			urlstr += "&PointPayment=";
			urlstr += paypoint;
			urlstr += "&BonusPayment=";
			urlstr += bonuspoint;
			urlstr += "&FactoryReturnUrl=";
			if (isTestmode() == true)
			{
				urlstr += MyCardTWParams.POINT_CHARGE_CB_URL_TEST;
			}
			else
			{
				urlstr += MyCardTWParams.POINT_CHARGE_CB_URL_RELEASE;
			}
			System.out.println("mycardtw tradeseq url->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
//			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			String res = in.readLine();

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			connection.disconnect();

			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/mycardtwseqpoint/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + uid + "_" + orderid;
			OSUtil.saveXML(root, filename);

			List<?> childrens = root.getChildren("ReturnAuthCode");
			
			System.out.println("1:" + root.getChildren().size());
			System.out.println("10:" + childrens.size());
			
			for (int i = 0 ; i < root.getChildren().size() ; i++)
			{
				System.out.println("111:" + ((Element) root.getChildren().get(i)).getName());
				System.out.println("222:" + ((Element) root.getChildren().get(i)).getText());
				String str = ((Element) root.getChildren().get(i)).getName();
				if (str.equals("ReturnAuthCode"))
				{
					authcode = ((Element) root.getChildren().get(i)).getText();
				}
			}
			if (childrens.size() > 0)
			{
				System.out.println("2");
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				System.out.println("3:" + str);
				if (str.equals("1"))
				{
					System.out.println("4");
					childrens = root.getChildren("ReturnAuthCode");
					if (childrens.size() > 0)
					{
						System.out.println("5");
						children = (Element) childrens.get(0);
						tradeseq = children.getText();
						System.out.println("6:" + tradeseq);
					}
				}
			}
			System.out.println("7");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return authcode;
	}
	
	private static MyCardTWOrderDataVO getTradeSeqFromMyCardIngame(String appid, String orderid, String uid)
	{
		MyCardTWOrderDataVO vo = new MyCardTWOrderDataVO();
		try
		{
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.INGAME_GET_AUTH_CODE_URL_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.INGAME_GET_AUTH_CODE_URL_RELEASE;
			}
			String mingwen = MyCardTWParams.INGAME_KEY_1 + appid + orderid + MyCardTWParams.INGAME_KEY_2;
			System.out.println("mycardtw mingwen->" + mingwen);
			String hash = StringEncrypt.EncryptSHA256(mingwen);
			urlstr = String.format(urlstr, appid, orderid, hash);
			System.out.println("mycardtw tradeseq url->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/mycardtwseqingame/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + appid + "_" + uid + "_" + orderid;
			OSUtil.saveFile(filename, res);

//			{"AuthCode":null,"TradeType":0,"ExtensionData":{},"ReturnMsg":"參數有誤","ReturnMsgNo":-901}
			JSONObject json = JSONObject.fromObject(res);
			vo.setExtensionData(json.getJSONObject("ExtensionData"));
			vo.setAuthCode(json.getString("AuthCode"));
			vo.setTradeType(json.getString("TradeType"));
			vo.setReturnMsg(json.getString("ReturnMsg"));
			vo.setReturnMsgNo(json.getString("ReturnMsgNo"));
//			vo = (MyCardTWOrderDataVO) JSONObject.toBean(json, MyCardTWOrderDataVO.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return vo;
	}

	public static String getTradeSeqFromMyCardBilling(String appid, int amount, int orderid, String uid)
	{
		String tradeseq = "|";
		try
		{
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.BILLING_GET_AUTH_CODE_URL_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.BILLING_GET_AUTH_CODE_URL_RELEASE;
			}
			urlstr = String.format(urlstr, appid, orderid, amount);
			System.out.println("mycardtw tradeseq url->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(true);
//			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
//
//			outs.write("");
//			outs.flush();
//			outs.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/mycardtwseqbilling/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + appid + "_" + uid + "_" + orderid;
			OSUtil.saveFile(filename, res);
			
			//<string xmlns=\"http://schemas.microsoft.com/2003/10/Serialization/\">1|授權成功|MFF121206082550|6B0602FC5DE69E59D4574C2608E81D88A592938D99A78938</string>
			//<string xmlns="http://schemas.microsoft.com/2003/10/Serialization/">-5|固定點輸入金額不符||</string>
			String[] s = res.split("[|]");
			if (s != null && s.length > 2)
			{
				String retcode = s[0];
				System.out.println("s0->" + retcode);
				int pos = retcode.lastIndexOf(">");
				if (pos > 0)
				{
					retcode = retcode.substring(pos + 1);
				}
				System.out.println("s0->" + retcode);
				if (retcode.equals("1"))
				{
					//成功
					tradeseq = s[3];
					System.out.println("s3->" + tradeseq);
					pos = tradeseq.indexOf("</string>");
					if (pos > 0)
					{
						tradeseq = retcode + "|" + tradeseq.substring(0, pos);
					}
					System.out.println("s3->" + tradeseq);
				}
				else
				{
					tradeseq = retcode + "|" + URLEncoder.encode(s[1], "utf-8");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return tradeseq;
	}

	private static int verifyTradeFromMyCard(String authcode, String tradeseq)
	{
		try
		{
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.BILLING_VERIFY_BILL_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.BILLING_VERIFY_BILL_RELEASE;
			}
			urlstr = String.format(urlstr, authcode);
			System.out.println("mycardtw verifytrade url->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/mycardtwbillingverify/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + tradeseq;
			OSUtil.saveFile(filename, res);
			
			//String res = "<string xmlns="http://schemas.microsoft.com/2003/10/Serialization/">-2|無此遊戲廠商登入IP|0</string>";
			String[] s = res.split("[|]");
			if (s != null && s.length > 2)
			{
				System.out.println("verifyTradeFromMyCard ret->" + s[0]);
				int pos = s[0].lastIndexOf(">");
				String ret = "0";
				if (pos > 0)
				{
					ret = s[0].substring(pos + 1);
				}
				System.out.println("verifyTradeFromMyCard ret->" + ret);
				if (ret.equals("1"))
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
			return 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	private static int confirmTradeFromMyCard(String authcode, String uid, String tradeseq)
	{
		try
		{
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.BILLING_CONFIRM_BILL_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.BILLING_CONFIRM_BILL_RELEASE;
			}
			urlstr = String.format(urlstr, uid , authcode);
			System.out.println("mycardtw vconfirmtrade url->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			
			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/mycardtwbillingconfirm/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + tradeseq;
			OSUtil.saveFile(filename, res);
			
			//String res = "<string xmlns="http://schemas.microsoft.com/2003/10/Serialization/">-1|無此筆交易或交易狀態不符</string>";
			String[] s = res.split("[|]");
			if (s != null && s.length > 1)
			{
				System.out.println("confirmTradeFromMyCard ret->" + s[0]);
				int pos = s[0].lastIndexOf(">");
				String ret = "0";
				if (pos > 0)
				{
					ret = s[0].substring(pos + 1);
				}
				System.out.println("confirmTradeFromMyCard ret->" + ret);
				if (ret.equals("1"))
				{
					return 1;
				}
				else
				{
					return 0;
				}
			}
			return 0;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return 0;
		}
	}

	public static void getCallbackRecordsFromMyCardTW(Vector<MyCardTWOrderDataVO> vos)
	{
		OrdersBean bean = new OrdersBean();
		for (int i = 0 ; i < vos.size() ; i++)
		{
			MyCardTWOrderDataVO vo = vos.get(i);

			if (vo.getReturnMsgNo().equals("1"))
			{
				Orders order = bean.qureyOrder(vo.getTradeSeq());
				String authcode = order.getExInfo();
				String uid = "0";
				if (order != null)
				{
					uid = order.getUId();
				}
				
				if (MyCardTWCharge.verifyTradeFromMyCard(authcode, order.getOrderId())== 1)
				{
					//验证成功
					if (MyCardTWCharge.confirmTradeFromMyCard(authcode, uid, order.getOrderId())== 1)
					{
						//请款成功
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						//失败
						bean.updateOrderKStatus(order.getOrderId(), Constants.MYCARD_TW_CONFIRM_FAILED);
					}
				}
				else
				{
					//失败
					bean.updateOrderKStatus(order.getOrderId(), Constants.MYCARD_TW_VERIFY_FAILED);
				}
			}
		}
	}
	
	public static void getCallbackFromMyCardTWBilling(MyCardTWOrderDataVO vo)
	{
		try
		{
			OrdersBean bean = new OrdersBean();
			System.out.println("mycartw cb status->" + vo.getReturnMsgNo());
			if (vo.getReturnMsgNo().equals("1"))
			{
				Orders order = bean.qureyOrder(vo.getTradeSeq());
				String uid = "0";
				if (order != null)
				{
					uid = order.getUId();
					if (order.getKStatus() == Constants.K_STSTUS_SUCCESS)
					{
						return;
					}
				}
				//成功
				if (MyCardTWCharge.verifyTradeFromMyCard(vo.getAuthCode(), vo.getTradeSeq())== 1)
				{
					//验证成功
					if (MyCardTWCharge.confirmTradeFromMyCard(vo.getAuthCode(), uid, vo.getTradeSeq())== 1)
					{
						//请款成功
						bean.updateOrderKStatusNoCB(vo.getTradeSeq(), Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						//失败
						bean.updateOrderKStatusNoCB(vo.getTradeSeq(), Constants.MYCARD_TW_CONFIRM_FAILED);
					}
				}
				else
				{
					//失败
					bean.updateOrderKStatusNoCB(vo.getTradeSeq(), Constants.MYCARD_TW_VERIFY_FAILED);
				}
			}
			else
			{
				//失败
				bean.updateOrderKStatusNoCB(vo.getTradeSeq(), Constants.MYCARD_TW_CALLBACK_FAILED);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static void getCallbackFromMyCardTWIngame(MyCardTWOrderDataVO vo)
	{
		try
		{
			System.out.println("mycartw cb ingame status->" + vo.getReturnMsgNo());
			System.out.println("mycartw cb ingame tradeseq->" + vo.getFacTradeSeq());
			System.out.println("mycartw cb ingame mycardid->" + vo.getCardId());
			
			if (vo.getFacTradeSeq() == null)
			{
				return;
			}
			OrdersBean bean = new OrdersBean();
			
			String exinfo = vo.getCardId() + ",";
			exinfo += vo.getFacMemId() + ",";
			exinfo += vo.getTradeSeq() + ",";
			exinfo += vo.getFacTradeSeq() + ",";
			exinfo += DateUtil.getCurrentTime() + "<BR>";
			bean.updateOrderAmountExinfo(vo.getFacTradeSeq(), exinfo, vo.getCardPoint());
			updateCallbackUrl(vo.getFacTradeSeq(), vo.getCardPoint());
			
			if (vo.getReturnMsgNo().equals("1"))
			{
				//成功
				bean.updateOrderKStatusNoCB(vo.getFacTradeSeq(), Constants.K_STSTUS_SUCCESS);
			}
			else
			{
				//失败
				bean.updateOrderKStatusNoCB(vo.getFacTradeSeq(), Constants.MYCARD_TW_CALLBACK_FAILED);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	private static void updateCallbackUrl(String orderid, String realprice)
	{
		CallbackBean callbackBean = new CallbackBean();
		Callback callbackvo = callbackBean.qureyCallback(orderid);
		if (callbackvo != null)
		{
			String url = callbackvo.getCallbackUrl();
			if (url.indexOf("?") >= 0)
			{
				url += "&amount=" + realprice;
				url += "&payId=" + orderid;
			}
			else
			{
				url += "?amount=" + realprice;
				url += "&payId=" + orderid;
			}
			callbackvo.setCallbackUrl(url);
			callbackBean.updateCallback(callbackvo);
		}
	}
	
	public static String getCallbackFromMyCardTWPoint(MyCardTWOrderDataVO vo)
	{
		String ret = "";
		try
		{
			OrdersBean bean = new OrdersBean();
			System.out.println("mycartw cb point status->" + vo.getReturnMsgNo());
			System.out.println("mycartw cb point tradeseq->" + vo.getFacTradeSeq());
			System.out.println("mycartw cb point otp->" + vo.getOneTimePasswd());
			String orderid = vo.getFacTradeSeq();
			int pos = orderid.indexOf(MyCardTWParams.POINT_SERVICE_ID);
			if (pos >= 0)
			{
				orderid = orderid.substring(pos + MyCardTWParams.POINT_SERVICE_ID.length());
			}
			System.out.println("mycartw cb point tradeseq2->" + orderid);
			if (vo.getReturnMsgNo().equals("1"))
			{
				//成功
				ret = VerifyMemberCost(vo);
				if (ret.equals(""))
				{
					ret = "SUCCESS";
					bean.updateOrderKStatusNoCB(orderid, Constants.K_STSTUS_SUCCESS);
				}
				else
				{
					//失败
					bean.updateOrderKStatusNoCB(orderid, Constants.MYCARD_TW_CALLBACK_FAILED);
				}
			}
			else
			{
				//失败
				bean.updateOrderKStatusNoCB(orderid, Constants.MYCARD_TW_CALLBACK_FAILED);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}

	private static String VerifyMemberCost(MyCardTWOrderDataVO vo)
	{
		String retstr = "";
		String ret = "";
		try
		{
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = MyCardTWParams.POINT_CONFIRM_BILL_TEST;
			}
			else
			{
				urlstr = MyCardTWParams.POINT_CONFIRM_BILL_RELEASE;
			}
			urlstr += "?AuthCode=";
			urlstr += vo.getAuthCode();
			urlstr += "&OneTimePassword=";
			urlstr += vo.getOneTimePasswd();
			System.out.println("mycardtw confirm point trade url->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

//			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
//			String res = in.readLine();
//			
//			connection.disconnect();

//			String path = OSUtil.getRootPath() + "../../logs/mycardtwpointconfirm/" + DateUtil.getCurTimeStr().substring(0, 8);
//			OSUtil.makeDirs(path);
//			String filename = path + "/" + vo.getTradeSeq();
//			OSUtil.saveFile(filename, res);

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			connection.disconnect();

			Element root = doc.getRootElement();
			String path = OSUtil.getRootPath() + "../../logs/mycardtwpointconfirm/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + vo.getFacTradeSeq();
			OSUtil.saveXML(root, filename);

			List<?> childrens = root.getChildren("ReturnMsgNo");
			
			System.out.println("1:" + root.getChildren().size());
			System.out.println("10:" + childrens.size());
			
			for (int i = 0 ; i < root.getChildren().size() ; i++)
			{
				System.out.println("111:" + ((Element) root.getChildren().get(i)).getName());
				System.out.println("222:" + ((Element) root.getChildren().get(i)).getText());
				String str = ((Element) root.getChildren().get(i)).getName();
				if (str.equals("ReturnMsgNo"))
				{
					ret = ((Element) root.getChildren().get(i)).getText();
				}
				if (str.equals("ReturnMsg"))
				{
					retstr = ((Element) root.getChildren().get(i)).getText();
				}
			}

			if (ret.equals("1"))
			{
				retstr = "";
			}
			return retstr;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "SYSTEM ERROR";
		}
	}
	
	public static String getInGameServicesList(String startdate, String enddate, String mycardid, String gameid)
	{
		OrdersBean bean = new OrdersBean();
		List<Orders> orders = null;
		String ret = "";
		
		if (mycardid.equals("") || mycardid.equals("0"))
		{
			orders = bean.qureyOrdersByTypeDate(gameid, startdate, enddate);
			for (int i = 0 ; i < orders.size() ; i++)
			{
				if (orders.get(i).getExInfo() != null)
				{
					ret += orders.get(i).getExInfo() + "\n";
				}
			}
		}
		else
		{
			orders = bean.qureyOrdersByTypeEx(gameid, mycardid);
			for (int i = 0 ; i < orders.size() ; i++)
			{
				if (orders.get(i).getExInfo() != null)
				{
					ret += orders.get(i).getExInfo() + "\n";
				}
			}
		}
		return ret;
	}
}

package noumena.payment.naver;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.Get;
import noumena.payment.util.NotifyPurchase;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class NaverCharge
{
	private static NaverParams params = new NaverParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		NaverCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_KRW);
		order.setUnit(Constants.CURRENCY_UNIT_YUAN);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_NAVER;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_NAVER;
			}
			cburl += "&currency=" + Constants.CURRENCY_KRW;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
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
					int ret = checkOrderFromNaver(order.getOrderId(), order.getPayId(), order.getSign(), order.getExInfo());
					st.setStatus(ret);
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
	
	public static int checkOrderFromNaver(String orderid, String paymentSeq, String appid, String naverversion)
	{
		try
		{
			String urlstr = NaverParams.NAVER_VERIFY_URL;
			
			if (naverversion != null && naverversion.equals("2")) 
			{
				urlstr = NaverParams.NAVER_VERIFY_URL_NEW;
			}
			
			urlstr = String.format(urlstr, params.getParams(appid).getCpid());
			urlstr += "?nonce=" + orderid;
			urlstr += "&paymentSeq=" + paymentSeq;
			
			String privatekey = params.getParams(appid).getPrivatekey();
			MACManager.initialize(privatekey);
			String encryptedUrl = MACManager.getEncryptUrl(urlstr, privatekey);

			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(naver cb request url)->" + encryptedUrl);

			String[] keys = new String[1];
			String[] values = new String[1];
			keys[0] = "IAP_KEY";
//			if (testmode == true)
//			{
//				values[0] = params.getParams(appid).getTestkey();
//			}
//			else
//			{
				values[0] = params.getParams(appid).getAppkey();
//			}
			String res = Get.doGet(encryptedUrl, keys, values);
//			URL url = new URL(encryptedUrl);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
////			connection.setDoOutput(true);
////			connection.setUseCaches(false);
////			connection.setDoInput(true);
//			connection.setRequestProperty("Content-type", "application/xml;charset=utf-8");
//			connection.setRequestProperty("Connection", "Keep-Alive");
//			connection.setRequestMethod("GET");
//			if (testmode == true)
//			{
//				connection.setRequestProperty("IAP_KEY", params.getTestKeyById(appid));
//			}
//			else
//			{
//				connection.setRequestProperty("IAP_KEY", params.getAppKeyById(appid));
//			}
//			connection.connect();
//			
////			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
////			outs.write("");
////			
////			outs.flush();
////			outs.close();
//			
//			BufferedReader in = new BufferedReader
//				(
//					new InputStreamReader(connection.getInputStream())
//				);
//			String res = "", line = null;
//			while ((line = in.readLine()) != null)
//			{
//				res += line;
//			}
//
//			connection.disconnect();
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(naver cb ret)->" + res);

			NaverOrderVO vo = new NaverOrderVO();
			boolean valid = true;
			try
			{
				String str = "";
				JSONObject json = JSONObject.fromObject(res);
				str = json.getString("code");
				vo.setCode(str);
				str = json.getString("message");
				vo.setMessage(str);
				str = json.getString("signature");
				vo.setSignature(str);
				
				str = json.getString("result");
				json = JSONObject.fromObject(str);
				str = json.getString("nonce");
				vo.setNonce(str);
				
				str = json.getString("receipt");
				json = JSONObject.fromObject(str);
				str = json.getString("environment");
				vo.setEnvironment(str);
				str = json.getString("paymentSeq");
				vo.setPaymentSeq(str);
				str = json.getString("approvedPaymentNo");
				vo.setApprovedPaymentNo(str);
				str = json.getString("appCode");
				vo.setAppCode(str);
				str = json.getString("appName");
				vo.setAppName(str);
				str = json.getString("developerName");
				vo.setDeveloperName(str);
				str = json.getString("naverId");
				vo.setNaverId(str);
				str = json.getString("paymentTime");
				vo.setPaymentTime(str);
				str = json.getString("productCode");
				vo.setProductCode(str);
				str = json.getString("productName");
				vo.setProductName(str);
				str = json.getString("productPrice");
				vo.setProductPrice(str);
				str = json.getString("paymentPrice");
				vo.setPaymentPrice(str);
				str = json.getString("extra");
				vo.setExtra(str);
				
				str = json.getString("discount");
				json = JSONObject.fromObject(str);
				str = json.getString("price");
				vo.setPrice(str);
			}
			catch (Exception e)
			{
				e.printStackTrace();
				valid = false;
			}
			
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null)
			{
				if (valid == true)
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayIdExinfo(orderid, vo.getPaymentSeq(), vo.getProductPrice(), vo.getNaverId() + "#" + vo.getApprovedPaymentNo());
						
						//支付成功
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						
						//通知kakao成功的订单
						NotifyPurchase.notify(orderid);
						
					}
					else
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(naver order)->(" + orderid + ")(" + order.getPayId() + ") had been succeed");
					}
				}
				else
				{
					//支付失败
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
				}
			}
			
			String path = OSUtil.getRootPath() + "../../logs/navercb/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + paymentSeq;
			
			OSUtil.saveFile(filename, res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return 1;
	}
		
	public static OrderIdVO getTransactionIdVO(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_KRW);
		order.setUnit(Constants.CURRENCY_UNIT_YUAN);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_NAVER;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_NAVER;
			}
			cburl += "&currency=" + Constants.CURRENCY_KRW;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
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
		params.initParams(NaverParams.CHANNEL_ID, new NaverParamsVO());
		
//		params.addApp("dalingzhu", "RENW317221395899076045", "AD_1002599", "m8NrbkmSNI", "vWZLliGY6P", "imwxml2sZPXmDCZ6yoxzmdgNR0JQd0XIfGlsvaE6vblG6NaCLlyIoAANDtVklYcd");
//		params.addApp("gaoguai", "BQYU332281398306467989", "AD_1005079", "LR16YqszQm", "6MDYZAzU5o", "SIFK32BMvhgHAeUUtEt9AN3dsZoTZbFLYEsAMRgzyuemR5io8ZoY1lRBtn0L4dqX");
//		params.addApp("M5", "VYGO544021418366635247", "AD_1000130", "zOMG9yK2lq", "gvwrYX8WJf", "8fEw1fSKvlJYRkkAnGlIhZyHynbgkZZdnaKpLZJnGC6ggLJZWVHeSZkMsLu2oPTD");
	}
}

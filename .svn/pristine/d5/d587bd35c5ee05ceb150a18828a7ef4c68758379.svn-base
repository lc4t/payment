package noumena.payment.heepay;

import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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
import noumena.payment.util.StringEncrypt;
import noumena.payment.util.TrustAnyTrustManager;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class HeepayCharge
{
	private static HeepayParams params = new HeepayParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		HeepayCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order, String pay_type, String return_url, String user_ip, String agent_id)
	{
		order.setCurrency(Constants.CURRENCY_RMB);
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
				cburl += "?pt=" + Constants.PAY_TYPE_HEEPAY;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_HEEPAY;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		String prepayid = getPrepayID(order,pay_type,return_url,user_ip, agent_id);
		orderIdVO.setMsg(prepayid);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	public static String getPrepayID(Orders order, String pay_type, String return_url, String user_ip, String agent_id)
	{
		String prepayid = "";
		String urlParameters = "";
		
		try {
			String appid = "0"; //所有app公用一套参数
			String pay_amt = new DecimalFormat("0.00").format(new Float(order.getAmount()));
			String agent_bill_time = DateUtil.formatDate(order.getCreateTime());
			String sign = "";
			String notify_url = "";
			if (testmode) 
			{
				notify_url = HeepayParams.NOTIFY_URL_TEST;
			}
			else
			{
				notify_url = HeepayParams.NOTIFY_URL_RELEASE;
			}
			
			urlParameters += "version=1";
			urlParameters += "&agent_id=" + agent_id;
			urlParameters += "&agent_bill_id=" + order.getOrderId();
			urlParameters += "&agent_bill_time=" + agent_bill_time;
			urlParameters += "&pay_type=" + pay_type;
			urlParameters += "&pay_amt=" + pay_amt;
			urlParameters += "&notify_url=" + notify_url;
			urlParameters += "&user_ip=" + user_ip;
			
			sign = StringEncrypt.Encrypt(urlParameters + "&key=" + params.getParams(appid).getSecretkey()).toLowerCase();
			
			urlParameters += "&return_url=" + return_url;
			urlParameters += "&goods_name=" + URLEncoder.encode(order.getExInfo(),"gb2312");
			urlParameters += "&remark=default";
			urlParameters += "&sign=" + sign;
			
			String urlstr = HeepayParams.CREATE_ORDER_URL + "?" + urlParameters;
			
			System.out.println("heepay create order urlstr--------->"+ urlstr);			
			
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
						//return false; //验证
					}
				}
			);

			connection.disconnect();

			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(connection.getInputStream());
			Element root = doc.getRootElement();
			prepayid = root.getTextTrim();
			
		} 
		catch (Exception e) {
			e.printStackTrace();
		} 
		return prepayid;
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
	
	public static String getCallbackFromHeepay(Map<String,String> heepayparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(heepay cb params)->" + heepayparams.toString());
		
    	String ret = "ok";   
    	
    	try {
			JSONObject json = JSONObject.fromObject(heepayparams);
			HeepayOrderVO ordervo = (HeepayOrderVO)JSONObject.toBean(json,HeepayOrderVO.class);
			String orderid = ordervo.getAgent_bill_id();
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				if (ordervo.getResult().equals("1"))
				{
					String appid = "0"; //所以app公用一套参数
					String minwen = "";
					String miwen = "";
					
					minwen += "result=";
					minwen += ordervo.getResult();
					minwen += "&agent_id=";
					minwen += ordervo.getAgent_id();
					minwen += "&jnet_bill_no=";
					minwen += ordervo.getJnet_bill_no();
					minwen += "&agent_bill_id=";
					minwen += ordervo.getAgent_bill_id();
					minwen += "&pay_type=";
					minwen += ordervo.getPay_type();
					minwen += "&pay_amt=";
					minwen += ordervo.getPay_amt();
					minwen += "&remark=";
					minwen += ordervo.getRemark();
					minwen += "&key=";
					minwen += params.getParams(appid).getSecretkey();
					
					miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
					
					if (miwen.equals(ordervo.getSign()))
					{
						//服务器签名验证成功
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getJnet_bill_no(), ordervo.getPay_amt(), ordervo.getAgent_id()+"#"+ordervo.getPay_type());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(heepay cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
					else
					{
						// 服务器签名验证失败
						ret = "error";
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(heepay cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
					}
				}
				else
				{
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(heepay cb) result_code is "+ordervo.getResult());
				}
				
				String path = OSUtil.getRootPath() + "../../logs/heepaycb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, heepayparams.toString());
			}	
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(heepay cb ret)->" + ret);		
    	
		return ret;
	}

	public static void init()
	{
		params.initParams(HeepayParams.CHANNEL_ID, new HeepayParamsVO());
	}
}

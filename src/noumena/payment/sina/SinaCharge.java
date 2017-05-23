package noumena.payment.sina;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

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

public class SinaCharge
{
	private static SinaParams params = new SinaParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		SinaCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_FEN);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_SINA;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_SINA;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
			
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
	
	public static String getCallbackFromSina(Map<String,String> sinaparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(sina cb params)->" + sinaparams.toString());
		
    	String ret = "OK";
    	
    	try {
    		JSONObject json = JSONObject.fromObject(sinaparams);
        	SinaOrderVO ordervo = (SinaOrderVO)JSONObject.toBean(json,SinaOrderVO.class);
        	String orderid = ordervo.getPt();
        	String appid = ordervo.getSource();
        	
        	OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{				
				//查询订单
				SinaOrderVO ckvo = checkOrder(ordervo.getOrder_id());
				
				//0 – 创建, 1 – 进行中 ,2 – 支付成功 ,3 – 支付失败 ,4 – 支付订单失败，充值订单成功
				if (ckvo.getOrder_status().equals("2")) 
				{
					//查询的订单支付成功
					String content = "";
					content += "actual_amount|";
					content += ordervo.getActual_amount();
					content += "|amount|";
					content += ordervo.getAmount();
					content += "|order_id|";
					content += ordervo.getOrder_id();
					content += "|order_uid|";
					content += ordervo.getOrder_uid();
					content += "|pt|";
					content += orderid;
					content += "|source|";
					content += appid;
					content += "|";
					content += params.getParams(appid).getSecretkey();
					
    				
					String sign = StringEncrypt.Encrypt(content, "SHA");
    				
    				if (sign.equals(ordervo.getSignature())) 
    				{
    					//验签成功
    					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
    					{
    						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getOrder_id(), ordervo.getActual_amount(), ordervo.getOrder_uid());
    						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
    					}
    					else
    					{
    						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(sina)-> order (" + orderid + ") had been succeed");
    					}
    				}
    				else 
    				{
    					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(sina)-> check signature fail");
					}
				}
				else
				{
					//订单支付未成功
	        		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(sina)-> order (" + orderid + ") status is " + ckvo.getOrder_status());
				}
				
				String path = OSUtil.getRootPath() + "../../logs/sinacb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, sinaparams.toString());
			}
		
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(sina cb ret)->" + ret);

		return ret;
	}
	
	public static SinaOrderVO checkOrder(String payid)
	{
		SinaOrderVO ckvo = null;
		try
		{			
			String urlstr = SinaParams.CHECK_ORDER_URL;
			urlstr = String.format(urlstr, payid);
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(sina)-> check order urlParameters -->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
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
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(sina)-> check order ret -->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			ckvo = (SinaOrderVO)JSONObject.toBean(json, SinaOrderVO.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return ckvo;
	}
	
	public static void init()
	{
		params.initParams(SinaParams.CHANNEL_ID, new SinaParamsVO());
	}
}

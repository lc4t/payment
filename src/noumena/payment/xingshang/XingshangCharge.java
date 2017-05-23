package noumena.payment.xingshang;

import java.net.URLEncoder;
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

public class XingshangCharge
{
	private static XingshangParams params = new XingshangParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		XingshangCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
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
				cburl += "?pt=" + Constants.PAY_TYPE_XINGSHANG;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_XINGSHANG;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
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
	
	public static String getCallbackFromXingshang(Map<String,String> xingshangparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(xingshang cb params)->" + xingshangparams.toString());
    	
    	String ret = "success";
    	
    	try {
			JSONObject json = JSONObject.fromObject(xingshangparams);
			XingshangOrderVO ordervo = (XingshangOrderVO)JSONObject.toBean(json,XingshangOrderVO.class);
			
			String orderid = ordervo.getAttach();
			
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				String appid = order.getExInfo();
				
				String minwen = "";
				String miwen = "";
				
				minwen += "orderid=";
				minwen += URLEncoder.encode(ordervo.getOrderid(),"utf-8");
				minwen += "&username=";
				minwen += URLEncoder.encode(ordervo.getUsername(),"utf-8");
				minwen += "&gameid=";
				minwen += ordervo.getGameid();
				minwen += "&roleid=";
				minwen += URLEncoder.encode(ordervo.getRoleid(),"utf-8");
				minwen += "&serverid=";
				minwen += ordervo.getServerid();
				minwen += "&paytype=";
				minwen += URLEncoder.encode(ordervo.getPaytype(),"utf-8");
				minwen += "&amount=";
				minwen += ordervo.getAmount();
				minwen += "&paytime=";
				minwen += ordervo.getPaytime();
				minwen += "&attach=";
				minwen += URLEncoder.encode(orderid,"utf-8");
				minwen += "&appkey=";
				minwen += params.getParams(appid).getAppkey();
				
				miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
				String sign = URLEncoder.encode(ordervo.getSign(),"utf-8");
				
				if (miwen.equals(sign))
				{
					//服务器签名验证成功
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
					{
						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getOrderid(), ordervo.getAmount(), ordervo.getPaytype()+"#"+ordervo.getUsername());
						bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
					} 
					else 
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(xingshang cb) order (" + order.getOrderId()+ ") had been succeed");
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = "errorSign";
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(xingshang cb)->(appid=" + appid + "),(content=" + minwen + "),(sign=" + miwen +")");
				}
				
				String path = OSUtil.getRootPath() + "../../logs/xingshangcb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, xingshangparams.toString());
			}			
		}
    	catch (Exception e) {
			e.printStackTrace();
			ret = "error";
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(xingshang cb ret)->" + ret);
		
		return ret;
	}

	public static void init()
	{
		params.initParams(XingshangParams.CHANNEL_ID, new XingshangParamsVO());
//		params.addApp("qunying", "17", "59e88b44f9024fc2f7b6699911fc31eb"); //三国群英OL
	}
}

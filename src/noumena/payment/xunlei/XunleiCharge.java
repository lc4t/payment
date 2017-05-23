package noumena.payment.xunlei;

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

public class XunleiCharge
{
	private static XunleiParams params = new XunleiParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		XunleiCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_XUNLEI2;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_XUNLEI2;
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
	
	public static String getCallbackFromXunlei(Map<String,String> xunleiparams)
	{
		//version_id=%s&merchant_id=%s&order_date=%s&order_id=%s&currency=%s&pay_sq=%s&pay_date=%s&card_num=%s&card_pwd=%s&pc_id=%s&card_status=%s&card_code=%s&card_amount=%s&merchant_key=%s
    	String ret = "1";
		String remoteip = xunleiparams.get("remoteip");
    	String orderid = xunleiparams.get("ext");
		if (remoteip == null
				||(!remoteip.equals("123.151.31.232") && !remoteip.equals("125.39.36.232")
				&& !remoteip.equals("123.151.31.233") && !remoteip.equals("125.39.36.233")
				&& !remoteip.equals("123.151.31.141") && !remoteip.equals("125.39.36.141")
				&& !remoteip.equals("123.151.31.210") && !remoteip.equals("125.39.36.210")))
		{
			ret = "-6";
		}
		else
		{
	    	String minwen = "";
	    	String miwen = "";
	    	
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order == null)
			{
				ret = "-3";
			}
			else
			{
				minwen += xunleiparams.get("orderid");
				minwen += xunleiparams.get("user");
				minwen += xunleiparams.get("gold");
				minwen += xunleiparams.get("money");
				minwen += xunleiparams.get("time");
				minwen += params.getAppKeyById(order.getSign());
				
				miwen = StringEncrypt.Encrypt(minwen);
				
		    	if (miwen.equals(xunleiparams.get("sign")))
				{
					//服务器签名验证成功
					//请在这里加上游戏的业务逻辑程序代码
					//获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
		    		// 交易处理成功
					try
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(orderid, xunleiparams.get("orderid"), xunleiparams.get("money"), xunleiparams.get("user") + "|" + xunleiparams.get("gold"));
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("xunlei order (" + order.getOrderId() + ") had been succeed");
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = "-2";
				}
			}
		}

		System.out.println("xunlei cb ->" + xunleiparams.toString());
		System.out.println("xunlei cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/xunleicb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, xunleiparams.toString());
		
		return ret;
	}

	public static void init()
	{
		params.addApp("xixuegui", "050056", "konggamexunlei12345");	//吸血鬼日记
	}
}

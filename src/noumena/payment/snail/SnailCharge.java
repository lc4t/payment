package noumena.payment.snail;

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

public class SnailCharge
{
	private static SnailParams params = new SnailParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		SnailCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_SNAIL;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_SNAIL;
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
	
	public static String getCallbackFromSnail(Map<String,String> snailparams)
	{
    	String ret = "{\"ErrorCode\":\"%d\",\"ErrorDesc\":\"%s\"}";

		String orderid = snailparams.get("CooOrderSerial");
    	String appid = snailparams.get("AppId");
    	String key = params.getAppKeyById(appid);

    	String minwen = "";
    	minwen += snailparams.get("AppId");
    	minwen += snailparams.get("Act");
    	minwen += snailparams.get("ProductName");
    	minwen += snailparams.get("ConsumeStreamId");
    	minwen += snailparams.get("CooOrderSerial");
    	minwen += snailparams.get("Uin");
    	minwen += snailparams.get("GoodsId");
    	minwen += snailparams.get("GoodsInfo");
    	minwen += snailparams.get("GoodsCount");
    	minwen += snailparams.get("OriginalMoney");
    	minwen += snailparams.get("OrderMoney");
    	minwen += snailparams.get("Note");
    	minwen += snailparams.get("PayStatus");
    	minwen += snailparams.get("CreateTime");
    	minwen += key;
    	String miwen = StringEncrypt.Encrypt(minwen);
    	
		if (miwen.equals(snailparams.get("Sign")))
		{
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
	
			try
			{
				if (order != null)
				{
					//服务器签名验证成功
					//请在这里加上游戏的业务逻辑程序代码
					//获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
		    		// 交易处理成功
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayIdExinfo(orderid, snailparams.get("ConsumeStreamId"), snailparams.get("OriginalMoney"), snailparams.get("Uin"));
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("snail order (" + order.getOrderId() + ") had been succeed");
					}
					ret = String.format(ret, 1, "");
				}
				else
				{
					ret = String.format(ret, 0, "");
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				ret = String.format(ret, 0, "");
			}
		}
		else
		{
			ret = String.format(ret, 5, "");
		}

		System.out.println("snail cb ->" + snailparams.toString());
		System.out.println("snail cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/snailcb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, snailparams.toString() + "<>" + snailparams.toString());
		
		return ret;
	}

	public static void init()
	{
		params.addApp("xixuegui", "158783", "F3EB3F7BB304CD017327D5F7471C9A72");	//吸血鬼日记
	}
}

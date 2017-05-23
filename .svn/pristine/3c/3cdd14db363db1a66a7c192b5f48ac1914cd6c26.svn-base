package noumena.payment.qidian;

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

public class QidianCharge
{
	private static QidianParams params = new QidianParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		QidianCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_QIDIAN);
		order.setUnit(Constants.CURRENCY_UNIT_JIAO);
		
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
				cburl += "?pt=" + Constants.PAY_TYPE_QIDIAN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_QIDIAN;
			}
			cburl += "&currency=" + Constants.CURRENCY_QIDIAN;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_JIAO;
			
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
	
	public static String getCallbackFromQidian(Map<String,String> qidianparams)
	{
    	String ret = "{\"Code\":%d,\"Price\":\"%d\"}";
    	String minwen = "";
    	String miwen = "";
    	String orderid = qidianparams.get("extra");
    	
		OrdersBean bean = new OrdersBean();
		Orders order = bean.qureyOrder(orderid);

		try
		{
			if (order == null)
			{
				ret = String.format(ret, -1, 0);
			}
			else
			{
		    	String appid = order.getSign();
				minwen += "amount=" + qidianparams.get("amount");
				minwen += "description=" + qidianparams.get("description");
				minwen += "extra=" + qidianparams.get("extra");
				minwen += "method=" + qidianparams.get("method");
				minwen += "orderid=" + qidianparams.get("orderid");
				minwen += "userid=" + qidianparams.get("userid");
				minwen += "username=" + qidianparams.get("username");
				minwen = minwen.toLowerCase();
				minwen += params.getAppKeyById(appid);
				
				miwen = StringEncrypt.Encrypt(minwen);
				
		    	if (miwen.equals(qidianparams.get("sig")))
				{
					//服务器签名验证成功
					//请在这里加上游戏的业务逻辑程序代码
					//获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
		    		// 交易处理成功
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						String amount = qidianparams.get("amount");
						if (appid.equals("126"))
						{
							//如果是搞怪的话，把实际支付金额扩大10倍
							int a = Integer.parseInt(amount);
							a = a * 10;
							amount = a + "";
						}
						bean.updateOrderAmountPayIdExinfo(orderid, qidianparams.get("orderid"), amount, qidianparams.get("userid") + "|" + qidianparams.get("username"));
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						ret = String.format(ret, 0, ((int) Double.parseDouble(order.getItemPrice())));
					}
					else
					{
						System.out.println("qidian order (" + order.getOrderId() + ") had been succeed");
					}
					ret = String.format(ret, 0, ((int) Double.parseDouble(order.getItemPrice())));
				}
				else
				{
					// 服务器签名验证失败
					ret = String.format(ret, -1, 0);
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = String.format(ret, -1, 0);
		}

		System.out.println("qidian cb ->" + qidianparams.toString());
		System.out.println("qidian cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/qidiancb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, qidianparams.toString());
		
		return ret;
	}

	public static void init()
	{
		params.addApp("xixuegui", "128", "pncot9fefkot9qbafkosiautvgfagq34gtedaf");		//吸血鬼日记
		params.addApp("qunying", "142", "Ggggshufuff9sissruasisistutsissitsursuraa");	//三国群英
		params.addApp("gaoguai", "126", "we2weddvfdcvgytfgytfvcxdert6yhgvcxwsx98");		//搞怪三国
		params.addApp("m5", "170", "8uyhj8u7ygfwqbfhj2wrfd0uqasjdoiuyh23456yfd");		//M5
		params.addApp("qzhuan", "171", "secdxvgewdq12errrfgybs8yterdcxbvfnwegvgw76");		//群英Q传
	}
}

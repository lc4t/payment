package noumena.payment.xyzs;

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

public class XyzsCharge
{
	private static XyzsParams params = new XyzsParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		XyzsCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_XYZS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_XYZS;
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
	
	public static String getCallbackFromXyzs(Map<String,String> xyzsparams)
	{
    	String ret = "{\"ret\":%d,\"msg\":\"%s\"}";
    	String minwen = "";
    	String miwen = "";
    	String minwen2 = "";
    	String miwen2 = "";
    	String orderid = xyzsparams.get("extra");
    	
		OrdersBean bean = new OrdersBean();
		Orders order = bean.qureyOrder(orderid);

		try
		{
			if (order == null)
			{
				ret = String.format(ret, 2, "");
			}
			else
			{
				minwen += "amount=" + xyzsparams.get("amount");
				minwen += "&extra=" + xyzsparams.get("extra");
				minwen += "&orderid=" + xyzsparams.get("orderid");
				minwen += "&serverid=" + xyzsparams.get("serverid");
				minwen += "&ts=" + xyzsparams.get("ts");
				minwen += "&uid=" + xyzsparams.get("uid");
				minwen2 = minwen;
				minwen = params.getParams(order.getSign()).getAppkey() + minwen;
				minwen2 = params.getParams(order.getSign()).getSecretkey() + minwen2;
				
				miwen = StringEncrypt.Encrypt(minwen);
				miwen2 = StringEncrypt.Encrypt(minwen2);

				System.out.println("xyzs cb sig->" + xyzsparams.get("sig"));
				System.out.println("xyzs cb kong sig->" + miwen2);
				
		    	if (miwen.equals(xyzsparams.get("sign")))
				{
					//服务器签名验证成功
					//请在这里加上游戏的业务逻辑程序代码
					//获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
		    		// 交易处理成功
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayIdExinfo(orderid, xyzsparams.get("orderid"), xyzsparams.get("amount"), xyzsparams.get("uid"));
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						ret = String.format(ret, 0, order.getItemPrice());
					}
					else
					{
						System.out.println("xyzs order (" + order.getOrderId() + ") had been succeed");
					}
					ret = String.format(ret, 0, "");
				}
				else
				{
					// 服务器签名验证失败
					ret = String.format(ret, 6, "");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = String.format(ret, 8, "");
		}

		System.out.println("xyzs cb ->" + xyzsparams.toString());
		System.out.println("xyzs cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/xyzscb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, xyzsparams.toString());
		
		return ret;
	}

	public static void init()
	{
		params.initParams(XyzsParams.CHANNEL_ID, new XyzsParamsVO());
		
//		params.addApp("xixuegui", "100001056", "qdOsKnlqL6oaGdwlDGdMThn861vu9Cjb", "");	//吸血鬼日记
//		params.addApp("gaoguai", "100000322", "KPUDE1UizVL2Q8gFXuaEBPXh9Nf8CFrM", "");	//搞怪三国
//		params.addApp("qunying", "100001583", "bhgxjF0aKYhIaTN6S1BtHaNQit4Z7qZI", "vqKREY2sGXFOquOOGcjaCQPFj6W6ApGY");	//三国群英
//		params.addApp("qzhuan", "100002330", "18LhSKkeNKSyUD3nprtDgTjhB2fhkCX8", "");	//三国群英换皮：Q传
//		params.addApp("M5", "100004141", "WJxiYpNxWCCKeKbvfSSyswGcjMtUlcnu", "aqwQjhQDy9K9W13TdjjHr5L9lzpOlnzx");	//M5
//		params.addApp("longzu", "100004124", "gsYhVrkP0oXPrVpHgqvL9l1yXlrVQRWo", "r8tq1Srdihda48oKAMlslZZWnJnFhM9j");	//龙族
//		params.addApp("m5", "100004603", "lS2VUbWDvqgQMuTPYd4mftWfck6nZCOH", "aHjCOhpSzEU0wx3jUXI1FZVdOfhixZJ0");	//Q传
//		params.addApp("m5", "100004432", "Ow7RvRPqLsxNfz6EdBk9HGKx0U17yiDs", "WwLvvvCZL21NjeVwjTCtfugPvaShW3oN");	//小青蛙
	}
}

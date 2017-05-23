package noumena.payment.baidu;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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

public class BaiduCharge
{
	private static BaiduParams params = new BaiduParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		BaiduCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_BAIDU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_BAIDU;
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

	public static String getCallbackFromBaidu(BaiduOrderVO ordervo, String orderstr)
	{
		String ret = BaiduParams.SUCCESS;
		try
		{
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(ordervo.getOrderid());
			
			if (order != null)
			{
				boolean isvlid = validMessage(ordervo, order.getExInfo());
	
				if (!isvlid)
				{
					return BaiduParams.ERR_SIGN;
				}
				else
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayId(ordervo.getOrderid(), ordervo.getOrderid(), ordervo.getAmount());
						
						//支付成功
						bean.updateOrderKStatus(ordervo.getOrderid(), Constants.K_STSTUS_SUCCESS);
						ret = BaiduParams.SUCCESS;
					}
					else
					{
						System.out.println("baidu order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
						ret = BaiduParams.ERR_REPEAT;
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/baiducb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + ordervo.getOrderid();
				
				OSUtil.saveFile(filename, orderstr);
			}
			else
			{
				ret = BaiduParams.ERR_NOORDER;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = BaiduParams.FAILURE;
		}
		return ret;
	}

	private static boolean validMessage(BaiduOrderVO ordervo, String appid) throws Exception
	{
		StringBuffer orderstr = new StringBuffer();
		orderstr.append(ordervo.getAmount());
		orderstr.append(ordervo.getCardtype());
		orderstr.append(ordervo.getOrderid());
		orderstr.append(ordervo.getResult());
		orderstr.append(ordervo.getTimetamp());
		orderstr.append(params.getAppKeyById(appid));
		orderstr.append(URLEncoder.encode(ordervo.getAid(), "utf-8"));
		
		String minwen = orderstr.toString();
		String miwen = StringEncrypt.Encrypt(minwen);
		
		System.out.println("baidu minwen->" + minwen);
		System.out.println("baidu my sign->" + miwen);
		System.out.println("baidu sign->" + ordervo.getClient_secret());
		
		if (ordervo.getClient_secret().equals(miwen))
		{
			if (ordervo.getResult().equals("1"))
			{
				return true;
			}
			else
			{
				return false;
			}
		}
		else
		{
			return false;
		}
	}
	
	public static void init()
	{
		params.addApp("t6", "1452", "4aba057a5ec082fc2ade563d04ba1bf2");
		params.addApp("hero", "1700", "3eb2850086599b27468a02a00a142801");
		params.addApp("gaoguai", "1885", "358183db50d9998b2dae088ac11ff287");
		params.addApp("t8", "2981059", "c694356990bbb0e51c7edc3b01a6fbce");
		params.addApp("m5", "3488535", "XSZlWP4Z4GLnUanGd9QfWClgrMjsGwNO");
		params.addApp("qingwa", "4435670", "2OQw5LHEEeZVqni3QWAeDAPUKVg3xqDp");
		params.addApp("qzhuan", "4759859", "UzZtqdGpbL9FHKEEa8KwWuNPEnGd24FN");	//群英Q传
		params.addApp("m3", "4820921", "DbCgokHGtRSzGP72Qh0kcKpOVxwQni4Q");		//M3
	}
}

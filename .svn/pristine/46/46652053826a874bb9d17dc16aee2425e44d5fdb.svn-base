package noumena.payment.baiduqianbao;

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

public class BaiduqianbaoCharge
{
	private static BaiduqianbaoParams params = new BaiduqianbaoParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		BaiduqianbaoCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_BAIDUQIANBAO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_BAIDUQIANBAO;
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
					st.setStatus(Constants.K_STSTUS_TIMEOUT);
				}
				else
				{
					st.setStatus(Constants.K_STSTUS_DEFAULT);
				}
			}
			else if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(Constants.K_STSTUS_SUCCESS);
			}
			else
			{
				//订单已经失败，直接返回订单状态
				st.setStatus(Constants.K_STSTUS_ERROR);
			}
			statusret.add(st);
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}

	public static String getCallbackFromBaidu(String appid, String transdata, String sign)
	{
		String ret = "SUCCESS";
		
		String key = params.getAppKeyById(appid);
		String minwen = transdata + key;
		String miwen = StringEncrypt.Encrypt(minwen);
		
		System.out.println("baiduqianbao minwen->" + minwen);
		System.out.println("baiduqianbao miwen->" + miwen);
		System.out.println("baiduqianbao sign->" + sign);
		
		if (miwen.equals(sign))
		{
			try
			{
//				BaiduqianbaoOrderVO vo = new BaiduqianbaoOrderVO();
				JSONObject json = JSONObject.fromObject(transdata);
				String orderid = json.getString("exorderno");
				String transid = json.getString("transid");
				String money = json.getInt("money") + "";
				String result = json.getInt("result") + "";
				
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
				if (order != null)
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayIdExinfo(orderid, transid, money, transdata);
						
						if (result.equals("0"))
						{
							//支付成功
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							//支付失败
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
						}
					}
					else
					{
						System.out.println("baiduqianbao order (" + order.getOrderId() + ") had been succeed");
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/baiduqianbaocb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, transdata);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			ret = "FAILURE";
		}
		
		return ret;
	}
	
	public static void init()
	{
		params.addApp("gaoguai", "2201151", "4Lz7cuDLkSGUa0FhdbLBb74O");	//alT96LfBmgTAGndfGkkR9MHgiStVcEKT
		params.addApp("gaoguai2", "2598161", "M31onRgDWFo3Q9lt8wUWN8FF");	//MBfI0gOm1zwXTnF9Ak8sZLLGklKhopyE
	}
}

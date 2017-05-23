package noumena.payment.tongbu;

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

public class TongbuCharge
{
	private static TongbuParams params = new TongbuParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		TongbuCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_TONGBU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_TONGBU;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(OSUtil.addZero(payId, 10), date);
		
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
			st.setPayId(OSUtil.addZero(order.getOrderId(), 10));
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

	public static String getCallbackFromTongbu(TongbuOrderVO vo)
	{
		String ret = "{\"status\":\"success\"}";
		
		try
		{
			StringBuffer signbuf = new StringBuffer();
			signbuf.append("source=");
			signbuf.append(vo.getSource());
			signbuf.append("&trade_no=");
			signbuf.append(vo.getTrade_no());
			signbuf.append("&amount=");
			signbuf.append(vo.getAmount());
			signbuf.append("&partner=");
			signbuf.append(vo.getPartner());
			signbuf.append("&paydes=");
			signbuf.append(vo.getPaydes());
			signbuf.append("&debug=");
			signbuf.append(vo.getDebug());
			signbuf.append("&tborder=");
			signbuf.append(vo.getTborder());
			signbuf.append("&key=");
			signbuf.append(params.getParams(vo.getPartner()).getSecretkey());
			
			String sign = StringEncrypt.Encrypt(signbuf.toString());
			System.out.println("tongbu cb->" + signbuf.toString());
			System.out.println("tongbu cb my sign->" + sign);
			
			String orderid = vo.getTrade_no();
			orderid = OSUtil.rmvZero(orderid);
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
				if (sign.equals(vo.getSign()))
				{
					if (order != null)
					{
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(orderid, vo.getTborder(), vo.getAmount(), vo.getPartner());
							
							//支付成功
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("tongbu order (" + order.getOrderId() + ") had been succeed");
						}
					}
				}
				else
				{
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
				}
			}
			
			String path = OSUtil.getRootPath() + "../../logs/tongbucb/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			
			OSUtil.saveFile(filename, signbuf.toString() + "&sign=" + vo.getSign());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
	
	public static void init()
	{
		params.initParams(TongbuParams.CHANNEL_ID, new TongbuParamsVO());
		
//		params.addApp("gaoguai", "140134", "@UJ5tEQod1AXNkxH0Jg5t7Qn1qAXkZx0");
//		params.addApp("xixuegui", "140665", "1pXMjZG0Tg4sDPncp*WMYwGTIg4D6Pcz");
//		params.addApp("m5", "140782", "r4Olan@VhXuERGerB4OaynVKhXE8Re2q");
//		params.addApp("gongzhu", "141043", "hWE7Rd2qANkam@UJWuERGd2A4Nkxm@Jh");
//		params.addApp("longzu", "141139", "lby#VLXvF8He3B5Olyn#KiXu8RH2rB4l");	//龙族
//		params.addApp("qzhuan", "141153", "5ObQn1KAXk8xHUrgODbQ#1KXuk8HeTg4");	//群英Q传
	}
}

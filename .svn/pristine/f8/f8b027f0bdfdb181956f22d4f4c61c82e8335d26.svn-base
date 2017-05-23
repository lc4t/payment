package noumena.payment.kunlun;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class KunlunCharge
{
//	private static KunlunParams params = new KunlunParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		KunlunCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_KUNLUN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_KUNLUN;
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

	public static String getCallbackFromKunlun(KunlunOrderVO vo)
	{
		String ret = "{\"retcode\":0,\"retmsg\":\"success\"}";
		
		try
		{
			StringBuffer logbuf = new StringBuffer();
			StringBuffer signbuf = new StringBuffer();
			logbuf.append("oid=");
			logbuf.append(vo.getOid());
			logbuf.append("&uid=");
			logbuf.append(vo.getUid());
			logbuf.append("&amount=");
			logbuf.append(vo.getAmount());
			logbuf.append("&coins=");
			logbuf.append(vo.getCoins());
			logbuf.append("&dtime=");
			logbuf.append(vo.getDtime());
			logbuf.append("&sign=");
			logbuf.append(vo.getSign());
			logbuf.append("&ext=");
			logbuf.append(vo.getExt());

			signbuf.append(vo.getOid());
			signbuf.append(vo.getUid());
			signbuf.append(vo.getAmount());
			signbuf.append(vo.getCoins());
			signbuf.append(vo.getDtime());
			signbuf.append(KunlunParams.KEY);
			
			String mysign = StringEncrypt.Encrypt(signbuf.toString());
			System.out.println("kunlun cb->" + logbuf.toString());
			System.out.println("kunlun cb my sign->" + mysign);
			
			if (vo.getSign().equals(mysign))
			{
				JSONObject json = JSONObject.fromObject(vo.getExt());
				String orderid = json.getString("partnersorderid");
				
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
				if (order != null)
				{
					if (vo.getSign().equals(mysign))
					{
						if (order != null)
						{
							if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
							{
								bean.updateOrderAmountPayIdExinfo(orderid, vo.getOid(), vo.getAmount(), vo.getUid() + "#" + vo.getCoins());
								
								CallbackBean cbb = new CallbackBean();
								Callback cb = cbb.qureyCallback(orderid);
								if (cb != null)
								{
									String cburl = cb.getCallbackUrl();
									cburl += "&money=" + vo.getAmount();
									cb.setCallbackUrl(cburl);
									cbb.updateCallback(cb);
								}
								
								//支付成功
								bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
							}
							else
							{
								System.out.println("kunlun order (" + order.getOrderId() + ") had been succeed");
							}
						}
					}
					else
					{
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/kunluncb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, signbuf.toString());
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ret;
	}
}

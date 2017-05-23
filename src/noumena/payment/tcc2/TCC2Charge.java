package noumena.payment.tcc2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
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
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class TCC2Charge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		TCC2Charge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_TCC2;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_TCC2;
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
			order.setProductId(order.getItemId());
			order.setSubId(order.getExInfo());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					status = Constants.K_STSTUS_TIMEOUT;
					st.setStatus(2);
				}
				else
				{
					String[] ps = order.getExInfo().split("#");
					TCC2OrderMlistVO retvo = checkOrderFromTCC2(order.getOrderId(), ps[0], ps[1]);
					TCC2OrderVO vo = retvo.getAppm_list().getAppm()[0];
					
					if (vo.getR_code().equals("0"))
					{
						//如果订单已经成功，直接返回订单状态
						st.setStatus(1);
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						//订单已经失败，直接返回订单状态
						st.setStatus(2);
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
					}
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

	private static TCC2OrderMlistVO checkOrderFromTCC2(String orderid, String subid, String transid)
	{
		TCC2OrderMlistVO ordervo = new TCC2OrderMlistVO();
		try
		{
			System.out.println("check order->" + orderid);
			String urlstr;
			if (isTestmode() == true)
			{
				urlstr = TCC2Params.CHECK_ORDER_STATUS_URL_TEST;
			}
			else
			{
				urlstr = TCC2Params.CHECK_ORDER_STATUS_URL_RELEASE;
			}
			urlstr += "?uid=" + subid;
			urlstr += "&trans_id=" + transid;
			System.out.println("url->" + urlstr);
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();
			//{"appm_list":{"appm":[{"trans_id":"98920130219185642339","r_desc":"success","uid":"1188095","status":"2","r_code":"0"}]}}

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/tcc2ordercheck/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			OSUtil.saveFile(filename, res);
			
			JSONObject json = JSONObject.fromObject(res);
			ordervo = (TCC2OrderMlistVO) JSONObject.toBean(json, TCC2OrderMlistVO.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return ordervo;
	}
}

package noumena.payment.amazon;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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

public class AmazonCharge
{
	public static String getTransactionId(Orders order)
	{
		OrderIdVO orderIdVO = getTransactionIdVO(order);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static OrderIdVO getTransactionIdVO(Orders order)
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
				cburl += "?pt=" + Constants.PAY_TYPE_AMAZON;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_AMAZON;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
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
			if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(1);
			}
			else if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//订单没有结果
				int ret = checkOrderFromAmazon(order.getOrderId(), order.getExInfo(), order.getPayId(), order.getAppId(), order.getItemId());
				if (ret == 0)
				{
					//验证成功，销毁订单
					st.setStatus(1);
					bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
				}
				else if (ret == 1)
				{
					//重复订单，销毁订单
					st.setStatus(4);
					bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
				}
				else
				{
					//验证失败，销毁订单
					st.setStatus(2);
					bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
				}
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
	
	private static boolean isOrderExist(String orderid, String receipt, String amazonuid)
	{
		OrdersBean bean = new OrdersBean();
		
		List<Orders> orders = bean.qureyOrdersAmazon(orderid, amazonuid, receipt);
		
		if (orders == null || orders.size() == 0)
		{
			return false;
		}
		else
		{
			return true;
		}
	}
	
	private static int checkOrderFromAmazon(String orderid, String receipt, String amazonuid, String appid, String productid)
	{
		int ret = 0;
		try
		{
			if (isOrderExist(orderid, receipt, amazonuid) == true)
			{
				return 1;
			}
			
			String urlstr = AmazonParams.VERIFY_URL;
			urlstr = String.format(urlstr, AmazonParams.SECRET_KEY, amazonuid, receipt);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			int respcode = connection.getResponseCode();
			if (respcode == 200)
			{
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String res = in.readLine();

				String path = OSUtil.getRootPath() + "../../logs/amazonordercheck/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				OSUtil.saveFile(filename, res);
				
				JSONObject json = JSONObject.fromObject(res);
				AmazonOrderVO ordervo = (AmazonOrderVO) JSONObject.toBean(json, AmazonOrderVO.class);
				
				if (ordervo.getSku() != null && ordervo.getPurchaseToken() != null)
				{
					if (ordervo.getSku().equals(appid + "." + productid))
					{
						ret = 0;
					}
					else
					{
						System.out.println("amazon sku->" + ordervo.getSku());
						System.out.println("amazon order app->" + appid + "." + productid);
						ret = -1;
					}
				}
				else
				{
					ret = -1;
				}
			}
			else if (respcode == 499)
			{
				//The Purchase Token was created with credentials that have expired, use renew to generate a valid purchase token.
				ret = -2;
			}
			else
			{
				ret = -1;
			}
			
			connection.disconnect();

			return ret;
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return -1;
		}
	}
	
	public static String getTransactionIdAndStatus(Orders vo)
	{
		OrderIdVO orderIdVO = getTransactionIdVO(vo);
		String ids = "";
		if (orderIdVO != null)
		{
			ids = orderIdVO.getPayId();
		}
		return checkOrdersStatus(ids);
	}
}

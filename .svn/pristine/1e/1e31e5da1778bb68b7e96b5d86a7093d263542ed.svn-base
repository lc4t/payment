package noumena.payment.vnsoha;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
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
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class VNSohaCharge
{
	private static VNSohaParams params = new VNSohaParams();
	private static boolean testmode = false;
	public static String heartbeat = "";
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		VNSohaCharge.testmode = testmode;
	}
	
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
				cburl += "?pt=" + Constants.PAY_TYPE_VNSOHA;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_VNSOHA;
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
				List<Orders> os = bean.qureyOrdersByPayId(order.getExInfo());
				if (os.size() > 1)
				{
					//第三方订单号已经存在，重复订单，该订单应该已经处理了，忽略掉该订单
					st.setStatus(4);
				}
				else
				{
					VNSohaOrderVO ret = checkOrderFromSoha(order.getOrderId(), order.getSign(), order.getExInfo());
					if (ret != null)
					{
						bean.updateOrderAmountPayId(order.getOrderId(), ret.getOrder_id(), ret.getPrice());
						if (ret.getStatus().equals("1"))
						{
							//验证成功
							//修改回调url，加上money参数表示实际支付金额
							CallbackBean callbackBean = new CallbackBean();
							Callback cbvo = callbackBean.qureyCallback(order.getOrderId());
							if (cbvo != null)
							{
								String cburl = cbvo.getCallbackUrl();
								if (cburl.indexOf("?") < 0)
								{
									cburl += "?money=" + ret.getPrice();
								}
								else
								{
									cburl += "&money=" + ret.getPrice();
								}
								cbvo.setCallbackUrl(cburl);
								callbackBean.updateCallback(cbvo);
							}
							
							st.setStatus(1);
							bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							//验证失败
							st.setStatus(2);
							bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
						}
					}
					else
					{
						st.setStatus(2);
						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
					}
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
	
	private static VNSohaOrderVO checkOrderFromSoha(String orderid, String vnappid, String vnorderid)
	{
		VNSohaOrderVO ordervo = null;
		try
		{
			String secret = "";
			String urlstr = VNSohaParams.VERIFY_URL;
			urlstr = String.format(urlstr, vnappid, secret, vnorderid);
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = in.readLine();

			connection.disconnect();

			String path = OSUtil.getRootPath() + "../../logs/vnsohaordercheck/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			OSUtil.saveFile(filename, res);

			JSONObject json = JSONObject.fromObject(res);
			ordervo = (VNSohaOrderVO) JSONObject.toBean(json, VNSohaOrderVO.class);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return ordervo;
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
	
	public static void init()
	{
		params.addVNSohaApp("t6_Jairbreak", "5538d45b1856e0229f710fd6044f5f9c", "b71f403f8b33c0fadbf2b72ad39d3ad4");
		params.addVNSohaApp("t6_Apple", "d3174aba4aa104577b2f5f9782b24734", "bb28f36d8674ebd8edc0a8424935abe7");
		params.addVNSohaApp("t6_Android", "05dd750a84743af27c7fecb19f746db4", "83c1456535cba9ded1589273c589eacb");
	}
}

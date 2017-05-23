package noumena.payment.appota;

import java.util.ArrayList;
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

public class AppOTACharge
{
	private static AppOTAParams params = new AppOTAParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		AppOTACharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_APPOTA;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_APPOTA;
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
				st.setStatus(3);
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

	public static String getCallbackFromAppOTA(AppOTAOrderVO ordervo, String orderstr)
	{
		String ret = AppOTAParams.SUCCESS;
		try
		{
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(ordervo.getTarget());
			
			if (order != null)
			{
				boolean isvlid = validMessage(ordervo, orderstr, order.getSign());
	
				if (!isvlid)
				{
					return AppOTAParams.ERR_SIGN;
				}
				else
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayId(ordervo.getTarget(), ordervo.getTransaction_id(), ordervo.getAmount());
						
						//支付成功
						bean.updateOrderKStatus(ordervo.getTarget(), Constants.K_STSTUS_SUCCESS);
						ret = AppOTAParams.SUCCESS;
					}
					else
					{
						System.out.println("appota order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
						ret = AppOTAParams.ERR_REPEAT;
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/appotacb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + ordervo.getTarget();
				
				OSUtil.saveFile(filename, orderstr);
			}
			else
			{
				ret = AppOTAParams.ERR_NOORDER;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = AppOTAParams.FAILURE;
		}
		return ret;
	}

	private static boolean validMessage(AppOTAOrderVO ordervo, String orderstr, String appid) throws Exception
	{
		String minwen = orderstr + params.getAppKeyById(appid);
		String miwen = StringEncrypt.Encrypt(minwen);
		
		System.out.println("appota minwen->" + minwen);
		System.out.println("appota my sign->" + miwen);
		System.out.println("appota sign->" + ordervo.getHash());
		
		if (ordervo.getHash().equals(miwen))
		{
			if (ordervo.getStatus().equals("1"))
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
		params.addApp("cangqiong", "0fbddb7dc44e614bbc9ff278661edefa0525f8f82", "cb71dbb9f42ffc2f27598b1301ab41070525f8f82");
		params.addApp("cangqiong2", "f03803abda67346b5d0088de4429c3c405260c353", "885a32be7970a311d22fb457ef77ad9505260c353");
	}
}

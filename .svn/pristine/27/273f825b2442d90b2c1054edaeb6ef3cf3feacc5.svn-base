package noumena.payment.wo;

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

public class WoCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		WoCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_WO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_WO;
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
			if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(1);
			}
			else if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//订单没有结果
				st.setStatus(3);
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
	
	private static String findValueByKey(String content, String key)
	{
		int pos1 = 0, pos2 = 0;
		String key1 = "<" + key + ">";
		String key2 = "</" + key + ">";
		
		pos1 = content.indexOf(key1);
		if (pos1 < 0)
		{
			return "";
		}
		pos2 = content.indexOf(key2);
		if (pos2 < 0)
		{
			return "";
		}
		
		return content.substring(pos1 + key1.length(), pos2);
	}

	public static String getCallbackFromWo(String cbdata)
	{
		WoOrderVO ordervo = new WoOrderVO();
		StringBuffer md5buf = new StringBuffer();
		String orderinfo = findValueByKey(cbdata, "callbackReq");
		ordervo.setOrderid(findValueByKey(orderinfo, "orderid"));
		md5buf.append("orderid=");
		md5buf.append(ordervo.getOrderid());
		ordervo.setOrdertime(findValueByKey(orderinfo, "ordertime"));
		md5buf.append("&ordertime=");
		md5buf.append(ordervo.getOrdertime());
		ordervo.setCpid(findValueByKey(orderinfo, "cpid"));
		md5buf.append("&cpid=");
		md5buf.append(ordervo.getCpid());
		ordervo.setAppid(findValueByKey(orderinfo, "appid"));
		md5buf.append("&appid=");
		md5buf.append(ordervo.getAppid());
		ordervo.setFid(findValueByKey(orderinfo, "fid"));
		md5buf.append("&fid=");
		md5buf.append(ordervo.getFid());
		ordervo.setConsumeCode(findValueByKey(orderinfo, "consumeCode"));
		md5buf.append("&consumeCode=");
		md5buf.append(ordervo.getConsumeCode());
		ordervo.setPayfee(findValueByKey(orderinfo, "payfee"));
		md5buf.append("&payfee=");
		md5buf.append(ordervo.getPayfee());
		ordervo.setPayType(findValueByKey(orderinfo, "payType"));
		md5buf.append("&payType=");
		md5buf.append(ordervo.getPayType());
		ordervo.sethRet(findValueByKey(orderinfo, "hRet"));
		md5buf.append("&hRet=");
		md5buf.append(ordervo.gethRet());
		ordervo.setStatus(findValueByKey(orderinfo, "status"));
		md5buf.append("&status=");
		md5buf.append(ordervo.getStatus());
		ordervo.setSignMsg(findValueByKey(orderinfo, "signMsg"));
		md5buf.append("&Key=");
		md5buf.append(WoParams.WO_KEY);

		System.out.println("wo minwen->" + md5buf.toString());
		OrdersBean bean = new OrdersBean();
		Orders order = bean.qureyOrder(ordervo.getOrderid());
		if (order != null)
		{
			String path = OSUtil.getRootPath() + "../../logs/wocb/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + ordervo.getOrderid();
			OSUtil.saveFile(filename, cbdata);
			
			if (order.getKStatus() == Constants.K_STSTUS_DEFAULT || order.getKStatus() == Constants.K_CON_ERROR)
			{
				String mysign = StringEncrypt.Encrypt(md5buf.toString());
				System.out.println("wo mysign->" + mysign);
				if (mysign.equals(ordervo.getSignMsg()))
				{
					if (ordervo.getStatus().equals("00000"))
					{
						bean.updateOrderKStatus(ordervo.getOrderid(), Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						bean.updateOrderKStatus(ordervo.getOrderid(), Constants.K_STSTUS_ERROR);
					}
				}
				else
				{
					bean.updateOrderKStatus(ordervo.getOrderid(), Constants.K_STSTUS_ERROR);
				}
			}
		}
		return "";
	}
}

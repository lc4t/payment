package noumena.payment.oppo;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
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
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import org.apache.commons.codec.binary.Base64;

public class OppoCharge
{
	private static OppoParams params = new OppoParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		OppoCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_OPPO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_OPPO;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
			
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
			try
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
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}
	
	public static String getCallbackFromOPPO(Map<String,Object> oppoparams)
	{
		String ret = "result=OK&resultMsg=";
		String orderid = "";
		String sporderid = "";
		String orderAmount = "";
		
		try
		{
			orderid = (String) oppoparams.get("partnerOrder");
			sporderid = (String) oppoparams.get("notifyId");
			orderAmount = (String) oppoparams.get("price");

			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
				String content = "";
				content += "notifyId=" + oppoparams.get("notifyId");
				content += "&partnerOrder=" + oppoparams.get("partnerOrder");
				content += "&productName=" + oppoparams.get("productName");
				content += "&productDesc=" + oppoparams.get("productDesc");
				content += "&price=" + oppoparams.get("price");
				content += "&count=" + oppoparams.get("count");
				content += "&attach=" + oppoparams.get("attach");
				
				if (doCheck(content, (String) oppoparams.get("sign"), OppoParams.PUBLIC_KEY) == true)
				{
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayId(orderid, sporderid, orderAmount);
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("oppo order (" + order.getOrderId() + ") had been succeed");
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("oppo cb->" + oppoparams.toString());
		
		String path = OSUtil.getRootPath() + "../../logs/oppocb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, oppoparams.toString());
		
		return ret;
	}

	private static boolean doCheck(String content, String sign, String publicKey) throws Exception
	{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] encodedKey = Base64.decodeBase64(publicKey.getBytes("UTF-8"));
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		java.security.Signature signature = java.security.Signature.getInstance("SHA1WithRSA");

		signature.initVerify(pubKey);
		signature.update(content.getBytes("UTF-8"));
		boolean bverify = signature.verify(Base64.decodeBase64(sign.getBytes("UTF-8")));
		return bverify;
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
				cburl += "?pt=" + order.getPayType();
			}
			else
			{
				cburl += "&pt=" + order.getPayType();
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}
	
	public static void init()
	{
		params.addApp("xixuegui", "249", "mogoo-4QUBwtRIR9HvPyG2Dy");	//吸血鬼
	}
}

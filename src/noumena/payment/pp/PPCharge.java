package noumena.payment.pp;

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
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class PPCharge
{
	private static RSAEncrypt rsaEncrypt = new RSAEncrypt();
	private static PPParams params = new PPParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		PPCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_PP;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_PP;
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

	public static String getCallbackFromPP(PPOrderVO ordervo, String orderstr)
	{
		String ret = PPParams.SUCCESS;
		try
		{
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(ordervo.getBillno());
			
			if (order != null)
			{
				boolean isvlid = validMessage(ordervo);
	
				if (!isvlid)
				{
					return PPParams.FAILURE;
				}
				else
				{
					if(order.getAmount().equals(Float.valueOf(ordervo.getAmount()))){
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayId(ordervo.getBillno(), ordervo.getOrder_id(), ordervo.getAmount());
							
							//支付成功
							bean.updateOrderKStatus(ordervo.getBillno(), Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("pp order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
						}
					}else{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(pp cb)->(order:" + order.getOrderId() + "),jin e bu pi pei");
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/ppcb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + ordervo.getBillno() + "_" + ordervo.getOrder_id();
				
				OSUtil.saveFile(filename, orderstr);
			}
			else
			{
				ret = "ILLEGAL ORDER";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = PPParams.FAILURE;
		}
		return ret;
	}

	private static boolean validMessage(PPOrderVO ordervo) throws Exception
	{
		String plainDataStr = "";
		try
		{
			BASE64Encoder base64Encoder = new BASE64Encoder();
			BASE64Decoder base64Decoder = new BASE64Decoder();
			
			byte[] dcDataStr = base64Decoder.decodeBuffer(ordervo.getSign());
			byte[] plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), dcDataStr);
			System.out.println("data length->" + plainData.length);
			System.out.println(RSAEncrypt.byteArrayToString(plainData));
			plainDataStr = new String(plainData);
			System.out.println(plainDataStr);
        }
		catch (Exception e)
		{
            System.err.println(e.getMessage());
        }
		
		return true;
	}
	
	public static void init()
	{
        try
        {
        	rsaEncrypt.loadPublicKey(RSAEncrypt.DEFAULT_PUBLIC_KEY);
        	System.out.println("pp load public key success");
        }
        catch (Exception e)
        {
        	System.err.println(e.getMessage());
        	System.err.println("pp load public key failed");
        }
//		params.addApp("t6", "100000145", "5ebfe555197b0d79d5464a6917bd2bd3");
		params.addApp("t6", "1397", "cdf848a4c3d11593dd222cffd05e54dc");
//		params.addApp("hero", "1831", "1d52bfefd2a78504c9ba976d152cc563");
		params.addApp("hero", "1931", "6dc1cc632e0683aa9ea4b8c674fc6cb0");
		params.addApp("gaoguai", "1979", "d254d28db6a1114e9e728c1b659aefa9");
		params.addApp("xixuegui", "3807", "e45fea7666180aa2647121c45b5c7e43");
		params.addApp("m5", "3999", "c30276e6709f40a62ff9cafa823a719c");
		params.addApp("qingwa", "4797", "c6e82d4399a6e206fd1d2145d7515245");
	}
}

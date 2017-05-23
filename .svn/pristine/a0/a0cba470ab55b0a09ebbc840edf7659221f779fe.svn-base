package noumena.payment.nearme;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.alipay.RSA;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class NearmeCharge
{
	private static NearmeParams params = new NearmeParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		NearmeCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order, String partnerid, String notifyurl, String name, String payprice)
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
				cburl += "?pt=" + Constants.PAY_TYPE_NEARME;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_NEARME;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		String sign = getPaySign(order, partnerid, notifyurl, name, payprice);
		orderIdVO.setMsg(sign);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	private static String getPaySign(Orders order, String partnerid, String notifyurl, String name, String payprice)
	{
		String sign = "";
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("partner_order=\"" + order.getOrderId() + "\"&");	// 商品订单号
		stringBuilder.append("price=\"" + payprice + "\"&");					// 商品总价
		stringBuilder.append("product_desc=\"" + order.getSign() + "\"&");		// 商品描述
		stringBuilder.append("productName=\"" + name + "\"&");					// 商品名称
		stringBuilder.append("count=\"" + 1 + "\"&");							// 恒定为1
		stringBuilder.append("appPackage=\"" + order.getAppId() + "\"&");		// 应用包名
		stringBuilder.append("partner_code=\"" + partnerid + "\"&");			// 开发者id
		stringBuilder.append("notify_url=\"" + notifyurl + "\"");				// 回调链接

		try
		{
			sign = RSA.sign(stringBuilder.toString(), NearmeParams.KONG_PRIVATE_KEY_PKCS8, "UTF-8");
        }
		catch (Exception e)
		{
            System.err.println(e.getMessage());
        }
		
		return sign;
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
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}

	public static String getCallbackFromOPPO(NearmeOrderVO vo, String sign)
	{
		String ret = "ok";

		String minwen = "";
		String postdata = "";

		postdata += "notify_id=" + vo.getNotify_id();
		postdata += "&partner_code=" + vo.getPartner_code();
		postdata += "&partner_order=" + vo.getPartner_order();
		postdata += "&orders=" + vo.getOrders();
		postdata += "&pay_result=" + vo.getPay_result();
		postdata += "&sign=" + vo.getSign();
		
		minwen += vo.getNotify_id();
		minwen += "&" + vo.getPartner_code();
		minwen += "&" + vo.getPartner_order();
		minwen += "&" + vo.getOrders();
		minwen += "&" + vo.getPay_result();
		
		System.out.println("oppo cb postdata->" + postdata);
		
		if (postdata == null || postdata.equals(""))
		{
			return ret;
		}
		
		boolean isvalid = RSA.verify(minwen, sign, NearmeParams.NEARME_PUBLIC_KEY, "UTF-8");
		
		if (isvalid)
		{
			try
			{
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(vo.getPartner_order());
				if (order != null)
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
//						bean.updateOrderAmountPayIdExinfo(vo.getPartner_order(), vo.getOrderid(), vo.getPaytotalfee(), vo.getPaytypeid());
						
						if (vo.getPay_result().toLowerCase().equals("ok"))
						{
							//支付成功
							bean.updateOrderKStatus(vo.getPartner_order(), Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							//支付失败
							bean.updateOrderKStatus(vo.getPartner_order(), Constants.K_STSTUS_ERROR);
						}
					}
					else
					{
						System.out.println("c3ggate order (" + order.getOrderId() + ") had been succeed");
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/oppocb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + vo.getPartner_order();
				
				OSUtil.saveFile(filename, postdata);
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
	}
}

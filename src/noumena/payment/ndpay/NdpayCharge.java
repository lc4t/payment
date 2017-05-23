package noumena.payment.ndpay;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class NdpayCharge
{
	private static NdpayParams params = new NdpayParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		NdpayCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_NDPAY;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_NDPAY;
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
			order.setProductId(order.getItemId());
			order.setSubId(order.getExInfo());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
//				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
//				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
//				Calendar cal2 = Calendar.getInstance();
//				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
//				{
//					status = Constants.K_STSTUS_TIMEOUT;
//					st.setStatus(2);
//				}
//				else
//				{
//					NdpayOrderVO vo = checkOrderFromNdpay(order.getOrderId());
//					
//					if (vo.getAct().equals("4"))
//					{
//						//如果订单已经成功，直接返回订单状态
//						st.setStatus(1);
//						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_SUCCESS);
//					}
//					else
//					{
//						//订单已经失败，直接返回订单状态
//						st.setStatus(2);
//						bean.updateOrderKStatus(order.getOrderId(), Constants.K_STSTUS_ERROR);
//					}
//				}
				st.setStatus(0);
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
	
	private static String getSign(NdpaycbOrderVO ordervo, String appkey)
	{
		StringBuilder strSign = new StringBuilder();
		strSign.append(ordervo.getAppId());
		strSign.append(ordervo.getAct());
		strSign.append(ordervo.getProductName());
		strSign.append(ordervo.getConsumeStreamId());
		strSign.append(ordervo.getCooOrderSerial());
		strSign.append(ordervo.getUin());
		strSign.append(ordervo.getGoodsId());
		strSign.append(ordervo.getGoodsInfo());
		strSign.append(ordervo.getGoodsCount());
		strSign.append(ordervo.getOriginalMoney());
		strSign.append(ordervo.getOrderMoney());
		strSign.append(ordervo.getNote());
		strSign.append(ordervo.getPayStatus());
		strSign.append(ordervo.getCreateTime());
		strSign.append(appkey);

		String miwen = StringEncrypt.Encrypt(strSign.toString()).toLowerCase();
		
		return miwen;
	}

	public static String getCallbackFromNdpay(NdpaycbOrderVO ordervo)
	{
		OrdersBean bean = new OrdersBean();
		String appkey = params.getParams(ordervo.getAppId()).getAppkey();
		String orderid = ordervo.getCooOrderSerial();
		
		if (ordervo.getAct().equals("1"))
		{
			//支付购买结果
			String sign = ordervo.getSign();
//			String minwen = String.format("{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}{%s}", 
//										ordervo.getAppId(), ordervo.getAct(), ordervo.getProductName(),
//										ordervo.getConsumeStreamId(), ordervo.getCooOrderSerial(), ordervo.getUin(),
//										ordervo.getGoodsId(), ordervo.getGoodsInfo(), ordervo.getGoodsCount(),
//										ordervo.getOriginalMoney(), ordervo.getOrderMoney(), ordervo.getNote(),
//										ordervo.getPayStatus(), ordervo.getCreateTime(), appkey);
//			String miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
			String miwen = getSign(ordervo, appkey);
			if (sign.equals(miwen))
			{
				//MD5码验证通过
				bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getConsumeStreamId(), ordervo.getOrderMoney(), ordervo.getUin());
				if (ordervo.getPayStatus().equals("1"))
				{
					//支付成功
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
				}
				else
				{
					bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
				}
			}
			else
			{
				bean.updateOrderKStatus(orderid, Constants.K_STSTUS_ERROR);
			}
		}
		
		NdpaycbRetVO retvo = new NdpaycbRetVO();
		retvo.setErrorCode("1");
		retvo.setErrorDesc("接收成功");
		
		JSONObject json = JSONObject.fromObject(retvo);
		return json.toString();
	}
	
	public static void init()
	{
		params.initParams(NdpayParams.CHANNEL_ID, new NdpayParamsVO());
		
//		params.addApp("xol", "109206", "cc8f63f9e5582466de4b688957b4b6c9b37bafc37bb03fe6");
//		params.addApp("galaxy2", "100797", "e93ca8bbaeed05ce627f2f9524dc456327fdcc76231d6aa7");
//		params.addApp("m1andk", "108992", "4e17756a965e8d3da616ef7d5393699d98b956cd6f624926");
//		params.addApp("m1ios", "104724", "f459f54420cb5d6f092d38113e544b453902070c61a0cf61");
//		params.addApp("t6", "107571", "83394f96a37d436cfab9c2c53d3d483c0ae506ac6937a0ff");
//		params.addApp("mingjiang", "106509", "36d80ecd3ff73cd9febc1dfcd3a6e52ea8b3ae3bfd4e4acb");
////		params.addApp("t6hougongios", "102592", "ec56a74e7b25e786c6d6134cc027210917994433719dccae");
////		params.addApp("t6hougongandroid", "103297", "cceec62ec3b7d1f51ccbd9a1a90069672fc4041215c7a6ab");
//		params.addApp("t6hougongios", "100621", "b72d6742792cb7cbc71cb48c2f26ab63af9f5d4164ec6b66");
//		params.addApp("t6hougongandroid", "102627", "cafa9cae263680bcd6015be551c7ea7cd8d2f49a29b30917");
//		params.addApp("heroios", "107236", "b3476c114803bb9312d2d8da506f6e49883a459d46293fbb");
//		params.addApp("heroandroid", "107577", "da979208e59fd17f106cd263c09a3068b67d845ee27a55a1");
//		params.addApp("gaoguaiios", "101310", "32c1ce6bc4e653ed3d2d2a9766b33bdaef272256144c94c7");
//		params.addApp("gaoguaiandroid", "107406", "1c1a36ecc80ae4605661f689ee02ec0d3321d5044f13ce9f");
//		params.addApp("xixuegui", "113711", "7ede25210332dd2d180e982b0155c6b5c0e41125eca8dd71");
//		params.addApp("t8", "114961", "df24a106e1950121e4103737426e92d83e7cf55a1200605d");
//		params.addApp("m5", "115402", "0dcb45ca8bd31e9e5b79f7a45b74e9504b6caf1083dc7c8b");
//		params.addApp("qzhuan", "116148", "fbb70f2203de2825eb2e579b7ed3c8a3dfb0ee383b208713");	//群英Q传
	}
}

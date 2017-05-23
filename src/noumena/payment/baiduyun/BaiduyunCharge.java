package noumena.payment.baiduyun;

import java.io.IOException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;
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

public class BaiduyunCharge
{
	private static BaiduyunParams params = new BaiduyunParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		BaiduyunCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_BAIDUYUN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_BAIDUYUN;
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
	
	public static String getCallbackFromBaiduyun(Map<String,String> baiduyunparams)
	{
		String ret = "";
		String orderid = baiduyunparams.get("CooperatorOrderSerial");
		String sporderid = baiduyunparams.get("OrderSerial");
    	String appid = baiduyunparams.get("AppID");
    	String sign = baiduyunparams.get("Sign");
    	String ocontent = baiduyunparams.get("Content");
    	String key = params.getAppKeyById(appid);

    	String content = "";
    	BASE64Decoder decode = new BASE64Decoder();
    	try
    	{
    		content = URLDecoder.decode(ocontent, "utf-8");
    		content = new String(decode.decodeBuffer(content), "utf-8");
		}
    	catch (IOException e)
    	{
			e.printStackTrace();
		}

		JSONObject json = JSONObject.fromObject(content);
//		BaiduyunOrderVO yunvo = (BaiduyunOrderVO) JSONObject.toBean(json, BaiduyunOrderVO.class);
		BaiduyunOrderVO yunvo = new BaiduyunOrderVO();
		yunvo.setBankDateTime(json.getString("BankDateTime"));
		yunvo.setExtInfo(json.getString("ExtInfo"));
		yunvo.setMerchandiseName(json.getString("MerchandiseName"));
		yunvo.setOrderMoney(json.getString("OrderMoney"));
		yunvo.setOrderStatus(json.getInt("OrderStatus") + "");
		yunvo.setStartDateTime(json.getString("StartDateTime"));
		yunvo.setStatusMsg(json.getString("StatusMsg"));
		yunvo.setUID(json.getInt("UID") + "");

    	String minwen = "";
    	minwen += appid;
    	minwen += sporderid;
    	minwen += orderid;
    	minwen += ocontent;
    	minwen += key;
    	String miwen = StringEncrypt.Encrypt(minwen);
    	
		if (miwen.equals(sign))
		{
			if (yunvo.getOrderStatus().equals("1"))
			{
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
		
				try
				{
					if (order != null)
					{
						//服务器签名验证成功
						//请在这里加上游戏的业务逻辑程序代码
						//获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
			    		// 交易处理成功
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(orderid, sporderid, yunvo.getOrderMoney(), yunvo.getUID());
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("baiduyun order (" + order.getOrderId() + ") had been succeed");
						}
						ret = getRetMsg(appid, "1", "success", "");
					}
					else
					{
						ret = getRetMsg(appid, "1", "invalid order", "");
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
					ret = getRetMsg(appid, "-2", "system error", "");
				}
			}
			else
			{
				ret = getRetMsg(appid, "1", "order failed", "");
			}
		}
		else
		{
			ret = getRetMsg(appid, "-1", "invalid sign", "");
		}

		System.out.println("baiduyun cb ->" + baiduyunparams.toString());
		System.out.println("baiduyun cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/baiduyuncb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, baiduyunparams.toString());
		
		return ret;
	}

	private static String getRetMsg(String appid, String retcode, String retmsg, String content)
	{
    	String ret = "{\"AppId\":%s,\"ResultCode\":%d,\"ResultMsg\":\"%s\",\"Sign\":\"%s\",\"Content\":\"%s\"}";
    	String minwen = "";
    	minwen += appid;
    	minwen += retcode;
    	minwen += params.getAppKeyById(appid);
    	String miwen = StringEncrypt.Encrypt(minwen);
    	ret = String.format(ret, appid, retcode, retmsg, miwen, content);
		return ret;
	}

	public static void init()
	{
		params.addApp("qunying", "3320324", "QxAC2fdoMMFc6zBTLMd3xTn9HjOwxObS");	//群英i7CrpGa8z22dC5kPxKD15Grm
		params.addApp("qunying", "3354304", "y1vDW4vYZel208zwTIOvPnDXIiDnO3N2");	//群英wTxGiuZbMAfYjHIHBVUSuGgG
		params.addApp("m5", "3488535", "XSZlWP4Z4GLnUanGd9QfWClgrMjsGwNO");	//群英IVtKlKHjZiCv45NPP2GtM9Ek
	}
}

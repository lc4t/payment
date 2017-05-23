package noumena.payment.itools;

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

public class CiToolsCharge
{
	private static CiToolsParams params = new CiToolsParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		CiToolsCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_ITOOLS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_ITOOLS;
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
	
	public static String getCallbackFromiTools(Map<String,String> itoolsparams)
	{
		String orderid = "0";
    	String ret = "success";
    	
    	String notifyData = itoolsparams.get("notify_data");
    	String sign = itoolsparams.get("sign");

		boolean verified = false;
		String notifyJson = "";

		try
		{
			//公钥RSA解密后的json字符串
			//解密后的json格式: {"order_id_com":"2013050900000712","user_id":"10010","amount":"0.10","account":"test001","order_id":"2013050900000713","result":"success"}
			notifyJson = RSASignature.decrypt(notifyData);

			//公钥对数据进行RSA签名校验
			verified = RSASignature.verify(notifyJson, sign);
		}
		catch (Exception e)
		{
			ret = "fail";
			e.printStackTrace();
		}
		
		if (verified)
		{
			JSONObject json = JSONObject.fromObject(notifyJson);
			CiToolsOrderVO ordervo = (CiToolsOrderVO) JSONObject.toBean(json, CiToolsOrderVO.class);

			OrdersBean bean = new OrdersBean();
			orderid = ordervo.getOrder_id_com();
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
						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getOrder_id(), ordervo.getAmount(), ordervo.getUser_id());
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("itools order (" + order.getOrderId() + ") had been succeed");
					}
				}
				else
				{
					ret = "fail";
				}
			}
			catch (Exception e)
			{
				e.printStackTrace();
				ret = "fail";
			}
		}

		System.out.println("itools cb ->" + notifyJson);
		System.out.println("itools cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/itoolscb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, itoolsparams.toString() + "<>" + notifyJson);
		
		return ret;
	}

	public static void init()
	{
		params.addApp("xixuegui", "405", "FC1E59CE7F5E3C8BF079393498759561");	//吸血鬼日记
		params.addApp("m5", "460", "E396BD1CEE947247167ECD0B6E3D367C");			//M5
		params.addApp("qunying", "486", "E396BD1CEE947247167ECD0B6E3D367C");	//三国群英
		params.addApp("qzhuan", "11059", "45A0A90F6294AE46CAABEE986CC0BDA0");	//群英Q传
		params.addApp("gongzhu", "776", "3DC8AE9F62DACED805CC729BB70382DC");	//小青蛙
	}
}

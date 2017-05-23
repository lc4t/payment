package noumena.payment.lenovo;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.lenovo.tools.CpTransSyncSignValidNew;
import noumena.payment.lenovo.util.CpTransSyncSignValid;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class LenovoCharge
{
	private static LenovoParams params = new LenovoParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		LenovoCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_LENOVO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_LENOVO;
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

	public static String getCallbackFromLenovo(String transdata, String sign)
	{
		String ret = LenovoParams.SUCCESS;
		try
		{
			if (transdata == null || "".equalsIgnoreCase(transdata))
			{
				return LenovoParams.FAILURE;
			}
			if (sign == null || "".equalsIgnoreCase(sign))
			{
				return LenovoParams.FAILURE;
			}

			OrdersBean bean = new OrdersBean();
			JSONObject json = JSONObject.fromObject(transdata);
			LenovoOrderVO ordervo = (LenovoOrderVO) JSONObject.toBean(json, LenovoOrderVO.class);
			Orders order = bean.qureyOrder(ordervo.getExorderno());
			
			if (order != null)
			{
				boolean isvlid = validMessage(transdata, sign, ordervo.getAppid());
	
				if (!isvlid)
				{
					return LenovoParams.FAILURE;
				}
				else
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						order.setMoney(ordervo.getMoney());
						order.setPayId(ordervo.getTransid());
						bean.updateOrderAmountPayId(order.getOrderId(), order.getPayId(), order.getMoney());
						
						if (ordervo.getResult().equals("0"))
						{
							//支付成功
							bean.updateOrderKStatus(ordervo.getExorderno(), Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							//支付失败
							bean.updateOrderKStatus(ordervo.getExorderno(), Constants.K_STSTUS_ERROR);
						}
					}
					else
					{
						System.out.println("lenovo order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/lenovocb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + ordervo.getExorderno() + "_" + ordervo.getTransid();
				
				String res = "";
				res += "transdata=" + transdata;
				res += "&sign=" + sign;
				OSUtil.saveFile(filename, res);
			}
			else
			{
				ret = "ILLEGAL ORDER";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = LenovoParams.FAILURE;
		}
		return ret;
	}

	private static boolean validMessage(String transdata, String sign, String appid) throws Exception
	{
		boolean flag;
		String key = params.getParams(appid).getAppkey();
		String validSign = params.getParams(appid).getValidSign();
		if ("new".equals(validSign)) 
		{
			flag = CpTransSyncSignValidNew.validSign(transdata, sign, key);
		}
		else
		{
			flag = CpTransSyncSignValid.validSign(transdata, sign, key);
		}
		return flag;
	}
	
	public static void init()
	{
		params.initParams(LenovoParams.CHANNEL_ID, new LenovoParamsVO());
//		params.addApp("m1", "20014000000001200140", "REU5NDAzREMwMUU4ODQ4OEJEQTVFNzUyQ0RCQUM1QjIyM0UwNzVEN01USXdOelkyTnprMk1ESTRNRFUzTkRReU9URXJNalk0TWpRNU1EZzVOekE0TVRJME1qQXdNakF3TVRFMk1EUXlNRFl6T1RRME1EQTBPRFkz");
////		params.addApp("t6", "20014000000002200140", "QjNENEZBQkIyODg0ODFCQTNFRjI0RjFDMEE2OEE4RDAxRUNFOUE5N01UUXhOelV5T0RBek5UWXpORGN5TlRjNU1Ea3JNVFV4TXpjeU1UZzVPRGd6TkRRME9EQXhOelUwTnpBM016WXpNREUzTVRBeU5qSTVOVGd6");
//		params.addApp("t6", "20014000000003200140", "MTJFNjExNUQ5NzdEQzYzRjczODZBMDBCRTMyQjlDQUFERjE0NDBDRk9UVTVOell6TkRBME9EVXdPREV4TURBMk15c3hNVEk0TXpZMk9EQXdNRFF3TWpVME9Ea3pORFF6TmpBNE56YzNPRGswT0RRek1qazRORGM9");
//		params.addApp("hero", "20014000000004200140", "ODFCOTNCNUY2NEQ0OTk2RjAwNTE5RUQ2MEJBOTNFNDUzQjI4MzgxRE1USXdPRFUxTWpNNU5EQTFORGs0TmpZeE16Y3JNVFkzT0RVek56Z3hORGcyTmprNE56WTBOVEkxTURnMU5qRTBORGN6TkRFeU5EZ3lOams1");
	}
}

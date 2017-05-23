package noumena.payment.yingyonghui;

import java.util.ArrayList;
import java.util.Calendar;
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
import noumena.payment.yingyonghui.tools.CpTransSyncSignValid;

public class YingyonghuiCharge
{
	private static YingyonghuiParams params = new YingyonghuiParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		YingyonghuiCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_YINGYONGHUI;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_YINGYONGHUI;
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
	
	public static String getCallbackFromYingyonghui(String transdata, String sign)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yingyonghui cb transdata)->" + transdata);
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yingyonghui cb sign)->" + sign);
		
    	String ret = "SUCCESS";   
    	
    	try {
    		if (transdata == null || "".equalsIgnoreCase(transdata))
			{
    			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yingyonghui cb)->transdata is null");
				return "FAILURE";
			}
			if (sign == null || "".equalsIgnoreCase(sign))
			{
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yingyonghui cb)->sign is null");
				return "FAILURE";
			}
			JSONObject json = JSONObject.fromObject(transdata);
			YingyonghuiOrderVO ordervo = (YingyonghuiOrderVO)JSONObject.toBean(json,YingyonghuiOrderVO.class);
			String orderid = ordervo.getExorderno();
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				boolean isvlid = validMessage(transdata, sign, ordervo.getAppid());
				
				if (!isvlid)
				{
					 //服务器签名验证失败
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yingyonghui cb)-> validSign is failed ");
					return "FAILURE";
				}
				else
				{
					//服务器签名验证成功
					if (ordervo.getResult().equals("0"))
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getTransid(), ordervo.getMoney(), ordervo.getFeetype() + "#" + ordervo.getTranstype());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yingyonghui cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
					else
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(yingyonghui cb) result="+ordervo.getResult());
					}
					
				}
				
				String path = OSUtil.getRootPath() + "../../logs/yingyonghuicb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				String res = "transdata=" + transdata + "&sign=" + sign;
				OSUtil.saveFile(filename, res);
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	return ret;
	}
	
	private static boolean validMessage(String transdata, String sign, String appid) throws Exception
	{
		boolean flag;
		String key = params.getParams(appid).getAppkey();
		flag = CpTransSyncSignValid.validSign(transdata, sign, key);
		
		return flag;
	}
	
	public static void init()
	{
		params.initParams(YingyonghuiParams.CHANNEL_ID, new YingyonghuiParamsVO());
	}
}

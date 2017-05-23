package noumena.payment.c37wan;

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
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class C37wanCharge
{
	private static C37wanParams params = new C37wanParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		C37wanCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_37WAN;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_37WAN;
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
	
	public static String getCallbackFrom37wan(Map<String,String> c37wanparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(c37wan cb params)->" + c37wanparams.toString());
		
    	String str = "{\"state\":\"%s\",\"data\":\"null\",\"msg\":\"%s\"}";
    	
    	String ret = String.format(str, "1","成功");
    	
    	JSONObject json = JSONObject.fromObject(c37wanparams);
    	C37wanOrderVO ordervo = (C37wanOrderVO)JSONObject.toBean(json,C37wanOrderVO.class);
    	
    	String orderid = ordervo.getDoid();
    	String appid = ordervo.getGid();
    	String minwen = "";
    	String miwen = "";
    	
    	minwen = ordervo.getTime();
    	minwen += params.getParams(appid).getAppkey();
    	minwen += ordervo.getOid();
    	minwen += orderid;
    	minwen += ordervo.getDsid();
    	minwen += ordervo.getUid();
    	minwen += ordervo.getMoney();
    	minwen += ordervo.getCoin();
    	
    	miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
    	
    	if (miwen.equals(ordervo.getSign()))
		{
			//服务器签名验证成功
			OrdersBean bean = new OrdersBean();
			try {
				// 支付成功
				Orders order = bean.qureyOrder(orderid);
				if (order != null) {
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
					{
						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getOid(), ordervo.getMoney(), ordervo.getUid());
						bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
					} 
					else 
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(c37wan) order (" + order.getOrderId()+ ") had been succeed");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
		    	ret = String.format(str, "0","失败");
			}
		
		}
		else
		{
			// 服务器签名验证失败
	    	ret = String.format(str, "0","失败");
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(c37wan cb)->(appid=" + ordervo.getGid()+",content="+ minwen +",sign="+ miwen);
		}
    	
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(c37wan cb ret)->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/c37wancb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, c37wanparams.toString());
		
		return ret;
	}

	public static void init()
	{
		params.initParams(C37wanParams.CHANNEL_ID, new C37wanParamsVO());
//		params.addApp("M5", "1001122", "Jov3A7L-tXd6kV*0ZBYyUiEFzRPrDjcK"); //真！吞食天地
//		params.addApp("gongzhu", "1001123", "M8l2N56zVIG3HLSiyQc;Ztrjp9Fxq.Y1"); //小青蛙
//		params.addApp("M3", "1001124", "6HqzkOD$pT/gPBVYtwxm5jGFyidsrlKn"); //正义红师
//		params.addApp("qunying", "1001125", "N4IQqzo6iZrl;sSpw2WB70GnJhXdkf9+"); //群英Q传
	}
	
	public static void main(String args[]){
		/**
		 * (appid=1002392,
		 * content=1458614614qYBCS+3oQPOpr7yNM0bGgWa*skxFHfDv37B0C72AA53A6C46DD54521D8733F15120160322104318262Fj0113691899706.0060
		 * ,sign=f4c619adc72abc929412c3619259bec0
		 * 
		 * 3aa58429c7eddf6cd9bfff4e3a24bf39
		 */
		String a = StringEncrypt.Encrypt("1458614614L4R6q3+QxENKBJfXuMCeWd;5cFarAm8937B0C72AA53A6C46DD54521D8733F15120160322104318262Fj0113691899706.0060").toLowerCase();
		System.out.println(a);
	}
}

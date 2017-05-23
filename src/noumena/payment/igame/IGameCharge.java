package noumena.payment.igame;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

public class IGameCharge
{
	private static IGameParams params = new IGameParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		IGameCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_IGAME;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_IGAME;
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
	
	public static String getCallbackFromIGame(Map<String,String> igameparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb params)->" + igameparams.toString());
		
    	String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>";   
    	ret += "<cp_notify_resp>";
    	ret += "<h_ret>%s</h_ret>";
    	ret += "<cp_order_id>%s</cp_order_id>";
    	ret += "</cp_notify_resp>";
    	
    	String h_ret = "0";
    	String orderid = "";
    	try {
			JSONObject json = JSONObject.fromObject(igameparams);
			IGameOrderVO ordervo = (IGameOrderVO)JSONObject.toBean(json,IGameOrderVO.class);
			orderid = ordervo.getCp_order_id();
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
			if (order != null) 
			{
				if (ordervo.getResult_code().equals("00"))
				{
					String appid = order.getExInfo();
					String minwen = "";
					String miwen = "";
					
					minwen += orderid;
					minwen += ordervo.getCorrelator();
					minwen += ordervo.getResult_code();
					minwen += ordervo.getFee();
					minwen += ordervo.getPay_type();
					minwen += ordervo.getMethod();
					minwen += params.getParams(appid).getAppkey();
					
					miwen = StringEncrypt.Encrypt(minwen);
					
					if (miwen.equals(ordervo.getSign()))
					{
						//服务器签名验证成功
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getCorrelator(), ordervo.getFee(), ordervo.getPay_type());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
					else
					{
						// 服务器签名验证失败
						h_ret = "-1";
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
					}
				}
				else
				{
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb) result_code is "+ordervo.getResult_code());
				}
				
				String path = OSUtil.getRootPath() + "../../logs/igamecb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, igameparams.toString());
			}	
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	ret = String.format(ret, h_ret, orderid);
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb ret)->" + ret);		
    	
		return ret;
	}
	
	public static String checkOrder(Map<String,String> igameparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb check params)->" + igameparams.toString());
		
		String if_pay="0";
		String orderid = "";
		String correlator = "";
		String game_account = "";
		String fee = "";
		String order_time = ""; 
		try {
			orderid = igameparams.get("cp_order_id");
			correlator = igameparams.get("correlator");
			
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			if (order!=null) 
			{
				SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
				order_time = df.format(new Date());
				game_account = order.getUId();
				fee = order.getAmount()+"";
				String appid = order.getExInfo().split("#")[0];
				String content = orderid;
				content += correlator;
				content += igameparams.get("order_time");
				content += igameparams.get("method");
				content += params.getParams(appid).getAppkey();
				String sign = StringEncrypt.Encrypt(content);
				
				if (sign.equals(igameparams.get("sign")))
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						if_pay = "0";
					}
					else
					{
						if_pay = "-1";
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb check)->order (" + order.getOrderId()+ ") had been succeed" );
					}
				}
				else
				{
					if_pay = "-1";
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb check)->sign is error" );
				}
			}
			else 
			{
				if_pay = "-1";
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb check)->order(" +orderid+") is unexist" );
			}			
		} 
		catch (Exception e) {
			e.printStackTrace();
		}

		String ret = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>";   
		ret += "<sms_pay_check_resp>";
		ret += "<cp_order_id>" + orderid + "</cp_order_id>";
		ret += "<correlator>" + correlator + "</correlator>";
		ret += "<game_account>" + game_account + "</game_account>";
		ret += "<fee>" + fee + "</fee>";
		ret += "<if_pay>" + if_pay + "</if_pay>";
		ret += "<order_time>" + order_time + "</order_time>";
		ret += "</sms_pay_check_resp>";
		
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(igame cb check ret)->"+ret );
		
		return ret;
	}

	public static void init()
	{
		params.initParams(IGameParams.CHANNEL_ID, new IGameParamsVO());
	}
}

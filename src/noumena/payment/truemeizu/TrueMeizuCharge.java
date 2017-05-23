package noumena.payment.truemeizu;

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

public class TrueMeizuCharge
{
	private static TrueMeizuParams params = new TrueMeizuParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		TrueMeizuCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_MEIZU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_MEIZU;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setOrderId(payId);
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		long time = System.currentTimeMillis()/1000;
		String sign = getSign(order,time);
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		orderIdVO.setMsg(sign);
		orderIdVO.setToken(time+"");
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
	
	public static String getCallbackFromMeizu(Map<String,String> meizuparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(meizu cb params)->" + meizuparams.toString());
		
    	String str = "{\"code\":\"%s\",\"redirect\":\"\",\"value\":\"\",\"msg\":\"%s\"}";
    	
    	String ret = String.format(str, "200","成功");
    	
    	TrueMeizuOrderVO ordervo = new TrueMeizuOrderVO();
    	ordervo.setApp_id(meizuparams.get("app_id"));
    	ordervo.setBuy_amount(meizuparams.get("buy_amount"));
    	ordervo.setCp_order_id(meizuparams.get("cp_order_id"));
    	ordervo.setCreate_time(meizuparams.get("create_time"));
    	ordervo.setNotify_id(meizuparams.get("notify_id"));
    	ordervo.setNotify_time(meizuparams.get("notify_time"));
    	ordervo.setOrder_id(meizuparams.get("order_id"));
    	ordervo.setPartner_id(meizuparams.get("partner_id"));
    	ordervo.setPay_time(meizuparams.get("pay_time"));
    	ordervo.setPay_type(meizuparams.get("pay_type"));
    	ordervo.setProduct_id(meizuparams.get("product_id"));
    	ordervo.setProduct_per_price(meizuparams.get("product_per_price"));
    	ordervo.setProduct_unit(meizuparams.get("product_unit"));
    	ordervo.setSign(meizuparams.get("sign"));
    	ordervo.setSign_type(meizuparams.get("sign_type"));
    	ordervo.setTotal_price(meizuparams.get("total_price"));
    	ordervo.setTrade_status(meizuparams.get("trade_status"));
    	ordervo.setUid(meizuparams.get("uid"));
    	ordervo.setUser_info(meizuparams.get("user_info"));
    	
    	String orderid = ordervo.getCp_order_id();
    	String appid = ordervo.getApp_id();
    	String minwen = "";
    	String miwen = "";
    	/*
    	 * app_id=464013&buy_amount=1&cp_order_id=2680&create_time=1413776092239&notify_id=1413776
			113206&notify_time=2014-10-20 11:35:13&order_id=14102000000298934&partner_id=5458428&
			pay_time=1413776113219&pay_type=0&product_id=2&product_per_price=1.0&product_unit= 个
			&total_price=1.0&trade_status=3&uid=9700193&user_info=其它回传附加信息:appSecret
    	 */
    	minwen += "app_id="+ ordervo.getApp_id() +"&";
    	minwen += "buy_amount="+ ordervo.getBuy_amount() +"&";
    	minwen += "cp_order_id="+ ordervo.getCp_order_id() +"&";
    	minwen += "create_time="+ ordervo.getCreate_time() +"&";
    	minwen += "notify_id="+ ordervo.getNotify_id() +"&";
    	minwen += "notify_time="+ ordervo.getNotify_time() +"&";
    	minwen += "order_id="+ ordervo.getOrder_id() +"&";
    	minwen += "partner_id="+ ordervo.getPartner_id() +"&";
    	minwen += "pay_time="+ ordervo.getPay_time() +"&";
    	minwen += "pay_type="+ ordervo.getPay_type() +"&";
    	minwen += "product_id="+ ordervo.getProduct_id() +"&";
    	minwen += "product_per_price="+ ordervo.getProduct_per_price() +"&";
    	minwen += "product_unit="+ ordervo.getProduct_unit() +"&";
    	minwen += "total_price="+ ordervo.getTotal_price() +"&";
    	minwen += "trade_status="+ ordervo.getTrade_status() +"&";
    	minwen += "uid="+ ordervo.getUid() +"&";
    	minwen += "user_info="+ ordervo.getUser_info() +":";
    	minwen += params.getParams(appid).getAppkey();
    	
    	miwen = StringEncrypt.Encrypt(minwen);
    	if (miwen.equals(ordervo.getSign()) && "3".equals(ordervo.getTrade_status()))
		{
			//服务器签名验证成功
			OrdersBean bean = new OrdersBean();
			try {
				// 支付成功
				Orders order = bean.qureyOrder(orderid);
				if (order != null) {
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
					{
						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getOrder_id(), ordervo.getTotal_price(), ordervo.getUid());
						bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
					} 
					else 
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(meizu) order (" + order.getOrderId()+ ") had been succeed");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
		    	ret = String.format(str, "900000","失败");
			}
		
		}
		else
		{
			// 服务器签名验证失败
	    	ret = String.format(str, "120014","失败");
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(truemeizu cb)->(appid=" + ordervo.getApp_id()+",content="+ ordervo.getSign() +",sign="+ miwen);
		}
    	
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(truemeizu cb ret)->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/truemeizucb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, meizuparams.toString());
		
		return ret;
	}
	
	public static String getSign(Orders order,long time){
		/*
		 * app_id=464013&buy_amount=1&cp_order_id=2680&create_time=139868782768&pay_type=0&product_body=
		&product_id=0&product_per_price=1.0&product_subject= 购 买 500 枚 金 币 &product_unit=
		&total_price=1.0&uid=5535004&user_info=:appSecret
		 */
		String sign = "";
		String miwen = "";
		miwen += "app_id="+ order.getSign() +"&";
		miwen += "buy_amount="+ order.getItemNum() +"&";
		miwen += "cp_order_id="+ order.getOrderId() +"&";
		miwen += "create_time="+ time +"&";
		miwen += "pay_type=0&";
		miwen += "product_body=&";
		miwen += "product_id="+ order.getItemId() +"&";
		miwen += "product_per_price="+ order.getAmount().intValue() +"&";
		miwen += "product_subject=" + order.getExInfo() +"&";
		miwen += "product_unit=个&";
		miwen += "total_price="+ order.getAmount().intValue() +"&";
		miwen += "uid="+ order.getUId()+"&";
		miwen += "user_info=:"+params.getParams(order.getSign()).getAppkey();
		System.out.println("meizu:"+miwen);
		sign = StringEncrypt.Encrypt(miwen);
		System.out.println("meizu:"+sign);
		return sign;
	}

	public static void init()
	{
		params.initParams(TrueMeizuParams.CHANNEL_ID, new TrueMeizuParamsVO());
//		params.addApp("M5", "1001122", "Jov3A7L-tXd6kV*0ZBYyUiEFzRPrDjcK"); //真！吞食天地
//		params.addApp("gongzhu", "1001123", "M8l2N56zVIG3HLSiyQc;Ztrjp9Fxq.Y1"); //小青蛙
//		params.addApp("M3", "1001124", "6HqzkOD$pT/gPBVYtwxm5jGFyidsrlKn"); //正义红师
//		params.addApp("qunying", "1001125", "N4IQqzo6iZrl;sSpw2WB70GnJhXdkf9+"); //群英Q传
	}
	public static void main(String args[]){
		System.out.println(DateUtil.getCurTimeStr());
		String a = StringEncrypt.Encrypt("app_id=3083385&buy_amount=10&cp_order_id=20161124185612442017&create_time=1479984972&pay_type=0&product_body=&product_id=000001&product_per_price=1&product_subject=商品描述&product_unit=个&total_price=1&uid=131727482&user_info=meizu:IaUYniHUa42WprSYlcfKo35bPItMdhFT");
		System.out.println(a);
	}
}

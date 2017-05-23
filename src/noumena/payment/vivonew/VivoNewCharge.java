package noumena.payment.vivonew;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.HttpUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class VivoNewCharge
{
	private static VivoNewParams params = new VivoNewParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		VivoNewCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order, String orderTitle, String orderDesc)
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
				cburl += "?pt=" + Constants.PAY_TYPE_VIVONEW;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_VIVONEW;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		String orderinfo = getOrderInfoFromVivo(order, orderTitle, orderDesc);
		orderIdVO.setMsg(orderinfo);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	private static String getOrderInfoFromVivo(Orders order, String orderTitle, String orderDesc)
	{
		//String appid = "0";	//所有app公用一套参数
		String appid = order.getSign();
		String urlstr = "https://pay.vivo.com.cn/vcoin/trade";
		String content = "";
		String ret = "";
		String orderinfo = "{\"vivoSignature\":\"%s\",\"vivoOrder\":\"%s\"}";
		String retinfo = String.format(orderinfo, "", "");
		Map<String,String> para = new HashMap<String,String>();

		try
		{
			String payprice = new DecimalFormat("0").format(new Float(order.getAmount()));
			String callback = "";
			if (testmode) {
				callback = VivoNewParams.CALLBACK_URL_TEST;
			}
			else {
				callback = VivoNewParams.CALLBACK_URL_RELEASE;
			}
			
			para.put("version", VivoNewParams.VIVO_VER);
			para.put("signMethod", VivoNewParams.VIVO_SIGN);
			para.put("cpId", params.getParams(appid).getCp_id());
			para.put("appId", order.getSign());
			para.put("cpOrderNumber", order.getOrderId());
			para.put("notifyUrl", callback);
			para.put("orderTime", DateUtil.getCurTimeStr());
			para.put("orderAmount", payprice);
			para.put("orderTitle", orderTitle);
			para.put("orderDesc", orderDesc);
			para.put("extInfo", "new");
			
			String sign = VivoNewSignUtils.getVivoSign(para, params.getParams(appid).getCp_key());
			
			content += "version=" + VivoNewParams.VIVO_VER;
			content += "&signMethod=" + VivoNewParams.VIVO_SIGN;
			content += "&signature=" + sign;
			content += "&cpId=" + params.getParams(appid).getCp_id();
			content += "&appId=" + order.getSign();
			content += "&cpOrderNumber=" + order.getOrderId();
			content += "&notifyUrl=" + URLEncoder.encode(callback, "utf-8");
			content += "&orderTime=" + DateUtil.getCurTimeStr();
			content += "&orderAmount=" + payprice;
			content += "&orderTitle=" + URLEncoder.encode(orderTitle, "utf-8");
			content += "&orderDesc=" + URLEncoder.encode(orderDesc, "utf-8");
			content += "&extInfo=new"; //透传参数

			System.out.println("vivonew get order info content ->" + content);
			
			ret = HttpUtil.doHttpsPost(urlstr, content);

			System.out.println("vivonew get order info ret ->" + ret);
			
			JSONObject json = JSONObject.fromObject(ret);
			String code = json.getString("respCode");
			if (code.equals("200"))
			{
				retinfo = String.format(orderinfo, json.getString("accessKey"), json.getString("orderNumber"));
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return retinfo;
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
	
	public static String getCallbackFromVivoNew(Map<String,String> vivonewparams)
	{
		System.out.println("vivonew cb info->" + vivonewparams.toString());
		String ret = "ok";
		
		String appid = vivonewparams.get("appId");
//		String respCode = vivoparams.get("respCode");
    	String orderid = vivonewparams.get("cpOrderNumber");
    	String sporderid = vivonewparams.get("orderNumber");
    	String orderAmount = vivonewparams.get("orderAmount");
    	String uid = vivonewparams.get("uid");
    	String tradeStatus = vivonewparams.get("tradeStatus");
    	
		if (tradeStatus != null && tradeStatus.equals("0000"))
		{
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);

			if (order != null)
			{
				if (VivoNewSignUtils.verifySignature(vivonewparams, params.getParams(appid).getCp_key()) == true)
				{
					try
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(orderid, sporderid, orderAmount, uid);
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("vivonew order (" + order.getOrderId() + ") had been succeed");
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else
				{
					System.out.println("vivonew cb verifySignature is failed");
				}
			}
		}
		System.out.println("vivonew cb ret-->"+ret);
		String path = OSUtil.getRootPath() + "../../logs/vivonewcb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, vivonewparams.toString());
		
		return ret;
	}

	public static void init()
	{
		params.initParams(VivoNewParams.CHANNEL_ID, new VivoNewParamsVO());
	}
}

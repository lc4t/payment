package noumena.payment.apus;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.caishen.CaishenOrderVO;
import noumena.payment.lenovo.util.Base64;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.util.TrustAnyTrustManager;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class ApusCharge
{
	private static ApusParams params = new ApusParams();
	
	public static String getTransactionId(Orders order)
	{
		//order.setCurrency(Constants.CURRENCY_USD);
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
				cburl += "?pt=" + Constants.PAY_TYPE_APUS;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_APUS;
			}
			//cburl += "&currency=" + Constants.CURRENCY_USD;
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
	
	public static String getCallbackFromApus(Map<String,String> apusparams)
	{
		String ret = "{\"code\":\"SUCCESS\"}";
		System.out.println("apus cb ->" + apusparams.toString());
		ApusOrderVO covo = new ApusOrderVO();
		covo.setCode(apusparams.get("code"));
		covo.setNotify_id(apusparams.get("notify_id"));
		covo.setSign(apusparams.get("sign"));
		covo.setSign_type(apusparams.get("sign_type"));
		covo.setAccount_id(apusparams.get("account_id"));
		covo.setTransaction_id(apusparams.get("transaction_id"));
		covo.setTrack_id(apusparams.get("track_id"));
		covo.setPayment_type(apusparams.get("payment_type"));
		covo.setTransaction_type(apusparams.get("transaction_type"));
		covo.setTransaction_status(apusparams.get("transaction_status"));
		covo.setAmt(apusparams.get("amt"));
		covo.setTopup_amt(apusparams.get("topup_amt"));
		covo.setTopup_currency(apusparams.get("topup_currency"));
		covo.setCurrency_code(apusparams.get("currency_code"));
		covo.setApp_id(apusparams.get("app_id"));
		covo.setProduct_id(apusparams.get("product_id"));
		covo.setGmt_create(apusparams.get("gmt_create"));
		covo.setGmt_finished(apusparams.get("gmt_finished"));
		covo.setPrivate_data(apusparams.get("private_data"));
		covo.setMessage(apusparams.get("message"));
		
		if("SUCCESS".equals(covo.getCode()) && "TRADE_SUCCESS".equals(covo.getTransaction_status())){
			String sign = StringEncrypt.Encrypt(getSignStr(apusparams));
			OrdersBean bean = new OrdersBean();
				try {
					if(sign.equals(apusparams.get("sign"))){
						// 支付成功
						Orders order = bean.qureyOrder(covo.getTrack_id());
						if (order != null) {
							if (order.getIscallback() == Constants.CALLBACK_ON){
								CallbackBean callbackBean = new CallbackBean();
								Callback callbackvo = callbackBean.qureyCallback(covo.getTrack_id());
								String cburl = callbackvo.getCallbackUrl();
								if (cburl != null && !cburl.equals("")){
									if("topup".equals(covo.getTransaction_type())){
										if (cburl.indexOf("?") == -1){
											cburl += "?currency=" + covo.getTopup_currency();
										}else{
											cburl += "&currency=" + covo.getTopup_currency();
										}
									}else{
										if (cburl.indexOf("?") == -1){
											cburl += "?currency=" + covo.getCurrency_code();
										}else{
											cburl += "&currency=" + covo.getCurrency_code();
										}
									}
									callbackvo.setCallbackUrl(cburl);
									callbackBean.updateCallback(callbackvo);
								}
							}
								if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
									order.setPayId(covo.getTransaction_id());
									if("topup".equals(covo.getTransaction_type())){
										order.setMoney(covo.getTopup_amt());
										order.setCurrency(covo.getTopup_currency());
									}else{
										order.setMoney(covo.getAmt());
										order.setCurrency(covo.getCurrency_code());
									}
									order.setUpdateTime(DateUtil.getCurrentTime());
									order.setExInfo(covo.getPayment_type());
									bean.updateOrder(covo.getTrack_id(), order);
									//bean.updateOrderAmountPayIdExinfo(covo.getTrack_id(), covo.getTransaction_id(), covo.getAmt(), "");
									bean.updateOrderKStatus(covo.getTrack_id(),Constants.K_STSTUS_SUCCESS);
									//bean.updateOrderExInfo(covo.getTrack_id(), covo.getPayment_type());
								} else {
									System.out.println("apus order ("+ order.getOrderId() + ") had been succeed");
								}
						}
					}else{
						System.out.println("apus order ("+ covo.getTrack_id() + ") sign is error");
						ret = "{\"code\":\"ILLEGAL_SIGN\"}";
					}
				} catch (Exception e) {
					e.printStackTrace();
					ret = "{\"code\":\"ERROR\"}";
				}
		}else{
			System.out.println("apus order ("+ covo.getTrack_id() + ") is fail.status:"+covo.getTransaction_status()+"  code:"+covo.getCode());
		}
			System.out.println("apus cb ret->" + ret);

			String path = OSUtil.getRootPath() + "../../logs/apuscb/"
					+ DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + covo.getTrack_id();

			OSUtil.saveFile(filename, apusparams.toString());
		return ret;
	}
	
	public static String getSignStr(Map<String, String> signparams) {
		StringBuilder content = new StringBuilder();
		List<String> keys = new ArrayList<String>(signparams.keySet());
		Collections.sort(keys);
		for(int i = 0; i<keys.size(); i++) {
			String key = keys.get(i);
			if("sign".equals(key) || "sign_type".equals(key)) {
				continue;
			}
			String value = signparams.get(key);
			if(value != null) {
				content.append((i == 0 ? "" : "&") + key + "=" + value);
			} else {
				content.append((i == 0 ? "" : "&") + key + "=");
	}
	}
	content.append("&secret_key=" + params.getParams(signparams.get("app_id")).getAppkey());
	return content.toString();
	}

	
	public static void init()
	{
		params.initParams(ApusParams.CHANNEL_ID, new ApusParamsVO());
	}
	
	public static void main(String args[]){//0c8865930fc94d8d61b1bdcc8312a65d
		String res="";
		try {
			URL	url = new URL("http://game.jinggle.net:8080/payment/kongzhong/notice?orderid=J201617370802000001&goodid=SHIPHUNTER_DIAMOND_01&goodname=花费6购买60钻石&serverid=200&userid=62733206723035136&pt=5049&currency=RMB&unit=100&status=1&payId=2016062217371432508Z&flowid=2016062221001004650271412320&payrealprice=6.00");
			System.out.println(url);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setUseCaches(false);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream())
				);
			String line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			connection.disconnect();
			
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println(res);
	}

}

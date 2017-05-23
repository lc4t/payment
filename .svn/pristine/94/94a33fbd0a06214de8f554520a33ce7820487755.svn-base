package noumena.payment.mo9;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import com.mokredit.payment.Md5Encrypt;

public class Mo9Charge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		Mo9Charge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_MO9;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_MO9;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		orderIdVO.setMsg(getTradeUrl(order));
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	private static void getPayInfoByMemo(String memo, Mo9OrderVO vo)
	{
		try
		{
			//appid#地区#货币#道具名
			System.out.println("memo->" + memo);
			if (memo != null)
			{
				String[] ps = memo.split("#");
				if (ps.length >= 3)
				{
					vo.setApp_id(ps[0]);
					vo.setLc(ps[1]);
					vo.setCurrency(ps[2]);
					vo.setItem_name(URLDecoder.decode(ps[3], "utf-8"));
				}
			}
		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
	}
	
	private static String getTradeUrl(Orders order)
	{
		String url = "";
		try
		{
			Mo9OrderVO vo = new Mo9OrderVO();
			getPayInfoByMemo(order.getExInfo(), vo);
			String key = "";
			
			if (testmode == true)
			{
				if (vo.getLc().equals("CN"))
				{
					url = Mo9Params.MO9_CN_TRADE_URL_TEST;
					vo.setPay_to_email(Mo9Params.MO9_CN_ACCOUNT_TEST);
					key = Mo9Params.MO9_CN_KEY_TEST;
				}
				else
				{
					url = Mo9Params.MO9_WORLD_TRADE_URL_TEST;
					vo.setPay_to_email(Mo9Params.MO9_WORLD_ACCOUNT_TEST);
					key = Mo9Params.MO9_WORLD_KEY_TEST;
				}
				vo.setNotify_url(Mo9Params.MO9_CB_URL_TEST);
			}
			else
			{
				if (vo.getLc().equals("CN"))
				{
					url = Mo9Params.MO9_CN_TRADE_URL_RELEASE;
					vo.setPay_to_email(Mo9Params.MO9_CN_ACCOUNT_RELEASE);
					key = Mo9Params.MO9_CN_KEY_RELEASE;
				}
				else
				{
					url = Mo9Params.MO9_WORLD_TRADE_URL_RELEASE;
					vo.setPay_to_email(Mo9Params.MO9_WORLD_ACCOUNT_RELEASE);
					key = Mo9Params.MO9_WORLD_KEY_RELEASE;
				}
				vo.setNotify_url(Mo9Params.MO9_CB_RELEASE);
			}
			
			vo.setVersion("2.1");
			vo.setReturn_url("");
			vo.setInvoice(order.getOrderId());
			vo.setExtra_param(order.getItemId());
			String payeridpre = vo.getApp_id();
			if (!vo.getLc().toLowerCase().equals("cn") && !vo.getLc().toLowerCase().equals("us"))
			{
				payeridpre += vo.getLc();
			}
			if (order.getUId() == null || order.getUId().equals(""))
			{
				vo.setPayer_id(payeridpre + order.getImei());
			}
			else
			{
				vo.setPayer_id(payeridpre + order.getUId());
			}
			vo.setAmount("" + order.getAmount());
			
			String sign = Md5Encrypt.sign(getRequestParams(vo), key);
			
			vo.setSign(sign);
			
			url += "&pay_to_email=" + vo.getPay_to_email();
			url += "&version=" + vo.getVersion();
			//url += "&return_url=" + vo.getReturn_url();
			url += "&notify_url=" + vo.getNotify_url();
			url += "&invoice=" + vo.getInvoice();
			url += "&extra_param=" + URLEncoder.encode(vo.getExtra_param(), "utf-8");
			url += "&payer_id=" + vo.getPayer_id();
			url += "&lc=" + vo.getLc();
			url += "&amount=" + vo.getAmount();
			url += "&currency=" + vo.getCurrency();
			url += "&item_name=" + URLEncoder.encode(vo.getItem_name(), "utf-8");
			url += "&app_id=" + vo.getApp_id();
			url += "&sign=" + vo.getSign();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return url;
	}
	
	private static Map<String, String> getRequestParams(Mo9OrderVO vo)
	{
		Map<String, String> payParams= new HashMap<String, String>();
		payParams.put("pay_to_email", vo.getPay_to_email());
		payParams.put("version", vo.getVersion());
		//payParams.put("return_url", vo.getReturn_url());
		payParams.put("notify_url", vo.getNotify_url());
		payParams.put("invoice", vo.getInvoice());
		payParams.put("extra_param", vo.getExtra_param());
		payParams.put("payer_id", vo.getPayer_id());
		payParams.put("lc", vo.getLc());
		payParams.put("amount", vo.getAmount());
		payParams.put("currency", vo.getCurrency());
		payParams.put("item_name", vo.getItem_name());
		payParams.put("app_id", vo.getApp_id());
		return payParams;
	}
	
	private static Map<String, String> getResponseParams(Mo9OrderVO vo)
	{
		Map<String, String> payParams= new HashMap<String, String>();
		payParams.put("pay_to_email", vo.getPay_to_email());
		payParams.put("payer_id", vo.getPayer_id());
		payParams.put("trade_no", vo.getTrade_no());
		payParams.put("trade_status", vo.getTrade_status());
		payParams.put("sign", vo.getSign());
		payParams.put("amount", vo.getAmount());
		payParams.put("currency", vo.getCurrency());
		payParams.put("req_amount", vo.getReq_amount());
		payParams.put("req_currency", vo.getReq_currency());
		payParams.put("item_name", vo.getItem_name());
		payParams.put("lc", vo.getLc());
		payParams.put("invoice", vo.getInvoice());
		payParams.put("app_id", vo.getApp_id());
		payParams.put("extra_param", vo.getExtra_param());
		return payParams;
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

	public static String getCallbackFromMo9(Mo9OrderVO ordervo)
	{
		String ret = "OK";
		try
		{
			String key = "";
			if (testmode == true)
			{
				if (ordervo.getLc().equals("CN"))
				{
					key = Mo9Params.MO9_CN_KEY_TEST;
				}
				else
				{
					key = Mo9Params.MO9_WORLD_KEY_TEST;
				}
			}
			else
			{
				if (ordervo.getLc().equals("CN"))
				{
					key = Mo9Params.MO9_CN_KEY_RELEASE;
				}
				else
				{
					key = Mo9Params.MO9_WORLD_KEY_RELEASE;
				}
			}
			String sign = Md5Encrypt.sign(getResponseParams(ordervo), key);
			if (ordervo.getSign().equals(sign))
			{
				OrdersBean bean = new OrdersBean();
				
				Orders order = bean.qureyOrder(ordervo.getInvoice());
				if (order != null)
				{
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						order.setMoney(ordervo.getAmount());
						order.setPayId(ordervo.getTrade_no());
						bean.updateOrderAmountPayId(order.getOrderId(), order.getPayId(), order.getMoney());
						
						if (ordervo.getTrade_status().toUpperCase().equals("TRADE_SUCCESS"))
						{
							//支付成功
							System.out.println("mo9 cb amount->" + ordervo.getAmount());
							System.out.println("mo9 cb req_amount->" + ordervo.getReq_amount());
							
							//修改回调url，加上money参数表示实际支付金额
							CallbackBean callbackBean = new CallbackBean();
							Callback cbvo = callbackBean.qureyCallback(ordervo.getInvoice());
							if (cbvo != null)
							{
								String cburl = cbvo.getCallbackUrl();
								if (cburl.indexOf("?") < 0)
								{
									cburl += "?money=" + ordervo.getAmount() + "&orderId=" + ordervo.getTrade_no();
								}
								else
								{
									cburl += "&money=" + ordervo.getAmount() + "&orderId=" + ordervo.getTrade_no();
								}
								cbvo.setCallbackUrl(cburl);
								callbackBean.updateCallback(cbvo);
							}
							
							if (!ordervo.getAmount().equals(ordervo.getReq_amount()))
							{
								bean.updateOrderKStatus(ordervo.getInvoice(), Constants.K_STSTUS_NOTENOUGH);
							}
							else
							{
								bean.updateOrderKStatus(ordervo.getInvoice(), Constants.K_STSTUS_SUCCESS);
							}
						}
						else
						{
							//支付失败
							bean.updateOrderKStatus(ordervo.getInvoice(), Constants.K_STSTUS_ERROR);
						}
					}
					else
					{
						System.out.println("MO9 order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
					}
				}
				else
				{
					ret = "ILLEGAL ORDER";
				}
				
				String path = OSUtil.getRootPath() + "../../logs/mo9cb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + ordervo.getApp_id() + "_" + ordervo.getPayer_id() + "_" + ordervo.getInvoice();
				
				String res = "";
				res += "pay_to_email=" + ordervo.getPay_to_email();
				res += "&payer_id=" + ordervo.getPayer_id();
				res += "&trade_no=" + ordervo.getTrade_no();
				res += "&trade_status=" + ordervo.getTrade_status();
				res += "&sign=" + ordervo.getSign();
				res += "&amount=" + ordervo.getAmount();
				res += "&currency=" + ordervo.getCurrency();
				res += "&req_amount=" + ordervo.getReq_amount();
				res += "&req_currency=" + ordervo.getReq_currency();
				res += "&item_name=" + ordervo.getItem_name();
				res += "&lc=" + ordervo.getLc();
				res += "&extra_param=" + ordervo.getExtra_param();
				res += "&app_id=" + ordervo.getApp_id();
				res += "&invoice=" + ordervo.getInvoice();
				OSUtil.saveFile(filename, res);
			}
			else
			{
				ret = "ILLEGAL SIGN";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = "ILLEGAL";
		}
		return ret;
	}
}

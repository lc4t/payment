package noumena.payment.mol;

import java.util.ArrayList;
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
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class MOLCharge
{
	private static MOLParams params = new MOLParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		MOLCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order, String paytype)
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
				cburl += "?pt=" + paytype;
			}
			else
			{
				cburl += "&pt=" + paytype;
			}
			
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	private static String getPurchaseURL(Orders order)
	{
		String url = "";
		
		return url;
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
			if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(1);
			}
			else if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//订单没有结果
				st.setStatus(3);
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
	
	public static String getCallbackFromMOL(Map<String,String> molparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(mol cb params)->" + molparams.toString());
    	
    	try {
    		JSONObject json = JSONObject.fromObject(molparams);
        	MOLOrderVO ordervo = (MOLOrderVO)JSONObject.toBean(json,MOLOrderVO.class);
        	String orderid = ordervo.getReferenceId();
        	OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			
        	if (order != null) 
			{
        		if (order.getIscallback() == Constants.CALLBACK_ON) 
				{
					CallbackBean callbackBean = new CallbackBean();
					Callback callbackvo = callbackBean.qureyCallback(orderid);
					String cburl = callbackvo.getCallbackUrl();
					if (cburl != null && !cburl.equals(""))
					{
						if (cburl.indexOf("?") == -1)
						{
							cburl += "?currency=" + ordervo.getCurrencyCode();
						}
						else
						{
							cburl += "&currency=" + ordervo.getCurrencyCode();
						}
						cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
						
						callbackvo.setCallbackUrl(cburl);
						callbackBean.updateCallback(callbackvo);
					}
				}
				
        		String statusCode = ordervo.getPaymentStatusCode();
        		if (statusCode == null)
        		{
        			statusCode = "";
				}
				if (statusCode.equals("00")) //00-成功，01-未超时，02-超时，99-失败
				{
					String appid = ordervo.getApplicationCode();
					String channelId = "";
					String VirtualCurrencyAmount = "";
					if (ordervo.getChannelId() != null) 
					{
						channelId = ordervo.getChannelId();
					}
					if (json.getString("VirtualCurrencyAmount") != null)
					{
						VirtualCurrencyAmount = json.getString("VirtualCurrencyAmount");
					}
					String minwen = "";
					String miwen = "";
					
					minwen += ordervo.getAmount();
					minwen += appid;
					minwen += channelId;
					minwen += ordervo.getCurrencyCode();
					minwen += ordervo.getCustomerId();
					minwen += ordervo.getPaymentId();
					minwen += statusCode;
					minwen += ordervo.getPaymentStatusDate();
					minwen += orderid;
					minwen += ordervo.getVersion();
					minwen += VirtualCurrencyAmount;
					minwen += params.getParams(appid).getSecretkey();
					
					miwen = StringEncrypt.Encrypt(minwen);
					
					if (miwen.equals(ordervo.getSignature()))
					{
						//服务器签名验证成功
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							order.setPayId(ordervo.getPaymentId());
							order.setMoney(ordervo.getAmount());
							order.setExInfo(order.getExInfo() + "#" + ordervo.getChannelId());
							order.setCurrency(ordervo.getCurrencyCode());
							order.setUnit(Constants.CURRENCY_UNIT_FEN);
							
							bean.updateOrder(orderid, order);
//							bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getPaymentId(), ordervo.getAmount(), ordervo.getCurrencyCode()+"#"+ordervo.getChannelId());
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(mol cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
					else
					{
						// 服务器签名验证失败
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(mol cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
					}
				}
				else
				{
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(mol cb) paymentStatusCode = "+statusCode);
				}
				
				String path = OSUtil.getRootPath() + "../../logs/molcb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + orderid;
				
				OSUtil.saveFile(filename, molparams.toString());
			}	
		} 
    	catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}
	
	public static void updateHeartbeat()
	{
//		//请款SOAP请求
//		String merchantid = "";
//		if (testmode == true)
//		{
//			merchantid = MOLParams.MERCHANT_ID_TEST;
//		}
//		else
//		{
//			merchantid = MOLParams.MERCHANT_ID_RELEASE;
//		}
//		Heartbeat ws = new Heartbeat();
//        HeartbeatSoap port = ws.getHeartbeatSoap();
//        HeartBeatResult recvData = port.getHeartBeat(merchantid);
//        heartbeat = recvData.getHB();
	}
	
	public static void init()
	{
		params.initParams(MOLParams.CHANNEL_ID, new MOLParamsVO());
	}
}

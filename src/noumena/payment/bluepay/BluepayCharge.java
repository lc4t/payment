package noumena.payment.bluepay;

import java.util.ArrayList;
import java.util.Calendar;
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

public class BluepayCharge
{
	private static BluepayParams params = new BluepayParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		BluepayCharge.testmode = testmode;
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
//			if (cburl.indexOf("?") == -1)
//			{
//				cburl += "?pt=" + Constants.PAY_TYPE_BLUEPAY;
//			}
//			else
//			{
//				cburl += "&pt=" + Constants.PAY_TYPE_BLUEPAY;
//			}
//			cburl += "&currency=" + Constants.CURRENCY_THB;
//			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
			
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
	
	public static String getCallbackFromBluepay(Map<String,String> bluepayparams, String queryString)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(bluepay cb params)->" + bluepayparams.toString());
    	
    	String ret = "success";
    	
    	try {
			JSONObject json = JSONObject.fromObject(bluepayparams);
			BluepayOrderVO ordervo = (BluepayOrderVO)JSONObject.toBean(json,BluepayOrderVO.class);
			
			String cmd = ordervo.getCmd();
			
			//CHG - 已发出计费请求，CFM - 已确认计费结果
			if ("CFM".equals(cmd))
			{
				String orderid = ordervo.getT_id();
				
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
				
				if (order != null) 
				{
					//sms（短代）, cashcard（充值卡）
					String paytype = "";
					if (ordervo.getInterfacetype().equals("sms"))
					{
						if(ordervo.getOperator().equals("Line")){
							paytype = Constants.PAY_TYPE_BLUEPAY_LINE;
						}else{
							paytype = Constants.PAY_TYPE_BLUEPAY_SMS;
						}
					}
					else if(ordervo.getInterfacetype().equals("bank"))
					{
						paytype = Constants.PAY_TYPE_BLUEPAY_BANK;
					}else if(ordervo.getInterfacetype().equals("Line")){
						paytype = Constants.PAY_TYPE_BLUEPAY_LINE;
					}else{
						paytype = Constants.PAY_TYPE_BLUEPAY_CARD;
					}
					
					if (order.getIscallback() == Constants.CALLBACK_ON) 
					{
						CallbackBean callbackBean = new CallbackBean();
						Callback callbackvo = callbackBean.qureyCallback(orderid);
						String cburl = callbackvo.getCallbackUrl();
						if (cburl != null && !cburl.equals(""))
						{
							if (cburl.indexOf("?") == -1)
							{
								cburl += "?pt=" + paytype;
							}
							else
							{
								cburl += "&pt=" + paytype;
							}
							cburl += "&currency=" + ordervo.getCurrency();
							cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;
							
							callbackvo.setCallbackUrl(cburl);
							callbackBean.updateCallback(callbackvo);
						}
					}
					
					String appid = ordervo.getProductid();
					String content = "";
					if (queryString != null )
					{
						content = queryString.replace("&encrypt=" + ordervo.getEncrypt(), "");
					}				
					
					String minwen = "";
					String miwen = "";
					
					minwen += content;
					minwen += params.getParams(appid).getSecretkey();
					
					miwen = StringEncrypt.Encrypt(minwen);
					
					if (miwen.equals(ordervo.getEncrypt()))
					{
						//服务器签名验证成功
						if (ordervo.getStatus().equals("200")) 
						{
							//支付成功
							if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
							{
								order.setPayType(paytype);
								order.setPayId(ordervo.getBt_id());
								order.setMoney(ordervo.getPrice());
								order.setExInfo(order.getExInfo() + "#" + ordervo.getMsisdn() + "#" + ordervo.getOperator());
								order.setCurrency(ordervo.getCurrency());
								order.setUnit(Constants.CURRENCY_UNIT_FEN);
								
								bean.updateOrder(orderid, order);
								//bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getBt_id(), ordervo.getPrice(), ordervo.getMsisdn()+"#"+ordervo.getOperator());
								bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
							} 
							else 
							{
								System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(bluepay cb) order (" + order.getOrderId()+ ") had been succeed");
							}
						}
						else
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(bluepay cb) status="+ordervo.getStatus());
						}
					}
					else
					{
						// 服务器签名验证失败
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(bluepay cb)->(appid=" + appid + "),(content=" + minwen + "),(sign=" + miwen +")");
					}
					
					String path = OSUtil.getRootPath() + "../../logs/bluepaycb/" + DateUtil.getCurTimeStr().substring(0, 8);
					OSUtil.makeDirs(path);
					String filename = path + "/" + orderid;
					
					OSUtil.saveFile(filename, bluepayparams.toString());
				}			
			}
			else
			{
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(bluepay cb)-> cmd=" + cmd);
			}
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(bluepay cb ret)->" + ret);
		
		return ret;
	}

	public static void init()
	{
		params.initParams(BluepayParams.CHANNEL_ID, new BluepayParamsVO());
	}
}

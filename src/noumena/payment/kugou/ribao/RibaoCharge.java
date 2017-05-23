package noumena.payment.kugou.ribao;

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

public class RibaoCharge {
	private static RibaoParams params = new RibaoParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		RibaoCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_RIBAO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_RIBAO;
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
	
	public static String getCallbackFromRibao(Map<String,String> ribaoparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(ribao cb params)->" + ribaoparams.toString());
		
    	String ret = "SUCCESS";   
    	try {
    		JSONObject json = JSONObject.fromObject(ribaoparams);
        	RibaoOrderVO ordervo = (RibaoOrderVO)JSONObject.toBean(json,RibaoOrderVO.class);
    		
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(ordervo.getOutorderid());
			
			if (order != null)
			{
				String minwen = ordervo.buildContent() + params.getParams(order.getSign()).getSecretkey();
				String miwen = StringEncrypt.Encrypt(minwen);
				
				if (miwen.equals(ordervo.getSign()))
				{
					//服务器签名验证成功
					if (ordervo.getStatus().equals("1")) 
					{
						//支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
						{
							bean.updateOrderAmountPayIdExinfo(order.getOrderId(), ordervo.getOrderid(), ordervo.getAmount(), ordervo.getUsername());
							bean.updateOrderKStatus(order.getOrderId(),Constants.K_STSTUS_SUCCESS);
						} 
						else 
						{
							System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(ribao cb) order (" + order.getOrderId()+ ") had been succeed");
						}
					}
					else
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(ribao cb) status="+ordervo.getStatus());
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = "FAIL";
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(ribao cb)->(appid:" + order.getSign() + "),(content:" + minwen + "),(sign:" + miwen +")");
				}
				
				String path = OSUtil.getRootPath() + "../../logs/ribaocb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + order.getOrderId();
				
				OSUtil.saveFile(filename, ordervo.toString());
			}
		}catch (Exception e) {
			e.printStackTrace();
		}
    	
    	System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(ribao cb ret)->" + ret);		
    	
		return ret;
	}

	public static void init()
	{
		params.initParams(RibaoParams.CHANNEL_ID, new RibaoParamsVO());
	}
}

package noumena.payment.nduo;

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

public class NduoCharge
{
	private static NduoParams params = new NduoParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		NduoCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_NDUO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_NDUO;
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
	
	public static String getCallbackFromNduo(Map<String,String> nduoparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(nduo cb params)->" + nduoparams.toString());
    	
    	String ret = "success";
    	
    	try {
			JSONObject json = JSONObject.fromObject(nduoparams);
			NduoOrderVO ordervo = (NduoOrderVO)JSONObject.toBean(json,NduoOrderVO.class);
			
			String orderid = ordervo.getAppTradeNo();
			String appid = ordervo.getAppKey();
			String minwen = "";
			String miwen = "";
			
			minwen += "amount=";
			minwen += ordervo.getAmount();
			minwen += "&appKey=";
			minwen += appid;
			minwen += "&appTradeNo=";
			minwen += orderid;
			minwen += "&body=";
			minwen += ordervo.getBody();
			minwen += "&nduoTradeNo=";
			minwen += ordervo.getNduoTradeNo();
			minwen += "&server=";
			minwen += ordervo.getServer();
			minwen += "&serverName=";
			minwen += ordervo.getServerName();
			minwen += "&subject=";
			minwen += ordervo.getSubject();
			minwen += "&userToken=";
			minwen += ordervo.getUserToken();
			minwen += params.getParams(appid).getSecretkey();
			
			miwen = StringEncrypt.Encrypt(minwen);
			
			if (miwen.equals(ordervo.getSign()))
			{
				//服务器签名验证成功
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
				if (order != null)
				{//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) 
					{
						bean.updateOrderAmountPayIdExinfo(orderid, ordervo.getNduoTradeNo(), ordervo.getAmount(), ordervo.getUserToken()+"#"+ordervo.getServerName()+"#"+ordervo.getSubject());
						bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
					} 
					else 
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(nduo cb) order (" + order.getOrderId()+ ") had been succeed");
					}
				}
			
			}
			else
			{
				// 服务器签名验证失败
				ret = "failed";
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(nduo cb)->(appid:" + appid + "),(content:" + minwen + "),(sign:" + miwen +")");
			}
			
			System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(nduo cb ret)->" + ret);
			
			String path = OSUtil.getRootPath() + "../../logs/nduocb/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;
			
			OSUtil.saveFile(filename, nduoparams.toString());
		}
    	catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}

	public static void init()
	{
		params.initParams(NduoParams.CHANNEL_ID, new NduoParamsVO());
//		params.addApp("qunying", "MNdoo201503230002", "a072a3ab925669444050dbcd8111d5d1"); //三国群英OL
	}
}

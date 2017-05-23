package noumena.payment.sougou;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;


public class SougouCharge
{
	private static SougouParams params = new SougouParams();
	
	public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
	
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
				cburl += "?pt=" + Constants.PAY_TYPE_SOUGOU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_SOUGOU;
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
	
	public static String getCallbackFromSougou(Map<String,String> m)
	{
		String ret = "OK";
		System.out.println("sougou cb ->" + m.toString());
		SougouOrderVO covo = new SougouOrderVO();
		covo.setGid(m.get("gid"));
		covo.setSid(m.get("sid"));
		covo.setUid(m.get("uid"));
		covo.setRole(m.get("role"));
		covo.setOid(m.get("oid"));
		covo.setDate(m.get("date"));
		covo.setAmount1(m.get("amount1"));
		covo.setAmount2(m.get("amount2"));
		covo.setTime(m.get("time"));
		covo.setAppdata(m.get("appdata"));
		covo.setRealAmount(m.get("realAmount"));
		covo.setAuth(m.get("auth"));
		/**
		 *  生成auth前参数组合：
			amount1=10&amount2=100&appdata=123&date=131018&gid=100&oid=T131018_90&
			realAmount=10&role=&sid=1&time=20131018202859&uid=1&{ECCE11A4-2D0A-4DC0-B4B6-05F94BF0C8FC}
			生成auth：
			5a261caf9fb6a7fb84d7ade6817b599f
		 */
		String  miwen = "";
				miwen += "amount1="+covo.getAmount1()+"&";
				miwen += "amount2="+covo.getAmount2()+"&";
				miwen += "appdata="+covo.getAppdata()+"&";
				miwen += "date="+covo.getDate()+"&";
				miwen += "gid="+covo.getGid()+"&";
				miwen += "oid="+covo.getOid()+"&";
				miwen += "realAmount="+covo.getRealAmount()+"&";
				miwen += "role="+covo.getRole()+"&";
				miwen += "sid="+covo.getSid()+"&";
				miwen += "time="+covo.getTime()+"&";
				miwen += "uid="+covo.getUid()+"&";
				miwen+= params.getParams(covo.getGid()).getAppkey();
		String sign = StringEncrypt.Encrypt(miwen);
		if(sign.equals(covo.getAuth())){
			OrdersBean bean = new OrdersBean();
			try {
				// 支付成功
				Orders order = bean.qureyOrder(covo.getAppdata());
				if (order != null) {
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
							bean.updateOrderAmountPayIdExinfo(covo.getAppdata(), covo.getOid(), covo.getAmount1(), "");
							bean.updateOrderKStatus(covo.getAppdata(),Constants.K_STSTUS_SUCCESS);
						} else {
							System.out.println("sougou order ("+ order.getOrderId() + ") had been succeed");
						}
				}else{
					System.out.println("sougou order ("+ covo.getAppdata() + ") bu cun zai");
					ret = "ERR_200";
				}
			} catch (Exception e) {
				e.printStackTrace();
				ret = "ERR_500";
			}
			
			System.out.println("sougou cb ret->" + ret);

			String path = OSUtil.getRootPath() + "../../logs/sougou/"
					+ DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + covo.getAppdata();

			OSUtil.saveFile(filename, m.toString());
		}else{
			ret = "ERR_100";
		}
		return ret;
	}
	
	public static void init()
	{
		params.initParams(SougouParams.CHANNEL_ID, new SougouParamsVO());
	}
	
	/*public static void main(String args[]){
		System.out.println(MD5.getMessageDigest("123123123123123123".getBytes()));
	}*/
}

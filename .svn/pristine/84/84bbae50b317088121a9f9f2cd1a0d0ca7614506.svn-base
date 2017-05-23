package noumena.payment.winner;

import java.net.URLEncoder;
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
import noumena.payment.util.HttpUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class WinnerCharge
{
	private static WinnerParams params = new WinnerParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		WinnerCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_HUAWEI;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_HUAWEI;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static String checkOrdersStatus(String paytype, String payIds, String acccode, String sid, String currpoint, String clientip, String gg_sign)
	{
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0 ; i < orders.size() ; i++)
		{
			OrderStatusVO st = new OrderStatusVO();
			try
			{
				Orders order = orders.get(i);
				int status = order.getKStatus();
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
						String ret = checkOrderFromWinner(paytype, order, acccode, sid, currpoint, clientip, gg_sign);
						if (ret.equals("101"))
						{
							st.setStatus(1);
						}
						else
						{
							st.setStatus(2);
						}
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
			catch (Exception e)
			{
				e.printStackTrace();
				st.setStatus(4);
				statusret.add(st);
			}
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}
	
	private static String checkOrderFromWinner(String paytype, Orders order, String acccode, String sid, String currpoint, String clientip, String gg_sign) throws Exception
	{
		String minwen = "";
		String miwen = "";
		String urlstr = "";
		String content = "";
		if (paytype.equals("ios"))
		{
			String receipt = order.getExInfo();
			receipt = receipt.replaceAll("\t", "");
			receipt = receipt.replaceAll("\n", "");
			
			minwen += acccode;
			minwen += receipt;
			minwen += order.getOrderId();
			minwen += sid;
			minwen += currpoint;
			minwen += WinnerParams.GAMEID;
			minwen += WinnerParams.SECRETKEY;
			miwen = StringEncrypt.Encrypt(minwen);
			
			urlstr = "http://thws.winnerconnect.com/billing/v05/topup.asmx/IOSTopup";
			content += "acc_code=" + acccode;
			content += "&char_id=" + order.getOrderId();
			content += "&sid=" + sid;
			content += "&sn_pin=" + URLEncoder.encode(receipt, "utf-8");
			content += "&price=" + order.getAmount();
			content += "&current_point=" + currpoint;
			content += "&gid=" + WinnerParams.GAMEID;
			content += "&clientIP=" + clientip;
			content += "&chksum=" + miwen;
		}
		else if (paytype.equals("gp"))
		{
			String gg_data = order.getExInfo();
			gg_data = gg_data.replaceAll("\t", "");
			gg_data = gg_data.replaceAll("\n", "");
			gg_sign = gg_sign.replaceAll("\t", "");
			gg_sign = gg_sign.replaceAll("\n", "");
			
			minwen += acccode;
			minwen += gg_data;
			minwen += gg_sign;
			minwen += order.getOrderId();
			minwen += sid;
			minwen += currpoint;
			minwen += WinnerParams.GAMEID;
			minwen += WinnerParams.SECRETKEY;
			miwen = StringEncrypt.Encrypt(minwen);
			
			urlstr = "http://thws.winnerconnect.com/billing/v05/topup.asmx/GoogleTopup";
			content += "acc_code=" + acccode;
			content += "&char_id=" + order.getOrderId();
			content += "&sid=" + sid;
			content += "&gg_data=" + URLEncoder.encode(gg_data, "utf-8");
			content += "&gg_sign=" + URLEncoder.encode(gg_sign, "utf-8");
			content += "&current_point=" + currpoint;
			content += "&gid=" + WinnerParams.GAMEID;
			content += "&clientIP=" + clientip;
			content += "&chksum=" + miwen;
		}
		
		String ret = HttpUtil.doHttpPost(urlstr, content);
		String result = "100";
		
		System.out.println("winner cb ret->" + ret);
		result = getKeyValue(ret, "ret_code");
		
//		SAXBuilder builder = new SAXBuilder();
//		Document doc = builder.build(ret);
//		Element root = doc.getRootElement();
//		
//		List<?> childrens = root.getChildren("acc_uid");
//		if (childrens.size() > 0)
//		{
//			Element children = (Element) childrens.get(0);
//			result = children.getText();
//		}
		
		return result;
	}
	
	public static String getCallbackFromWinner(Map<String,Object> winnerparams)
	{
		String ret = "ok";
		String orderid = "";
		String sporderid = "";
		String orderAmount = "";
		String result = "";
		String checksum = "";
		
		try
		{
			orderid = (String) winnerparams.get("char_id");
			sporderid = (String) winnerparams.get("orderid");
			orderAmount = (String) winnerparams.get("amount");
			result = (String) winnerparams.get("result");
			checksum = (String) winnerparams.get("checksum");
			
			String minwen = "";
			String miwen = "";
			minwen += orderid;
			minwen += sporderid;
			minwen += orderAmount;
			minwen += result;
			minwen += WinnerParams.SECRETKEY;
			miwen = StringEncrypt.Encrypt(minwen);
			
			if (checksum.equals(miwen))
			{
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(orderid);
		
				if (order != null)
				{
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayId(orderid, sporderid, orderAmount);
						bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
					}
					else
					{
						System.out.println("winner order (" + order.getOrderId() + ") had been succeed");
					}
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		System.out.println("winner cb->" + winnerparams.toString());
		
		String path = OSUtil.getRootPath() + "../../logs/winnercb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, winnerparams.toString());
		
		return ret;
	}
	
	public static OrderIdVO getTransactionIdVO(Orders order)
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
				cburl += "?pt=" + order.getPayType();
			}
			else
			{
				cburl += "&pt=" + order.getPayType();
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}
	
	public static String getTransactionIdAndStatus(String paytype, Orders vo, String acccode, String sid, String currpoint, String clientip, String gg_sign)
	{
		if (paytype.equals("ios"))
		{
			String receipt = vo.getExInfo();
			if (receipt == null || receipt.length() < 30)
			{
				List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
				OrderStatusVO st = new OrderStatusVO();
				st.setPayId("0");
				st.setStatus(2);
				statusret.add(st);
				JSONArray arr = JSONArray.fromObject(statusret);
				return arr.toString();
			}
		}
		OrderIdVO orderIdVO = getTransactionIdVO(vo);
		String ids = "";
		if (orderIdVO != null)
		{
			ids = orderIdVO.getPayId();
		}
		return checkOrdersStatus(paytype, ids, acccode, sid, currpoint, clientip, gg_sign);
	}
	
	private static String getKeyValue(String content, String key)
	{
		//<retcode>xxxx</retcode>  ||  <retcode>xxxx<\retcode>
		String value = "";
		String startkey = "<" + key + ">";
		String endkey1 = "<\\" + key + ">";
		String endkey2 = "</" + key + ">";
		int pos = 0;
		
		pos = content.indexOf(startkey);
		if (pos >= 0)
		{
			value = content.substring(pos + startkey.length());
			pos = value.indexOf(endkey1);
			if (pos < 0)
			{
				pos = value.indexOf(endkey2);
			}
			
			value = value.substring(0, pos);
		}
		
		return value;
	}

	public static void init()
	{
//		params.addApp("qunying", "3320324", "QxAC2fdoMMFc6zBTLMd3xTn9HjOwxObS");	//群英i7CrpGa8z22dC5kPxKD15Grm
	}
}

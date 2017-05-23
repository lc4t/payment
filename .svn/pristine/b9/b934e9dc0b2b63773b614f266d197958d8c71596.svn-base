package noumena.payment.linyou;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.userverify.ChannelVerify;
import noumena.payment.userverify.util.Util;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class LinyouCharge
{
	private static LinyouParams params = new LinyouParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		LinyouCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_LINYOU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_LINYOU;
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
	
	public static String getCallbackFromLinyou(Map<String,String> linyouparams)
	{
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(linyou cb params)->" + linyouparams.toString());
		JSONObject json = JSONObject.fromObject(linyouparams);
		LinyouOrderVO vo =(LinyouOrderVO)JSONObject.toBean(json,LinyouOrderVO.class);
		String ret = "{\"errno\": 1000,\"errmsg\":\"\" ,\"data\":{\"orderId\":\""+vo.getOrderId()+"\",\"amount\":\""+vo.getAmount()+"\",\"game\":\""+vo.getGame()+"\",\"zone\":\""+vo.getZone()+"\",\"uid\":\""+vo.getUid()+"\" }}";
    	String miwen = "";
    	String orderid = "";
    	
		try 
		{
			orderid = vo.getPayExt();
			OrdersBean bean = new OrdersBean();	
			Orders order = bean.qureyOrder(orderid);
			if (order != null)
			{
				String minwen = vo.getGame()+vo.getOrderId()+vo.getAmount()+vo.getUid()+vo.getZone()+vo.getGoodsId()+vo.getPayTime()+vo.getPayChannel()+vo.getPayExt()+"#";
				//fktjd1671381c4fcb4f0a5f45ea335df64657da3a8981符石1470995591201608121752490113Vl#439ceeced27667306abf60ad42cc5908
				//03e5856f4c25aed646a408fba02dda36
				miwen = StringEncrypt.Encrypt(minwen+params.getParams(vo.getGame()).getSecretkey());
				
				if (miwen.equals(vo.getSign())) 
				{
					//服务器签名验证成功
					//支付成功
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
					{
						bean.updateOrderAmountPayIdExinfo(orderid, vo.getOrderId(), vo.getAmount(), "");
						bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
					} 
					else
					{
						System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(linyou) order (" + order.getOrderId()+ ") had been succeed");
					}
				}
				else
				{
					// 服务器签名验证失败
					ret = "{\"errno\": 1001,\"errmsg\":\"\" ,\"data\":{\"orderId\":\""+vo.getOrderId()+"\",\"amount\":\""+vo.getAmount()+"\",\"game\":\""+vo.getGame()+"\",\"zone\":\""+vo.getZone()+"\",\"uid\":\""+vo.getUid()+"\" }}";
					System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(linyou cb)->(appid="+order.getSign()+",content="+minwen+",sign="+miwen+")" );
				}
				
			}
			else 
			{
				//无效订单
				System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(linyou cb )-> can not get order by orderid(" + orderid+")");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("=====(" + DateUtil.getCurTimeStr() + ")=====channel(linyou cb ret)->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/linyoucb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, linyouparams.toString());
		
		return ret;
	}
	
	public static String getUidByToken(String token,String appid){
		String id = "";
		try
		{
			//System.out.println("linyoutoken:"+token);
			//System.out.println("linyouappkey:"+params.getParams(appid).getAppkey());
			String miwen = StringEncrypt.Encrypt(token+params.getParams(appid).getAppkey());
			//System.out.println("linyoumiwen:"+miwen);
			String urlstr = "http://g.linnyou.com/api/index";
			String urlParameters ="data="+token+"&sign="+miwen;
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}

			connection.disconnect();
			//System.out.println("linyoures:"+res);
			id = res;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}

	public static void init()
	{
		params.initParams(LinyouParams.CHANNEL_ID, new LinyouParamsVO());
		//params.addApp("qunying", "ac7479de010c170f6e9aa56703a80ae5", "q43hSqq7Gt4wptlSJNZvzoRz5HP4qclp");//三国群英OL
	}
	
	public static void main(String args[]){
		System.out.println(StringEncrypt.Encrypt("fktjd1671421c4fcb4f0a5f45ea335df64657da3a8981符石14710000512016081219071950890F#439ceeced27667306abf60ad42cc5908"));
	}
}

package noumena.payment.kuaiyong;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class KuaiyongCharge
{
	private static KuaiyongParams params = new KuaiyongParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		KuaiyongCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_KUAIYONG;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_KUAIYONG;
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

	public static String getCallbackFromKuaiyong(KuaiyongOrderVO vo)
	{
		String ret = "success";

		String minwen = "";
		String postdata = "";

		minwen += "dealseq=" + vo.getDealseq();
		minwen += "&notify_data=" + vo.getNotify_data();
		minwen += "&orderid=" + vo.getOrderid();
		minwen += "&subject=" + vo.getSubject();
		minwen += "&uid=" + vo.getUid();
		minwen += "&v=" + vo.getV();
		
		postdata = minwen + "&gameid=" + vo.getGameid();
		postdata += "&sign=" + vo.getSign();
		
		System.out.println("kuaiyong cb postdata->" + postdata);
		
		try {
			String cporderid = vo.getDealseq();
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(cporderid);
			
			if (order!=null)
			{
				String appid = order.getSign();
				String publickey = params.getParams(appid).getAppkey();
				boolean isvalid = RSASignature.doCheck(minwen, vo.getSign(), publickey, "UTF-8");
				
				if (isvalid)
				{
					RSAEncrypt rsaEncrypt= new RSAEncrypt();
					   
					//加载公钥   
					try
					{
						rsaEncrypt.loadPublicKey(publickey); 
						//加载公钥成功
					}
					catch (Exception e)
					{  
						//加载公钥失败
						e.printStackTrace();
					}  
					   
					//公钥解密通告加密数据
					byte[] dcDataStr = Base64.decode(vo.getNotify_data());
					byte[] plainData = rsaEncrypt.decrypt(rsaEncrypt.getPublicKey(), dcDataStr);  
					//获取到加密通告信息
					String result = new String(plainData, "UTF-8");
					//开发商业务逻辑处理
			           
					String value = getKeyValue(result, "dealseq");
					if (!value.equals(vo.getDealseq()))
					{
						ret = "failed";
					}
					else
					{
						
						value = getKeyValue(result, "payresult");
						vo.setPayresult(value);
						if (!value.equals("0"))
						{
							//支付失败
							ret = "failed";
							bean.updateOrderKStatus(cporderid, Constants.K_STSTUS_ERROR);
						}
						else
						{
							//支付成功
							value = getKeyValue(result, "fee");
							if(!Float.valueOf(value).equals(order.getAmount())){
								//支付失败
								ret = "failed";
								bean.updateOrderKStatus(cporderid, Constants.K_STSTUS_ERROR);
							}else{
								vo.setFee(value);
								
								if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
								{
									bean.updateOrderAmountPayIdExinfo(cporderid, vo.getOrderid(), vo.getFee(), vo.getSubject());
									bean.updateOrderKStatus(cporderid, Constants.K_STSTUS_SUCCESS);
								}
								else
								{
									System.out.println("kuaiyong order (" + order.getOrderId() + ") had been succeed");
								}
								
								String path = OSUtil.getRootPath() + "../../logs/kuaiyongcb/" + DateUtil.getCurTimeStr().substring(0, 8);
								OSUtil.makeDirs(path);
								String filename = path + "/" + cporderid;
								
								OSUtil.saveFile(filename, postdata);
							}
							
						}
					}
				}
				else
				{
					ret = "failed";
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
		
		return ret;
	}
	
	private static String getKeyValue(String content, String key)
	{
		//dealseq=20130219160809567&fee=0 .01&payresult=0
		String value = "";
		String[] keys = content.split("&");
		int pos = 0;
		
		for (int i = 0 ; i < keys.length ; i++)
		{
			pos = keys[i].indexOf("=");
			if (pos >= 0)
			{
				if (keys[i].substring(0, pos).equals(key))
				{
					return keys[i].substring(pos + 1);
				}
			}
		}
		
		return value;
	}
	
	public static void init()
	{
		params.initParams(KuaiyongParams.CHANNEL_ID, new KuaiyongParamsVO());
//		params.addApp("gaoguai", "4038", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPtxJwSjpyMd23NhJqVR3o7QvA/0ZXhNBxmZ29FBsH/DwjxuhcYYz08GIWRF6AT0Watxu2In9M7Rnbecyy4uyT9gJG4WWbBCPa592+N7i88/zpP18equvHUT+US833pjw4jBebFOEAgGATC4rTbqjNiH5TXbcZrfFHkxapNsuYiwIDAQAB");	//2ec36f47fb4714a5971112a2e80a54cf
//		params.addApp("M5", "5617", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCj3VL1uhizQjjdMYYLDAVhEKM5CpoGHfipwQf8iGCZr0ynfkOXqemxm0eaOgoCQ1mY1B35vVFTPudo/P4bRkkh86oAJ1Srk9NTuEFrSy6RbQ6l1yk9ZRUT6BXH2iuuV0VKiNBWoOhSRlsw9ZO8LBxLnxF5Bemjz3jLk/zLCeCQwQIDAQAB");	//24866fdcc3d9badfd4fe5362423af750
//		params.addApp("qzhuan", "7301", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDJma4cmPi6bftHAZKgGh9Aa+rlfKm9njQdTiJv0hmn9WVHhIZTzZ+NBELQCztu9wTOHKU31Ie+Sggj3N0h5OVmDBNh+SI5bnmPoDeVeeXTwKcT1GRmYXx/kcae1AWpa4uxbL6DYNeOlARoJ4BzE0GOOW2e439heLcwLftkSgU30QIDAQAB");	//X9A7NFCq2m4kn6khm1ufcYWKVAOlqiBn
	}
	
	public static void main(String args[]){
		System.out.println(Float.valueOf("10.0").equals(10f));
	}
}

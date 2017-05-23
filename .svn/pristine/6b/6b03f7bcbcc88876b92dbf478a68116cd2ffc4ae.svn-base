package noumena.payment.mobojoy;

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
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class MobojoyCharge
{
	private static MobojoyParams params = new MobojoyParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		MobojoyCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_MOBOJOY;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_MOBOJOY;
			}
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

	public static String getCallbackFromMobojoy(MobojoyOrderVO vo)
	{
		String ret = "Success";

		String minwen = "";
		String miwen = "";

		minwen += "App_Id=" + vo.getApp_Id();
		minwen += "&Create_Time=" + vo.getCreate_Time();
		minwen += "&Extra=" + vo.getExtra();
		minwen += "&Pay_Status=" + vo.getPay_Status();
		minwen += "&Recharge_M=" + vo.getRecharge_M();
		minwen += "&Recharge_Money=" + vo.getRecharge_Money();
		minwen += "&Recharge_Money_Code=" + vo.getRecharge_Money_Code();
		minwen += "&Uin=" + vo.getUin();
		minwen += "&Urecharge_Id=" + vo.getUrecharge_Id();
		minwen += MobojoyParams.MOBOJOY_KEY;
		
		miwen = StringEncrypt.Encrypt(minwen);
		
		System.out.println("mobojoy cb ->" + minwen);
		System.out.println("mobojoy cb sign->" + vo.getSign());
		System.out.println("mobojoy cb my sign->" + miwen);
		
		if (vo.getSign().equals(miwen))
		{
			try
			{
				OrdersBean bean = new OrdersBean();
				String cporderid = vo.getUrecharge_Id();
				if (!vo.getPay_Status().equals("1"))
				{
					//支付失败
					bean.updateOrderKStatus(cporderid, Constants.K_STSTUS_ERROR);
				}
				else
				{
					//支付成功
					Orders order = bean.qureyOrder(cporderid);
					if (order != null)
					{
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(cporderid, vo.getUin(), vo.getRecharge_M(), vo.getRecharge_Money() + "&" + vo.getRecharge_Money_Code());
							bean.updateOrderKStatus(cporderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("mobojoy order (" + order.getOrderId() + ") had been succeed");
						}
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/mobojoycb/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + cporderid;
				
				OSUtil.saveFile(filename, minwen);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		else
		{
			ret = "failed";
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
		params.addApp("gaoguai", "4038", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPtxJwSjpyMd23NhJqVR3o7QvA/0ZXhNBxmZ29FBsH/DwjxuhcYYz08GIWRF6AT0Watxu2In9M7Rnbecyy4uyT9gJG4WWbBCPa592+N7i88/zpP18equvHUT+US833pjw4jBebFOEAgGATC4rTbqjNiH5TXbcZrfFHkxapNsuYiwIDAQAB");	//2ec36f47fb4714a5971112a2e80a54cf
	}
}

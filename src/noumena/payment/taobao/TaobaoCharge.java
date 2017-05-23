package noumena.payment.taobao;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallBackGameServBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.bean.PayItemBean;
import noumena.payment.bean.PayServerBean;
import noumena.payment.model.Orders;
import noumena.payment.model.PayItems;
import noumena.payment.model.PayServer;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;
import noumena.payment.vo.Status;

public class TaobaoCharge
{
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
				cburl += "?pt=" + Constants.PAY_TYPE_TMALL + "&num=1";
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_TMALL + "&num=1";
			}
			if (cburl != null && !cburl.equals(""))
			{
				payId = bean.CreateOrder(order, cburl);
			}
			else
			{
				payId = bean.CreateOrder(order);
			}
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
			order.setProductId(order.getItemId());
			order.setSubId(order.getExInfo());
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

	public static TaobaoRetMsgVO checkUid(String sid, String uid) throws Exception
	{
		//0、验证请求是否合法
		//1、通过sid（淘宝id）找到对应的游戏、服务器、验证地址等信息
		//2、去游戏验证uid是否合法

		TaobaoRetMsgVO ret = new TaobaoRetMsgVO();
		
		//0
//		if (isMsgValid("", "") == false)
//		{
//			//回调请求不合法
//			ret.setMsg("签名不正确");
//			return ret;
//		}
		
		//1
		PayServer server = getGameServerInfo(sid, uid);
		if (server == null)
		{
			//sid不合法
			ret.setMsg("淘宝id不合法");
			return ret;
		}
		
		//2
		String url = server.getCheckUrl();
		if (url.indexOf("?") == -1)
		{
			url += "?gameid=" + server.getGameId();
		}
		else
		{
			url += "&gameid=" + server.getGameId();
		}
		url += "&action=0";
		url += "&serverid=" + server.getServerId();
		url += "&userid=" + URLEncoder.encode(uid, "utf-8");;
		
		CallBackGameServBean cbean = new CallBackGameServBean();
		String res = cbean.doGet(url);
		JSONObject json = JSONObject.fromObject(res);
		Status s = (Status) JSONObject.toBean(json, Status.class);
		
		if (s != null && s.getStatus() == 0)
		{
			ret.setRet("1");
			ret.setMsg("");
		}
		else
		{
			ret.setRet("0");
			ret.setMsg("用户验证非法");
		}
		
		return ret;
	}

	private static boolean checkPrice(String gameid, String itemid, int amount)
	{
		//验证天猫的价格和itemid是否合法
		PayItemBean bean = new PayItemBean();
		List<PayItems> items = bean.selectByGame(gameid);
		for (int i = 0 ; i < items.size() ; i++)
		{
			PayItems item = items.get(i);
			if (item.getItemid().equals(itemid))
			{
				if (item.getItemprice() == amount)
				{
					return true;
				}
			}
		}
		
		return false;
	}

	public static TaobaoRetMsgVO getCallbackTaobao(String sid, String uid, String itemid, String amount, int num, String sign, String taobaoid, String signstr)
	{
		//0、根据淘宝订单号判断是否是重复订单
		//1、验证请求是否合法
		//2、通过sid（淘宝id）找到对应的游戏、服务器、回调地址等信息
		//2.5、检验请求的商品id和价格是否和定义的一致
		//3、构造订单vo
		//4、创建订单
		//5、验证订单
		TaobaoRetMsgVO ret = new TaobaoRetMsgVO();
		
		try
		{
			uid = URLDecoder.decode(uid, "GBK");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			//uid解码异常
			ret.setMsg("uid解码异常");
			return ret;
		}
		
		//0
		if (isOrderExist(taobaoid) == true)
		{
			//订单重复
			ret.setMsg("订单重复");
			return ret;
		}
		
		//1
		if (isMsgValid(sign, signstr) == false)
		{
			//回调请求不合法
			ret.setMsg("签名不正确");
			return ret;
		}
		
		//2
		PayServer server = getGameServerInfo(sid, uid);
		if (server == null)
		{
			//sid不合法
			ret.setMsg("淘宝id不合法");
			return ret;
		}
		
		//2.5
		int iamount = Float.valueOf(amount).intValue();
		if (checkPrice(server.getGameId(), itemid, iamount) == false)
		{
			//商品id和价格和定义的不一致
			ret.setMsg("商品id不合法");
			System.out.println("taobao cb item id invalid->gameid(" + server.getGameId() + ")itemid(" + itemid + ")itemprice(" + iamount + ")");
			return ret;
		}
		
		//3
		Orders order = new Orders();
		String cburl = server.getCallbackUrl();
		try
		{
			String Parameter = "";
			Parameter += "userid=" + URLEncoder.encode(uid, "utf-8");
			Parameter += "&gameid=" + server.getGameId();
			Parameter += "&serverid=" + server.getServerId();
			Parameter += "&itmeid=" + itemid;
			Parameter += "&amount=" + iamount;
			Parameter += "&itemprice=" + 0;
			Parameter += "&pt=" + Constants.PAY_TYPE_TMALL;
			Parameter += "&num=" + num;
			cburl += "?" + Parameter;
			
			Date date = new Date();
			SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			order.setUId(uid);
			order.setAppId(server.getGameId());
			if (amount == null || amount.equals(""))
			{
				amount = "0.00";
			}
			else
			{
				amount = new DecimalFormat("0.00").format(new Float(amount));
			}
			order.setAmount(Float.valueOf(amount));
			order.setCreateTime(df1.format(date));
			order.setPayType(Constants.PAY_TYPE_TMALL);
			order.setItemId(itemid);
			order.setItemNum(num);
			order.setPayId(taobaoid);
			order.setMoney(amount);
			order.setChannel(Constants.getGameIdByAppId(server.getGameId()) + "A0ABE00A0000000");
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret.setMsg("订单创建失败");
			return ret;
		}
		
		//4
		OrdersBean bean = new OrdersBean();
		String payid = bean.CreateOrder(order, cburl);
		
		//5
		bean.updateOrderKStatus(payid, Constants.K_STSTUS_SUCCESS);
		
		ret.setRet("1");
		ret.setMsg("");
		
		return ret;
	}

	private static boolean isOrderExist(String taobaoid)
	{
		//0、验证淘宝订单号是否重复
		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrdersByPayId(taobaoid);
		
		if (orders.size() > 0)
		{
			return true;
		}
		else
		{
			return false;
		}
	}

	private static boolean isMsgValid(String sign, String signstr)
	{
		//1、验证请求是否合法
		String minwen = signstr + TaobaoParams.PRIVATE_KEY;
		String miwen = StringEncrypt.Encrypt(minwen);
		return sign.toLowerCase().equals(miwen.toLowerCase());
	}
	
	public static void main(String[] args)
	{
		String signstr = "amount400.00itemidTOKEN_TM_12num10sid28492tbOrderNo55555565typepayuid454545";
		String minwen = signstr + TaobaoParams.PRIVATE_KEY;
		String miwen = StringEncrypt.Encrypt(minwen);
		System.out.println("miwen->" + miwen);
	}

	private static PayServer getGameServerInfo(String sid, String uid)
	{
		//2、通过sid（淘宝id）找到对应的游戏、服务器、回调地址等信息
		PayServer server = null;
		PayServerBean payServerBean = new PayServerBean();
		List<PayServer> payServer = payServerBean.selectByTaobao(sid);
		
		if (payServer.size() == 0)
		{
			return null;
		}
		
		if (payServer.size() > 1)
		{
			System.out.println("duplicate taobao id!!!!!");
		}
		
		server = payServer.get(0);
		
		return server;
	}
}

package noumena.payment.i4;

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
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;
import cn.i4.pay.sdk.service.PayService;

public class I4Charge
{
	private static I4Params params = new I4Params();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		I4Charge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_I4;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_I4;
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
	
	public static String getCallbackFromI4(Map<String,String> i4params, I4OrderVO vo)
	{
    	String ret = "success";
		String status = vo.getStatus();		//交易状态
		String orderid = vo.getBillno();	//厂商订单号
		String publickey = params.getParams(vo.getApp_id()).getPublic_key();
		if(null == publickey || "".equals(publickey)){
			publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAxc2tC3dMdIH+jUvBekMWsq+OVq0PMtxDmc9JF/asyA/fs6fccS7xJ+4JXL/RXSRsmXBjB3WBc/TZdklX05OmwAmq7XMtIfI40UxvHc2gcd+HbbtsN280bXzZcAZa9N7I7VvnfgQ4futHbaKiSno/e8tJOTzCSzRdNh7/POLOHQIDAQAB";
		}
		if (PayService.verifySignature(i4params, publickey))
		{
			//服务器签名验证成功
			//请在这里加上游戏的业务逻辑程序代码
			//获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
			if (null != status && status.equals("0"))
			{
			    // 交易处理成功
				OrdersBean bean = new OrdersBean();
				try
				{
					//支付成功
					Orders order = bean.qureyOrder(orderid);
					if (order != null)
					{
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(orderid, vo.getOrder_id(), vo.getAmount(), vo.getAccount());
							bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							System.out.println("i4 order (" + order.getOrderId() + ") had been succeed");
						}
					}
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else
			{
			}
		}
		else
		{
			// 服务器签名验证失败
			ret = "fail";
		}

		System.out.println("i4 cb ->" + i4params.toString());
		System.out.println("i4 cb ret->" + ret);
		
		String path = OSUtil.getRootPath() + "../../logs/i4cb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;
		
		OSUtil.saveFile(filename, i4params.toString());
		
		return ret;
	}

	public static void init()
	{
		params.initParams(I4Params.CHANNEL_ID, new I4ParamsVO());
//		params.addApp("xixuegui", "76", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAxc2tC3dMdIH+jUvBekMWsq+OVq0PMtxDmc9JF/asyA/fs6fccS7xJ+4JXL/RXSRsmXBjB3WBc/TZdklX05OmwAmq7XMtIfI40UxvHc2gcd+HbbtsN280bXzZcAZa9N7I7VvnfgQ4futHbaKiSno/e8tJOTzCSzRdNh7/POLOHQIDAQAB");
//		params.addApp("qunying", "235", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAxc2tC3dMdIH+jUvBekMWsq+OVq0PMtxDmc9JF/asyA/fs6fccS7xJ+4JXL/RXSRsmXBjB3WBc/TZdklX05OmwAmq7XMtIfI40UxvHc2gcd+HbbtsN280bXzZcAZa9N7I7VvnfgQ4futHbaKiSno/e8tJOTzCSzRdNh7/POLOHQIDAQAB");
//		params.addApp("gaoguai", "240", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAxc2tC3dMdIH+jUvBekMWsq+OVq0PMtxDmc9JF/asyA/fs6fccS7xJ+4JXL/RXSRsmXBjB3WBc/TZdklX05OmwAmq7XMtIfI40UxvHc2gcd+HbbtsN280bXzZcAZa9N7I7VvnfgQ4futHbaKiSno/e8tJOTzCSzRdNh7/POLOHQIDAQAB");
//		params.addApp("m5", "211", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAxc2tC3dMdIH+jUvBekMWsq+OVq0PMtxDmc9JF/asyA/fs6fccS7xJ+4JXL/RXSRsmXBjB3WBc/TZdklX05OmwAmq7XMtIfI40UxvHc2gcd+HbbtsN280bXzZcAZa9N7I7VvnfgQ4futHbaKiSno/e8tJOTzCSzRdNh7/POLOHQIDAQAB");
//		params.addApp("qzhuan", "470", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAxc2tC3dMdIH+jUvBekMWsq+OVq0PMtxDmc9JF/asyA/fs6fccS7xJ+4JXL/RXSRsmXBjB3WBc/TZdklX05OmwAmq7XMtIfI40UxvHc2gcd+HbbtsN280bXzZcAZa9N7I7VvnfgQ4futHbaKiSno/e8tJOTzCSzRdNh7/POLOHQIDAQAB");
//		params.addApp("gongzhu", "508", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAxc2tC3dMdIH+jUvBekMWsq+OVq0PMtxDmc9JF/asyA/fs6fccS7xJ+4JXL/RXSRsmXBjB3WBc/TZdklX05OmwAmq7XMtIfI40UxvHc2gcd+HbbtsN280bXzZcAZa9N7I7VvnfgQ4futHbaKiSno/e8tJOTzCSzRdNh7/POLOHQIDAQAB");
//		params.addApp("PaPa", "1260", "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCkk18WvRlWovbruLxh+2UKpmKTBi99WIc7KYacFo+A6yEOwA73Jyl7gA1eESkQOgEgsHXiF12AwVSl/fk2g8FrJlpkw1OPGvu4e1CVymvFNuSwOOVV0aBg58i3osMvtyR7h+UFhcZaU4GhZrbWDVhX4C3OWntFFo968HtCCmPEsQIDAQAB");
		
	}
}

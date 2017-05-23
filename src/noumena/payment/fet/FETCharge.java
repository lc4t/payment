package noumena.payment.fet;

import java.io.StringReader;
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

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class FETCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		FETCharge.testmode = testmode;
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
				cburl += "?pt=" + Constants.PAY_TYPE_FET;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_FET;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		orderIdVO.setMsg(getDMD5(order.getProductId(), "" + payId));
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
				//如果订单状态是初始订单或者是网络连接有问题状态的订单则去第三方支付中心验证后再返回订单状态
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					status = Constants.K_STSTUS_TIMEOUT;
					st.setStatus(2);
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
	
	public static FETOrderVO getItemsFromFET(FETOrderVO req)
	{
		FETOrderVO ret = new FETOrderVO();
		
		try
		{
			String paymentId = req.getOrderid();
			String pkgId = req.getPkgId();
//			String pkgv = req.getPkgv();
			String dmd5 = req.getDmd5();
			
			String sign = getDMD5(pkgId, paymentId);
			System.out.println("fet items md5->" + dmd5);
			System.out.println("fet items sign->" + sign);
			if (sign.equals(dmd5))
			{
				if (paymentId == null || paymentId.equals(""))
				{
					ret.setValue(FETParams.GET_ITEMS_RET2_CODE);
					ret.setDesc(FETParams.GET_ITEMS_RET2_MSG);
				}
				else
				{
					OrdersBean bean = new OrdersBean();
					Orders order = bean.qureyOrder(paymentId);
					if (order == null)
					{
						ret.setValue(FETParams.GET_ITEMS_RET2_CODE);
						ret.setDesc(FETParams.GET_ITEMS_RET2_MSG);
					}
					else
					{
						String exinfo = order.getExInfo();
						String[] ps = exinfo.split("#");
						if (!pkgId.equals(ps[0]))
						{
							ret.setValue(FETParams.GET_ITEMS_RET2_CODE);
							ret.setDesc(FETParams.GET_ITEMS_RET2_MSG);
						}
						else
						{
							ret.setValue(FETParams.GET_ITEMS_RET0_CODE);
							ret.setDesc(FETParams.GET_ITEMS_RET0_MSG);
							ret.setStore(ps[1]);
							ret.setProductId(order.getItemId());
							ret.setName(ps[0]);
							ret.setPrice("" + order.getAmount());
						}
					}
				}
			}
			else
			{
				ret.setValue(FETParams.GET_ITEMS_RET1_CODE);
				ret.setDesc(FETParams.GET_ITEMS_RET1_MSG);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret.setValue(FETParams.GET_ITEMS_RET10_CODE);
			ret.setDesc(FETParams.GET_ITEMS_RET10_MSG);
		}
		
		return ret;
	}
	
	public static FETOrderVO getOrderCBFromFET(String orderinfo)
	{
		FETOrderVO ret = new FETOrderVO();
		try
		{
			StringReader read = new StringReader(orderinfo);
			InputSource source = new InputSource(read);
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(source);
			Element root = doc.getRootElement();
			
			String paymentId = "0";
			String result = "";
			String pkgId = "";
			String dmd5 = "";
			List<?> childrens = root.getChildren("paymentId");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				paymentId = children.getText();
			}
			childrens = root.getChildren("result");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				result = children.getText();
			}
			childrens = root.getChildren("pkgId");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				pkgId = children.getText();
			}
			childrens = root.getChildren("dmd5");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				dmd5 = children.getText();
			}
			
			String sign = getDMD5(pkgId, paymentId);
			System.out.println("fet cb md5->" + dmd5);
			System.out.println("fet cb sign->" + sign);
			if (sign.equals(dmd5))
			{
				//md5合法
				OrdersBean bean = new OrdersBean();
				Orders order = bean.qureyOrder(paymentId);
				if (order == null)
				{
					ret.setOrderid(paymentId);
					ret.setValue(FETParams.CALLBACK_RET3_CODE);
					ret.setDesc(FETParams.CALLBACK_RET3_MSG);
				}
				else
				{
					if (result == null || result.equals(""))
					{
						ret.setOrderid(paymentId);
						ret.setValue(FETParams.CALLBACK_RET10_CODE);
						ret.setDesc(FETParams.CALLBACK_RET10_MSG);
					}
					else
					{
						ret.setOrderid(paymentId);
						ret.setValue(FETParams.CALLBACK_RET0_CODE);
						ret.setDesc(FETParams.CALLBACK_RET0_MSG);
						
						if (result.equals(FETParams.CALLBACK_RESULT_SUCCESS))
						{
							bean.updateOrderKStatus(paymentId, Constants.K_STSTUS_SUCCESS);
						}
						else
						{
							bean.updateOrderKStatus(paymentId, Constants.K_STSTUS_ERROR);
						}
					}
				}
			}
			else
			{
				ret.setOrderid(paymentId);
				ret.setValue(FETParams.CALLBACK_RET11_CODE);
				ret.setDesc(FETParams.CALLBACK_RET11_MSG);
			}
			
			String path = OSUtil.getRootPath() + "../../logs/fetcb/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + paymentId;
			OSUtil.saveXML(root, filename);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret.setOrderid("0");
			ret.setValue(FETParams.CALLBACK_RET20_CODE);
			ret.setDesc(FETParams.CALLBACK_RET20_MSG);
		}
		return ret;
	}
	
	private static String getDMD5(String pkgname, String orderid)
	{
		String account = FETParams.ACOUNT;
		String key = FETParams.KEY;
		String data = account + "/" + pkgname + "/" + orderid + "/" + key;
		return StringEncrypt.dmd5(data);
	}
}

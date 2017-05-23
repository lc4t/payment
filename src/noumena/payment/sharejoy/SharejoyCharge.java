package noumena.payment.sharejoy;

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

public class SharejoyCharge {
	private static SharejoyParams params = new SharejoyParams();
	private static boolean testmode = false;

	public static boolean isTestmode() {
		return testmode;
	}

	public static void setTestmode(boolean testmode) {
		SharejoyCharge.testmode = testmode;
	}

	public static String getTransactionId(Orders order) {
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_FEN);
		
		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + Constants.PAY_TYPE_SHAREJOY;
			} else {
				cburl += "&pt=" + Constants.PAY_TYPE_SHAREJOY;
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

	public static String checkOrdersStatus(String payIds) {
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0; i < orders.size(); i++) {
			Orders order = orders.get(i);
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT
					|| status == Constants.K_CON_ERROR) {
				// 如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT) {
					st.setStatus(4);
				} else {
					st.setStatus(3);
				}
			} else if (status == Constants.K_STSTUS_SUCCESS) {
				// 如果订单已经成功，直接返回订单状态
				st.setStatus(1);
			} else {
				// 订单已经失败，直接返回订单状态
				st.setStatus(2);
			}
			statusret.add(st);
		}
		JSONArray arr = JSONArray.fromObject(statusret);

		return arr.toString();
	}

	public static String getCallbackFromSharejoy(String data) {
		SharejoyOrderVO vo = null;
		JSONObject json = JSONObject.fromObject(data);
		vo = (SharejoyOrderVO) JSONObject.toBean(json, SharejoyOrderVO.class);

		String ret = "success";
		String minwen = "";
		String miwen = "";
		String orderid = vo.getOut_trade_no();

		minwen += vo.getClient_ip();
		minwen += vo.getExtension_info();
		minwen += vo.getGame_id();
		minwen += vo.getGame_money();
		minwen += vo.getId();
		minwen += vo.getMerchant_id();
		minwen += vo.getMoney();
		minwen += vo.getOrder_no();
		minwen += vo.getOrder_status();
		minwen += vo.getOut_trade_no();
		minwen += vo.getPay_money();
		minwen += vo.getPay_time();
		minwen += vo.getProduct_desc();
		minwen += vo.getProduct_name();
		minwen += vo.getRole();
		minwen += vo.getUid();
		minwen += vo.getUsername();
		minwen += vo.getZone_id();
		minwen += params.getAppKeyById(vo.getGame_id());

		miwen = StringEncrypt.Encrypt(minwen).toLowerCase();

		if (miwen.equals(vo.getSign())) {
			// 服务器签名验证成功
			// 请在这里加上游戏的业务逻辑程序代码
			// 获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
			// 交易处理成功
			OrdersBean bean = new OrdersBean();
			try {
				// 支付成功
				Orders order = bean.qureyOrder(orderid);
				if (order != null) {
					if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
						bean.updateOrderAmountPayIdExinfo(orderid, vo.getOrder_no(), vo.getMoney(), vo.getUid());
						bean.updateOrderKStatus(orderid,
								Constants.K_STSTUS_SUCCESS);
					} else {
						System.out.println("sharejoy order ("
								+ order.getOrderId() + ") had been succeed");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			// 服务器签名验证失败
			ret = "fail";
		}

		System.out.println("sharejoy cb ->" + data);
		System.out.println("sharejoy cb ret->" + ret);

		String path = OSUtil.getRootPath() + "../../logs/sharejoycb/"
				+ DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;

		OSUtil.saveFile(filename, data);

		return ret;
	}

	public static void init() {
		params.addApp("gongzhu", "18", "tb98ggbsnmxylwjtzwjchck2y2lycecr");
	}
}

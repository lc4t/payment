package noumena.payment.jufenghudong;

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

public class JufenghudongCharge {
	private static JufenghudongParams params = new JufenghudongParams();
	private static boolean testmode = false;

	public static boolean isTestmode() {
		return testmode;
	}

	public static void setTestmode(boolean testmode) {
		JufenghudongCharge.testmode = testmode;
	}

	public static String getTransactionId(Orders order) {
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_YUAN);
		
		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + Constants.PAY_TYPE_JUFENGHUDONG;
			} else {
				cburl += "&pt=" + Constants.PAY_TYPE_JUFENGHUDONG;
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
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR) {
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

	public static String getCallbackFromJufeng(Map<String, String> jufenghudongparams) {
		String ret = "";
		String minwen = "";
		String miwen = "";
		String transaction = jufenghudongparams.get("transaction"); // 飓风方的订单号
		String payType = jufenghudongparams.get("payType"); // 支付方式
		String userId = jufenghudongparams.get("userId"); // 飓风互动的用户ID
		String serverNo = jufenghudongparams.get("serverNo"); // 服务器编号
		String amount = jufenghudongparams.get("amount"); // 金额
		String cardPoint = jufenghudongparams.get("cardPoint");// 充值点数
		String gameUserId = jufenghudongparams.get("gameUserId");// 游戏用户ID
		String transactionTime = jufenghudongparams.get("transactionTime");// 订单交易时间
		String orderid = jufenghudongparams.get("gameExtend");
		String platform = jufenghudongparams.get("platform");
		String status = jufenghudongparams.get("status");// 充值状态
		String currency = jufenghudongparams.get("currency");// 货币类型
		String _sign = jufenghudongparams.get("_sign");// 签名

		OrdersBean bean = new OrdersBean();
		Orders order = bean.qureyOrder(orderid);

		try {
			if (order != null) {

				minwen += "amount=";
				minwen += amount;
				minwen += "&cardPoint=";
				minwen += cardPoint;
				minwen += "&currency=";
				minwen += currency;
				minwen += "&gameExtend=";
				minwen += orderid;
				minwen += "&gameUserId=";
				minwen += gameUserId;
				minwen += "&payType=";
				minwen += payType;
				minwen += "&platform=";
				minwen += platform;
				minwen += "&serverNo=";
				minwen += serverNo;
				minwen += "&status=";
				minwen += status;
				minwen += "&transaction=";
				minwen += transaction;
				minwen += "&transactionTime=";
				minwen += transactionTime;
				minwen += "&userId=";
				minwen += userId;

				miwen = StringEncrypt.Encrypt(minwen);
				miwen = StringEncrypt.Encrypt(miwen + params.getParams(order.getSign()).getSecretkey());

				if (miwen.equals(_sign)) {
					// 服务器签名验证成功
					// 请在这里加上游戏的业务逻辑程序代码
					// 获取通知返回参数，可参考接口文档中通知参数列表(以下仅供参考)
					// 交易处理成功

					// 支付成功

						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS)
						{
							bean.updateOrderAmountPayIdExinfo(orderid,transaction, amount, serverNo);
							bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
						}
						 else {
							System.out.println("jufenghudong order ("+ order.getOrderId()+ ") had been succeed");
						}
						ret = "{\"status\":\"" + order.getKStatus()+ "\",\"transIDO\":\"" + orderid + "\"}";
					

				} else {
					// 服务器签名验证失败
					ret = "{\"status\":\"0\",\"transIDO\":\"" + orderid + "\"}";
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
			ret = "{\"status\":\"0\",\"transIDO\":\"" + orderid + "\"}";
		}

		System.out.println("jufenghudong cb ->" + jufenghudongparams.toString());
		System.out.println("jufenghudong cb ret->" + ret);

		String path = OSUtil.getRootPath() + "../../logs/jufenghudongcb/"
				+ DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderid;

		OSUtil.saveFile(filename, jufenghudongparams.toString());


		return ret;
	}

	public static void init() {
		params.initParams(JufenghudongParams.CHANNEL_ID, new JufenghudongParamsVO());
		//params.addApp("进击吧三国", "b54adb32d961d9ef608aef06fc5cf96d","b237578fdb8ae71b64852d297eaeec6e");
	}
}

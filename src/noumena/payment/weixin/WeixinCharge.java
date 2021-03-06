package noumena.payment.weixin;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.xml.XMLSerializer;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class WeixinCharge {

	private static WeixinParams params = new WeixinParams();
	private static boolean testmode = true;

	public static boolean isTestmode() {
		return testmode;
	}

	public static void setTestmode(boolean testmode) {
		WeixinCharge.testmode = testmode;
	}

	public static String getTransactionId(Orders order, String body,
			String remoteip) {
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_FEN);

		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + Constants.PAY_TYPE_WEIXIN;
			} else {
				cburl += "&pt=" + Constants.PAY_TYPE_WEIXIN;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;

			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);

		String prepayid = getPrepayID(order, body, remoteip);
		orderIdVO.setMsg(prepayid);

		JSONObject json = JSONObject.fromObject(orderIdVO);
		System.out
				.println("weixin===============================getTransactionId"
						+ json.toString());
		return json.toString();
	}

	private static String getPrepayID(Orders order, String body, String remoteip) {
		String prepayid = "";

		StringBuilder sb = new StringBuilder("");
		try {
			WeixinParamPrepared wpp = new WeixinParamPrepared(order.getSign(),
					params.getAppShanghuKeyById(order.getSign()), UUID
							.randomUUID().toString().replace("-", ""),
					new String(body.getBytes(), "UTF-8"), new String(
							body.getBytes(), "UTF-8"), order.getOrderId(),
					order.getAmount().intValue() + "", WeixinParams.NOTIFY_URL,
					remoteip, "APP",
					params.getAppSecretKeyById(order.getSign()));
			// Create connection
			URL url = new URL("https://api.mch.weixin.qq.com/pay/unifiedorder");
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setRequestMethod("POST");
			connection.setUseCaches(false);
			connection.setReadTimeout(10000);
			connection.setInstanceFollowRedirects(true);
			connection.setRequestProperty("Cache-Control", "no-cache");
			connection.setRequestProperty("Content-Type", "text/xml");
			connection.connect();

			// POST Request
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());
			out.writeBytes(wpp.getXmlInfo());
			out.flush();
			out.close();

			// Read response
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String lines;

			while ((lines = reader.readLine()) != null) {
				lines = new String(lines.getBytes(), "utf-8");
				sb.append(lines);
			}
			reader.close();
			connection.disconnect();
			System.out.print("wx_response:" + sb.toString());

		} catch (Exception e) {
			return null;
		}
		XMLSerializer xmlSerializer = new XMLSerializer();
		JSONObject jsonObj = JSONObject.fromObject(xmlSerializer.read(
				sb.toString()).toString());
		prepayid = jsonObj.getString("prepay_id");
		// {"prepayid":"1201000000141126148dbd023a544744","errcode":0,"errmsg":"Success"}

		System.out.println("weixin prepay prepayid ->" + prepayid);
		long now = System.currentTimeMillis() / 1000;
		jsonObj.clear();
		jsonObj.accumulate("noncestr", order.getOrderId());
		jsonObj.accumulate("prepayid", prepayid);
		jsonObj.accumulate("timestamp", now);
		System.out.println(prepayid);
		return jsonObj.toString();
	}

	public static String checkOrdersStatus(String payIds) {
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0; i < orders.size(); i++) {
			try {
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
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JSONArray arr = JSONArray.fromObject(statusret);

		return arr.toString();
	}

	public static String getCallbackFromWeixin(WeixinOrderVO weixinparams) {
		System.out.println("weixin cb->" + weixinparams.toString());
		String ret = "<return_code><![CDATA[SUCCESS]]></return_code>";

		try {

			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(weixinparams.getOut_trade_no());
			if (order != null) {
				// if (sign.equals(miwen))
				{
					if ("SUCCESS".equals(weixinparams.getResult_code())) {
						// 支付成功
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
							bean.updateOrderAmountPayIdExinfo(
									weixinparams.getOut_trade_no(),
									weixinparams.getTransaction_id(),
									weixinparams.getTotal_fee(), "");
							bean.updateOrderKStatus(
									weixinparams.getOut_trade_no(),
									Constants.K_STSTUS_SUCCESS);
						} else {
							System.out
									.println("weixin order ("
											+ order.getOrderId()
											+ ") had been succeed");
						}
					} else {
						bean.updateOrderKStatus(weixinparams.getOut_trade_no(),
								Constants.K_STSTUS_ERROR);
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			return "<return_code><![CDATA[FAIL]]></return_code>";
		}
		System.out.println("weixin cb->" + weixinparams.toString());

		String path = OSUtil.getRootPath() + "../../logs/weixincb/"
				+ DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + weixinparams.getOut_trade_no();

		OSUtil.saveFile(filename, weixinparams.toString());

		return ret;
	}

	public static OrderIdVO getTransactionIdVO(Orders order) {
		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + order.getPayType();
			} else {
				cburl += "&pt=" + order.getPayType();
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		return new OrderIdVO(payId, date);
	}

	public static void init() {
		params.addApp("d61", "wxe72317818223f788",
				"wxe72317818223f788wxe72317818223", "1318855801"); // 口袋战争
		params.addApp("d66", "wxc305fb66be965b21",
				"00938a1836d65dbe250ce99abd4agtsn", "1320620501");
		params.addApp("d62", "wx2a6400f7baec2070",
				"cbc121cdac1fa9795340cb90b36b2c3c", "1334796501");
		params.addApp("g6", "wxe61285931a570e56",
				"wxe61285931a570e56wxe61285931ag6", "1365071202");
		params.addApp("m10", "wx1ac01de2efb5fb8e",
				"wx1ac01de2efb5fb8ewx1ac01de2em10", "1374416702");
		params.addApp("c1", "wx7db1f3a902c1ff5f",
				"wx7db1f3a902c1ff5fwx7db1f3a9d183", "1366211402");
		params.addApp("c1", "wx7db1f3a902c1ff5f",
				"wx7db1f3a902c1ff5fwx7db1f3a9d183", "1366211402");
	}

	public static void main(String[] args) {
		init();
		Orders order = new Orders();
		order.setSign("wx7db1f3a902c1ff5f");
		order.setAmount(1100f);
		order.setOrderId("20161025115452546Vg3");
		try {
			WeixinCharge.getPrepayID(order, "在枯", "127.0.0.1");
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		try {
			// appsign = URLDecoder.decode(appsign, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

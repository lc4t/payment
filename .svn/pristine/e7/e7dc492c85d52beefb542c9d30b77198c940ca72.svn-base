package noumena.payment.c7477sdk;

import java.text.SimpleDateFormat;
import java.util.Map;

import noumena.pay.util.MD5;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;

public class C7477sdkCharge {
	private static C7477sdkParams params = new C7477sdkParams();
	private static String secretkey = "88187b6da5f1dc6592d2cab456a11992";
	public static SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	public static String getCallbackFromSougou(Map<String, String> m) {
		try {
			System.out.println("getCallback185sy  cb ================>"
					+ m.toString());
			String appid = m.get("appid");
			String uid = m.get("uid");
			String payMoney = m.get("pay_money");
			String outTradeNo = m.get("out_trade_no");
			String ts = m.get("ts");
			String sign = m.get("sign");
			String param = m.get("param");
			String perSign = appid + outTradeNo + payMoney + ts + uid
					+ params.getParams(appid).getAppkey();
			System.out
					.println(outTradeNo
							+ "C7477sdk   getCallback===============>perSign"
							+ perSign);
			String endsign = MD5.md5(perSign, "UTF-8");
			System.out
					.println(outTradeNo
							+ "C7477sdk   getCallback===============>endsign"
							+ endsign);
			// 判断签名
			if (!sign.equals(endsign)) {
				System.out
						.println(outTradeNo
								+ "C7477sdk   getCallback==========result======>sign error");
				return "{\"msg\":\"sign error\"}";
			}
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(outTradeNo);
			// 订单不存在
			if (order == null) {
				System.out
						.println(outTradeNo
								+ "C7477sdk   getCallback=========result======>order is not exit");
				return "{\"errcode\":1,\"msg\":\"order is not exit\"}";
			}
			// 如果订单已经成功
			if (order.getKStatus() == Constants.K_STSTUS_SUCCESS) {
				System.out
						.println(outTradeNo
								+ "C7477sdk   getCallback=========result======>order is not exit");
				return "{\"errcode\":4,\"msg\":\"order is already succeed\"}";
			}
			// 判断价格
			if (!(order.getAmount() == Float.valueOf(payMoney))) {
				System.out
						.println(outTradeNo
								+ "C7477sdk   getCallback=========result======>order is not exit");
				return "{\"errcode\":1,\"msg\":\"rmb is error\"}";
			}

			bean.updateOrderAmountPayIdExinfo(outTradeNo, sign, payMoney, param);
			bean.updateOrderKStatus(outTradeNo, Constants.K_STSTUS_SUCCESS);
			System.out
					.println(outTradeNo
							+ "C7477sdk   getCallback=========result======>order is success");
			return "{\"errcode\":0,\"msg\":\"order is success\"}";
		} catch (Exception e) {
			System.out
					.println("C7477sdk   getCallback=========result======>error");
			e.printStackTrace();
			return "{\"errcode\":6,\"msg\":\"error\"}";
		}
	}

	/*
	 * public static void main(String args[]){
	 * System.out.println(MD5.getMessageDigest
	 * ("123123123123123123".getBytes())); }
	 */
	public static void init() {
		params.initParams(C7477sdkParams.CHANNEL_ID, new C7477SDKVO());
	}
}

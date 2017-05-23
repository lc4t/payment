package noumena.payment.cggame;

import java.util.Map;

import noumena.pay.util.MD5;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;

public class CgGameCharge {
	private static CgGameParams cgparams = new CgGameParams();

	public static String getCallbackFromCgGameCharge(Map<String, String> params) {
		System.out
				.println("getCallbackFromCgGameCharge====================param "
						+ params.toString());
		String from = params.get("from");
		String ext1 = params.get("ext1");
		String ext2 = params.get("ext2");
		String gameid = params.get("gameid");
		String money = params.get("money");
		String orderid = params.get("orderid");
		String outorderid = params.get("outorderid");
		String role = params.get("role");
		String serverid = params.get("serverid");
		String time = params.get("time");
		String userid = params.get("userid");
		String sign = params.get("sign");
		try {
			String preSign = "";
			if (ext1 != null) {
				preSign = preSign + "ext1=" + ext1;
			}
			if (ext2 != null) {
				preSign = preSign + "&ext2=" + ext2;

			}
			preSign = preSign + "&from=" + from+"&gameid=" + gameid + "&money=" + money
					+ "&orderid=" + orderid + "&outorderid=" + outorderid
					+ "&role=" + role + "&serverid=" + serverid + "&time="
					+ time + "&userid=" + userid
					+ "&"+cgparams.getParams(gameid).getAppkey();
			System.out.println(outorderid
					+ "getCallbackFromCgGameCharge====================perSign "
					+ preSign);
			String endsignmd5 = MD5.md5(preSign, "UTF-8");
			System.out
					.println(outorderid
							+ "getCallbackFromCgGameCharge====================endsignmd5 "
							+ endsignmd5);
			if (!sign.equals(endsignmd5)) {
				System.out
						.println(outorderid
								+ "getCallbackFromCgGameCharge====================sign is error "
								+ endsignmd5 + " param sign==" + sign);
				return "sign is error";
			}
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(outorderid);
			if (order == null) {
				System.out
						.println(outorderid
								+ "getCallbackFromCgGameCharge====================order is not exist ");
				return "order is not exist";
			}
			if (order.getKStatus() == Constants.K_STSTUS_SUCCESS) {
				System.out
						.println(outorderid
								+ "getCallbackFromCgGameCharge====================order is already success");
				return "success";
			}

			if (order.getKStatus() != Constants.K_STSTUS_DEFAULT) {
				System.out
						.println(outorderid
								+ "getCallbackFromCgGameCharge====================order is repeated ");
				return "order is repeated";
			}
			if (!order.getAmount().equals(Float.parseFloat(money))) {
				System.out
						.println(outorderid
								+ "getCallbackFromCgGameCharge====================order amount is different "
								+ order.getAmount() + " " + money);
				return "order amount is different ";
			}
			bean.updateOrderAmountPayIdExinfo(outorderid, orderid, money, role
					+ "/" + serverid + "/" + userid + "/" + ext1 + "/" + ext2);
			bean.updateOrderKStatus(outorderid, Constants.K_STSTUS_SUCCESS);
			return "success";
		} catch (Exception e) {
			System.out
					.println(outorderid
							+ "getCallbackFromCgGameCharge====================system error ");
			e.printStackTrace();
			return "system error";
		}

	}

	public static void main(String arg[]) {

	}

	public static void init() {
		cgparams.initParams(CgGameParams.CHANNEL_ID, new CgGameVO());
	}

}

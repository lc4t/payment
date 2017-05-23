package noumena.payment.guopan;

import java.util.Map;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.now.MD5;
import noumena.payment.util.Constants;

public class GuoPanCharge {

	private static String SERVER_KEY = "JKMUA1YOTEPQSQ9YS26FY69GB78Y4NSDH1TJYS1WGNVVIJ896T6GH91IQHY0ZO69";

	/***
	 * 回调
	 * 
	 * @param vo
	 * @return
	 */
	public static String getCallbackFromGuoPan(Map<String, String> guopanparams) {

		System.out.println("getCallbackFromGuoPan  cb ================>"
				+ guopanparams.toString());
		String tradeNo = guopanparams.get("trade_no");
		String serialNumber = guopanparams.get("serialNumber");
		String money = guopanparams.get("money");
		String status = guopanparams.get("status");
		String timeStamp = guopanparams.get("t");
		String sign = guopanparams.get("sign");
		String appid = guopanparams.get("appid");
		String itemId = guopanparams.get("item_id");
		String itemPrice = guopanparams.get("item_price");
		String itemCount = guopanparams.get("item_count");
		String reserved = guopanparams.get("reserved");
		String presign = serialNumber + money + status + timeStamp + SERVER_KEY;
		try {
			if ("0".equals(status)) {
				return "fail";
			}
			if ("2".equals(status)) {
				return "not sufficient funds";
			}
			System.out
					.println(serialNumber+" getCallbackFromGuoPan  cb ================> persign"
							+ presign);
			String endSign = MD5.md5(presign, "UTF-8");
			System.out
					.println(serialNumber+" getCallbackFromGuoPan  cb ================> endSign"
							+ endSign);
			if (!endSign.equals(sign)) {
				System.out
				.println(serialNumber+" getCallbackFromGuoPan  cb ================> endSign"
						+ sign+" "+endSign);
				return "sign is error";
			}
			OrdersBean bean = new OrdersBean();
			Orders orders = bean.qureyOrder(serialNumber);
			if (orders == null) {
				System.out
				.println(serialNumber+" getCallbackFromGuoPan  cb ================> order is not exist"
						);
				return "order is not exist";
			}
			if (!orders.getKStatus().equals(Constants.K_STSTUS_DEFAULT)) {
				System.out
				.println(serialNumber+" getCallbackFromGuoPan  cb ================> order is repeating"
						);
				return "order is repeating ";
			}
			if (!orders.getAmount().equals(Float.parseFloat(money))) {
				System.out.println(serialNumber+"money is differen =========order "
						+ orders.getAmount() + "input"
						+ Float.parseFloat(money));
				return "money is different ";
			}
			bean.updateOrderAmountPayIdExinfo(serialNumber, tradeNo, money,
					reserved);
			bean.updateOrderKStatus(serialNumber, Constants.K_STSTUS_SUCCESS);
			System.out
			.println(serialNumber+" getCallbackFromGuoPan  cb ================> order is success"
					);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();

			return "system error ";
		}
	}
}

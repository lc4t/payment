package noumena.payment.lumi;

import java.util.Map;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.now.MD5;
import noumena.payment.util.Constants;

public class LumiCharge {

	private static LumiParams params = new LumiParams();

	public static void init() {
		params.initParams(LumiParams.CHANNEL_ID, new LumiVO());
	}

	/***
	 * 回调
	 * 
	 * @param vo
	 * @return
	 */
	public static String getCallbackFromLuMi(Map<String, String> lumiparams,
			String requestParam) {

		System.out.println("getCallbackFromLuMi  cb ================>"
				+ lumiparams.toString());
		String orderid = lumiparams.get("orderid");
		String agentOid = lumiparams.get("agentOid");
		String userId = lumiparams.get("userid");
		String currency = lumiparams.get("currency");
		String roleId = lumiparams.get("roleid");
		String roleName = lumiparams.get("rolename");
		String game = lumiparams.get("game");
		String server = lumiparams.get("server");
		String amount = lumiparams.get("amount");
		String gold = lumiparams.get("gold");
		String paytype = lumiparams.get("paytype");
		String info = lumiparams.get("info");
		String timestamp = lumiparams.get("timestamp");
		String pvc = lumiparams.get("pvc");
		String sign = lumiparams.get("sign");
		try {
			String presign = requestParam.replace("&", "")
					+ params.getParams(game).getAppkey();
			System.out.println(agentOid
					+ " getCallbackFromLuMi  cb ================> persign"
					+ presign);
			String endSign = MD5.md5(presign, "UTF-8").toLowerCase();
			System.out.println(agentOid
					+ " getCallbackFromLuMi  cb ================> endSign"
					+ endSign);
			if (!endSign.equals(sign)) {
				System.out.println(agentOid
						+ " getCallbackFromLuMi  cb ================> endSign"
						+ sign + " " + endSign);
				return "{\"code\":\"3003\",\"error\":\"USER_NOT_EXIST\"}";
			}
			OrdersBean bean = new OrdersBean();
			Orders orders = bean.qureyOrder(agentOid);
			if (orders == null) {
				System.out
						.println(agentOid
								+ " getCallbackFromLuMi  cb ================> order is not exist");
				return "{\"code\":\"3005\",\"error\":\"FAILURE\"}";
			}
			if (!orders.getKStatus().equals(Constants.K_STSTUS_DEFAULT)) {
				System.out
						.println(agentOid
								+ " getCallbackFromLuMi  cb ================> order is repeating");
				return "{\"code\":\"3006\",\"error\":\"REPEAT_ORDER\"}";
			}
			if (!orders.getAmount().equals(Float.parseFloat(amount))) {
				System.out.println(agentOid
						+ "money is differen =========order "
						+ orders.getAmount() + "input"
						+ Float.parseFloat(amount));
				return "{\"code\":\"3007\",\"error\":\"AMOUNT_DIFFER\"}";
			}
			bean.updateOrderAmountPayIdExinfo(agentOid, orderid, amount,
					paytype + "/" + userId + "/" + server + "/" + roleName
							+ "/" + roleId + "/" + gold + "/" + timestamp + "/"
							+ currency + "/" + info + "/" + pvc);
			bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
			System.out
					.println(agentOid
							+ " getCallbackFromLuMi  cb ================> order is success");
			return "{\"code\":\"3\",\"error\":\"SUCCESS\"}";
		} catch (Exception e) {
			e.printStackTrace();

			return "{\"code\":\"3002\",\"error\":\"PARAMETER_NOT_LEGAL\"}";
		}
	}
}

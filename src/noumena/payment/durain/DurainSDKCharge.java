package noumena.payment.durain;

import java.util.Map;

import noumena.pay.util.MD5;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;

/***
 * 榴莲的回调
 * @author kz
 *
 */
public class DurainSDKCharge {
	private static DurainParams durainparams = new DurainParams();
	public static String getCallbackDurain(
			Map<String, String> params) {
		
		System.out.println("DurainSDKCharge====================param "
				+ params.toString());
		String appid = params.get("appid");
		String orderId = params.get("orderId");
		String userId = params.get("userId");
		String serverId = params.get("serverId");
		String roleId = params.get("roleId");
		String roleName = params.get("roleName");
		String money = params.get("money");
		String extInfo = params.get("extInfo");
		String status = params.get("status");
		String sign = params.get("sign");
		try {
			String preSign =appid+durainparams.getParams(appid).getAppkey()+orderId+userId+serverId+roleId+roleName+money+extInfo+status;
			System.out
					.println(extInfo
							+ "DurainSDKCharge====================perSign "
							+ preSign);
			String endsignmd5 = MD5.md5(preSign, "UTF-8");
			System.out.println(extInfo
					+ "DurainSDKCharge====================endsignmd5 "
					+ endsignmd5);
			if (!sign.equals(endsignmd5)) {
				System.out.println(extInfo
						+ "DurainSDKCharge====================sign is error "
						+ endsignmd5 + " param sign==" + sign);
				return "FAILURE";
			}
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(extInfo);
			if (order == null) {
				System.out
						.println(extInfo
								+ "DurainSDKCharge====================order is not exist ");
				return "FAILURE";
			}
			if (order.getKStatus().equals( Constants.K_STSTUS_SUCCESS)) {
				System.out
						.println(extInfo
								+ "DurainSDKCharge====================order is already success");
				return "SUCCESS";
			}
			if (!order.getKStatus().equals( Constants.K_STSTUS_DEFAULT)) {
				System.out
						.println(extInfo
								+ "DurainSDKCharge====================order is repeated ");
				return "FAILURE";
			}
			if (!order.getAmount().equals(Float.parseFloat(money))) {
				System.out
						.println(extInfo
								+ "DurainSDKCharge====================order amount is different "
								+ order.getAmount() + " " + money);
				return "FAILURE";
			}
			bean.updateOrderAmountPayIdExinfo(extInfo, orderId, money,
					appid+"/"+userId+"/"+serverId+"/"+roleId+"/"+roleName);
			bean.updateOrderKStatus(extInfo, Constants.K_STSTUS_SUCCESS);
			return "SUCCESS";
		} catch (Exception e) {
			System.out.println(extInfo
					+ "DurainSDKCharge====================system error ");
			e.printStackTrace();
			return "FAILURE";
		}

	}

	public static void main(String arg[]) {

	}

	public static void init() {
		durainparams.initParams(DurainParams.CHANNEL_ID, new DurainVO());
	}

}

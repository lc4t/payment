package noumena.payment.NicePlayV3;

import java.util.Map;

import net.sf.json.JSONObject;
import noumena.pay.util.MD5;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;

/***
 * 台湾NicePlayV3渠道的接入
 * 
 * @author kz
 * 
 */
public class NicePlayV3Charge {
	private static NicePlayParams params = new NicePlayParams();

	public static void init() {
		params.initParams(NicePlayParams.CHANNEL_ID, new NicePlayVO());
	}

	public static String callBackNicePlay(Map<String, String> m) {
		try {
			JSONObject jsonObject = JSONObject.fromObject(m);

			String tradeid = m.get("tradeid");
			String appid = m.get("appid");
			String gameuid = m.get("gameuid");
			String itemprice = m.get("itemprice");
			String gameorder = m.get("gameorder");
			String ts = m.get("ts");
			String sign = m.get("sign");
			String key = params.getParams(appid).getAppkey();
			String preSign = tradeid + ts + gameuid + itemprice + key;
			System.out.println(gameorder
					+ "callBackNicePlay perSign======================"
					+ preSign);
			String endSign = MD5.md5(preSign, "UTF-8");
			System.out.println(gameorder
					+ "callBackNicePlay endSign======================"
					+ endSign);
			if (!endSign.equals(sign)) {
				return "{\"code\":\"sign is error\"}";
			}
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(gameorder);
			if (order == null) {
				System.out
						.println(gameorder
								+ "callBackNicePlay=========result======>order is not exit");
				return "{\"code\":\"order is not exit\"}";
			}
			// 如果订单已经成功
			if (order.getKStatus() == Constants.K_STSTUS_SUCCESS) {
				System.out
						.println(gameorder
								+ "callBackNicePlay=========result======>order is already success");
				return "{\"code\":\"1\"}";
			}

			if (!(order.getAmount().equals(Float.valueOf(itemprice)))) {
				System.out
						.println(gameorder
								+ "callBackNicePlay=========result======>order amount is error"
								+ order.getAmount() + " "
								+ Float.valueOf(itemprice));
				return "{\"code\":\"amount is error\"}";
			}

			bean.updateOrderAmountPayIdExinfo(gameorder, tradeid, itemprice,
					jsonObject.toString());
			bean.updateOrderKStatus(gameorder, Constants.K_STSTUS_SUCCESS);
			System.out.println(gameorder
					+ "callBackNicePlay=========result======>order is success");
			return "{\"code\":\"1\"}";

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"code\":500}";
		}

	}

}

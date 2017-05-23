package noumena.payment.pengyouwan;

import net.sf.json.JSONObject;
import noumena.pay.util.MD5;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;

public class PengyouwanCharge {
	private static PengYouWanParams params = new PengYouWanParams();
	public static String getCallbackFromPengyouwan(
			String pengyouwanparams) {
		try {
		JSONObject json = JSONObject.fromObject(pengyouwanparams);
		//String ver = json.getString("ver");
		String tid = json.getString("tid");
		String sign = json.getString("sign");
		String gamekey = json.getString("gamekey");
		String channel = json.getString("channel");
		String cpOrderid = json.getString("cp_orderid");
		String chOrderid = json.getString("ch_orderid");
		String amount = json.getString("amount");
		String cpParam = json.getString("cp_param");
		String preSign = params.getParams(gamekey).getAppkey()+cpOrderid+chOrderid+amount;
		System.out.println(cpOrderid + "PengyouwanCharge==========>perSign"
				+ preSign);
		// MD5加密验证
		String signmd5 = "";
	
			signmd5 = MD5.md5(preSign, "UTF-8");
		
		System.out.println(cpOrderid + "PengyouwanCharge=========signmd5"
				+ signmd5 + " sign" + sign);
		if (!signmd5.equals(sign)) {
			System.out.println(cpOrderid + "PengyouwanCharge sign is error");
			return "{\"ack\":\"101\",\"msg\":\"sign is error\"}";
		}
		// 验证订单
		OrdersBean bean = new OrdersBean();
		Orders order = bean.qureyOrder(cpOrderid);
		if (order == null) {
			System.out.println(cpOrderid
					+ "PengyouwanCharge=========order is not exist");
			return "{\"ack\":\"102\",\"msg\":\"order is not exit\"}";
		}

		if (!order.getAmount().equals(Float.parseFloat(amount))) {
			System.out
					.println(cpOrderid
							+ "PengyouwanCharge====================order amount is different "
							+ order.getAmount() + " " + Float.parseFloat(amount));
			return "{\"ack\":\"103\",\"msg\":\"order amount is different\"}";
		}
		// 更改订单状态
		if (!order.getKStatus().equals(Constants.K_STSTUS_SUCCESS)) {
			bean.updateOrderAmountPayIdExinfo(cpOrderid, chOrderid, amount, tid+"/"+channel+"/"+cpParam);
			bean.updateOrderKStatus(cpOrderid, Constants.K_STSTUS_SUCCESS);
			System.out.println(cpOrderid
					+ "PengyouwanCharge============ order ("
					+ order.getOrderId() + ")  succeed");
			return "{\"ack\":\"200\",\"msg\":\"Ok\"}";
		} else {
			System.out.println(cpOrderid
					+ "PengyouwanCharge================== order ("
					+ order.getOrderId() + ") had been succeed");
			return "{\"ack\":\"200\",\"msg\":\"order had been succeed\"}";
		}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return "{\"ack\":\"104\",\"msg\":\"system error\"}";
		}

	}
	public static void init() {
		params.initParams(PengYouWanParams.CHANNEL_ID, new PengYouWanVO());
	}

	public static void main(String arg[]) {

	}

}

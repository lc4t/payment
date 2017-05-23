package noumena.payment.tutu;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import noumena.pay.util.MD5;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderStatusVO;

public class TutuCharge {
	//private static AiYingYongParams params = new AiYingYongParams();
	// private static String secretkey = "88187b6da5f1dc6592d2cab456a11992";

	public static String getCallbackFromTutu(Map<String, String> m) {
		try {
			System.out
					.println("getCallbackFromTutu  cb ================>"
							+ m.toString());
			String uid = m.get("uid");
			String cporder = m.get("cporder");
			String cpappid = m.get("cpappid");
			String money = m.get("money");
			String order = m.get("order");
			String sign = m.get("sign");
			// 验证参数
			String perSign = "n_time=" +URLEncoder.encode(nTime,"UTF-8")  + "&appid=" + appid + "&o_id="
					+ oId + "&t_fee=" + tFee + "&g_name=" + URLEncoder.encode(gName,"UTF-8") + "&g_body="
					+ URLEncoder.encode(gBody,"UTF-8") + "&t_status=" + tStatus + params.getParams(appid).getAppkey();
			 perSign=perSign.replace(" ", "+");
			System.out.println(oId
					+ "getCallbackFromTutu===============>perSign"
					+ perSign);
			String endsign = MD5.md5(perSign, "UTF-8");
			System.out.println(oId
					+ "getCallbackFromTutu===============>endsign"
					+ endsign);
			// 判断签名
			if (!oSign.equals(endsign)) {
				System.out
						.println(oId
								+ "getCallbackFromTutu=========result======>sign error");
				return "{\"errcode\":2,\"msg\":\"sign error\"}";
			}
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(oId);
			// 订单不存在
			if (order == null) {
				System.out
						.println(oId
								+ "getCallbackFromTutu=========result======>order is not exit");
				return "{\"errcode\":1,\"msg\":\"order is not exit\"}";
			}
			// 如果订单已经成功
			if (order.getKStatus() == Constants.K_STSTUS_SUCCESS) {
				System.out
						.println(oId
								+ "getCallbackFromTutu=========result======>order is already success");
				return "{\"errcode\":4,\"msg\":\"order is already succeed\"}";
			}
			// 判断价格
			if (!(order.getAmount().equals(Float.valueOf(tFee)))) {
				System.out
						.println(oId
								+ "getCallbackFromTutu=========result======>order amount is error"
								+ order.getAmount() + " " + Float.valueOf(tFee));
				return "{\"errcode\":1,\"msg\":\"amount is error\"}";
			}

			bean.updateOrderAmountPayIdExinfo(oId, oId, tFee, gName + "/"
					+ gBody+"/"+uParam);
			bean.updateOrderKStatus(oId, Constants.K_STSTUS_SUCCESS);
			System.out
					.println(oId
							+ "getCallbackFromTutu=========result======>order is success");
			return "success";
		} catch (Exception e) {
			System.out
					.println("getCallbackFromAiYingyong=========result======>error");
			e.printStackTrace();
			return "{\"errcode\":6,\"msg\":\"error\"}";
		}
	}

	public static void init() {
		params.initParams(TutuParams.CHANNEL_ID, new TutuParamsVO());
	}
	/*
	 * public static void main(String args[]){
	 * System.out.println(MD5.getMessageDigest
	 * ("123123123123123123".getBytes())); }
	 */
}

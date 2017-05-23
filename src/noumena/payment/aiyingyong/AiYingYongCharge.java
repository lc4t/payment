package noumena.payment.aiyingyong;

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

public class AiYingYongCharge {
	private static AiYingYongParams params = new AiYingYongParams();
	// private static String secretkey = "88187b6da5f1dc6592d2cab456a11992";
	public static SimpleDateFormat format = new SimpleDateFormat(
			"yyyyMMddHHmmss");

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
		}
		JSONArray arr = JSONArray.fromObject(statusret);

		return arr.toString();
	}

	public static String getCallbackFromAiYingyong(Map<String, String> m) {
		try {
			System.out
					.println("getCallbackFromAiYingyong  cb ================>"
							+ m.toString());
			String nTime = m.get("n_time");
			String appid = m.get("appid");
			String oId = m.get("o_id");
			String tFee = m.get("t_fee");
			String gName = m.get("g_name");
			String gBody = m.get("g_body");
			String tStatus = m.get("t_status");
			String oSign = m.get("o_sign");
			String uParam = m.get("u_param");
			// 验证参数
			String perSign = "n_time=" +URLEncoder.encode(nTime,"UTF-8")  + "&appid=" + appid + "&o_id="
					+ oId + "&t_fee=" + tFee + "&g_name=" + URLEncoder.encode(gName,"UTF-8") + "&g_body="
					+ URLEncoder.encode(gBody,"UTF-8") + "&t_status=" + tStatus + params.getParams(appid).getAppkey();
			 perSign=perSign.replace(" ", "+");
			System.out.println(oId
					+ "getCallbackFromAiYingyong===============>perSign"
					+ perSign);
			String endsign = MD5.md5(perSign, "UTF-8");
			System.out.println(oId
					+ "getCallbackFromAiYingyong===============>endsign"
					+ endsign);
			// 判断签名
			if (!oSign.equals(endsign)) {
				System.out
						.println(oId
								+ "getCallbackFromAiYingyong=========result======>sign error");
				return "{\"errcode\":2,\"msg\":\"sign error\"}";
			}
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(oId);
			// 订单不存在
			if (order == null) {
				System.out
						.println(oId
								+ "getCallbackFromAiYingyong=========result======>order is not exit");
				return "{\"errcode\":1,\"msg\":\"order is not exit\"}";
			}
			// 如果订单已经成功
			if (order.getKStatus() == Constants.K_STSTUS_SUCCESS) {
				System.out
						.println(oId
								+ "getCallbackFromAiYingyong=========result======>order is already success");
				return "{\"errcode\":4,\"msg\":\"order is already succeed\"}";
			}
			// 判断价格
			if (!(order.getAmount().equals(Float.valueOf(tFee)))) {
				System.out
						.println(oId
								+ "getCallbackFromAiYingyong=========result======>order amount is error"
								+ order.getAmount() + " " + Float.valueOf(tFee));
				return "{\"errcode\":1,\"msg\":\"amount is error\"}";
			}

			bean.updateOrderAmountPayIdExinfo(oId, oId, tFee, gName + "/"
					+ gBody+"/"+uParam);
			bean.updateOrderKStatus(oId, Constants.K_STSTUS_SUCCESS);
			System.out
					.println(oId
							+ "getCallbackFromAiYingyong=========result======>order is success");
			return "success";
		} catch (Exception e) {
			System.out
					.println("getCallbackFromAiYingyong=========result======>error");
			e.printStackTrace();
			return "{\"errcode\":6,\"msg\":\"error\"}";
		}
	}

	public static void init() {
		params.initParams(AiYingYongParams.CHANNEL_ID, new AiYingYongParamsVO());
	}
	/*
	 * public static void main(String args[]){
	 * System.out.println(MD5.getMessageDigest
	 * ("123123123123123123".getBytes())); }
	 */
}

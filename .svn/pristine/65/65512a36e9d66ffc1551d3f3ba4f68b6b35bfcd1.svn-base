package noumena.payment.newmycard;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.getorders.OrderUtil;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;

public class mycardMain {
	public static void main(String args[]) throws Exception {
		List<Orders> listOrders = getOrders();
		OrdersBean bean = new OrdersBean();
		if (listOrders != null && !listOrders.isEmpty()) {
			for (int i = 0; i < listOrders.size(); i++) {
				Orders orders = listOrders.get(i);
				try {

					if (orders.getScreenSize() == null) {
						JSONObject jsonString = new JSONObject();
						jsonString.put("FacServiceId", "THOL");
						jsonString.put("FacTradeSeq", orders.getOrderId());
						jsonString.put("StartDateTime", "");
						jsonString.put("EndDateTime", "");
						jsonString.put("CancelStatus", "0");
						String hash = getHash(jsonString);
						jsonString.put("Hash", hash);
						String result = OrderUtil
								.getHttpsInterenceStream(
										"https://b2b.mycard520.com.tw/MyBillingPay/api/SDKTradeQuery",
										"get", jsonString.toString());
						JSONObject jsonObjectResultOut = JSONObject
								.fromObject(result);
						if (jsonObjectResultOut.get("ReturnCode").equals("1")) {
							JSONArray jsonObjectEnd = JSONArray
									.fromObject(jsonObjectResultOut
											.get("ListSDKTradeQuery"));
						String paymentType=JSONObject.fromObject(jsonObjectEnd.get(0)).getString("PaymentType");
						orders.setScreenSize(paymentType);
						bean.updateOrdernew(orders.getOrderId(), orders);

						}
					}
				} catch (Exception e) {
				e.printStackTrace();
				System.out.println(orders.getOrderId()+"error");
				}
			}
		}
	}

	private static String getHash(JSONObject jsonObject) throws Exception {
		String preHashValue = jsonObject.getString("FacServiceId")
				+ jsonObject.getString("FacTradeSeq")
				+ jsonObject.getString("StartDateTime")
				+ jsonObject.getString("EndDateTime")
				+ jsonObject.getString("CancelStatus")
				+ "eD8mkUBBm5GXLaP6Fk8lhKKBQXAgNAeA";
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] bt = preHashValue.getBytes("UTF-8");
		md.update(bt);
		String afsign = bytes2Hex(md.digest()); // to HexString
		afsign = afsign.replace("-", "").toLowerCase();
		return afsign;
	}

	/**
	 * sha256加密
	 * 
	 * @param bts
	 * @return
	 */
	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

	private static List<Orders> getOrders() {
		List<Orders> listOrders = new ArrayList<Orders>();
		OrdersBean bean = new OrdersBean();
		String startDate = "2016-11-11 00:00:00";
		String endDate = "2016-12-21 00:00:00";
		listOrders = bean.qureyOrdersByTypeDatenew("'"
				+ Constants.PAY_TYPE_MYCARD_TW_BILLING + "'", startDate,
				endDate);
		try {
			listOrders.addAll(bean.qureyOrdersByTypeDatenew("'"
					+ Constants.ZHIGUAN_ATM + "'", startDate, endDate));
		} catch (Exception e) {
			System.out.println("ZHIGUAN_ATM order is empty");
		}
		try {
			listOrders.addAll(bean.qureyOrdersByTypeDatenew("'"
					+ Constants.ZHIGUAN_BIANLI + "'", startDate, endDate));
		} catch (Exception e) {
			System.out.println("ZHIGUAN_BIANLI order is empty");
		}
		try {
			listOrders.addAll(bean.qureyOrdersByTypeDatenew("'"
					+ Constants.ZHIGUAN_DIANXIN + "'", startDate, endDate));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ZHIGUAN_DIANXIN order is empty");
		}
		try {
			listOrders.addAll(bean.qureyOrdersByTypeDatenew("'"
					+ Constants.ZHIGUAN_FAMILY + "'", startDate, endDate));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ZHIGUAN_FAMILY, order is empty");
		}
		try {
			listOrders.addAll(bean.qureyOrdersByTypeDatenew("'"
					+ Constants.ZHIGUAN_XINYONG + "'", startDate, endDate));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ZHIGUAN_XINYONG, order is empty");
		}
		try {
			listOrders.addAll(bean.qureyOrdersByTypeDatenew("'"
					+ Constants.ZHIGUAN_SHITI + "'", startDate, endDate));
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("ZHIGUAN_SHITI, order is empty");
		}
		return listOrders;
	}
}

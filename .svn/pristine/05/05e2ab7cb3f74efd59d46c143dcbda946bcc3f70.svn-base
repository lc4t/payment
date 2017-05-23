package noumena.payment.onestore;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.tstore.TStoreEOrderVO;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class OneStoreCharge {
	// private static OneStoreParams params = new OneStoreParams();
	private static String oneStoreCheckUrl = "https://iap.tstore.co.kr/digitalsignconfirm.iap";

	public static String getTransactionId(Orders order) {
		// order.setCurrency(Constants.CURRENCY_USD);
		order.setUnit(Constants.CURRENCY_UNIT_FEN);
		order.setCurrency(Constants.CURRENCY_KRW);
		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + Constants.PAY_TYPE_ONESTORE;
			} else {
				cburl += "&pt=" + Constants.PAY_TYPE_ONESTORE;
			}
			cburl += "&currency=" + Constants.CURRENCY_KRW;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	/***
	 * 检查订单状态
	 * 
	 * @param txid
	 * @param appid
	 * @param signdata
	 * @return
	 */
	public static String verifyReceipt(String payid, String txid, String appid,
			String signdata) {
		OrderStatusVO st = new OrderStatusVO();
		st.setPayId(payid);
		st.setStatus(2);
		;
		System.out.println(payid + "OneStore check param =====("
				+ DateUtil.getCurTimeStr() + ")============>txid" + txid
				+ "===>appid" + appid + "===>signdata" + signdata);
		OrdersBean bean = new OrdersBean();
		// 从数据库中获取订单信息
		Orders orders = bean.qureyOrder(payid);

		if (orders == null) {
			System.out.println(payid + "OneStore orders =====("
					+ DateUtil.getCurTimeStr()
					+ ")============>orders is not exit");
			st.setStatus(2);
			return JSONObject.fromObject(st).toString();
		}
		st.setAmount(String.valueOf(orders.getAmount()));
		System.out.println(payid + "OneStore orders =====("
				+ DateUtil.getCurTimeStr() + ")============>" + orders);
		if (orders.getKStatus() == Constants.K_STSTUS_SUCCESS) {
			// 如果订单已经成功，直接返回订单状态
			st.setStatus(1);
			System.out.println(payid + "OneStore check result  =====("
					+ DateUtil.getCurTimeStr() + ")===========txid" + txid
					+ "orders is already succeed");
			return JSONObject.fromObject(st).toString();
		}
		System.out.println(payid + "OneStore check param =====("
				+ DateUtil.getCurTimeStr() + ")============>order1 txid"
				+ orders.getPayId() + "===>appid" + appid + "===>signdata"
				+ orders.getExInfo());
		// 去onestore提供的接口获取信息
		if (orders.getPayId() == null || "".equals(orders.getPayId())) {
			orders.setExInfo(signdata);
			orders.setPayId(txid);
			bean.updateOrder(orders.getOrderId(), orders);
		}
		System.out.println(payid + "OneStore check param =====("
				+ DateUtil.getCurTimeStr() + ")============>order1 txid"
				+ orders.getPayId() + "===>appid" + appid + "===>signdata"
				+ orders.getExInfo());
		String result = callOneStoreInterence(orders.getPayId(), appid,
				orders.getExInfo());
		System.out.println(payid + "OneStore callback  =====("
				+ DateUtil.getCurTimeStr() + ")============txid" + txid + " "
				+ result);
		JSONObject obj = JSONObject.fromObject(result);
		// 开始判断相关状态
		String status = obj.getString("status");
		// 请求返回的结果是不正常的
		if ("9".equals(status)) {
			System.out.println(payid + "OneStore check result  =====("
					+ DateUtil.getCurTimeStr() + ")==========txid" + txid + " "
					+ getFailInfo(obj));
			st.setStatus(2);
			bean.updateOrderKStatus(payid, Constants.K_STSTUS_ERROR);
			return JSONObject.fromObject(st).toString();
			// 请求成功的状态
		} else if ("0".equals(status)) {

			// 先判断一下金额产品是否对的上
			return updateOrderStatus(st, orders, txid, bean, obj);
		}
		return JSONObject.fromObject(st).toString();
	}

	public static String checkOrdersStatus(String payIds) {
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0; i < orders.size(); i++) {
			Orders order = orders.get(i);
			order.setProductId(order.getItemId());
			order.setSubId(order.getExInfo());
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					st.setStatus(4);
				}
				else
				{
					st.setStatus(3);
				}
			}else if (status == Constants.K_STSTUS_SUCCESS) {
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

	/***
	 * 判断订单信息并更新订单状态
	 * 
	 * @param st
	 * @param order
	 * @param orderid
	 * @param bean
	 * @param obj
	 * @return
	 */
	private static String updateOrderStatus(OrderStatusVO st, Orders order,
			String txid, OrdersBean bean, JSONObject obj) {
		System.out.println(order.getOrderId() + "OneStore orders =====("
				+ DateUtil.getCurTimeStr() + ")============>orders " + order);
		JSONArray orderData = JSONArray.fromObject(obj.get("product"));
		JSONObject producesInfo = JSONObject.fromObject(orderData.get(0));
		if (orderData != null && !orderData.isEmpty()) {
			if (!producesInfo.getString("product_id").equals(order.getItemId())) {
				System.out.println(order.getOrderId()
						+ "OneStore check result  =====("
						+ DateUtil.getCurTimeStr() + ")==========txid" + txid
						+ " " + "product_id is different====>product_id"
						+ producesInfo.getString("product_id") + "order"
						+ order.getItemId());
				st.setStatus(2);
				return JSONObject.fromObject(st).toString();
			}
			if (Float.parseFloat(producesInfo.getString("charge_amount")) != order
					.getAmount()) {
				System.out.println(order.getOrderId()
						+ "OneStore check result  =====("
						+ DateUtil.getCurTimeStr() + ")==========txid" + txid
						+ " " + "charge_amount is different====>product_id"
						+ producesInfo.getString("charge_amount") + "order"
						+ order.getAmount());
				st.setStatus(2);
				return JSONObject.fromObject(st).toString();
			}

		}
		// 更改订单状态到成功
		if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
			bean.updateOrderAmountPayIdExinfo(order.getOrderId(), txid,
					producesInfo.getString("charge_amount"), "");
			bean.updateOrderKStatus(order.getOrderId(),
					Constants.K_STSTUS_SUCCESS);
			st.setStatus(1);
			System.out.println(order.getOrderId()
					+ "OneStore check result  =====("
					+ DateUtil.getCurTimeStr() + ")==========txid" + txid
					+ " is ok");
			return JSONObject.fromObject(st).toString();
		}
		System.out.println(order.getOrderId() + "OneStore check result  =====("
				+ DateUtil.getCurTimeStr() + ")==========txid" + txid
				+ " is fail");
		st.setTid(txid);
		st.setStatus(2);
		return JSONObject.fromObject(st).toString();
	}

	/***
	 * 根据状态获取
	 * 
	 * @param obj
	 * @return
	 */
	private static String getFailInfo(JSONObject obj) {
		String detail = obj.getString("detail");

		if ("1000".equals(detail)) {
			ResultBean resultBean = new ResultBean(
					false,
					"Required parameters insufficiency(Code that occurs when the parameters among TXId, APPID and signdata are insufficient when requesting the search",
					"缺少必要的参数");
			return JSONObject.fromObject(resultBean).toString();
		}
		if ("9100".equals(detail)) {
			ResultBean resultBean = new ResultBean(
					false,
					"No Purchase History(In case of no purchase history through signdata)",
					"没有该支付记录");
			return JSONObject.fromObject(resultBean).toString();
		}
		if ("9113".equals(detail)) {
			ResultBean resultBean = new ResultBean(false,
					"E-Receipt Data Validity Verification Error",
					"验证信息错误，应该是signdata 不正确");
			return JSONObject.fromObject(resultBean).toString();
		}
		if ("9999".equals(detail)) {
			ResultBean resultBean = new ResultBean(
					false,
					" SYSTEM ERROR(It is an internal error of IAP. In this case, it is required to ask for the Operation Team.)",
					"棒槌的系统炸了");
			return JSONObject.fromObject(resultBean).toString();
		} else {
			ResultBean resultBean = new ResultBean(false, "I don't know",
					"未知的错误代码");
			return JSONObject.fromObject(resultBean).toString();
		}

	}

	private static String callOneStoreInterence(String txid, String appid,
			String receipt) {
		System.out.println("txid: " + txid + " appid:" + appid + " receipt:"
				+ receipt);
		TStoreEOrderVO ordervo = new TStoreEOrderVO();
		ordervo.setAppid(appid);
		ordervo.setSigndata(receipt);
		ordervo.setTxid(txid);

		String urlstr = oneStoreCheckUrl;
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			connection.setRequestProperty("Content-type", "application/json");
			OutputStreamWriter outs = new OutputStreamWriter(
					connection.getOutputStream());
			JSONObject json = JSONObject.fromObject(ordervo);
			outs.write(json.toString());
			outs.flush();
			outs.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null) {
				res += line;
			}

			connection.disconnect();
			System.out.println("callOneStoreInterence get token ret ->" + txid
					+ " " + res);
			json = JSONObject.fromObject(res);

			String path = OSUtil.getRootPath() + "../../logs/onestore/"
					+ DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + txid;
			OSUtil.saveFile(filename, res);
			return json.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "";
	}

	/****
	 * 请求oneStore提供的接口获得支付信息
	 * 
	 * @param txid
	 * @param appid
	 * @param signData
	 */

	public static void main(String args[]) {

		String aString = callOneStoreInterence(

				"TX_00000000385734",
				"OA00705921",
				"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");
		System.out.println(aString);

		// 0c8865930fc94d8d61b1bdcc8312a65d
		/*
		 * String res = ""; try { URL url = new URL(
		 * "http://game.jinggle.net:8080/payment/kongzhong/notice?orderid=J201617370802000001&goodid=SHIPHUNTER_DIAMOND_01&goodname=花费6购买60钻石&serverid=200&userid=62733206723035136&pt=5049&currency=RMB&unit=100&status=1&payId=2016062217371432508Z&flowid=2016062221001004650271412320&payrealprice=6.00"
		 * ); System.out.println(url); HttpURLConnection connection =
		 * (HttpURLConnection) url .openConnection();
		 * 
		 * connection.setDoOutput(true); connection.setDoInput(true);
		 * connection.setInstanceFollowRedirects(false);
		 * connection.setUseCaches(false);
		 * connection.setRequestProperty("Connection", "Keep-Alive");
		 * connection.setRequestMethod("GET");
		 * connection.setRequestProperty("Content-Type",
		 * "application/x-www-form-urlencoded");
		 * connection.setRequestProperty("charset", "utf-8");
		 * 
		 * connection.connect();
		 * 
		 * DataOutputStream wr = new DataOutputStream(
		 * connection.getOutputStream()); wr.flush(); wr.close();
		 * 
		 * BufferedReader in = new BufferedReader(new InputStreamReader(
		 * connection.getInputStream())); String line = null; while ((line =
		 * in.readLine()) != null) { res += line; } connection.disconnect();
		 * 
		 * } catch (Exception e) { e.printStackTrace(); }
		 * System.out.println(res);
		 */
	}

}

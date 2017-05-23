package noumena.payment.newmycard;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.MessageDigest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.CallbackBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.TrustAnyTrustManager;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class NewMyCardTWCharge {
	private static boolean isTest = false;
	private static String key = "eD8mkUBBm5GXLaP6Fk8lhKKBQXAgNAeA";
	private static String testPayAuthReceiptUrl = "https://test.b2b.mycard520.com.tw/MyBillingPay/api/AuthGlobal";
	private static String payAuthReceiptUrl = "https://b2b.mycard520.com.tw/MyBillingPay/api/AuthGlobal";
	private static String testRecipeResultUrl = "https://test.b2b.mycard520.com.tw/MyBillingPay/api/TradeQuery";
	private static String recipeResultUrl = "https://b2b.mycard520.com.tw/MyBillingPay/api/TradeQuery";
	private static String testRemoveMoneyUrl = "https://test.b2b.mycard520.com.tw/MyBillingPay/api/PaymentConfirm";
	private static String removeMoneyUrl = "https://b2b.mycard520.com.tw/MyBillingPay/api/PaymentConfirm";
	
	
	
	
/*	
	*//***
	 * 网页支付回调
	 * @param m
	 * @return
	 *//*
	public static String getCallbackFromNewMycardWebCb(Map<String, String> m) {
		
	}*/
	/***
	 * 获取订单号并给与授权码
	 * 
	 * @param order
	 * @param productName
	 * @return
	 */
	public static String getTransactionId(Orders order, String productName,
			String trainType, String cuurrency, String facServiceId,
			String serverid, String paymentType) {
		if (serverid == null) {
			serverid = "";
		}
		if (paymentType == null) {
			paymentType = "";
		}
		if (order.getItemId() == null) {
			order.setItemId("");
		}
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO("error", date);
		try {

			order.setCurrency(cuurrency);
			OrdersBean bean = new OrdersBean();
			String cburl = order.getCallbackUrl();
			String payId;
			if (cburl == null || cburl.equals("")) {
				payId = bean.CreateOrder(order);
			//	payId = bean.CreateOrder(order, cburl);
			} else {
				order.setUnit(Constants.CURRENCY_UNIT_YUAN);
				payId = bean.CreateOrder(order, cburl);
			}
		//	payId = bean.CreateOrder(order, cburl);
			// 申请的实体
			MyCardPayAuthInputBean myCardPayAuthInputBean = new MyCardPayAuthInputBean(
					facServiceId, payId, trainType, serverid, order.getUId(),
					paymentType, "", productName, String.valueOf(order
							.getAmount()), cuurrency, String.valueOf(isTest),
					"");
			String respose = getMyCardPayAuthNum(myCardPayAuthInputBean, key);
			JSONObject resposeJson = JSONObject.fromObject(respose);
			String returnCode = resposeJson.getString("ReturnCode");

			if ("1".equals(returnCode)) {
				String authCode = resposeJson.getString("AuthCode");
				orderIdVO = new OrderIdVO(payId, date);
				order.setSign(authCode);
				bean.updateOrder(payId, order);
				orderIdVO.setMsg(respose);
				orderIdVO.setToken(respose);
				return JSONObject.fromObject(orderIdVO).toString();
			} else {
				orderIdVO.setToken(respose);
				orderIdVO.setMsg(resposeJson.getString("ReturnMsg"));
				return JSONObject.fromObject(orderIdVO).toString();
			}
		} catch (Exception e) {
			System.out.println("new my card getTransactionId=========="
					+ DateUtil.getCurTimeStr()
					+ "====================>respose is error" + e);
			return JSONObject.fromObject(orderIdVO).toString();
		}
	}

	/***
	 * 通过接口验证订单
	 * 
	 * @param payid
	 * @param authCode
	 * @return
	 */

	public static String checkOrdersInterance(String payid, String authCode) {
		System.out.println(DateUtil.getCurTimeStr()+"checkOrdersInterance enter"+payid);
		OrdersBean bean = new OrdersBean();
		OrderStatusVO st = new OrderStatusVO();
		st.setPayId(payid);
		Orders order = bean.qureyOrder(payid);
		System.out.println(DateUtil.getCurTimeStr()+"checkOrdersInterance getOrder"+payid);
		// 订单不存在
		if (order == null) {
			System.out
					.println(payid
							+ " checkOrdersInterance====================>order is null");
			st.setStatus(2);
			return JSONObject.fromObject(st).toString();
		}
		// 订单已经成功
		if (order.getKStatus() == Constants.K_STSTUS_SUCCESS) {
			System.out
					.println(payid
							+ " checkOrdersInterance====================>order is already succeed");
			// 如果订单已经成功，直接返回订单状态
			st.setStatus(1);
		}
		String recipeRespose = reciepOrders(authCode);
		JSONObject jsonObjectRecipe = JSONObject.fromObject(recipeRespose);
		String recipeResposeResultCode = jsonObjectRecipe
				.getString("ReturnCode");
		String recipepayResult = jsonObjectRecipe.getString("PayResult");
		String amount = jsonObjectRecipe.getString("Amount");
		String mycardNo = jsonObjectRecipe.getString("MyCardTradeNo");
		// String paymentType = jsonObjectRecipe.getString("PaymentType");
		String promoCode = jsonObjectRecipe.getString("PromoCode");
		String myCardType = jsonObjectRecipe.getString("MyCardType");
		String paymentType = jsonObjectRecipe.getString("PaymentType");
		CallbackBean callbackBean = new CallbackBean();
		Callback callback = callbackBean.qureyCallback(order.getOrderId());
		System.out
				.println(payid
						+ " checkOrdersInterance paymentType====================>order before "
						+ order.toString());
		System.out
				.println(payid
						+ " checkOrdersInterance paymentType====================>callback before "
						+ callback.getCallbackUrl());
		order.setScreenSize(paymentType);
		order = setPayType(myCardType, order, paymentType);
		order.setItemPrice(promoCode);
		// 修改回调地址

		String cburl = callback.getCallbackUrl();
		cburl += "&pt=" + order.getPayType();
		cburl += "&amount=" + Double.parseDouble("" + order.getAmount());
		cburl += "&currency=" + order.getCurrency();
		cburl += "&unit=" + order.getUnit();
		cburl += "&promoCode=" + promoCode;
		callback.setCallbackUrl(cburl);
		System.out
				.println(payid
						+ " checkOrdersInterance paymentType====================>order later "
						+ order.toString());
		System.out
				.println(payid
						+ " checkOrdersInterance paymentType====================>callback later "
						+ callback.getCallbackUrl());
		callbackBean.updateCallback(callback);
		bean.updateOrder(order.getOrderId(), order);
		// 订单验证不成功
		if (!"1".equals(recipeResposeResultCode)) {
			System.out
					.println(payid
							+ " checkOrdersInterance====================>reciepOrders request fail "
							+ recipeRespose);
			st.setStatus(2);

			return JSONObject.fromObject(st).toString();
		}
		// 订单验证失败
		if ("0".equals(recipepayResult)) {
			System.out
					.println(payid
							+ " checkOrdersInterance====================>reciepOrders pay fail "
							+ recipeRespose);
			st.setStatus(2);
			return JSONObject.fromObject(st).toString();
		}
		// 不是交易成功的状态
		if (!"3".equals(recipepayResult)) {
			System.out
					.println(payid
							+ " checkOrdersInterance====================>reciepOrders pay fail "
							+ recipeRespose);
			st.setStatus(3);
			return JSONObject.fromObject(st).toString();
		}
		String removeMoneyRespose = removeMoney(authCode, payid, amount,
				mycardNo);
		JSONObject jsonObjectRemove = JSONObject.fromObject(removeMoneyRespose);
		String removeResultCode = jsonObjectRemove.getString("ReturnCode");
		// 扣款成功
		if ("1".equals(removeResultCode)) {
			System.out
					.println(payid
							+ " checkOrdersInterance====================>order removeMoney success");
			// 如果扣款成功，直接返回订单状态
			st.setStatus(1);
			return JSONObject.fromObject(st).toString();
		}
		// 扣款失败状态
		System.out
				.println(payid
						+ " checkOrdersInterance====================>order removeMoney fail");
		st.setStatus(2);
		return JSONObject.fromObject(st).toString();
	}

	/***
	 * 设定支付方式
	 * 
	 * @param myCardType
	 * @param order
	 */
	private static Orders setPayType(String myCardType, Orders order,
			String paymentType) {
		// family
		Orders tempOrders = order;
		if ("18".equals(myCardType)) {
			tempOrders.setPayType(Constants.ZHIGUAN_FAMILY);
		}
		// 便利商店
		else if ("19".equals(myCardType)) {
			tempOrders.setPayType(Constants.ZHIGUAN_BIANLI);
		}
		// 实体
		else if ("INGAME".equals(paymentType) || "FA031".equals(paymentType)
				|| "HE0033".equals(paymentType)
				|| "COSTPOINT".equals(paymentType)) {
			tempOrders.setPayType(Constants.ZHIGUAN_SHITI);
		}
		// 信用
		else if ("DG0400004".equals(paymentType)) {
			tempOrders.setPayType(Constants.ZHIGUAN_XINYONG);
		}
		// 电信
		else if ("HE0004".equals(paymentType) || "HE0003".equals(paymentType)
				|| "HE0011".equals(paymentType) || "HE0001".equals(paymentType)
				|| "HE0021".equals(paymentType) || "FS0018".equals(paymentType)
				|| "HE0037".equals(paymentType) || "HE0034".equals(paymentType)) {
			tempOrders.setPayType(Constants.ZHIGUAN_DIANXIN);
		}
		return tempOrders;

	}

	/**
	 * 检验订单章台
	 * 
	 * @param payIds
	 * @return
	 */
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

	/****
	 * 执行扣款动作
	 * 
	 * @param authCode
	 * @return
	 */
	public static String removeMoney(String authCode, String orderId,
			String amount, String mycardNo) {
		// 验证必要参数
		if (authCode == null || "".equals(authCode)) {
			System.out
					.println(" reciepOrders====================>authCode is null");
			return "{\"result\":\"order is not exit\"}";
		}
		if (orderId == null || "".equals(orderId)) {
			System.out
					.println(" reciepOrders====================>orderId is null");
			return "{\"result\":\"order is not exit\"}";
		}
		JSONObject jsonObject = JSONObject.fromObject("{\"AuthCode\":\""
				+ authCode + "\"}");
		try {
			OrdersBean bean = new OrdersBean();
			Orders orders = bean.qureyOrder(orderId);
			// 订单不存在
			if (orders == null) {
				System.out.println(orderId
						+ " removeMoney====================>order is not exit");
				return "{\"result\":\"order is not exit\"}";
			}
			// 早已经的支付成功
			if (orders.getCStatus() == Constants.K_STSTUS_SUCCESS) {
				System.out.println(orderId
						+ " ====================>removeMoney alerdy succeed");
				return "{\"result\":\"alerdy succeed\"}";
			}
			String respose = callInterfacePost(getRemoveMoneyUrlUrl(),
					jsonObject);
			System.out.println("mycard removeMoney authcode" + authCode
					+ " orderid " + orderId + "======================>"
					+ respose);
			JSONObject jObject = JSONObject.fromObject(respose);
			String resultCode = jObject.getString("ReturnCode");
			String facTradeSeq = jObject.getString("FacTradeSeq");
			String tradeSeq = jObject.getString("TradeSeq");
			if (!facTradeSeq.equals(orderId)) {
				System.out.println(facTradeSeq);
				System.out.println(orderId);
				System.out.println(facTradeSeq.equals(orderId));
				System.out.println(orderId + "facTradeSeq" + facTradeSeq
						+ " orderId+" + orderId
						+ "====================>order is different");
				return "{\"result\":\"order is differen\"}";
			}
			// 扣款未成功
			if (!"1".equals(resultCode)) {
				return respose;
			}
			if (Float.parseFloat(amount) == orders.getAmount()) {
				bean.updateOrderAmountPayIdExinfo(facTradeSeq, tradeSeq,
						String.valueOf(amount), mycardNo);
				// 更新订单状态
				bean.updateOrderKStatus(facTradeSeq, Constants.K_STSTUS_SUCCESS);
				return respose;
			}
			System.out.println("orderid money different===============> order "
					+ orders.getAmount() + " amount" + amount);
			return "{\"result\":\"money different\"}";
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("orderid removeMoney error===============>");
			e.printStackTrace();

		}
		return "{\"result\":\"error\"}";
	}

	// 获取mycard交易授權碼的请求url
	private static String getRemoveMoneyUrlUrl() {
		if (isTest) {
			return testRemoveMoneyUrl;
		}
		return removeMoneyUrl;
	}

	/****
	 * 验证订单的结果
	 * 
	 * @param authCode
	 * @return
	 */
	public static String reciepOrders(String authCode) {
		if (authCode == null || "".equals(authCode)) {
			System.out
					.println(" reciepOrders====================>authCode is null");
			return "{\"result\":\"order is not exit\"}";
		}
		JSONObject jsonObject = JSONObject.fromObject("{\"AuthCode\":\""
				+ authCode + "\"}");
		try {
			String respose = callInterfacePost(getRecipeResultUrl(), jsonObject);
			System.out.println("mycard reciepOrders authcode" + authCode
					+ "======================>" + respose);
			JSONObject jObject = JSONObject.fromObject(respose);
			String resultCode = jObject.getString("ReturnCode");
			String mycardNo = jObject.getString("MyCardTradeNo");
			String payResult = jObject.getString("PayResult");
			String facTradeSeq = jObject.getString("FacTradeSeq");
			String amount = jObject.getString("Amount");
			// 交易成功更新参数
			if ("1".equals(resultCode) && "3".equals(payResult)) {
				System.out.println("mycard reciepOrders order id" + facTradeSeq
						+ "======================>mycardNo" + mycardNo
						+ " Amount" + amount);

			}
			return respose;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("reciepOrders error===============>");
			e.printStackTrace();

		}

		return "{\"result\":\"error\"}";
	}

	private static String getRecipeResultUrl() {
		if (isTest) {
			return testRecipeResultUrl;
		}
		return recipeResultUrl;
	}

	/***
	 * 获取mycard交易授權碼
	 * 
	 * @return
	 */
	private static String getMyCardPayAuthNum(
			MyCardPayAuthInputBean myCardPayAuthInputBean, String key) {
		try {
			String url = getMyCardPayAuthNumUrl();
			System.out.println("getMyCardPayAuthNum======================url"
					+ url);
			String hash;
			// 获取hash值
			hash = getMyCardPayAuthNumHash(myCardPayAuthInputBean, key);
			System.out.println(myCardPayAuthInputBean.getFacTradeSeq()
					+ "==============>hash" + hash);
			myCardPayAuthInputBean.setHash(hash);
			// 请求接口
			JSONObject obj = JSONObject.fromObject(myCardPayAuthInputBean);
			String respose = callInterfacePost(url, obj);
			System.out.println(myCardPayAuthInputBean.getFacTradeSeq()
					+ "getMyCardPayAuthNum==============>result" + respose);
			return respose;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println(myCardPayAuthInputBean.getFacTradeSeq()
					+ "getMyCardPayAuthNum==============>error" + e);
			return "";
		}
	}

	/***
	 * 请求接口
	 * 
	 * @param url
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	private static String callInterfacePost(String url, JSONObject obj)
			throws Exception {
		obj = formatJson(obj);
		String endUrl = getUrlAndParam(url, obj);
		System.out.println("url" + url + " JSONObject" + obj);
		System.out
				.println(DateUtil.getCurTimeStr() +"callInterfacePost==============================request url"
						+ endUrl);
		SSLContext context = SSLContext.getInstance("SSL");
		context.init(null, new TrustManager[] { new TrustAnyTrustManager() },
				new java.security.SecureRandom());
		HttpsURLConnection connection = (HttpsURLConnection) new URL(endUrl)
				.openConnection();
		connection.setSSLSocketFactory(context.getSocketFactory());
		connection.setDoOutput(true);
		connection.setDoInput(true);
		connection.setUseCaches(false);
		connection.setInstanceFollowRedirects(true);
		connection.setHostnameVerifier(new HostnameVerifier() {
			@Override
			public boolean verify(String arg0, SSLSession arg1) {
				// return true; //不验证
				return false; // 验证
			}
		});
		try {

			connection.connect();
		} catch (Exception e) {
			// TODO: handle exception
		}
		// 加入参数
		DataOutputStream out = new DataOutputStream(
				connection.getOutputStream());

		out.writeBytes(obj.toString());
		out.flush();
		out.close();
		BufferedReader in = new BufferedReader(new InputStreamReader(
				connection.getInputStream()));
		String res = "", line = null;
		while ((line = in.readLine()) != null) {
			res += line;
		}
		in.close();
		connection.disconnect();
		return res.toString();
	}

	private static String getUrlAndParam(String url, JSONObject jsonObject) {
		String urlTemp = url;
		int i = 0;
		@SuppressWarnings("unchecked")
		Map<String, String> map = jsonObject;
		Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry<String, String> entry = it.next();
			if (i == 0) {
				urlTemp = urlTemp + "?" + entry.getKey() + "="
						+ entry.getValue();
				i = i + 1;
			} else {
				urlTemp = urlTemp + "&" + entry.getKey() + "="
						+ entry.getValue();
			}

		}
		return urlTemp;
	}

	/***
	 * 获取驗證授權碼所需的hash值
	 * 
	 * @return
	 * @throws Exception
	 */
	private static String getMyCardPayAuthNumHash(
			MyCardPayAuthInputBean myCardPayAuthInputBean, String key)
			throws Exception {
		String preHashValue = myCardPayAuthInputBean.params(key);
		System.out.println("getMyCardPayAuthNumHash preHashValue"+preHashValue);
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] bt = preHashValue.getBytes("UTF-8");
		md.update(bt);
		String afsign = bytes2Hex(md.digest()); // to HexString
		afsign = afsign.replace("-", "").toLowerCase();
		return afsign;
	}

	// 获取mycard交易授權碼的请求url
	private static String getMyCardPayAuthNumUrl() {
		if (isTest) {
			return testPayAuthReceiptUrl;
		}
		return payAuthReceiptUrl;
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

	/***
	 * 补单流程的回调方法
	 * 
	 * @param ip
	 * @param returnCode
	 * @param facTradeSeq
	 * @return
	 */
	public static String alternateCb(String ip, String returnCode,
			String facTradeSeq) {
		System.out.println(facTradeSeq + "alternateCb==========="
				+ DateUtil.getCurTimeStr() + "===================>" + ip);
		if (!isIp(ip)) {
			System.out.println("alternateCb=========="
					+ DateUtil.getCurTimeStr()
					+ "====================>ip is error");
			return "{\"result\":\"ip is not exit\"}";
		}
		if (!"1".equals(returnCode)) {

			System.out.println("alternateCb==========="
					+ DateUtil.getCurTimeStr()
					+ "===================>ip is error");
			return "{\"result\":\"returnCode is not success\"}";
		}
		OrdersBean bean = new OrdersBean();
		String orderId[] = facTradeSeq.split(",");
		List<String> list = new ArrayList<String>();
		if (orderId != null) {
			for (int i = 0; i < orderId.length; i++) {
				String orderNow = orderId[i];
				orderNow = orderNow.replace("[", "");
				orderNow = orderNow.replace("]", "");
				orderNow = orderNow.replace("\"", "");
				orderNow = orderNow.replace(",", "");
				Orders orders = bean.qureyOrder(orderNow);
				System.out.println(orderNow + "alternateCb==============="
						+ DateUtil.getCurTimeStr() + "===============>"
						+ orders);
				if (orders == null) {
					System.out.println(orderNow + "alternateCb=============="
							+ DateUtil.getCurTimeStr()
							+ "================>order is not exit ");
					continue;
				}
				String respose = checkOrdersInterance(orders.getOrderId(),
						orders.getSign());
				System.out.println(orderNow + "alternateCb=============="
						+ DateUtil.getCurTimeStr() + "================>"
						+ respose);
				list.add(respose);
			}
		}
		return JSONArray.fromObject(list).toString();

	}

	/***
	 * 交易成功資料之差異比對
	 * 
	 * @return
	 */
	public static String compareCb(String ip, String startDateTime,
			String endDateTime, String myCardTradeNo,String gameId) {
		System.out.println("compareCb==========" + DateUtil.getCurTimeStr()
				+ "====================>" + ip);
		if (!isIp(ip)) {
			System.out.println("compareCb==========" + DateUtil.getCurTimeStr()
					+ "====================>ip is error");
			return "{\"result\":\"ip is not exit\"}";
		}

		String result = "";
		OrdersBean bean = new OrdersBean();
		// 根据时间查询
		if (startDateTime != null && !"".equals(startDateTime)
				&& endDateTime != null && !"".equals(endDateTime)) {
			System.out.println("compareCb==========" + DateUtil.getCurTimeStr()
					+ "====================>bean.qureyOrdersByTypeDate");
			SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			String startDateTimeFormat = startDateTime.replace("T", " ");
			String endDateTimeFormat = endDateTime.replace("T", " ");
			List<Orders> listOrders = new ArrayList<Orders>();
			try {
				listOrders = bean.qureyOrdersByTypeDate("'"
						+ Constants.PAY_TYPE_MYCARD_TW_BILLING + "'",
						formatter.format(formatter.parse(startDateTimeFormat)),
						formatter.format(formatter.parse(endDateTimeFormat)));
				try {
					listOrders
							.addAll(bean.qureyOrdersByTypeDate("'"
									+ Constants.ZHIGUAN_ATM + "'", formatter
									.format(formatter
											.parse(startDateTimeFormat)),
									formatter.format(formatter
											.parse(endDateTimeFormat))));
				} catch (Exception e) {
					System.out.println("ZHIGUAN_ATM order is empty");
				}
				try {
					listOrders
							.addAll(bean.qureyOrdersByTypeDate("'"
									+ Constants.ZHIGUAN_BIANLI + "'", formatter
									.format(formatter
											.parse(startDateTimeFormat)),
									formatter.format(formatter
											.parse(endDateTimeFormat))));
				} catch (Exception e) {
					System.out.println("ZHIGUAN_BIANLI order is empty");
				}
				try {
					listOrders
							.addAll(bean.qureyOrdersByTypeDate("'"
									+ Constants.ZHIGUAN_DIANXIN + "'",
									formatter.format(formatter
											.parse(startDateTimeFormat)),
									formatter.format(formatter
											.parse(endDateTimeFormat))));
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("ZHIGUAN_DIANXIN order is empty");
				}
				try {
					listOrders
							.addAll(bean.qureyOrdersByTypeDate("'"
									+ Constants.ZHIGUAN_FAMILY + "'", formatter
									.format(formatter
											.parse(startDateTimeFormat)),
									formatter.format(formatter
											.parse(endDateTimeFormat))));
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("ZHIGUAN_FAMILY, order is empty");
				}
				try {
					listOrders
							.addAll(bean.qureyOrdersByTypeDate("'"
									+ Constants.ZHIGUAN_XINYONG + "'",
									formatter.format(formatter
											.parse(startDateTimeFormat)),
									formatter.format(formatter
											.parse(endDateTimeFormat))));
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("ZHIGUAN_XINYONG, order is empty");
				}
				try {
					listOrders
							.addAll(bean.qureyOrdersByTypeDate("'"
									+ Constants.ZHIGUAN_SHITI + "'", formatter
									.format(formatter
											.parse(startDateTimeFormat)),
									formatter.format(formatter
											.parse(endDateTimeFormat))));
				} catch (Exception e) {
					// TODO: handle exception
					System.out.println("ZHIGUAN_SHITI, order is empty");
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return "{\"result\":\"time is error \"}";
			}
			listOrders=getListOrdersFliterChannel(listOrders, gameId);
			if (listOrders != null) {
				for (int i = 0; i < listOrders.size(); i++) {
					Orders orderTemp = listOrders.get(i);
					String resultTemp = orderTemp.getScreenSize() + ","
							+ orderTemp.getPayId() + ",";
					if (orderTemp.getExInfo() != null) {
						resultTemp = resultTemp
								+ orderTemp.getExInfo().replace("null#", "")
								+ ",";
					}
					resultTemp = resultTemp + orderTemp.getOrderId() + ","
							+ orderTemp.getUId() + "," + orderTemp.getAmount()
							+ "," + orderTemp.getCurrency() + ","
							+ orderTemp.getUpdateTime().replace(" ", "T")
							+ "<BR>";
					System.out.println("compareCb=========="
							+ DateUtil.getCurTimeStr()
							+ "====================>order" + resultTemp);
					result = result + resultTemp;
				}

			}

		}
		// 根据myCardTradeNo去查询
		if (myCardTradeNo != null && !"".equals(myCardTradeNo)) {
			System.out
					.println("compareCb=========="
							+ DateUtil.getCurTimeStr()
							+ "====================>bean.qureyOrdersByTypeEx  myCardTradeNo");
			// 查询所有有关的mycard的订单
			List<Orders> listOrders = bean
					.qureyOrdersByTypeEx("'"
							+ Constants.PAY_TYPE_MYCARD_TW_BILLING + "'",
							myCardTradeNo);
			try {
				listOrders.addAll(bean.qureyOrdersByTypeEx("'"
						+ Constants.ZHIGUAN_ATM + "'", myCardTradeNo));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ZHIGUAN_ATM order is empty");
			}
			try {
				listOrders.addAll(bean.qureyOrdersByTypeEx("'"
						+ Constants.ZHIGUAN_BIANLI + "'", myCardTradeNo));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ZHIGUAN_BIANLI order is empty");
			}
			try {
				listOrders.addAll(bean.qureyOrdersByTypeEx("'"
						+ Constants.ZHIGUAN_DIANXIN + "'", myCardTradeNo));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ZHIGUAN_DIANXIN order is empty");
			}
			try {
				listOrders.addAll(bean.qureyOrdersByTypeEx("'"
						+ Constants.ZHIGUAN_FAMILY + "'", myCardTradeNo));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ZHIGUAN_FAMILY order is empty");
			}
			try {
				listOrders.addAll(bean.qureyOrdersByTypeEx("'"
						+ Constants.ZHIGUAN_SHITI + "'", myCardTradeNo));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ZHIGUAN_SHITI order is empty");
			}
			try {
				listOrders.addAll(bean.qureyOrdersByTypeEx("'"
						+ Constants.ZHIGUAN_XINYONG + "'", myCardTradeNo));
			} catch (Exception e) {
				// TODO: handle exception
				System.out.println("ZHIGUAN_XINYONG order is empty");
			}
			listOrders=getListOrdersFliterChannel(listOrders, gameId);
			if (listOrders != null) {
				for (int i = 0; i < listOrders.size(); i++) {
					Orders orderTemp = listOrders.get(i);
					String resultTemp = orderTemp.getScreenSize() + ","
							+ orderTemp.getPayId() + ",";
					if (orderTemp.getExInfo() != null) {
						resultTemp = resultTemp
								+ orderTemp.getExInfo().replace("null#", "")
								+ ",";
					}
					resultTemp = resultTemp + orderTemp.getOrderId() + ","
							+ orderTemp.getUId() + "," + orderTemp.getAmount()
							+ "," + orderTemp.getCurrency() + ","
							+ orderTemp.getUpdateTime().replace(" ", "T")
							+ "<BR>";
					System.out.println("compareCb=========="
							+ DateUtil.getCurTimeStr()
							+ "====================>order" + resultTemp);
					result = result + resultTemp;
				}

			}
		}
		System.out.println("compareCb==========" + DateUtil.getCurTimeStr()
				+ "====================>result" + result);
		return result;
	}

	public static JSONObject formatJson(JSONObject orgJson) {
		JSONObject jo = new JSONObject();
		try {
			JSONObject jsonObject = orgJson;
			@SuppressWarnings("unchecked")
			Iterator<String> iterator = jsonObject.keys();

			while (iterator.hasNext()) {
				String key = (String) iterator.next();
				char chars[] = key.toCharArray();
				if (key.length() > 1 && Character.isLowerCase(key.charAt(0))) {
					chars[0] = Character.toUpperCase(chars[0]);
				}

				Boolean bl = jsonObject.isEmpty();
				if (bl) {
					jo.put(new String(chars), null);
				} else {
					Object object = jsonObject.get(key);
					try {
						if (object instanceof Number) {
							// Log.i("MainActivity-----------------",
							// "result:1");
							jo.put(new String(chars),
									((Number) object).intValue());
						} else {
							// Log.i("MainActivity-----------------",
							// "result:2");
							jo.put(new String(chars), jsonObject.getString(key));
						}
					} catch (Exception e) {
						jo.put(new String(chars), jsonObject.getString(key));
					}
				}
			}
		} catch (Exception e) {

			e.printStackTrace();
		}
		return jo;
	}

	private static String getPayTypeIdById(String id) {
		if (id.equals(Constants.PAY_TYPE_MYCARD_TW_BILLING)) {
			return "默認記錄";
		} else if (id.equals(Constants.ZHIGUAN_ATM)) {
			return "智冠_ATM";
		} else if (id.equals(Constants.ZHIGUAN_BIANLI)) {
			return "智冠_便利商店";
		} else if (id.equals(Constants.ZHIGUAN_FAMILY)) {
			return "智冠_便利商店（Family）";
		} else if (id.equals(Constants.ZHIGUAN_DIANXIN)) {
			return "智冠_電信賬單";
		} else if (id.equals(Constants.ZHIGUAN_SHITI)) {
			return "智冠_實體通路";
		} else if (id.equals(Constants.ZHIGUAN_XINYONG)) {
			return "智冠_信用卡";
		} else {
			return "";
		}
	}

	private static List<Orders> getListOrdersFliterChannel(List<Orders> orders,String gameId){
		List<Orders> listOrders=orders;
		if(listOrders!=null&&!listOrders.isEmpty()){
			Iterator<Orders> iterator = listOrders.iterator(); 
			while(iterator.hasNext()){
				Orders order = iterator.next();
				String channel=order.getChannel();
				try {
					String channelNum=channel.substring(0, 3);	
					if(!channelNum.contains(gameId)){
						iterator.remove();
					}
				} catch (Exception e) {
					// TODO: handle exception
					iterator.remove();
				}
				}
		}
		return listOrders;
	}

	/***
	 * 回调检验是否是有效ip
	 * 
	 * @param ip
	 * @return
	 */
	private static boolean isIp(String ip) {
		if ("218.32.37.148".equals(ip) || "220.130.127.125".equals(ip)||"40.83.124.36".equals(ip)) {
			return true;
		}
		return true;
	}

	public static void main(String args[]) throws UnsupportedEncodingException {

	}
}

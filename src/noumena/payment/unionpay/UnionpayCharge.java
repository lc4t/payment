package noumena.payment.unionpay;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import com.unionpay.upmp.sdk.conf.UpmpConfig;
import com.unionpay.upmp.sdk.service.UpmpService;

public class UnionpayCharge {
	private static UnionpayParams params = new UnionpayParams();
	private static boolean testmode = false;

	public static boolean isTestmode() {
		return testmode;
	}

	public static void setTestmode(boolean testmode) {
		UnionpayCharge.testmode = testmode;
	}

	public static String getTransactionId(Orders order) {
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_FEN);

		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + Constants.PAY_TYPE_UNIONPAY;
			} else {
				cburl += "&pt=" + Constants.PAY_TYPE_UNIONPAY;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_FEN;

			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		orderIdVO.setMsg(getTransId(payId, order.getAmount()));

		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	private static String getTransId(String orderid, float amount) {
		String ret = "";
		// 请求要素
		Map<String, String> req = new HashMap<String, String>();
		req.put("version", UpmpConfig.VERSION);// 版本号
		req.put("charset", UpmpConfig.CHARSET);// 字符编码
		req.put("transType", "01");// 交易类型
		req.put("merId", UpmpConfig.MER_ID);// 商户代码
		req.put("backEndUrl", UpmpConfig.MER_BACK_END_URL);// 通知URL
		// req.put("frontEndUrl", UpmpConfig.MER_FRONT_END_URL);// 前台通知URL(可选)
		req.put("orderDescription", "订单描述");// 订单描述(可选)
		req.put("orderTime",
				new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()));// 交易开始日期时间yyyyMMddHHmmss
		req.put("orderTimeout", "");// 订单超时时间yyyyMMddHHmmss(可选)
		while (orderid.length() < 8) {
			orderid = "0" + orderid;
		}
		req.put("orderNumber", orderid);// 订单号(商户根据自己需要生成订单号)
		req.put("orderAmount", ((int) amount) + "");// 订单金额
		req.put("orderCurrency", "156");// 交易币种(可选)
		req.put("reqReserved", "透传信息");// 请求方保留域(可选，用于透传商户信息)

		// 保留域填充方法
		Map<String, String> merReservedMap = new HashMap<String, String>();
		merReservedMap.put("test", "test");
		req.put("merReserved", UpmpService.buildReserved(merReservedMap));// 商户保留域(可选)

		Map<String, String> resp = new HashMap<String, String>();
		boolean validResp = UpmpService.trade(req, resp);

		// 商户的业务逻辑
		if (validResp) {
			// 服务器应答签名验证成功
			ret = resp.get("tn");
		} else {
			// 服务器应答签名验证失败

		}

		return ret;
	}

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

	public static String getCallbackFromUnionpay(Map<String, String> params,
			String transStatus, String orderNumber) {
		String ret = "success";

		while (orderNumber.substring(0, 1).equals("0")) {
			orderNumber = orderNumber.substring(1);
		}

		if (UpmpService.verifySignature(params)) {
			OrdersBean bean = new OrdersBean();
			// 服务器签名验证成功
			if (null != transStatus && transStatus.equals("00")) {
				// 交易处理成功
				try {
					// 支付成功
					Orders order = bean.qureyOrder(orderNumber);
					if (order != null) {
						if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
							bean.updateOrderAmountPayIdExinfo(orderNumber,
									params.get("qn"),
									params.get("settleAmount"),
									params.get("settleCurrency"));
							bean.updateOrderKStatus(orderNumber,
									Constants.K_STSTUS_SUCCESS);
						} else {
							System.out
									.println("unionpay order ("
											+ order.getOrderId()
											+ ") had been succeed");
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				// 支付失败
				bean.updateOrderKStatus(orderNumber, Constants.K_STSTUS_ERROR);
			}

			ret = "success";
		} else {
			// 服务器签名验证失败
			ret = "fail";
		}

		System.out.println("unionpay cb ->" + params.toString());
		System.out.println("unionpay cb ret->" + ret);

		String path = OSUtil.getRootPath() + "../../logs/unionpaycb/"
				+ DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + orderNumber;

		OSUtil.saveFile(filename, params.toString());

		return ret;
	}

	public static String queryOrderFromUnionpay(String querydate,
			String queryorderid) {
		String ret = "success";

		Map<String, String> req = new HashMap<String, String>();
		req.put("version", UpmpConfig.VERSION);// 版本号
		req.put("charset", UpmpConfig.CHARSET);// 字符编码
		req.put("transType", "01");// 交易类型
		req.put("merId", UpmpConfig.MER_ID);// 商户代码
		req.put("orderTime", querydate);// 交易开始日期时间yyyyMMddHHmmss或yyyyMMdd
		req.put("orderNumber", queryorderid);// 订单号
		// 保留域填充方法
		Map<String, String> merReservedMap = new HashMap<String, String>();
		merReservedMap.put("test", "test");
		req.put("merReserved", UpmpService.buildReserved(merReservedMap));// 商户保留域(可选)

		Map<String, String> resp = new HashMap<String, String>();
		boolean validResp = UpmpService.query(req, resp);

		// 商户的业务逻辑
		if (validResp) {
			// 服务器应答签名验证成功
			System.out.println("union pay query ret->" + resp.toString());
			ret = resp.toString();
		} else {
			// 服务器应答签名验证失败

		}
		return ret;
	}

	public static String getNewCallbackFromUnionpay(Map<String, String> params) {
		try {
			String orderId = params.get("orderId");// 10. 商户订单号 R
			String queryId = params.get("queryId");// 16. 交易查询流水号 M
													// 消费交易的流水号，供后续查询用
			String txnAmt = params.get("txnAmt");// 12. 交易金额 R
			/*
			 * String version =params.get("version");//版本号 String
			 * encoding=params.get("encoding");//编码方式 String
			 * signature=params.get("signature");// 签名 M String
			 * signMethod=params.get("signMethod");//4. 签名方法 M String
			 * txnType=params.get("txnType");//5. 交易类型 R String
			 * txnSubType=params.get("txnSubType");//6. 交易子类 R String
			 * bizType=params.get("bizType");//7. 产品类型 R String
			 * accessType=params.get("accessType");//8. 接入类型 R String
			 * merId=params.get("merId");//9. 商户代码 R String
			 * txnTime=params.get("txnTime");//11. 订单发送时间 R String
			 * currencyCode=params.get("currencyCode");// 13. 交易币种 M 默认为 156
			 * String reqReserved=params.get("reqReserved");// 14. 请求方保留域 R
			 * String reserved=params.get("reserved");//15. 保留域 O String
			 * respCode=params.get("respCode");//17. 响应码 M String
			 * respMsg=params.get("respMsg");//18. 响应信息 M String
			 * settleAmt=params.get("settleAmt");//19. 清算金额 M String
			 * settleCurrencyCode=params.get("settleCurrencyCode");//20. 清算币种 M
			 * String settleDate=params.get("settleDate");//21. 清算日期 M String
			 * traceNo=params.get("traceNo");//22. 系统跟踪号 M String
			 * traceTime=params.get("traceTime");//23. 交易传输时间 M String
			 * exchangeDate=params.get("exchangeDate");//24. 兑换日期 C
			 * 交易成功，交易币种和清算币种不一致的时候返回 String
			 * exchangeRate=params.get("exchangeRate");//25. 汇率 C
			 * 交易成功，交易币种和清算币种不一致的时候返回 String accNo=params.get("accNo");//26. 账号
			 * C 根据商户配置返回 String payCardType=params.get("payCardType");//27.
			 * 支付卡类型 C 根据商户配置返回 String payType=params.get("payType");//28. 支付方式
			 * C 根据商户配置返回 String payCardNo=params.get("payCardNo");//29. 支付卡标识 C
			 * 移动支付交易时，根据商户配置返回 String
			 * payCardIssueName=params.get("payCardIssueName");//30. 支付卡名称 C
			 * 移动支付交易时，根据商户配置返回 String bindId=params.get("bindId");//31. 绑定标识号 R
			 * 绑定支付时，根据商户配置返回 String
			 * signPubKeyCert=params.get("signPubKeyCert");//32. 签名公钥证书 C 使用 RSA
			 * 签名方式时必选，此域填写银联签名公钥证书。 String
			 * accSplitData=params.get("accSplitData");//33. 分账域
			 */System.out.println(orderId + "unionpay cb ->"
					+ params.toString());
			if (!UpmpService.verifySignature(params)) {
				System.out.println(orderId + "newunionpay cb  sign is error");
				return "fail";
			}
			OrdersBean bean = new OrdersBean();
			// 支付成功
			Orders order = bean.qureyOrder(orderId);
			if (order == null) {
				System.out.println(orderId
						+ "newunionpay cb  order is not exit");
				return "fail";
			}
			if (order.getKStatus() == Constants.K_STSTUS_SUCCESS) {

				System.out.println(orderId
						+ "newunionpay cb  order is already success");
				return "success";
			}

			if (order.getKStatus() != Constants.K_STSTUS_DEFAULT) {
				System.out.println(orderId + "newunionpay cb  order is repeat");
				return "fail";
			}

			bean.updateOrderAmountPayIdExinfo(orderId, queryId, txnAmt,
					JSONObject.fromObject(params).toString());
			bean.updateOrderKStatus(orderId, Constants.K_STSTUS_SUCCESS);
			return "success";

		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("newunionpay cb  error");
			return "fail";
		}

	}

	public static void init() {
		params.initParams(UnionpayParams.CHANNEL_ID, new UnionpayOrderVO());
		// params.addApp("gaoguai", "4038",
		// "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPtxJwSjpyMd23NhJqVR3o7QvA/0ZXhNBxmZ29FBsH/DwjxuhcYYz08GIWRF6AT0Watxu2In9M7Rnbecyy4uyT9gJG4WWbBCPa592+N7i88/zpP18equvHUT+US833pjw4jBebFOEAgGATC4rTbqjNiH5TXbcZrfFHkxapNsuYiwIDAQAB");
		// //2ec36f47fb4714a5971112a2e80a54cf
	}
}

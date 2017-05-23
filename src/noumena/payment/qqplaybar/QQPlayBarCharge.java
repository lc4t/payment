package noumena.payment.qqplaybar;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.getorders.OrderUtil;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderIdVO;

/***
 * QQ玩吧的支付
 * 
 * @author kz
 * 
 */
public class QQPlayBarCharge {
	private static String buyPlayZoneItemUrl = "http://openapi.tencentyun.com/v3/user/buy_playzone_item";
	private static String testBuyPlayZoneItemUrl = "http://119.147.19.43/v3/user/buy_playzone_item";
	private static String buyPlayZoneItemAfterUrl = "/v3/user/buy_playzone_item";
	private static String appkey = "";
	private static boolean istest = false;

	/***
	 * 获取玩吧的订单信息
	 * 
	 * @param order
	 * @param payType
	 * @param openid
	 * @param openkey
	 * @param appid
	 * @param sig
	 * @param pf
	 * @param format
	 * @param userip
	 * @param zoneid
	 * @param itemid
	 * @param count
	 * @return
	 */
	public static String getPlayBarPayId(Orders order, String payType,
			String openid, String openkey, String appid, String sig, String pf,
			String format, String userip, String zoneid, String itemid,
			String count) {
		// 获取接口的支付信息
		String callPlayBarResponse = callPlayBarPayInterance(openid, openkey,
				appid, pf, format, userip, zoneid, itemid, count);
		JSONObject jsonObjectResponse = JSONObject
				.fromObject(callPlayBarResponse);
		String code = jsonObjectResponse.getString("code");
		OrdersBean bean = new OrdersBean();
		if (code.equals("0")) {
			// 成功的情况
			String dataString = jsonObjectResponse.getString("data");
			String billno = JSONObject.fromObject(dataString).getString(
					"billno");
			String respose = OrderUtil.getTransactionId(order, "",
					callPlayBarResponse);
			String payid = JSONObject.fromObject(respose).getString("payId");
			Orders orderBean = bean.qureyOrder(payid);
			orderBean.setPayId(billno);
			bean.updateOrder(payid, orderBean);
			return respose;
		} else {
			// qq玩吧返回错误信息
			System.out.println("QQPlayBarCharge result=====================>"
					+ callPlayBarResponse);
			String date = DateUtil.formatDate(order.getCreateTime());
			OrderIdVO orderIdVO = new OrderIdVO("", date);
			orderIdVO.setMsg(callPlayBarResponse);
			return JSONObject.fromObject(orderIdVO).toString();
		}

	}

	/***
	 * 请求qqplay的接口
	 * 
	 * @param openid
	 * @param openkey
	 * @param appid
	 * @param sig
	 * @param pf
	 * @param format
	 * @param userip
	 * @param zoneid
	 * @param itemid
	 * @param count
	 * @return
	 */
	public static String callPlayBarPayInterance(String openid, String openkey,
			String appid, String pf, String format, String userip,
			String zoneid, String itemid, String count) {
		JSONObject jsonObject = new JSONObject();
		try {
			String sig = getPlayBarPaySign(openid, openkey, appid, pf, format,
					userip, zoneid, itemid, count);
			jsonObject.element("openid", URLEncoder.encode(openid, "RFC 1738"));

			jsonObject.element("openkey",
					URLEncoder.encode(openkey, "RFC 1738"));
			jsonObject.element("appid", URLEncoder.encode(appid, "RFC 1738"));
			jsonObject.element("sig", URLEncoder.encode(sig, "RFC 1738"));
			jsonObject.element("pf", URLEncoder.encode(pf, "RFC 1738"));
			jsonObject.element("format", URLEncoder.encode(format, "RFC 1738"));
			jsonObject.element("userip", URLEncoder.encode(userip, "RFC 1738"));
			jsonObject.element("zoneid", URLEncoder.encode(zoneid, "RFC 1738"));
			jsonObject.element("itemid", URLEncoder.encode(itemid, "RFC 1738"));
			jsonObject.element("count", URLEncoder.encode(count, "RFC 1738"));
			String respose = OrderUtil.getHttpInterenceWink(
					getBuyPlayZoneItemUrl(), "POST", jsonObject);
			System.out
					.println("callPlayBarPayInterance========================>"
							+ respose);
			return respose;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return "error";
		}
	}

	/***
	 * 
	 * @return
	 */
	private static String getBuyPlayZoneItemUrl() {
		if (istest) {
			return buyPlayZoneItemUrl;
		}
		return testBuyPlayZoneItemUrl;
	}

	/***
	 * 玩吧支付的签名
	 * 
	 * @param url
	 * @return
	 */
	private static String getPlayBarPaySign(String openid, String openkey,
			String appid, String pf, String format, String userip,
			String zoneid, String itemid, String count) {
		try {
			String preSign = URLEncoder
					.encode(buyPlayZoneItemAfterUrl, "UTF-8");
			String parmString = "appid=" + appid + "&count=" + count
					+ "&format=" + format + "&itemid=" + itemid + "&openid="
					+ openid + "&openkey=" + openkey + "&pf=" + pf + "&userip="
					+ userip + "&zoneid=" + zoneid;
			parmString = URLEncoder.encode(parmString, "UTF-8");
			//
			preSign = "POST&" + preSign + "&" + parmString;
			System.out
					.println("getPlayBarPaySign=======================>preSign"
							+ preSign);
			byte[] afterSign = HmacSHA1Encrypt(preSign, appkey + "&");
			System.out
					.println("getPlayBarPaySign=======================>afterSign"
							+ preSign.toString());
			String base64String = Base64.encode(afterSign);
			System.out.println("base64String=======================>afterSign"
					+ base64String);
			return base64String;
		} catch (Exception e) {
			e.printStackTrace();
			return "error";
		}

	}

	/**
	 * 使用 HMAC-SHA1 签名方法对对encryptText进行签名
	 * 
	 * @param encryptText
	 *            被签名的字符串
	 * @param encryptKey
	 *            密钥
	 * @return
	 * @throws Exception
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey)
			throws Exception {
		byte[] data = encryptKey.getBytes("UTF-8");
		// 根据给定的字节数组构造一个密钥,第二参数指定一个密钥算法的名称
		SecretKey secretKey = new SecretKeySpec(data, "HmacSHA1");
		// 生成一个指定 Mac 算法 的 Mac 对象
		Mac mac = Mac.getInstance("HmacSHA1");
		// 用给定密钥初始化 Mac 对象
		mac.init(secretKey);

		byte[] text = encryptText.getBytes("UTF-8");
		// 完成 Mac 操作
		return mac.doFinal(text);
	}

	/***
	 * 回调更改订单信息
	 * 
	 * @param orderId
	 * @return
	 */
	public static String cbSetOrderStatus(String orderId) {
		OrdersBean bean = new OrdersBean();
		Orders orders = bean.qureyOrder(orderId);
		if (orders.getKStatus().equals(Constants.K_STSTUS_SUCCESS)) {
			System.out
					.println(orderId
							+ "cbSetOrderStatus-=================>order has been succeed");
			return "success";
		} else {
			System.out.println(orderId
					+ "cbSetOrderStatus-=================>order is ok");
			bean.updateOrderKStatus(orderId, Constants.K_STSTUS_SUCCESS);
			return "success";
		}
	}

}

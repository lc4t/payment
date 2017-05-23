package noumena.payment.getorders;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.TrustAnyTrustManager;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.Resource;
import org.apache.wink.client.RestClient;

public class OrderUtil {
	/***
	 * 简单通用获取订单号的方法
	 * 
	 * @param order
	 * @param payType
	 * @param other
	 * @return
	 */
	public static String getTransactionId(Orders order, String payType,
			String other) {

		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(order);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?pt=" + payType;
			} else {
				cburl += "&pt=" + payType;
			}
			cburl += "&currency=" + order.getCurrency();
			cburl += "&unit=" + order.getUnit();

			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		orderIdVO.setMsg(other);
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}

	/***
	 * 验证订单的通用方法
	 * 
	 * @param payIds
	 * @return
	 */
	public static String checkOrdersStatus(String payIds, OrderStatusVO otherst) {
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0; i < orders.size(); i++) {
			try {
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
						otherst.setPayId(order.getOrderId());
						statusret.add(otherst);
						continue;
					}
				} else if (status == Constants.K_STSTUS_SUCCESS) {
					// 如果订单已经成功，直接返回订单状态
					st.setStatus(1);
				} else {
					// 订单已经失败，直接返回订单状态
					st.setStatus(2);
				}
				statusret.add(st);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		JSONArray arr = JSONArray.fromObject(statusret);

		return arr.toString();
	}

	/****
	 * 利用wink。cline去请求接口 （jdk1.7以上用起来很方便）
	 * 
	 * @param url
	 * @param requesType
	 *            请求方式 post or get
	 * @param object
	 * @return
	 */
	public static String getHttpInterenceWink(String url, String requesType,
			JSONObject object) {
		RestClient client = new RestClient();
		// 简单的get方法
		if ("get".equals(requesType.toLowerCase())) {
			String urlEnd = getUrlAndParam(url, object);
			System.out.println("call url========================" + urlEnd);
			org.apache.wink.client.Resource resource = client.resource(urlEnd);
			ClientResponse response = (resource).get();
			// 接收返回响应体
			return response.getEntity(String.class);
		}
		/***
		 * 采用post方法
		 */
		if ("post".equals(requesType.toLowerCase())) {
			String jsonString = "";
			if (object != null) {
				jsonString = object.toString();
			}
			Resource resource = client.resource(url);
			//resource.contentType("application/json;charset=UTF-8");
			ClientResponse response = resource.post(jsonString);
			return response.getEntity(String.class);
		}

		return "requesType error";
	}

	/***
	 * 传统输入输出流请求接口
	 * 
	 * @param url
	 * @param requesType
	 * @param object
	 * @return
	 */
	public static String getHttpInterenceStream(String url, String requesType,
			String jsonString) {
		if (!"post".equals(requesType.toLowerCase())
				&& !"get".equals(requesType.toLowerCase())) {
			return "requesType error";
		}
		try {
			URL urlFormat = new URL(url);
			HttpURLConnection connection = (HttpURLConnection) urlFormat
					.openConnection();
			connection.setDoOutput(true);

			connection.setDoInput(true);
			if ("post".equals(requesType.toLowerCase())) {
				connection.setRequestMethod("POST");
			}
			connection.setRequestProperty("Content-type", "application/json");
			OutputStreamWriter outs = new OutputStreamWriter(
					connection.getOutputStream());
			outs.write(jsonString);
			outs.flush();
			outs.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			connection.disconnect();
			return res.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return "requesType error";

	}

	/**
	 * 从http获取参数并组成map
	 * @param request
	 * @return
	 */
	public static Map<String, String> getParamMapFromHttp(HttpServletRequest request){
		Map<String, String> params = new HashMap<String, String>();
		Map<?, ?> requestParams = request.getParameterMap();
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		return params;
	}
	/***
	 * 请求https接口的通用方法
	 * 
	 * @param url
	 * @param requesType
	 * @param jsonString
	 * @return
	 */
	public static String getHttpsInterenceStream(String url, String requesType,
			String jsonString) {
		try {
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null,
					new TrustManager[] { new TrustAnyTrustManager() },
					new java.security.SecureRandom());
			HttpsURLConnection connection = (HttpsURLConnection) new URL(url)
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

			connection.connect();

			// 加入参数
			DataOutputStream out = new DataOutputStream(
					connection.getOutputStream());

			out.writeBytes(jsonString);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "requesType error";
	}

	/****
	 * 将url和参数拼合到一起
	 * 
	 * @param url
	 * @param jsonObject
	 * @return
	 */
	public static String getUrlAndParam(String url, JSONObject jsonObject) {
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
	 * 将json数据的 key 首字母改变为大写 解决json格式化key全部转化为小写的问题
	 * 
	 * @param orgJson
	 * @return
	 */
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
}

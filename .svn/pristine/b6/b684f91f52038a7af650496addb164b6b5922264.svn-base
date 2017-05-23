package noumena.pay;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.RestClient;

public class NowSubmit extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	// appkey参数
	public static String nophoneappKey = "O3nY4Ig3KEZXYg1PpDxczLWZfpbnRccO";
	public static String phoneappKey = "hcNmo3CBAZ9bnQKd65aP8hE9KPI5glrc";
	
	private static final String GATEWAY = "https://pay.ipaynow.cn/";

	// 服务器异步通知页面路径 需http://格式的完整路径，必须外网可以正常访问
	// public static String notifyUrl = "http://p.ko.cn/pay/nowcb";
	public static String notifyUrl = "http://paystage.ko.cn:6001/paymentsystem/nowcb";
	//public static String notifyUrl = "http://localhost:8080/PaymentKong/nowcb";
	// 页面跳转同步通知页面路径 需http://格式的完整路径，必须外网可以正常访问
	// public static String frontNotifyUrl = "http://p.ko.cn/pay/nowhtmlcb";
	public static String frontNotifyUrl = "http://paystage.ko.cn:6001/paymentsystem/nowhtmlcb";

	public static String mhtOrderStartTime = dateFormat.format(new Date());

	// 字符编码格式
	public static String input_charset = "UTF-8";

	public static String mhtCurrencyType = "156";

	public static String mhtOrderType = "01";

	public static String payChannelType = "13";
	public static String payPhoneChannelType = "1301";
	public static String mhtSignType = "MD5";

	public static String funcode = "WP001";

	public static String deviceType = "08";
	public static String devicephoneType = "06";
	
	public static String appidType = "1486549349236931";
	public static String appidphoneType = "1410853543946442";
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

	}
	
	public static String getDeviceType(String type){
		if("0".equals(type)){
		return 	deviceType	;
		}else{
			return devicephoneType;
		}
	}

	public static String getPayChannelType(String type){
		if("0".equals(type)){
		return 	payChannelType	;
		}else{
			return payPhoneChannelType;
		}
	}
	
	public static String getAppKey(String type){
		if("0".equals(type)){
		return 	nophoneappKey	;
		}else{
			return phoneappKey;
		}
	}
	
	public static String getAppIp(String type){
		if("0".equals(type)){
		return 	appidType	;
		}else{
			return appidphoneType;
		}
	}

	/**
	 * 建立请求，以表单HTML形式构造（默认）
	 * 
	 * @param sParaTemp
	 *            请求参数数组
	 * @param strMethod
	 *            提交方式。两个值可选：post、get
	 * @param strButtonName
	 *            确认按钮显示文字
	 * @return 提交表单HTML文本
	 * @throws UnsupportedEncodingException
	 */
	public static String buildRequest(Map<String, String> sPara,
			String strMethod, String strButtonName)
			throws UnsupportedEncodingException {
		
		// 待请求参数数组
		List<String> keys = new ArrayList<String>(sPara.keySet());

		StringBuffer sbHtml = new StringBuffer();
		sbHtml.append(GATEWAY);
		for (int i = 0; i < keys.size(); i++) {
			if (i == 0) {
				sbHtml.append("?" + keys.get(i) + "="
						+ URLEncoder.encode(sPara.get(keys.get(i)), "UTF-8"));
			} else {
				sbHtml.append("&" + keys.get(i) + "="
						+ URLEncoder.encode(sPara.get(keys.get(i)), "UTF-8"));
			}
		}
		System.out.println("now buildRequest=====================>" + sbHtml);
		RestClient client = new RestClient();
		org.apache.wink.client.Resource resource = client.resource(sbHtml
				.toString());
		ClientResponse response = (resource).get();
		// 接收返回响应体
		String result = response.getEntity(String.class);
		String tempString[] = result.split("tn=");
		String weixinUrl = tempString[tempString.length - 1];
		weixinUrl = URLDecoder.decode(weixinUrl, "utf-8");
		System.out.println("now buildRequest=====================http result"
				+ weixinUrl);
		weixinUrl = weixinUrl.replace("%3D", "=");
		String tString[] = weixinUrl.split("&appId");
		weixinUrl = tString[0];
		System.out.println("now buildRequest=====================http result"
				+ weixinUrl);
		return weixinUrl;

	}

	public static String getOrderId(String uid, String pkgid, String itemid,
			String price, String cburl, String channel) {
		String orderid = "";
		String urlstr = "http://paystage.ko.cn:6001/paymentsystem/now";
		urlstr += "?model=1";
		urlstr += "&uid=" + uid;// 角色ID
		urlstr += "&pkgid=" + pkgid;// 包名
		urlstr += "&itemid=" + itemid;// 商品ID
		urlstr += "&price=" + price;// 价格
		urlstr += "&cburl=" + cburl;// 回调地址
		urlstr += "&channel=" + channel;// 渠道号
		try {
			URL url = new URL(urlstr);

			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setReadTimeout(5000);
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(
					connection.getOutputStream());
			outs.flush();
			outs.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			connection.disconnect();
			JSONObject json = JSONObject.fromObject(res);
			orderid = json.getString("payId");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return orderid;
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occurs
	 */
	public void init() throws ServletException {

	}

}

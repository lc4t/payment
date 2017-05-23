package noumena.payment.dao.servlet.now;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import noumena.pay.NowSubmit;
import noumena.payment.now.MD5Facade;

import org.apache.wink.client.ClientResponse;
import org.apache.wink.client.RestClient;

public class NowBuildPayServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static SimpleDateFormat dateFormat = new SimpleDateFormat(
			"yyyyMMddHHmmss");

	// appkey参数
	public static String appKey = "RhYUQH1emGSFrvehb2qObqJgeOQgKPBb";

	private static final String GATEWAY = "https://pay.ipaynow.cn/";

	// 服务器异步通知页面路径 需http://格式的完整路径，必须外网可以正常访问
	// public static String notifyUrl = "http://p.ko.cn/pay/nowcb";
	public static String notifyUrl = "http://paystage.ko.cn:6001/paymentsystem/nowcb";
	// 页面跳转同步通知页面路径 需http://格式的完整路径，必须外网可以正常访问
	// public static String frontNotifyUrl = "http://p.ko.cn/pay/nowhtmlcb";
	public static String frontNotifyUrl = "http://paystage.ko.cn:6001/paymentsystem/nowhtmlcb";

	public static String mhtOrderStartTime = dateFormat.format(new Date());

	// 字符编码格式
	public static String input_charset = "UTF-8";

	public static String mhtCurrencyType = "156";

	public static String mhtOrderType = "01";

	public static String payChannelType = "1301";

	public static String mhtSignType = "MD5";

	public static String funcode = "WP001";

	public static String deviceType = "06";

	/*
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * 
	 * @param response the response send by the server to the client
	 * 
	 * @throws ServletException if an error occurred
	 * 
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
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
	public void doPost(String strMethod, String strButtonName,
			HttpServletRequest request, HttpServletResponse response2)
			throws ServletException, IOException {

		String appId = request.getParameter("appid");// Appid
		String productname = request.getParameter("productname");
		String amount = request.getParameter("amount");
		String cbgameurl = request.getParameter("cbgameurl");// 返回游戏地址
		String productdesc = request.getParameter("productdesc");
		String channel = request.getParameter("channel");// 渠道号
		String packagename = request.getParameter("packagename");// 包名
		String uid = request.getParameter("uid");// 角色ID
		String productid = request.getParameter("productid");// 商品ID
		String cburl = request.getParameter("cburl");// 服务器回调地址
		// 做MD5签名
		String mhtOrderNo = NowSubmit.getOrderId(uid, packagename, productid,
				amount, cburl, channel);// 获取支付中心订单号
		Map<String, String> dataMap = new HashMap<String, String>();
		dataMap.put("appId", appId);
		dataMap.put("mhtOrderNo", mhtOrderNo);
		dataMap.put("mhtOrderName", productname);
		dataMap.put("mhtCurrencyType", NowSubmit.mhtCurrencyType);
		dataMap.put("mhtOrderAmt", amount);
		dataMap.put("mhtOrderDetail", productdesc);
		dataMap.put("mhtOrderType", NowSubmit.mhtOrderType);
		dataMap.put("mhtOrderStartTime", NowSubmit.mhtOrderStartTime);
		dataMap.put("notifyUrl", NowSubmit.notifyUrl);
		dataMap.put("frontNotifyUrl", NowSubmit.frontNotifyUrl);
		dataMap.put("mhtCharset", NowSubmit.input_charset);
		dataMap.put("payChannelType", NowSubmit.payChannelType);
		// 商户保留域， 可以不用填。 如果商户有需要对每笔交易记录一些自己的东西，可以放在这个里面
		dataMap.put("mhtReserved", cbgameurl);
		String mhtSignature = MD5Facade.getFormDataParamMD5(dataMap,
				NowSubmit.deviceType, NowSubmit.input_charset);
		Map<String, String> sPara = new HashMap<String, String>();
		sPara.put("appId", appId);
		sPara.put("mhtOrderNo", mhtOrderNo);
		sPara.put("mhtOrderName", productname);
		sPara.put("mhtCurrencyType", NowSubmit.mhtCurrencyType);
		sPara.put("mhtOrderAmt", amount);
		sPara.put("mhtOrderDetail", productdesc);
		sPara.put("mhtOrderType", NowSubmit.mhtOrderType);
		sPara.put("mhtOrderStartTime", NowSubmit.mhtOrderStartTime);
		sPara.put("notifyUrl", NowSubmit.notifyUrl);
		sPara.put("frontNotifyUrl", NowSubmit.frontNotifyUrl);
		sPara.put("mhtCharset", NowSubmit.input_charset);

		sPara.put("mhtSignType", NowSubmit.mhtSignType);
		sPara.put("mhtSignature", mhtSignature);
		sPara.put("funcode", NowSubmit.funcode);
		sPara.put("deviceType", NowSubmit.deviceType);
		sPara.put("payChannelType", NowSubmit.payChannelType);
		sPara.put("mhtReserved", cbgameurl);
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

		String hrefString = "<a href=\"" + weixinUrl + "\">微信支付</a>";

		/*
		 * sbHtml.append("<form id=\"submit\" name=\"submit\" action=\"" +
		 * GATEWAY +"\" input_charset=" + input_charset + "\" method=\"" +
		 * strMethod + "\">");
		 * 
		 * for (int i = 0; i < keys.size(); i++) { String name = (String)
		 * keys.get(i); String value = (String) sPara.get(name);
		 * 
		 * sbHtml.append("<input type=\"hidden\" name=\"" + name + "\" value=\""
		 * + value + "\"/>"); }
		 * 
		 * //submit按钮控件请不要含有name属性
		 * sbHtml.append("<input type=\"submit\" value=\"" + strButtonName +
		 * "\" style=\"display:none;\"></form>");
		 * sbHtml.append("<script>document.forms['submit'].submit();</script>");
		 */
		String ret = hrefString;

		response2.setContentType("text/html");
		PrintWriter out = response2.getWriter();
		out.println(ret);
		out.flush();
		out.close();
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
	 *             if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

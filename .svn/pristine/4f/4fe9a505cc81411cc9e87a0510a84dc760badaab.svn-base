package noumena.payment.dao.servlet.qqplaybar;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.getorders.OrderUtil;
import noumena.payment.model.Orders;
import noumena.payment.qqplaybar.QQPlayBarCharge;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderStatusVO;

/***
 * qq玩吧的请求地址
 * 
 * @author kz
 * 
 */
public class QQPlayBarServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * model: 1 - 客户端请求支付 2 - 客户端请求验证订单 
	 * 
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");

		// 必需的参数
		String stype = request.getParameter("model"); // 请求的类型：1-得到交易id；2-查询订单状态
		String uid = request.getParameter("uid"); //
		// String pkgid = request.getParameter("pkgid"); //
		String itemid = request.getParameter("itemid"); //
		String payprice = request.getParameter("price"); // 一定是人民币元
		String cburl = request.getParameter("cburl"); // 单机游戏可以不需要回调地址

		// 选填的参数
		String imei = request.getParameter("imei"); //
		String channel = request.getParameter("channel"); //
		String device_type = request.getParameter("device_type"); //
		String device_id = request.getParameter("device_id"); //
		String gversion = request.getParameter("gversion"); //
		String osversion = request.getParameter("osversion"); //

		// 支付方的参数
		String appid = request.getParameter("appid"); //

		// 验证订单用参数
		String payIds = request.getParameter("payIds"); // 待查询的所有订单号，以“,”分隔
		String cuurrency = request.getParameter("cuurrency");// 币种

		String openid = request.getParameter("openid");// QQ号码转化得到的ID
		String openkey = request.getParameter("openkey");// qq session key
		String sig = request.getParameter("sig");// qq 请求串的签名
		String pf = request.getParameter("pf");// qq openid
		String format = request.getParameter("format");// qq 定义API返回的数据格式。
		String userip = request.getParameter("userip");// qq 用户的IP。
		String zoneid = request.getParameter("zoneid");// qq 区ID
		String count = request.getParameter("count");// qq 兑换道具数量
		if (payprice == null || payprice.equals("")) {
			payprice = "0.00";
		} else {
			payprice = new DecimalFormat("0.00").format(new Float(payprice));
		}
		String ret = "";
		// 获取订单号，并请求到交易授权码
		if (stype == null) {
			System.out
					.println("QQPlayBarServlet  model is error===========================================");
		} else if (stype.equals("1")) {
			Orders vo = new Orders();
			vo.setCurrency(cuurrency);
			vo.setImei(imei);
			vo.setUId(uid);
			vo.setProductId(itemid);
			vo.setItemId(itemid);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setDeviceId(device_id);
			vo.setDeviceType(device_type);
			vo.setChannel(channel);
			vo.setAppId(appid);
			vo.setAmount(Float.valueOf(payprice));
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setCallbackUrl(cburl);
			System.out.println("QQPlayBarServlet app id=================>"
					+ vo.getAppId() + "====product id=" + vo.getProductId());
			// 获取交易，付款申请
			ret = QQPlayBarCharge.getPlayBarPayId(vo, "", openid, openkey,
					appid, sig, pf, format, userip, zoneid, itemid, count);
		} else if (stype.equals("2")) {
			OrderStatusVO st = new OrderStatusVO();
			st.setStatus(3);
			ret = OrderUtil.checkOrdersStatus(payIds, st);
		} else {
			System.out
					.println("QQPlayBarServlet  model is error===========================================");
		}
		System.out.println("QQPlayBarServlet order id ret=============>" + ret);
		response.setCharacterEncoding("utf-8");
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(ret);
		out.flush();
		out.close();

	}
}

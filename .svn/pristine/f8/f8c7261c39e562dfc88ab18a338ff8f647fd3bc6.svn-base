package noumena.payment.dao.servlet.augame;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.augame.AuGameCharge;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;

public class AuGameServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to get.
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
	 * model:
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
		String stype = request.getParameter("model"); // 请求的类型：1-得到交易id；2-查询订单状态    3-按照au的算法验证订单并更改订单状态
		String uid = request.getParameter("uid"); //
		String pkgid = request.getParameter("pkgid"); //
		String itemid = request.getParameter("itemid"); //
		String payprice = request.getParameter("price"); // 一定是人民币元
		String cburl = request.getParameter("cburl"); // 单机游戏可以不需要回调地址
		String cuurrency = request.getParameter("cuurrency");//、币种
		// String appid = request.getParameter("appid");

		// 选填的参数
		String imei = request.getParameter("imei"); //
		String channel = request.getParameter("channel"); //
		String device_type = request.getParameter("device_type"); //
		String device_id = request.getParameter("device_id"); //
		String gversion = request.getParameter("gversion"); //
		String osversion = request.getParameter("osversion"); //
		

		// model为2的参数
		String payIds = request.getParameter("payIds"); // 待查询的所有订单号，以“,”分隔
		// model为3的参数
		String resultCode = request.getParameter("resultCode");
		String receipt = request.getParameter("receipt");
		String signature = request.getParameter("signature");
		String payId = request.getParameter("payId");
		String amount = request.getParameter("amount");
		String otherId = request.getParameter("otherId");
		// 支付方的参数

		String ret = "";
		if (stype == null) {
			stype = "";
		}
		if (stype.equals("1")) {
			Orders vo = new Orders();
			vo.setImei(imei);
			vo.setUId(uid);
			vo.setItemId(itemid);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setDeviceId(device_id);
			vo.setDeviceType(device_type);
			vo.setChannel(channel);
			vo.setAppId(pkgid);
			// vo.setSign(appid);
			if (payprice == null) {
				payprice = "0";
			}
			vo.setAmount(Float.valueOf(payprice));
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setPayType(Constants.PAY_TYPE_AUGAME);
			vo.setCallbackUrl(cburl);
			vo.setCurrency(cuurrency);
			ret = AuGameCharge.getTransactionId(vo);
		} else if (stype.equals("2")) {
			ret = AuGameCharge.checkOrdersStatus(payIds);
		} else if (stype.equals("3")) {
			try {
				ret = AuGameCharge.getAuGameCallbackFrom(resultCode, receipt,
						signature, payId, amount, otherId);
				System.out
						.println("AuGame receipt================================================>"
								+ ret);
			} catch (Exception e) {
				e.printStackTrace();
				ret = " AuGame receipt invalid";
			}
		} else {
			System.out.println("AuGame model invalid----("
					+ DateUtil.getCurTimeStr()
					+ ")---------------------------------->" + stype);
			ret = "invalid";
		}

		System.out.println("AuGame order id------(" + DateUtil.getCurTimeStr()
				+ ")----------------------------------------->(" + uid + ")"
				+ ret);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(ret);
		out.flush();
		out.close();
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

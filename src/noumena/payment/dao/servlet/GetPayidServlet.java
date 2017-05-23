package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderIdVO;

public class GetPayidServlet extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public GetPayidServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String gameid = request.getParameter("gameid");
		String areaid = request.getParameter("areaid");
		String serverid = request.getParameter("serverid");
		String imei = request.getParameter("imei");
		String accountid = request.getParameter("accountid");
//		if ("3000000".equals(gameid)) {
//			accountid = "abc";
//		}
		String roleid = request.getParameter("roleid");
		String payprice = request.getParameter("payprice");
		String paymemo = request.getParameter("paymemo");
		String cburl = request.getParameter("cburl");
		String channel = request.getParameter("channel");
		String device_type = request.getParameter("device_type");
		String device_id = request.getParameter("device_id");
		String gversion = request.getParameter("gversion");
		String osversion = request.getParameter("osversion");
		String payTypeId = request.getParameter("payTypeId");
		String ip = request.getRemoteAddr();
		
		if (payprice == null)
		{
			payprice = "0";
		}
		payprice = new DecimalFormat("0.00").format(new Float(payprice)); 
		Orders vo = new Orders();
		vo.setImei(imei);
		vo.setUId(accountid);
		vo.setGversion(gversion);
		vo.setOsversion(osversion);
		vo.setDeviceId(device_id);
		vo.setDeviceType(device_type);
		vo.setChannel(channel);
		vo.setAppId(gameid);
		vo.setAmount(Float.valueOf(payprice));
		vo.setCreateTime(DateUtil.getCurrentTime());
		vo.setExInfo(paymemo + "#" + ip);
		String pt = "";
		if (payTypeId == null || "".equals(payTypeId))
		{
			payTypeId = Constants.PAY_TYPE_ZFB;
			pt = Constants.PAY_TYPE_DACHENG_ZFB;
		}
		else
		{
			if (payTypeId.equals(Constants.PAY_TYPE_ZFB) || payTypeId.equals(Constants.PAY_TYPE_ZFB_BANK) || payTypeId.equals(Constants.PAY_TYPE_ZFB_BANK_NEW))
			{
				pt = Constants.PAY_TYPE_DACHENG_ZFB;
			}
			else
			{
				pt = Constants.PAY_TYPE_DACHENG_SZX;
			}
		}
		vo.setPayType(payTypeId);
		vo.setCurrency(Constants.CURRENCY_RMB);
		vo.setUnit(Constants.CURRENCY_UNIT_YUAN);
		
		OrdersBean bean = new OrdersBean();
		String payId;
		if (cburl == null || cburl.equals("")) {
			payId = bean.CreateOrder(vo);
		} else {
			if (cburl.indexOf("?") == -1) {
				cburl += "?gross=" + payprice + "&pt=" + pt;
			} else {
				cburl += "&gross=" + payprice + "&pt=" + pt;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			
			payId = bean.CreateOrder(vo, cburl);
		}
		String date = DateUtil.formatDate(vo.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		JSONObject json = JSONObject.fromObject(orderIdVO);
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(json.toString());
		out.flush();
		out.close();
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

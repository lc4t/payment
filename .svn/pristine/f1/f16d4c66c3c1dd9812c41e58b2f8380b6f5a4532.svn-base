package noumena.payment.dao.servlet.newmycard;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import noumena.payment.newmycard.NewMyCardTWCharge;
import noumena.payment.util.DateUtil;

public class NewMyCardAlternateSerlvet extends HttpServlet {
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
		String data = request.getParameter("DATA");
		JSONObject jsonObject = JSONObject.fromObject(data);
		String returnCode = jsonObject.getString("ReturnCode"); // 订单状态
		String returnMsg = jsonObject.getString("ReturnMsg"); // 返回信息
		String facServiceId = jsonObject.getString("FacServiceId"); // 服务号
		String totalNum = jsonObject.getString("TotalNum"); // 数量
		String facTradeSeq = jsonObject.getString("FacTradeSeq"); // 自己的订单id
		String ip = request.getRemoteAddr();
		System.out.println("NewMyCardAlternateSerlvet  param-========"
				+ DateUtil.getCurTimeStr() + "==================>ReturnCode"
				+ returnCode + " ReturnMsg" + returnMsg + " FacServiceId"
				+ facServiceId + " TotalNum" + totalNum + " FacTradeSeq"
				+ facTradeSeq);
		String ret = NewMyCardTWCharge.alternateCb(ip, returnCode, facTradeSeq);
		System.out.println("NewMyCardAlternateSerlvet  result-========"
				+ DateUtil.getCurTimeStr() + "==================>" + ret);
		response.setCharacterEncoding("utf-8");
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

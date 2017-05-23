package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.appota.AppOTACharge;
import noumena.payment.appota.AppOTAOrderVO;

public class AppOTAcbServlet extends HttpServlet
{

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
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
	 * 
	 * model:
	 * 		1 - 客户端请求支付
	 * 		2 - 客户端请求验证订单
	 * 		11 - 小米请求Token服务成功状态回调
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
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		String amount = request.getParameter("amount");
		String country_code = request.getParameter("country_code");
		String currency = request.getParameter("currency");
		String sandbox = request.getParameter("sandbox");
		String state = request.getParameter("state");
		String status = request.getParameter("status");
		String target = request.getParameter("target");
		String transaction_id = request.getParameter("transaction_id");
		String transaction_type = request.getParameter("transaction_type");
		String hash = request.getParameter("hash");
		
		AppOTAOrderVO ordervo = new AppOTAOrderVO();
		ordervo.setAmount(amount);
		ordervo.setCountry_code(country_code);
		ordervo.setCurrency(currency);
		ordervo.setSandbox(sandbox);
		ordervo.setState(state);
		ordervo.setStatus(status);
		ordervo.setTarget(target);
		ordervo.setTransaction_id(transaction_id);
		ordervo.setTransaction_type(transaction_type);
		ordervo.setHash(hash);
		
		StringBuffer orderstr = new StringBuffer();
		orderstr.append(amount);
		orderstr.append(country_code);
		orderstr.append(currency);
		orderstr.append(sandbox);
		orderstr.append(state);
		orderstr.append(status);
		orderstr.append(target);
		orderstr.append(transaction_id);
		orderstr.append(transaction_type);
		
		System.out.println("appota cb ->" + orderstr.toString());
		
		String ret = AppOTACharge.getCallbackFromAppOTA(ordervo, orderstr.toString());
		
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
	public void init() throws ServletException
	{
		// Put your code here
	}

}

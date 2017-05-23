package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.pp.PPCharge;
import noumena.payment.pp.PPOrderVO;

public class PPcbServlet extends HttpServlet
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
		String order_id = request.getParameter("order_id");
		String billno = request.getParameter("billno");
		String account = request.getParameter("account");
		String amount = request.getParameter("amount");
		String status = request.getParameter("status");
		String app_id = request.getParameter("app_id");
		String uuid = request.getParameter("uuid");
		String roleid = request.getParameter("roleid");
		String zone = request.getParameter("zone");
		String sign = request.getParameter("sign");
		
		PPOrderVO ordervo = new PPOrderVO();
		ordervo.setOrder_id(order_id);
		ordervo.setBillno(billno);
		ordervo.setAccount(account);
		ordervo.setAmount(amount);
		ordervo.setStatus(status);
		ordervo.setApp_id(app_id);
		ordervo.setUuid(uuid);
		ordervo.setRoleid(roleid);
		ordervo.setZone(zone);
		ordervo.setSign(sign);
		
		StringBuffer orderstr = new StringBuffer();
		orderstr.append("order_id=");
		orderstr.append(order_id);
		orderstr.append("&billno=");
		orderstr.append(billno);
		orderstr.append("&account=");
		orderstr.append(account);
		orderstr.append("&amount=");
		orderstr.append(amount);
		orderstr.append("&status=");
		orderstr.append(status);
		orderstr.append("&app_id=");
		orderstr.append(app_id);
		orderstr.append("&uuid=");
		orderstr.append(uuid);
		orderstr.append("&roleid=");
		orderstr.append(roleid);
		orderstr.append("&zone=");
		orderstr.append(zone);
		orderstr.append("&sign=");
		orderstr.append(sign);
		
		System.out.println("pp cb ->" + orderstr.toString());
		
		String ret = PPCharge.getCallbackFromPP(ordervo, orderstr.toString());
		
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

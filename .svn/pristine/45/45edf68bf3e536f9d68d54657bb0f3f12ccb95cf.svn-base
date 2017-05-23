package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.baidu.BaiduCharge;
import noumena.payment.baidu.BaiduOrderVO;

public class VNSohacbServlet extends HttpServlet
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
		String cardtype = request.getParameter("cardtype");
		String orderid = request.getParameter("orderid");
		String result = request.getParameter("result");
		String timetamp = request.getParameter("timetamp");
		String aid = request.getParameter("aid");
		String client_secret = request.getParameter("client_secret");
		
		BaiduOrderVO ordervo = new BaiduOrderVO();
		ordervo.setAmount(amount);
		ordervo.setCardtype(cardtype);
		ordervo.setOrderid(orderid);
		ordervo.setResult(result);
		ordervo.setTimetamp(timetamp);
		ordervo.setAid(aid);
		ordervo.setClient_secret(client_secret);
		
		StringBuffer orderstr = new StringBuffer();
		orderstr.append("amount=");
		orderstr.append(amount);
		orderstr.append("&cardtype=");
		orderstr.append(cardtype);
		orderstr.append("&orderid=");
		orderstr.append(orderid);
		orderstr.append("&result=");
		orderstr.append(result);
		orderstr.append("&timetamp=");
		orderstr.append(timetamp);
		orderstr.append("&aid=");
		orderstr.append(aid);
		orderstr.append("&client_secret=");
		orderstr.append(client_secret);
		
		String ret = BaiduCharge.getCallbackFromBaidu(ordervo, orderstr.toString());
		
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

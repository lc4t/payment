package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.downjoy.DownjoyCharge;
import noumena.payment.downjoy.DownjoycbOrderVO;

public class DownjoycbServlet extends HttpServlet
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
		StringBuffer buffer = new StringBuffer();
		DownjoycbOrderVO ordervo = new DownjoycbOrderVO();

		String gameid = request.getParameter("gameid");
		buffer.append("&");
		buffer.append("gameid=");
		buffer.append(gameid);
		ordervo.setAppid(gameid);

		String result = request.getParameter("result");
		buffer.append("&");
		buffer.append("result=");
		buffer.append(result);
		ordervo.setResult(result);

		String money = request.getParameter("money");
		buffer.append("&");
		buffer.append("money=");
		buffer.append(money);
		ordervo.setMoney(money);

		String order = request.getParameter("order");
		buffer.append("&");
		buffer.append("order=");
		buffer.append(order);
		ordervo.setOrder(order);

		String mid = request.getParameter("mid");
		buffer.append("&");
		buffer.append("mid=");
		buffer.append(mid);
		ordervo.setMid(mid);

		String time = request.getParameter("time");
		buffer.append("&");
		buffer.append("time=");
		buffer.append(time);
		ordervo.setTime(time);

		String signature = request.getParameter("signature");
		buffer.append("&");
		buffer.append("signature=");
		buffer.append(signature);
		ordervo.setSignature(signature);

		String ext = request.getParameter("ext");
		buffer.append("&");
		buffer.append("ext=");
		buffer.append(ext);
		ordervo.setExt(ext);
		
		System.out.println("downjoy cb ->" + buffer.toString());
		
		String ret = DownjoyCharge.getCallbackFromDownjoy(ordervo);
		
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

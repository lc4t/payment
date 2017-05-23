package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.test.TestCBOrderVO;
import noumena.payment.test.TestCharge;

public class TestcbServlet extends HttpServlet
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
		//接受360Pay返回的参数
		TestCBOrderVO cbvo = new TestCBOrderVO();
		StringBuffer buffer = new StringBuffer();
		StringBuffer signstr = new StringBuffer();
		
		String app_order_id = request.getParameter("app_order_id");	//应用订单号
		if (app_order_id != null && !app_order_id.equals(""))
		{
			signstr.append("#");
			signstr.append(app_order_id);
		}
		buffer.append("&");
		buffer.append("app_order_id=");
		buffer.append(app_order_id);
		cbvo.setApp_order_id(app_order_id);
		
		String gateway_flag = request.getParameter("gateway_flag");	//如果支付返回成功，返回success，如果支付过程断路，返回空，如果支付失败，返回fail
		if (gateway_flag != null && !gateway_flag.equals(""))
		{
			signstr.append("#");
			signstr.append(gateway_flag);
		}
		buffer.append("&");
		buffer.append("gateway_flag=");
		buffer.append(gateway_flag);
		cbvo.setGateway_flag(gateway_flag);
		
		TestCharge.getOrderCBFromTest(cbvo, signstr.toString());
		
		PrintWriter out = response.getWriter();
		out.println("ok");
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

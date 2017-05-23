package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.vo.OrderStatusVO;

public class QueryOrdersStatusServlst extends HttpServlet {

	/**
	 * Constructor of the object.
	 */
	public QueryOrdersStatusServlst() {
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
//		System.out.println("[QueryOrdersStatusServlst]: say hello");
		String payIds = request.getParameter("payIds");
		String ret = "";
		if (payIds == null || payIds.equals(""))
		{
			ret = "payids is invalid";
		}
		else
		{
			String[] orderIds = payIds.split(",");
			
			OrdersBean bean = new OrdersBean();
			List<OrderStatusVO> orderStatusVOList = bean.qureyOrdersStatus(orderIds);
			JSONArray arr = JSONArray.fromObject(orderStatusVOList);
			ret = arr.toString();
		}

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println(ret);
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
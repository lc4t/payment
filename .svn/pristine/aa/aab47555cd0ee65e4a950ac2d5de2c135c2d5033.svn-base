package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.adways.AdwaysCharge;
import noumena.payment.adways.AdwaysOrderVO;

public class AdwayscbServlet extends HttpServlet
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
		AdwaysOrderVO vo = new AdwaysOrderVO();
		String str = "";
		
		str = request.getParameter("identifier");
		if (str != null)
		{
			vo.setIdentifier(str);
		}
		str = request.getParameter("achieve_id");
		if (str != null)
		{
			vo.setAchieve_id(str);
		}
		str = request.getParameter("point");
		if (str != null)
		{
			vo.setPoint(str);
		}
		str = request.getParameter("user");
		if (str != null)
		{
			vo.setUser(str);
		}
		str = request.getParameter("confirm_flag");
		if (str != null)
		{
			vo.setConfirm_flag(str);
		}
		str = request.getParameter("type");
		if (str != null)
		{
			vo.setType(str);
		}
		
		System.out.println("adways cb identifier->" + vo.getIdentifier());
		System.out.println("adways cb achieve_id->" + vo.getAchieve_id());
		System.out.println("adways cb point->" + vo.getPoint());
		System.out.println("adways cb user->" + vo.getUser());
		System.out.println("adways cb confirm_flag->" + vo.getConfirm_flag());
		System.out.println("adways cb type->" + vo.getType());

		String ret = AdwaysCharge.getCallbackFromAdways(vo);
		
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

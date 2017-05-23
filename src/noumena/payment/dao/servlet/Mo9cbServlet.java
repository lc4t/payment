package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.mo9.Mo9Charge;
import noumena.payment.mo9.Mo9OrderVO;

public class Mo9cbServlet extends HttpServlet
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
		Mo9OrderVO vo = new Mo9OrderVO();
		String str = "";
		
		str = request.getParameter("pay_to_email");
		if (str != null)
		{
			vo.setPay_to_email(str);
		}
		str = request.getParameter("payer_id");
		if (str != null)
		{
			vo.setPayer_id(str);
		}
		str = request.getParameter("trade_no");
		if (str != null)
		{
			vo.setTrade_no(str);
		}
		str = request.getParameter("trade_status");
		if (str != null)
		{
			vo.setTrade_status(str);
		}
		str = request.getParameter("sign");
		if (str != null)
		{
			vo.setSign(str);
		}
		str = request.getParameter("amount");
		if (str != null)
		{
			vo.setAmount(str);
		}
		str = request.getParameter("currency");
		if (str != null)
		{
			vo.setCurrency(str);
		}
		str = request.getParameter("req_amount");
		if (str != null)
		{
			vo.setReq_amount(str);
		}
		str = request.getParameter("req_currency");
		if (str != null)
		{
			vo.setReq_currency(str);
		}
		str = request.getParameter("item_name");
		if (str != null)
		{
			vo.setItem_name(str);
		}
		str = request.getParameter("lc");
		if (str != null)
		{
			vo.setLc(str);
		}
		str = request.getParameter("extra_param");
		if (str != null)
		{
			vo.setExtra_param(str);
		}
		str = request.getParameter("app_id");
		if (str != null)
		{
			vo.setApp_id(str);
		}
		str = request.getParameter("invoice");
		if (str != null)
		{
			vo.setInvoice(str);
		}
		
		System.out.println("mo9 cb payer_id->" + vo.getPayer_id());
		System.out.println("mo9 cb trade_no->" + vo.getTrade_no());
		System.out.println("mo9 cb trade_status->" + vo.getTrade_status());
		System.out.println("mo9 cb order_id->" + vo.getInvoice());
		System.out.println("mo9 cb extra_param->" + vo.getExtra_param());

		String ret = Mo9Charge.getCallbackFromMo9(vo);
		
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

package noumena.payment.dao.servlet.tongbu;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.tongbu.TongbuCharge;
import noumena.payment.tongbu.TongbuOrderVO;

public class TongbucbServlet extends HttpServlet
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
		String source = request.getParameter("source");
		String trade_no = request.getParameter("trade_no");
		String amount = request.getParameter("amount");
		String partner = request.getParameter("partner");
		String paydes = request.getParameter("paydes");
		String debug = request.getParameter("debug");
		String tborder = request.getParameter("tborder");
		String sign = request.getParameter("sign");
		
		TongbuOrderVO vo = new TongbuOrderVO();
		vo.setSource(source);
		vo.setTrade_no(trade_no);
		vo.setAmount(amount);
		vo.setPartner(partner);
		vo.setPaydes(paydes);
		vo.setDebug(debug);
		vo.setTborder(tborder);
		vo.setSign(sign);
		
		String ret = TongbuCharge.getCallbackFromTongbu(vo);
		
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

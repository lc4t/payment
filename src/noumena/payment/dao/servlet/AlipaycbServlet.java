package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.alipay.AlipayCharge;
import noumena.payment.alipay.AlipayOrderDataVO;

public class AlipaycbServlet extends HttpServlet
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

//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		int pos = res.indexOf("&sign_type=");
//		res = res.substring(0, pos);
//		
//		System.out.println("alipay cb source->" + res);
		
		AlipayOrderDataVO vo = new AlipayOrderDataVO();
		String p = "";
		StringBuffer signbuf = new StringBuffer();
		
		p = request.getParameter("body");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("body=" + p);
		}
		vo.setBody(p);
		
		p = request.getParameter("buyer_email");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&buyer_email=" + p);
		}
		vo.setBuyer_email(p);
		
		p = request.getParameter("buyer_id");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&buyer_id=" + p);
		}
		vo.setBuyer_id(p);
		
		p = request.getParameter("discount");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&discount=" + p);
		}
		vo.setDiscount(p);
		
		p = request.getParameter("gmt_close");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&gmt_close=" + p);
		}
		vo.setGmt_close(p);
		
		p = request.getParameter("gmt_create");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&gmt_create=" + p);
		}
		vo.setGmt_create(p);
		
		p = request.getParameter("gmt_payment");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&gmt_payment=" + p);
		}
		vo.setGmt_payment(p);
		
		p = request.getParameter("is_total_fee_adjust");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&is_total_fee_adjust=" + p);
		}
		vo.setIs_total_fee_adjust(p);
		
		p = request.getParameter("notify_id");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&notify_id=" + p);
		}
		vo.setNotify_id(p);
		
		p = request.getParameter("notify_time");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&notify_time=" + p);
		}
		vo.setNotify_time(p);
		
		p = request.getParameter("notify_type");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&notify_type=" + p);
		}
		vo.setNotify_type(p);
		
		p = request.getParameter("out_trade_no");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&out_trade_no=" + p);
		}
		vo.setOut_trade_no(p);
		
		p = request.getParameter("payment_type");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&payment_type=" + p);
		}
		vo.setPayment_type(p);
		
		p = request.getParameter("price");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&price=" + p);
		}
		vo.setPrice(p);
		
		p = request.getParameter("quantity");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&quantity=" + p);
		}
		vo.setQuantity(p);
		
		p = request.getParameter("seller_email");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&seller_email=" + p);
		}
		vo.setSeller_email(p);
		
		p = request.getParameter("seller_id");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&seller_id=" + p);
		}
		vo.setSeller_id(p);
		
		p = request.getParameter("subject");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&subject=" + p);
		}
		vo.setSubject(p);
		
		p = request.getParameter("total_fee");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&total_fee=" + p);
		}
		vo.setTotal_fee(p);
		
		p = request.getParameter("trade_no");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&trade_no=" + p);
		}
		vo.setTrade_no(p);
		
		p = request.getParameter("trade_status");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&trade_status=" + p);
		}
		vo.setTrade_status(p);
		
		p = request.getParameter("use_coupon");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		else
		{
			signbuf.append("&use_coupon=" + p);
		}
		vo.setUse_coupon(p);
		
		p = request.getParameter("sign");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		vo.setSign(p);
		
		p = request.getParameter("sign_type");
		if (p == null || p.equals(""))
		{
			p = "";
		}
		vo.setSign_type(p);
		
		System.out.println("alipay cb1 ->" + signbuf.toString() + "&sign=" + vo.getSign() + "&sign_type=" + vo.getSign_type());
		
		String ret = AlipayCharge.getCallbackAlipay(vo, signbuf.toString());
		
		System.out.println("alipay cb ret->" + ret);
		
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

package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.fet.FETCharge;
import noumena.payment.fet.FETOrderVO;

public class FETItemsServlet extends HttpServlet
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
//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("res->" + res);
		FETOrderVO vo = new FETOrderVO();
		vo.setOrderid(request.getParameter("paymentId"));
		vo.setPkgId(request.getParameter("pkgId"));
		vo.setPkgv(request.getParameter("pkgv"));
		vo.setDmd5(request.getParameter("dmd5"));
		System.out.println("fet items req->" + vo.getOrderid() + "/" + vo.getPkgId());
		
		FETOrderVO ret = FETCharge.getItemsFromFET(vo);
		String retstr = "<PaymentRet>";
		retstr += "<retCode>";
		retstr += ret.getValue();
		retstr += "</retCode>";
		retstr += "<retMsg>";
		retstr += ret.getDesc();
		retstr += "</retMsg>";
		retstr += "<payment>";
		retstr += "<store>";
		retstr += ret.getStore();
		retstr += "</store>";
		retstr += "<paymentId>";
		retstr += ret.getOrderid();
		retstr += "</paymentId>";
		retstr += "<amount>";
		retstr += ret.getPrice();
		retstr += "</amount>";
		retstr += "<product>";
		retstr += "<productId>";
		retstr += ret.getProductId();
		retstr += "</productId>";
		retstr += "<name>";
		retstr += ret.getName();
		retstr += "</name>";
		retstr += "<quantity>1</quantity>";
		retstr += "<price>";
		retstr += ret.getPrice();
		retstr += "</price>";
		retstr += "</product>";
		retstr += "</payment>";
		retstr += "</PaymentRet>";

		System.out.println("fet items ret->" + retstr);
		PrintWriter out = response.getWriter();
		out.println(retstr);
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

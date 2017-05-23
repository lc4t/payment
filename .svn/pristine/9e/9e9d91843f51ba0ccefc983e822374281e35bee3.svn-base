package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.StringEncrypt;

public class NotifyServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public NotifyServlet() {
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
	 * @param request
	 *            the request send by the client to the server
	 * @param response
	 *            the response send by the server to the client
	 * @throws ServletException
	 *             if an error occurred
	 * @throws IOException
	 *             if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request, response);
	}

	/**
	 * The doPost method of the servlet. <br>
	 * 
	 * This method is called when a form has its tag value method equals to
	 * post.
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
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

//		System.out.println("[notifyservlet] say hello! ");
//		System.out.println("[notifyservlet] say hello! ");
		
		String payId = request.getParameter("payId");
		String kzpayId = request.getParameter("kzpayId");
		String status = request.getParameter("status");
		String paymemo = request.getParameter("paymemo");
		String payamount = request.getParameter("payamount");
		String sign = request.getParameter("sign");
		sign = sign.toLowerCase();
		System.out.println("[notifyservlet] doPost: "+sign);
		OrdersBean bean = new OrdersBean();
		Orders vo = bean.qureyOrder(payId);
		if (vo == null) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("{\"status\":-1}");
			out.flush();
			out.close();
			return;
		}
		String mySign = null;
		if (mySign == null || "".equals(mySign)) {
			String tmp = "";
			tmp += vo.getOrderId();
			tmp += vo.getAppId();
			tmp += vo.getImei();
			String  payprice = new DecimalFormat("0.00").format(vo.getAmount()); 
			tmp += payprice;
			tmp += vo.getCreateTime();
			String[] s = vo.getExInfo().split("#");
			if (s.length > 0) {
				tmp += s[0];
			}
			//tmp += vo.getExInfo();
			System.out.println(tmp+ Constants.U_KEY);
			mySign = StringEncrypt.Encrypt(tmp + Constants.U_KEY);
		}

		System.out.println("payId=" + payId);
		System.out.println("kzpayId=" + kzpayId);
		System.out.println("status=" + status);
		System.out.println("paymemo=" + paymemo);
		System.out.println("sign=" + sign);
		System.out.println("mysign=" + mySign);
		System.out.println("---------------------------------------------------------");
		
		System.out.println(vo.getOrderId());
		System.out.println(vo.getAppId());
		System.out.println(vo.getImei());
		System.out.println(vo.getAmount());
		System.out.println(vo.getCreateTime());
		String[] s1 = vo.getExInfo().split("#");
		System.out.println(s1[0]);
		System.out.println(Constants.U_KEY);

		if (payId == null || kzpayId == null || status == null
				|| paymemo == null || sign == null || !sign.equals(mySign)) {
			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("{\"status\":" + Constants.BACK_K_ERROR + "}");
			out.flush();
			out.close();
		} else {
			try
			{
				if (payamount == null || payamount.equals(""))
				{
					payamount = vo.getAmount() + "";
				}
				bean.updateOrderAmountPayId(payId, "", payamount);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			int _status = bean.updateOrderKStatus(payId, Integer.valueOf(status));

			response.setContentType("text/html");
			PrintWriter out = response.getWriter();
			out.println("{\"status\":" + _status + "}");
			out.flush();
			out.close();
		}
	}

	/**
	 * Initialization of the servlet. <br>
	 * 
	 * @throws ServletException
	 *             if an error occure
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

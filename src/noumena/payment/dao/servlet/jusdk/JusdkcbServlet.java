package noumena.payment.dao.servlet.jusdk;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.c07073.C07073Charge;
import noumena.payment.jusdk.JusdkCharge;


public class JusdkcbServlet extends HttpServlet
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
		response.setContentType("text/html;charset=UTF-8");

//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("19pay cb->" + res);

		Map<String, String> transformedMap = new HashMap<String,String>();
	       String notify_data = request.getParameter("notify_data");
	       transformedMap.put("notify_data", notify_data);
	       String orderid = request.getParameter("orderid");
	       transformedMap.put("orderid", orderid);
	       String sign = request.getParameter("sign");
	       transformedMap.put("sign", sign);
	       String dealseq = request.getParameter("dealseq");
	       transformedMap.put("dealseq", dealseq);
	       String uid = request.getParameter("uid");
	       transformedMap.put("uid", uid);
	       String subject = request.getParameter("subject");
	       transformedMap.put("subject", subject);
	       String v = request.getParameter("v");
	       transformedMap.put("v", v);

		String ret = JusdkCharge.getCallbackFromJusdk(transformedMap);

		response.setContentType("text/html;charset=UTF-8");
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

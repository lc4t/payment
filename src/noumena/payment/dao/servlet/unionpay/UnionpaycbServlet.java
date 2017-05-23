package noumena.payment.dao.servlet.unionpay;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.unionpay.UnionpayCharge;

public class UnionpaycbServlet extends HttpServlet
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

//		orderTime=20140604101932&
//		settleDate=0424&
//		orderNumber=01014222&
//		exchangeRate=0&
//		signature=5efc2e8baa50f1ac0b0f0ca3724dc2b0&
//		settleCurrency=156&
//		signMethod=MD5&
//		transType=01&
//		respCode=00&
//		charset=UTF-8&
//		sysReserved=%7BtraceTime%3D0604101932%26acqCode%3D00215800%26traceNumber%3D078832%26cardInfo%3DezI5NmMyMTAwMDA4OTdlNTAwMTAwYmQ0OXzmi5vllYbpk7booYx8NjIyNioqKioqKioqNjc4NXww%250ANXw2MjI2NDQwMTIzNDU2Nzg1fQ%253D%253D%250A%7D&
//		version=1.0.0&
//		settleAmount=1&
//		transStatus=00&
//		reqReserved=%E9%80%8F%E4%BC%A0%E4%BF%A1%E6%81%AF&
//		merId=880000000000084&
//		qn=201406041019320788327

//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("union pay cb->" + res);
		
		String transStatus = request.getParameter("transStatus");// 交易状态
		String orderNumber = request.getParameter("orderNumber");// 商户订单号
		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();)
		{
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++)
			{
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}

		String ret = UnionpayCharge.getCallbackFromUnionpay(params, transStatus, orderNumber);

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

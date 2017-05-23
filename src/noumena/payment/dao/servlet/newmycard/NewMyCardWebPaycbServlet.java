package noumena.payment.dao.servlet.newmycard;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.newmycard.NewMyCardTWCharge;
import noumena.payment.util.DateUtil;

public class NewMyCardWebPaycbServlet extends HttpServlet {

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
	 * model: 1 - 客户端请求支付 2 - 客户端请求验证订单 11 - 小米请求Token服务成功状态回调
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
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		Map<String, String> params = new HashMap<String, String>();
		Map<?, ?> requestParams = request.getParameterMap();
		for (Iterator<?> iter = requestParams.keySet().iterator(); iter
				.hasNext();) {
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++) {
				valueStr = (i == values.length - 1) ? valueStr + values[i]
						: valueStr + values[i] + ",";
			}
			System.out.println("NewMyCardWebPaycbServlet"+name+" "+valueStr);
			params.put(name, valueStr);
		}
		String remoteip = request.getRemoteAddr();
		params.put("remoteip", remoteip);
		String ret="fail";
		System.out.println("newmycardtwebcb reciepOrders   =====("
				+ DateUtil.getCurTimeStr() + ")=========");
		String payId=params.get("FacTradeSeq");
		String returnCode=params.get("ReturnCode");
		if(params!=null&&returnCode!=null&&returnCode.equals("1")){
		OrdersBean bean = new OrdersBean();
		Orders order=bean.qureyOrder(payId);
		if(order==null){
			ret="order is not exit";	
		}else{
		ret = NewMyCardTWCharge.checkOrdersInterance(payId, order.getSign());
		}
		}
		System.out.println("newmycardtwebcb =================result" + ret);
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
	public void init() throws ServletException {
		// Put your code here
	}

}

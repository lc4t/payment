package noumena.payment.dao.servlet.gnetop;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.gnetop.GnetopCharge;
import noumena.payment.gnetop.GnetopOrderVO;

public class GnetopcbServlet extends HttpServlet {

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

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");
		String orderId = request.getParameter("orderid"); // orderid
		String serverId = request.getParameter("serverid"); // 服务器id
		String account = request.getParameter("account"); // 账户
		String userId = request.getParameter("userid"); // 角色id
		String produceId = request.getParameter("productid"); // id
		String amount = request.getParameter("amount"); // 充值金额，单位分
		String gamepoints = request.getParameter("gamepoints"); // 描述
		String sign = request.getParameter("sign"); // md5签名
		String time = request.getParameter("time"); // 时间戳
		String secondValue = "";
		String ret = "";
		boolean is = true;
		try {
			@SuppressWarnings("unchecked")
			Map<String, String> requestParams = request.getParameterMap();
			int count = 0;

			for (Iterator<?> iter = requestParams.keySet().iterator(); iter
					.hasNext();) {
				count = count + 1;
				if (count == 2) {
					String key = (String) iter.next();
					secondValue = requestParams.get(key);
					is = true;
					break;
				}

			}
		} catch (Exception e) {
			ret = "{\"result\":\"error\",\"msg\":\"param is error\",\"data\":\"\"}";
		}
		if (is) {

			GnetopOrderVO vo = new GnetopOrderVO(orderId, serverId, account,
					userId, produceId, amount, gamepoints, sign, time);

			ret = GnetopCharge.getCallbackFromGnetop(vo, secondValue);
		}

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

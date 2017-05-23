package noumena.payment.dao.servlet.pps;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.pps.PPSCharge;
import noumena.payment.pps.PPSOrderVO;

public class PPScbServlet extends HttpServlet
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
	//http://paystage.ko.cn:6001/paymentsystem/ppscb?appid=535
	//http://p.ko.cn/pay/ppscb?appid=535
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=UTF-8");

//		?user_id=65430637&role_id=354546&order_id=2569214&money=100&time=1283916711&userData=xxxxxx&sign=6203f0fb85944e76b2157f49fb27beb9a2c7cd04
//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("union pay cb->" + res);

		String appid = request.getParameter("appid");		//appid
		String user_id = request.getParameter("user_id");	//用户id
		String role_id = request.getParameter("role_id");	//角色id
		String order_id = request.getParameter("order_id");	//平台订单号
		String money = request.getParameter("money");		//充值金额，单位元
		String time = request.getParameter("time");			//时间戳
		String userData = request.getParameter("userData");	//自定义数据（商户订单号）
		String sign = request.getParameter("sign");			//md5签名
		
		PPSOrderVO vo = new PPSOrderVO();
		vo.setAppid(appid);
		vo.setUser_id(user_id);
		vo.setRole_id(role_id);
		vo.setOrder_id(order_id);
		vo.setMoney(money);
		vo.setTime(time);
		vo.setUserData(userData);
		vo.setSign(sign);
		
		String ret = PPSCharge.getCallbackFromPPS(vo);

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

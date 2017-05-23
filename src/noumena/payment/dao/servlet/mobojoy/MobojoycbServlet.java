package noumena.payment.dao.servlet.mobojoy;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.mobojoy.MobojoyCharge;
import noumena.payment.mobojoy.MobojoyOrderVO;

public class MobojoycbServlet extends HttpServlet
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
		String App_Id = request.getParameter("App_Id");
		String Uin = request.getParameter("Uin");
		String Urecharge_Id = request.getParameter("Urecharge_Id");
		String Extra = request.getParameter("Extra");
		String Recharge_Money_Code = request.getParameter("Recharge_Money_Code");
		String Recharge_Money = request.getParameter("Recharge_Money");
		String Recharge_M = request.getParameter("Recharge_M");
		String Pay_Status = request.getParameter("Pay_Status");
		String Create_Time = request.getParameter("Create_Time");
		String Sign = request.getParameter("Sign");
		
		MobojoyOrderVO vo = new MobojoyOrderVO();
		
		vo.setApp_Id(App_Id);
		vo.setUin(Uin);
		vo.setUrecharge_Id(Urecharge_Id);
		vo.setExtra(Extra);
		vo.setRecharge_Money_Code(Recharge_Money_Code);
		vo.setRecharge_Money(Recharge_Money);
		vo.setRecharge_M(Recharge_M);
		vo.setPay_Status(Pay_Status);
		vo.setCreate_Time(Create_Time);
		vo.setSign(Sign);
		
		String ret = MobojoyCharge.getCallbackFromMobojoy(vo);

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

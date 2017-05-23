package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.xiaomi.xiaomiCharge;
import noumena.payment.xiaomi.xiaomiOrderVO;

public class xiaomicbServlet extends HttpServlet
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
		xiaomiOrderVO vo = new xiaomiOrderVO();
		
		vo.setAppId(request.getParameter("appId"));			//请求的类型：1-得到交易id；2-查询订单状态
		vo.setCpOrderId(request.getParameter("cpOrderId"));			//tstore的appid
		vo.setCpUserInfo(request.getParameter("cpUserInfo"));	//tstore的付费id
		vo.setUid(request.getParameter("uid"));				//imei
		vo.setOrderId(request.getParameter("orderId"));			//uid
		vo.setOrderStatus(request.getParameter("orderStatus"));		//不要
		vo.setPayFee(request.getParameter("payFee"));		//不要
		vo.setProductCode(request.getParameter("productCode"));			//回调游戏的url
		vo.setProductName(request.getParameter("productName"));		//渠道号
		vo.setProductCount(request.getParameter("productCount"));	//设备类型，iPhone/ANDK
		vo.setPayTime(request.getParameter("payTime"));	//设备id
		vo.setSignature(request.getParameter("signature"));		//游戏版本
		
		xiaomiCharge.getCallbackFromXiaomi(vo);
		
		PrintWriter out = response.getWriter();
		out.println("{\"errcode\":200,\"errMsg\":\"\"}");
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

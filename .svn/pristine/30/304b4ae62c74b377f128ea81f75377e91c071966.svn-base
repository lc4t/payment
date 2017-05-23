package noumena.payment.dao.servlet.common;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.common.CommonCharge;

public class CommonServlet extends HttpServlet
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
		
		//必需的参数
		String stype = request.getParameter("model");			//请求的类型：1-得到交易id；2-查询订单状态
//		String uid = request.getParameter("uid");				//
//		String pkgid = request.getParameter("pkgid");			//
//		String itemid = request.getParameter("itemid");			//
//		String payprice = request.getParameter("price");		//一定是人民币元
//		String cburl = request.getParameter("cburl");			//单机游戏可以不需要回调地址
//		String paytype = request.getParameter("paytype");		//支付渠道
//		
//		//选填的参数
//		String imei = request.getParameter("imei");				//
//		String channel = request.getParameter("channel");		//
//		String device_type = request.getParameter("device_type");	//
//		String device_id = request.getParameter("device_id");	//
//		String gversion = request.getParameter("gversion");		//
//		String osversion = request.getParameter("osversion");	//

		
		//验证订单用参数
		String payIds = request.getParameter("payIds");			//待查询的所有订单号，以","分隔
		
		String ret = "";
		if (stype == null)
		{
			stype = "";
		}
		if (stype.equals("1"))
		{
//			Orders vo = new Orders();
//			vo.setImei(imei);
//			vo.setUId(uid);
//			vo.setItemId(itemid);
//			vo.setGversion(gversion);
//			vo.setOsversion(osversion);
//			vo.setDeviceId(device_id);
//			vo.setDeviceType(device_type);
//			vo.setChannel(channel);
//			vo.setAppId(pkgid);
//			if (payprice == null)
//			{
//				payprice = "0";
//			}
//			vo.setAmount(Float.valueOf(payprice) * 100);
//			vo.setCreateTime(DateUtil.getCurrentTime());
//			vo.setPayType(paytype);
//			vo.setCallbackUrl(cburl);
//			
//			ret = CommonCharge.getTransactionId(vo);
		}
		else if (stype.equals("2"))
		{
			ret = CommonCharge.checkOrdersStatus(payIds);
		}
		else
		{
			System.out.println("common model invalid->" + stype);
			ret = "invalid";
		}

//		System.out.println("common order id->(" + uid + ")" + ret);
		System.out.println("order status-->"+ret);
		response.setContentType("text/html");
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

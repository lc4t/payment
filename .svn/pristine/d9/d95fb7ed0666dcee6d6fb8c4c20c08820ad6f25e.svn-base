package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.mo9.Mo9Charge;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;

public class Mo9Servlet extends HttpServlet
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
		
		//必需的参数
		String stype = request.getParameter("model");			//请求的类型：1-得到交易id；2-查询订单状态
		String uid = request.getParameter("uid");				//
		String pkgid = request.getParameter("pkgid");			//
		String itemid = request.getParameter("itemid");			//
		String payprice = request.getParameter("price");		//
		String cburl = request.getParameter("cburl");			//单机游戏可以不需要回调地址
		
		//选填的参数
		String imei = request.getParameter("imei");				//
		String channel = request.getParameter("channel");		//
		String device_type = request.getParameter("device_type");	//
		String device_id = request.getParameter("device_id");	//
		String gversion = request.getParameter("gversion");		//
		String osversion = request.getParameter("osversion");	//

		//支付方的参数
		String appid = request.getParameter("appid");			//支付渠道方生成的产品id
		String name = request.getParameter("name");				//商品名称 
		String loc = request.getParameter("loc");				//支付用户的地区，支持 CN, TW, US 
		String currency = request.getParameter("currency");		//货币为支付货币类型，支持 CNY, USD，在地区为 CN 时该用CNY，其他地区用 USD 

		//验证订单用参数
		String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
		
		String ret = "";
		if (stype.equals("1"))
		{
			String paymemo = appid + "#" + loc + "#" + currency + "#" + URLEncoder.encode(name, "utf-8");
			Orders vo = new Orders();
			vo.setImei(imei);
			vo.setUId(uid);
			vo.setItemId(itemid);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setDeviceId(device_id);
			vo.setDeviceType(device_type);
			vo.setChannel(channel);
			vo.setAppId(pkgid);
			vo.setAmount(Float.valueOf(payprice));
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setExInfo(paymemo);
			vo.setPayType(Constants.PAY_TYPE_MO9);
			vo.setCallbackUrl(cburl);
			
			System.out.println("mo9 appid(" + vo.getAppId() + ")price(" + vo.getAmount() + ")paymemo(" + vo.getExInfo() + ")");
			
			ret = Mo9Charge.getTransactionId(vo);
		}
		else
		{
			System.out.println("mo9 check order ids->" + payIds);
			ret = Mo9Charge.checkOrdersStatus(payIds);
		}

		System.out.println("mo9 order id->" + ret);
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

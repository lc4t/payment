package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.cht.CHTCharge;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;

public class CHTServlet extends HttpServlet
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
	 * http://localhost:8080/paymentsystem/tstore?model=1&appid=OA00302047&cburl=abc
	 * http://localhost:8080/paymentsystem/tstore?model=2&payIds=1,2
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
		String productid = request.getParameter("productid");	//支付渠道方生成的产品id
		String transid = request.getParameter("transid");		//支付渠道方生成的“交易的識別碼”
		String subid = request.getParameter("subid");			//支付渠道方生成的“交易的收據”（Receipt）

		//验证订单用参数
		String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
		
		String ip = request.getRemoteAddr();

		String ret = "";
		if (stype.equals("1"))
		{
			Orders vo = new Orders();
			vo.setImei(imei);
			vo.setUId(uid);
			vo.setProductId(itemid);
			vo.setItemId(itemid);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setDeviceId(device_id);
			vo.setDeviceType(device_type);
			vo.setChannel(channel);
			vo.setAppId(pkgid);
			vo.setAmount(Float.valueOf(payprice));
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setExInfo(productid + "#" + transid + "#" + subid + "#" + ip);
			vo.setPayType(Constants.PAY_TYPE_CHT);
			vo.setCallbackUrl(cburl);
			
			ret = CHTCharge.getTransactionId(vo);
		}
		else
		{
			ret = CHTCharge.checkOrdersStatus(payIds);
		}

		System.out.println("cht order id->" + ret);
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

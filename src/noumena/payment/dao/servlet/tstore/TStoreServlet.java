package noumena.payment.dao.servlet.tstore;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.model.Orders;
import noumena.payment.tstore.TStoreCharge;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;

public class TStoreServlet extends HttpServlet
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
		String stype = request.getParameter("model");			//请求的类型：1-得到交易id；2-查询订单状态
		System.out.println("tstoretype*********"+stype);
		String ret = "";
		if (stype == null)
		{
			stype = "";
		}
		if (stype.equals("1"))
		{
			String gameid = request.getParameter("appid");			//tstore的appid
			String productid = request.getParameter("productid");	//tstore的付费id
			String imei = request.getParameter("imei");				//imei
			String accountid = request.getParameter("uid");			//uid
			String payprice = request.getParameter("price");		//不要
			String paymemo = request.getParameter("paymemo");		//不要
			String cburl = request.getParameter("cburl");			//回调游戏的url
			String channel = request.getParameter("channel");		//渠道号
			String device_type = request.getParameter("device_type");	//设备类型，iPhone/ANDK
			String device_id = request.getParameter("device_id");	//设备id
			String gversion = request.getParameter("gversion");		//游戏版本
			String osversion = request.getParameter("osversion");	//操作系统版本
			String payTypeId = request.getParameter("payTypeId");	//不要
			String ip = request.getRemoteAddr();

			if (payprice == null || payprice.equals(""))
			{
				payprice = "0.00";
			}
			else
			{
				payprice = new DecimalFormat("0.00").format(new Float(payprice));
			}
			Orders vo = new Orders();
			vo.setImei(imei);
			vo.setUId(accountid);
			vo.setProductId(productid);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setDeviceId(device_id);
			vo.setDeviceType(device_type);
			vo.setChannel(channel);
			vo.setAppId(gameid);
			vo.setAmount(Float.valueOf(payprice));
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setExInfo(paymemo + "#" + ip);
			if (payTypeId == null || "".equals(payTypeId))
			{
				payTypeId = Constants.PAY_TYPE_TSTORE;
			}
			vo.setPayType(payTypeId);
			vo.setCallbackUrl(cburl);
			
			System.out.println("tstore appid(" + vo.getAppId() + ")price(" + vo.getAmount() + ")paymemo(" + vo.getExInfo() + ")");
			ret = TStoreCharge.getTransactionId(vo);
		}
		/*else if (stype.equals("2"))
		{
			String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
			System.out.println("tstore check order ids->" + payIds);
			ret = TStoreCharge.checkOrdersStatus(payIds);
		}*/
		else if (stype.equals("22"))
		{
			String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
			System.out.println("tstore check order2 ids->" + payIds);
			ret = TStoreCharge.checkOrdersStatusNew(payIds);
		}
		else if (stype.equals("receipt"))
		{
			String txid = request.getParameter("txid");					//订单号
			String appid = request.getParameter("appid");				//订单appid
			String receipt = request.getParameter("receipt");			//订单收据
			System.out.println("tstore check order new->" + txid + "#" + appid + "#" + receipt);
			ret = TStoreCharge.checkOrdersStatusNew(txid, appid, receipt);
		}
		else
		{
			System.out.println("tstore model invalid->" + stype);
		}

		System.out.println("tstore order id->" + ret);
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

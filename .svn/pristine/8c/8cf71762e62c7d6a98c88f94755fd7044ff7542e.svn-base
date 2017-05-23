package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.adways.AdwaysCharge;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;

public class AdwaysServlet extends HttpServlet
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
		//必需的参数
		request.setCharacterEncoding("utf-8");
		String ret = "";
		
		String stype = request.getParameter("model");			//请求的类型：1-得到交易id；2-查询订单状态
		if (stype == null)
		{
			stype = "";
		}
		
		try
		{
			if (stype.equals("1"))
			{
				String gameid = request.getParameter("appid");			//appid
				String productid = request.getParameter("productid");	//付费id
				String imei = request.getParameter("imei");				//imei
				String accountid = request.getParameter("uid");			//uid
				String payprice = request.getParameter("price");		//
				String paymemo = request.getParameter("paymemo");		//地区#货币#最低信用道具id#道具名
				String cburl = request.getParameter("cburl");			//回调游戏的url
				String channel = request.getParameter("channel");		//渠道号
				String device_type = request.getParameter("device_type");	//设备类型，iPhone/ANDK
				String device_id = request.getParameter("device_id");	//设备id
				String gversion = request.getParameter("gversion");		//游戏版本
				String osversion = request.getParameter("osversion");	//操作系统版本
				String payTypeId = request.getParameter("payTypeId");	//
				String subId = request.getParameter("subid");			//sub id
	
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
				vo.setItemId(productid);
				vo.setGversion(gversion);
				vo.setOsversion(osversion);
				vo.setDeviceId(device_id);
				vo.setDeviceType(device_type);
				vo.setChannel(channel);
				vo.setAppId(gameid);
				vo.setAmount(Float.valueOf(payprice));
				vo.setCreateTime(DateUtil.getCurrentTime());
				vo.setExInfo(paymemo);
				if (payTypeId == null || "".equals(payTypeId))
				{
					payTypeId = Constants.PAY_TYPE_ADWAYS;
				}
				vo.setPayType(payTypeId);
				vo.setCallbackUrl(cburl);
				vo.setSubId(subId);
				
				System.out.println("adways appid(" + vo.getAppId() + ")price(" + vo.getAmount() + ")");
				
				ret = AdwaysCharge.getTransactionId(vo);
			}
			else if (stype.equals("2"))
			{
				String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
				System.out.println("adways check order ids->" + payIds);
				ret = AdwaysCharge.checkOrdersStatus(payIds);
			}
			else
			{
				ret = "model is invalid";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			ret = "-------------->adways exception<------------------";
		}

		System.out.println("adways order id->" + ret);
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

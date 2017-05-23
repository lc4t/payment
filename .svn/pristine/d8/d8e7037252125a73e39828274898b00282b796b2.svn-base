package noumena.payment.dao.servlet.winner;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.ios.IOSCharge;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.winner.WinnerCharge;

public class WinnerServlet extends HttpServlet
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
		String payprice = request.getParameter("price");		//一定是人民币元
		String cburl = request.getParameter("cburl");			//单机游戏可以不需要回调地址
		
		//选填的参数
		String imei = request.getParameter("imei");				//
		String channel = request.getParameter("channel");		//
		String device_type = request.getParameter("device_type");	//
		String device_id = request.getParameter("device_id");	//
		String gversion = request.getParameter("gversion");		//
		String osversion = request.getParameter("osversion");	//

		//支付方的参数
		String acccode = request.getParameter("acccode");		//Winner创建的Account Code
		String sid = request.getParameter("sid");				//Server ID
		String currpoint = request.getParameter("currpoint");	//当前点数
		String clientip = request.getRemoteAddr();				//客户端ip
		String paytype = request.getParameter("paytype");		//支付类型，ios、gp
		String receipt = request.getParameter("receipt");		//iOS支付收据
		String gg_data = request.getParameter("gg_data");		//GooglePlay收据
		String gg_sign = request.getParameter("gg_sign");		//GooglePlay签名
		String pt = "";
		if (paytype.equals("ios"))
		{
			pt = Constants.PAY_TYPE_WINNERIOS;
		}
		else if (paytype.equals("gp"))
		{
			pt = Constants.PAY_TYPE_WINNERGP;
		}
		else
		{
			stype = "";
		}
		
		//验证订单用参数
		String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
		
		String ret = "";
		if (stype == null)
		{
			stype = "";
		}
		if (stype.equals("1"))
		{
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
			if (payprice == null)
			{
				payprice = "0";
			}
			vo.setAmount(Float.valueOf(payprice));
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setPayType(pt);
			vo.setCallbackUrl(cburl);
			
			ret = WinnerCharge.getTransactionId(vo);
		}
//		else if (stype.equals("2"))
//		{
//			ret = WinnerCharge.checkOrdersStatus(payIds);
//		}
		else if (stype.equals("3"))
		{
			//创建订单并且返回订单结果
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
			vo.setPayType(pt);
			vo.setCallbackUrl(cburl);
			
			if (paytype.equals("ios"))
			{
				vo.setExInfo(receipt);
			}
			else
			{
				vo.setExInfo(gg_data);
			}

			System.out.println("winner charge ->" + uid + "(" + System.currentTimeMillis() + ")");
			ret = WinnerCharge.getTransactionIdAndStatus(paytype, vo, acccode, sid, currpoint, clientip, gg_sign);
		}
		else
		{
			System.out.println("winner model invalid->" + stype + "|" + paytype);
			ret = "invalid";
		}

		System.out.println("winner order id->(" + uid + ")" + ret);
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

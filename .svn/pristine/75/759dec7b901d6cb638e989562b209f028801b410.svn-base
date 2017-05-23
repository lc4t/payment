package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.model.Orders;
import noumena.payment.qihu.QihuCharge;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;

public class QihuServlet extends HttpServlet
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
		String stype = request.getParameter("model");			//请求的类型：1-得到交易id；2-查询订单状态；info-得到一些支付信息
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
		String code = request.getParameter("code");			//通过sdk产生的授权码
		String appkey = request.getParameter("appkey");		//360分配的游戏的appkey
		
		String token = request.getParameter("token");		//通过支付中心得到
		String qihuid = request.getParameter("qihuid");		//通过支付中心得到

		//验证订单用参数
		String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
		
		String ret = "";
		if (stype == null)
		{
			stype = "";
		}
		if (stype.equals("1"))
		{
			if(!"com.kongzhong.publish.gz.qh360".equals(pkgid)){
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
				vo.setExInfo(token + "<#>" + qihuid);
				vo.setPayType(Constants.PAY_TYPE_QIHU);
				vo.setCallbackUrl(cburl);
				
				ret = QihuCharge.getTransactionId(vo);
			}
			
		}
		else if (stype.equals("info"))
		{
			//通过code得到支付信息（uid和token）
			ret = QihuCharge.getTransactionInfo(code, appkey);
		}
		else if (stype.equals("token"))
		{
			//通过token得到支付信息（uid和token）
			ret = QihuCharge.getTransactionInfoByToken(token);
		}
		else if (stype.equals("2"))
		{
			ret = QihuCharge.checkOrdersStatus(payIds);
		}
		else
		{
			ret = "model is invalid";
		}

		System.out.println(DateUtil.getCurTimeStr()+"qh360_order_id1->" + ret);
		response.setContentType("text/html");
		System.out.println(DateUtil.getCurTimeStr()+"qh360_order_id2->" + ret);
		PrintWriter out = response.getWriter();
		System.out.println(DateUtil.getCurTimeStr()+"qh360_order_id3->" + ret);
		out.println(ret);
		System.out.println(DateUtil.getCurTimeStr()+"qh360_order_id4->" + ret);
		out.flush();
		System.out.println(DateUtil.getCurTimeStr()+"qh360_order_id5->" + ret);
		out.close();
		System.out.println(DateUtil.getCurTimeStr()+"qh360_order_id6->" + ret);
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

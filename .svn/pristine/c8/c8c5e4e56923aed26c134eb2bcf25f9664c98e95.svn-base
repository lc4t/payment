package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLDecoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.ndpay.NdpayCharge;
import noumena.payment.ndpay.NdpaycbOrderVO;

public class NdpaycbServlet extends HttpServlet
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
		response.setCharacterEncoding("gbk");
//		response.setContentType("charset=gbk");
		
		StringBuffer buffer = new StringBuffer();
		NdpaycbOrderVO ordervo = new NdpaycbOrderVO();

		String AppId = request.getParameter("AppId");
		buffer.append("&");
		buffer.append("AppId=");
		buffer.append(AppId);
		ordervo.setAppId(AppId);

		String Act = request.getParameter("Act");
		buffer.append("&");
		buffer.append("Act=");
		buffer.append(Act);
		ordervo.setAct(Act);
		
		{
			String str = request.getQueryString();
			String s = "ProductName=";
			int pos=0;
			pos = str.indexOf(s);
			str = str.substring(pos + s.length());
			pos = str.indexOf("&");
			str = str.substring(0, pos);
			str = URLDecoder.decode(str, "utf-8");
			ordervo.setProductName(str);
		}
		
//		String ProductName = request.getParameter("ProductName");
//		buffer.append("&");
//		buffer.append("ProductName=");
//		buffer.append(ProductName);
//		ordervo.setProductName(ProductName);

		String ConsumeStreamId = request.getParameter("ConsumeStreamId");
		buffer.append("&");
		buffer.append("ConsumeStreamId=");
		buffer.append(ConsumeStreamId);
		ordervo.setConsumeStreamId(ConsumeStreamId);

		String CooOrderSerial = request.getParameter("CooOrderSerial");
		buffer.append("&");
		buffer.append("CooOrderSerial=");
		buffer.append(CooOrderSerial);
		ordervo.setCooOrderSerial(CooOrderSerial);

		String Uin = request.getParameter("Uin");
		buffer.append("&");
		buffer.append("Uin=");
		buffer.append(Uin);
		ordervo.setUin(Uin);

		String GoodsId = request.getParameter("GoodsId");
		buffer.append("&");
		buffer.append("GoodsId=");
		buffer.append(GoodsId);
		ordervo.setGoodsId(GoodsId);

		{
			String str = request.getQueryString();
			String s = "GoodsInfo=";
			int pos=0;
			pos = str.indexOf(s);
			str = str.substring(pos + s.length());
			pos = str.indexOf("&");
			str = str.substring(0, pos);
			str = URLDecoder.decode(str, "utf-8");
			ordervo.setGoodsInfo(str);
		}
		
//		String GoodsInfo = request.getParameter("GoodsInfo");
//		buffer.append("&");
//		buffer.append("GoodsInfo=");
//		buffer.append(GoodsInfo);
//		ordervo.setGoodsInfo(GoodsInfo);

		String GoodsCount = request.getParameter("GoodsCount");
		buffer.append("&");
		buffer.append("GoodsCount=");
		buffer.append(GoodsCount);
		ordervo.setGoodsCount(GoodsCount);

		String OriginalMoney = request.getParameter("OriginalMoney");
		buffer.append("&");
		buffer.append("OriginalMoney=");
		buffer.append(OriginalMoney);
		ordervo.setOriginalMoney(OriginalMoney);

		String OrderMoney = request.getParameter("OrderMoney");
		buffer.append("&");
		buffer.append("OrderMoney=");
		buffer.append(OrderMoney);
		ordervo.setOrderMoney(OrderMoney);

		{
			String str = request.getQueryString();
			String s = "Note=";
			int pos=0;
			pos = str.indexOf(s);
			str = str.substring(pos + s.length());
			pos = str.indexOf("&");
			str = str.substring(0, pos);
			str = URLDecoder.decode(str, "utf-8");
			ordervo.setNote(str);
		}
		
//		String Note = request.getParameter("Note");
//		buffer.append("&");
//		buffer.append("Note=");
//		buffer.append(Note);
//		ordervo.setNote(Note);

		String PayStatus = request.getParameter("PayStatus");
		buffer.append("&");
		buffer.append("PayStatus=");
		buffer.append(PayStatus);
		ordervo.setPayStatus(PayStatus);

		String CreateTime = request.getParameter("CreateTime");
		buffer.append("&");
		buffer.append("CreateTime=");
		buffer.append(CreateTime);
		ordervo.setCreateTime(request.getParameter("CreateTime"));

		String Sign = request.getParameter("Sign");
		buffer.append("&");
		buffer.append("Sign=");
		buffer.append(Sign);
		ordervo.setSign(request.getParameter("Sign"));
		
		System.out.println("91 cb ->" + buffer.toString());
		
		String ret = NdpayCharge.getCallbackFromNdpay(ordervo);
		
		ret = "{\"ErrorCode\":\"1\",\"ErrorDesc\":\"接收成功\"}";
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

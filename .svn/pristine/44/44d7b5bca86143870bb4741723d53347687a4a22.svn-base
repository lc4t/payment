package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.kongmp.KongMPCharge;
import noumena.payment.kongmp.KongMPOrderVO;

public class KongMPcbServlet extends HttpServlet
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
		String ret = "";
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if (action == null)
		{
			ret = "{ret:-1;msg:\"\"}";
		}
		else if (action.equals("1"))
		{
			//通知手机号码
			String phoneid = request.getParameter("phoneid");
			String content = request.getParameter("content");
			
			ret = KongMPCharge.getPhoneId(phoneid, content);
		}
		else if (action.equals("2"))
		{
			//通知获取手机号码的短信目的号码发生变化
			String smsdestid = request.getParameter("smsdestid");
			
			ret = KongMPCharge.changeSMSDestId(smsdestid);
		}
		else if (action.equals("3"))
		{
			//通知包月订购完成
			KongMPOrderVO ordervo = new KongMPOrderVO();
			String orderid = request.getParameter("orderid");
			String mob = request.getParameter("mob");
			String serviceId = request.getParameter("serviceId");
			String price = request.getParameter("price");
			String time = request.getParameter("time");
			String status = request.getParameter("status");
			String sign = request.getParameter("sign");
			
			StringBuffer logstr = new StringBuffer();
			logstr.append("&orderid=");
			logstr.append(orderid);
			ordervo.setOrderid(orderid);
			logstr.append("&mob=");
			logstr.append(mob);
			ordervo.setMob(mob);
			logstr.append("&serviceId=");
			logstr.append(serviceId);
			ordervo.setServiceId(serviceId);
			logstr.append("&price=");
			logstr.append(price);
			ordervo.setPrice(price);
			logstr.append("&time=");
			logstr.append(time);
			ordervo.setTime(time);
			logstr.append("&status=");
			logstr.append(status);
			ordervo.setStatus(status);
			logstr.append("&sign=");
			logstr.append(sign);
			ordervo.setSign(sign);
			
			System.out.println("kongmp cb subscribed->" + logstr.toString());
			
			ret = KongMPCharge.getCallbackFromKong(ordervo, 0);
		}
		else if (action.equals("4"))
		{
			//通知包月退订完成
			KongMPOrderVO ordervo = new KongMPOrderVO();
			String orderid = request.getParameter("orderid");
			String sign = request.getParameter("sign");
			ordervo.setOrderid(orderid);
			ordervo.setSign(sign);
			System.out.println("kongmp cb unsubscribed->" + "orderid=" + orderid + "&sign=" + sign);
			ret = KongMPCharge.getCallbackFromKong(ordervo, 1);
		}

		System.out.println("kongmp cb ret->" + ret);
		response.setCharacterEncoding("UTF-8");
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

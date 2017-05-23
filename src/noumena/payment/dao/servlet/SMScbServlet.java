package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.sms.SMSCharge;
import noumena.payment.sms.SMSOrderDataVO;

public class SMScbServlet extends HttpServlet
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
		SMSOrderDataVO vo = new SMSOrderDataVO();
		String orderid = request.getParameter("orderNumber");
		if (orderid == null)
		{
			orderid = "";
		}
		vo.setOrderid(orderid);
		vo.setPhoneid(request.getParameter("mobile"));
		vo.setMessageid(request.getParameter("messageid"));
		vo.setStatus(request.getParameter("report"));
		vo.setRettime(request.getParameter("reportReturnTime"));
		
		System.out.println("sms orderNumber->" + vo.getOrderid());
		System.out.println("sms mobile->" + vo.getPhoneid());
		System.out.println("sms messageid->" + vo.getMessageid());
		System.out.println("sms report->" + vo.getStatus());
		System.out.println("sms reportReturnTime->" + vo.getRettime());
		
		try
		{
//			String path = OSUtil.getRootPath() + "../../logs/cmgccb/" + DateUtil.getCurTimeStr().substring(0, 8);
//			OSUtil.makeDirs(path);
//			String filename = path + "/" + vo.getUserId() + "_" + vo.getCpParam();
//			OSUtil.saveXML(root, filename);
		
			String ret = SMSCharge.getCallbackSMS(vo);

			System.out.println("sms ret->(" + vo.getOrderid() + ")" + ret);
			
			PrintWriter out = response.getWriter();
			out.println(ret);
			out.flush();
			out.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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

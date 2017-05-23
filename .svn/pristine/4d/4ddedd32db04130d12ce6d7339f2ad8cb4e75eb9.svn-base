package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.mycardtw.MyCardTWCharge;
import noumena.payment.mycardtw.MyCardTWOrderDataVO;

public class MyCardTWcbsServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * MyCard台湾Billing模式支付成功回调
	 * 
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
//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("mycartw cb success->" + res);

		MyCardTWOrderDataVO vo = new MyCardTWOrderDataVO();
		vo.setAuthCode(request.getParameter("AuthCode"));
		vo.setReturnMsgNo(request.getParameter("ReturnMsgNo"));
		vo.setReturnMsg(request.getParameter("ReturnMsg"));
		vo.setTradeSeq(request.getParameter("TradeSeq"));
		vo.setCardKind(request.getParameter("CardKind"));
		vo.setProjNo(request.getParameter("ProjNo"));
		vo.setMyCardId(request.getParameter("MyCardId"));
		vo.setPrice(request.getParameter("Price"));
		vo.setCrrnyShotName(request.getParameter("CrrnyShotName"));
		
		System.out.println("mycartw billing cb->" + vo.getAuthCode());
		System.out.println("mycartw billing cb->" + vo.getReturnMsgNo());
		System.out.println("mycartw billing cb->" + vo.getReturnMsg());
		//ReturnMsgNo=1&ReturnMsg=&AuthCode=6B0602FC5DE69E593C9D68E5A5461CAD08C832E6EC0945ED&TradeSeq=235&CardKind=&ProjNo=&MyCardId=&Price=&CrrnyShotName=
		MyCardTWCharge.getCallbackFromMyCardTWBilling(vo);
		
		PrintWriter out = response.getWriter();
		out.println("SUCCESS");
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

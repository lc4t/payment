package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.mycardtw.MyCardTWCharge;
import noumena.payment.mycardtw.MyCardTWOrderDataVO;

public class MyCardTWcbfServlet extends HttpServlet
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
	 * MyCard台湾Billing模式支付失败回调
	 * 
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("mycartw cb failed->" + res);
		
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
		//mycartw cb failed->ReturnMsgNo=DBT00123&ReturnMsg=%25E7%25B3%25BB%25E7%25B5%25B1%25E7%2599%25BC%25E7%2594%259F%25E9%258C%25AF%25E8%25AA%25A4%25EF%25BC%258C%25E8%25AB%258B%25E6%25B4%25BD%25E5%25AE%25A2%25E6%259C%258D%25EF%25BC%2581&AuthCode=&TradeSeq=225&CardKind=&ProjNo=&MyCardId=&Price=&CrrnyShotName=
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

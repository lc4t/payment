package noumena.payment.dao.servlet;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.bean.OrdersBean;
import noumena.payment.gash.GASHCharge;
import noumena.payment.gash.GASHParams;
import noumena.payment.gash.GASHPaytypeParams;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;

import com.gashplus.gps.transaction.Trans;

public class GASHcbServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * MyCard台湾Billing模式补充订单回调
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
		String retstr = GASHParams.GASH_MSG_SUCCESS;
		String cbdata = request.getParameter("data");

		Trans trans = new Trans(cbdata);

        String recvRCode = trans.getNodes().get("RCODE");
        String recvPayStatus = trans.getNodes().get("PAY_STATUS");
        String recvERPC = trans.getNodes().get("ERPC");
        String recvAmount = trans.getNodes().get("AMOUNT");
        String recvCid = trans.getNodes().get("CID");
        String recvCoid = trans.getNodes().get("COID");
        String recvPaid = trans.getNodes().get("PAID");

		org.dom4j.Document doc = trans.generateXmlDoc();
		String cbcontent = doc.asXML();
		String path = OSUtil.getRootPath() + "../../logs/gashordercb/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + recvCoid;
		OSUtil.saveFile(filename, cbcontent);
		
		GASHPaytypeParams paytype = GASHCharge.getGashPaytypeParams(recvCid);
		trans.setKey(paytype.getKey1());
		trans.setIv(paytype.getKey2());
		trans.setPwd(paytype.getPwd());
		
        String erpc = trans.getErpc(trans.getKey(), trans.getIv());

		OrdersBean bean = new OrdersBean();
        if (erpc != null && !erpc.equals("") && erpc.equals(recvERPC))
        {
        	//验证通过，合法回调
        	if (recvRCode.equals("0000"))
        	{
		        if (recvPayStatus.equals("S"))
		        {
		        	//成功
		        	String settledata = GASHCharge.getSettleData(recvPaid, recvCoid, recvAmount);
		        	String recvdata = GASHCharge.settle(settledata);
		        	retstr = GASHCharge.handleSettle(recvPaid, recvdata);
		        }
		        else if (recvPayStatus.equals("F"))
		        {
		        	//失败
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
	        		retstr = GASHParams.GASH_MSG_FAILD_ORDER;
		        }
		        else if (recvPayStatus.equals("T"))
		        {
		        	//交易超时
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
	        		retstr = GASHParams.GASH_MSG_ORDER_TIMEOUT;
		        }
		        else if (recvPayStatus.equals("0"))
		        {
		        	//交易未完成
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_TIMEOUT);
	        		retstr = GASHParams.GASH_MSG_ORDER_RELAY;
		        }
		        else if (recvPayStatus.equals("W"))
		        {
		        	//交易待确认
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_TIMEOUT);
	        		retstr = GASHParams.GASH_MSG_ORDER_RELAY;
		        }
        	}
        	else
        	{
        		retstr = GASHParams.GASH_MSG_FAILD_ORDER;
				bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
        	}
        }
        else
        {
        	//压码不正确
        	retstr = GASHParams.GASH_MSG_ERROR_CODE;
			bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
        }

        response.setCharacterEncoding("utf-8");
		response.sendRedirect("gash/result.jsp?ret=" + URLEncoder.encode(retstr, "utf-8"));
//		PrintWriter out = response.getWriter();
//		out.println(retstr);
//		out.flush();
//		out.close();
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

package noumena.payment.dao.servlet.i4;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.i4.I4Charge;
import noumena.payment.i4.I4OrderVO;

public class I4cbServlet extends HttpServlet
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
		response.setContentType("text/html;charset=UTF-8");

//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("i4 pay cb->" + res);

		Map<String,String> params = new HashMap<String,String>();
		Map requestParams = request.getParameterMap();
		for (Iterator iter = requestParams.keySet().iterator(); iter.hasNext();)
		{
			String name = (String) iter.next();
			String[] values = (String[]) requestParams.get(name);
			String valueStr = "";
			for (int i = 0; i < values.length; i++)
			{
				valueStr = (i == values.length - 1) ? valueStr + values[i] : valueStr + values[i] + ",";
			}
			params.put(name, valueStr);
		}
		
		String order_id = request.getParameter("order_id");	//兑换订单号
		String billno = request.getParameter("billno");		//厂商订单号
		String account = request.getParameter("account");	//通行证账号
		String amount = request.getParameter("amount");		//兑换爱思币数量
		String status = request.getParameter("status");		//状态:0-正常状态;1-已兑换过并成功返回
		String app_id = request.getParameter("app_id");		//厂商应用 ID(原样返回给游戏服)
		String role = request.getParameter("role");			//厂商应用角色 id(原样返回给游戏服)
		String zone = request.getParameter("zone");			//厂商应用分区 id(原样返回给游戏服)
		String sign = request.getParameter("sign");			//签名(RSA 私钥加密)
		
		I4OrderVO vo = new I4OrderVO();
		vo.setOrder_id(order_id);
		vo.setBillno(billno);
		vo.setAccount(account);
		vo.setAmount(amount);
		vo.setStatus(status);
		vo.setApp_id(app_id);
		vo.setRole(role);
		vo.setZone(zone);
		vo.setSign(sign);

		String ret = I4Charge.getCallbackFromI4(params, vo);

		response.setContentType("text/html;charset=UTF-8");
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

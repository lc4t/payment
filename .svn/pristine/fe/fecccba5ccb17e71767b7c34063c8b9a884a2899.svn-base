package noumena.payment.dao.servlet.weimall;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.weimall.WeiMallCharge;

/**
 * @author LiangJun
 * 空中微信商城
 *
 */
public class WeiMallServlet extends HttpServlet
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
		System.out.println("weimall request->" + request.getQueryString());
		String stype = request.getParameter("type");		//请求的类型：serverinfo-得到区服信息；pay-通知交易结果；checkuser-查询用户是否合法
		
		if (stype == null)
		{
			ret = "参数不合法";
		}
		else
		{
			if (stype.equals("serverinfo"))
			{
				//得到区服信息
				try
				{
					String gameid = request.getParameter("gameid");			//游戏id
					ret = WeiMallCharge.getServerInfo(gameid);
					//[{"gameid":"4200000","serverid":"10000","servername":"大领主测试服1"},{"gameid":"1800000","serverid":"1800001_1800000","servername":"阿波罗"},{"gameid":"1800000","serverid":"1800002_1800000","servername":"波塞冬"},{"gameid":"1800000","serverid":"1800003_1800000","servername":"赫拉"}]
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if (stype.equals("checkuser"))
			{
				//检验用户是否合法
				try
				{
					String gameid = request.getParameter("gameid");		//游戏id
					String sid = request.getParameter("serverid");		//区服id
					String uid = request.getParameter("uid");			//玩家id
					
					ret = WeiMallCharge.checkUid(gameid, sid, uid);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
			else if (stype.equals("pay"))
			{
				//通知订单结果
				String gameid = request.getParameter("gameid");		//游戏id
				String sid = request.getParameter("serverid");		//区服id
				String uid = request.getParameter("uid");			//玩家id
				String uniqueuid = request.getParameter("uniqueid");//玩家id
				String itemid = request.getParameter("itemid");		//道具codename
				String amount = request.getParameter("amount");		//总金额
				String num = request.getParameter("num");			//数量
				String orderid = request.getParameter("orderid");	//微信商城订单号
				String sign = request.getParameter("sign");			//签名
				
				if (num == null || num.equals("") || itemid == null | amount == null)
				{
					ret = "支付参数不合法";
				}
				else
				{
					ret = WeiMallCharge.getCallbackWeiMall(gameid, sid, uid, uniqueuid, itemid, amount, Integer.parseInt(num), sign, orderid);
				}
			}
			else
			{
				//非法
				ret = "参数不合法";
			}
		}
		
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println(ret.toString());
		out.flush();
		out.close();
		
		System.out.println("weimall cb->" + ret.toString());
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

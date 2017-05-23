package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import noumena.payment.taobao.TaobaoCharge;
import noumena.payment.taobao.TaobaoRetMsgVO;

/**
 * @author LiangJun
 * 空中天猫商城
 *
 */
public class TaobaoServlet extends HttpServlet
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
		TaobaoRetMsgVO result = null;
		String str = request.getQueryString();
		if (str == null)
		{
			result = new TaobaoRetMsgVO();
			result.setMsg("参数不合法");
		}
		else
		{
			str = new String(str.getBytes("ISO-8859-1"), "GBK");
			str = java.net.URLDecoder.decode(str, "GBK");
			str = StrEdit(str.split("&"));
			
			String stype = request.getParameter("type");		//请求的类型：pay-通知交易结果；checkuser-查询用户是否合法
			String sid = request.getParameter("sid");			//淘宝服务器id
			String uid = request.getParameter("uid");			//玩家id
			String itemid = request.getParameter("itemid");		//道具codename
			String amount = request.getParameter("amount");		//总金额
			String num = request.getParameter("num");			//数量
			String sign = request.getParameter("sign");			//签名
			String taobaoid = request.getParameter("tbOrderNo");//淘宝订单号
			
			if (stype == null || sid == null || uid == null || sign == null)
			{
				result = new TaobaoRetMsgVO();
				result.setMsg("参数不合法");
			}
			else
			{
				if (stype.equals("checkuser"))
				{
					//检验用户是否合法
					try
					{
						result = TaobaoCharge.checkUid(sid, uid);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				else if (stype.equals("pay"))
				{
					//通知订单结果
					if (num == null || num.equals("") || itemid == null | amount == null || taobaoid == null)
					{
						result = new TaobaoRetMsgVO();
						result.setMsg("参数不合法");
					}
					else
					{
						result = TaobaoCharge.getCallbackTaobao(sid, uid, itemid, amount, Integer.parseInt(num), sign, taobaoid, str);
					}
				}
				else
				{
					//非法
					result = new TaobaoRetMsgVO();
					result.setMsg("参数不合法");
				}
			}
		}
		
		if (result == null)
		{
			result = new TaobaoRetMsgVO();
		}
		JSONObject ret = JSONObject.fromObject(result);
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		PrintWriter out = response.getWriter();
		out.println(ret.toString());
		out.flush();
		out.close();
		
		System.out.println("taobao cb->" + ret.toString());
	}
	
	private String StrEdit(String[] arr)
	{
		Arrays.sort(arr); //ascii码排序
		String temp = "";
		for (String s : arr)
		{
			if (!s.startsWith("sign="))
			{
				temp += s;
			}
		}
		
		String result = "";
		String[] arr2 = temp.split("=");
		for (String s : arr2)
		{
			result += s;
		}
		
		return result;
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

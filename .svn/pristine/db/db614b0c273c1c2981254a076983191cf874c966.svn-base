package noumena.payment.dao.servlet;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import noumena.payment.kingnet.KingnetCharge;
import noumena.payment.kingnet.KingnetOrderVO;

public class KingnetcbServlet extends HttpServlet
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
		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
		String res = in.readLine();
		System.out.println("kingnet request info->" + res);
		String ret = "";
		request.setCharacterEncoding("utf-8");
		String action = request.getParameter("action");
		if (action == null)
		{
			ret = "{\"ret\":-1,\"msg\":\"\"}";
		}
		else if (action.equals("user"))
		{
			//得到用户信息
//			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//			String res = in.readLine();
//			System.out.println("kingnet get user info->" + res);
			JSONObject json = JSONObject.fromObject(res);
			String uid = json.getString("uid");
			String serverid = json.getString("server_id");
			
			ret = KingnetCharge.getUserinfoFromKingnet(uid, serverid);
		}
		else if (action.equals("server"))
		{
			//得到服务器信息
			ret = KingnetCharge.getServerinfoFromKingnet();
		}
		else if (action.equals("pay"))
		{
			//支付通知
			KingnetOrderVO ordervo = new KingnetOrderVO();
			ordervo.setTs(getValueByKey(res, "ts"));
			ordervo.setSig(getValueByKey(res, "sig"));
			ordervo.setKda(getValueByKey(res, "kda"));
			ordervo.setUser_id(getValueByKey(res, "user_id"));
			ordervo.setSid(getValueByKey(res, "sid"));
			ordervo.setNumber(getValueByKey(res, "number"));
			ordervo.setAmount(getValueByKey(res, "amount"));
			ordervo.setRole_id(getValueByKey(res, "role_id"));
			ordervo.setOrder_id(getValueByKey(res, "order_id"));
			ordervo.setActive1(getValueByKey(res, "active1"));
			ordervo.setActive2(getValueByKey(res, "active2"));
			
			StringBuffer requeststr = new StringBuffer();
			requeststr.append("&ts=");
			requeststr.append(ordervo.getTs());
			requeststr.append("&sig=");
			requeststr.append(ordervo.getSig());
			requeststr.append("&kda=");
			requeststr.append(ordervo.getKda());
			requeststr.append("&user_id=");
			requeststr.append(ordervo.getUser_id());
			requeststr.append("&sid=");
			requeststr.append(ordervo.getSid());
			requeststr.append("&number=");
			requeststr.append(ordervo.getNumber());
			requeststr.append("&amount=");
			requeststr.append(ordervo.getAmount());
			requeststr.append("&role_id=");
			requeststr.append(ordervo.getRole_id());
			requeststr.append("&order_id=");
			requeststr.append(ordervo.getOrder_id());
			requeststr.append("&active1=");
			requeststr.append(ordervo.getActive1());
			requeststr.append("&active2=");
			requeststr.append(ordervo.getActive2());
//			BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//			String res = in.readLine();
			System.out.println("kingnet cb->" + requeststr.toString());
//			JSONObject json = JSONObject.fromObject(res);
//			KingnetOrderVO ordervo = (KingnetOrderVO) JSONObject.toBean(json, KingnetOrderVO.class);
			
			ret = KingnetCharge.getCallbackFromKingnet(ordervo);
		}

		System.out.println("kingnet cb ret->" + ret);
		response.setCharacterEncoding("UTF-8");
		PrintWriter out = response.getWriter();
		out.println(ret);
		out.flush();
		out.close();
	}
	
	private String getValueByKey(String content, String key)
	{
		String value = "", temp = "";
		String key1 = key + "=";
		String key2 = "&";
		int pos1 = 0, pos2=0;
		
		pos1 = content.indexOf(key1);
		if (pos1 >= 0)
		{
			temp = content.substring(pos1 + key1.length());
			pos2 = temp.indexOf(key2);
			if (pos2 >= 0)
			{
				value = temp.substring(0, pos2);
			}
			else
			{
				value = temp;
			}
		}
		
		return value;
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

package noumena.payment.dao.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.bean.CallBackGameServBean;
import noumena.payment.bean.OrdersBean;
import noumena.payment.bean.PayGameBean;
import noumena.payment.bean.PayServerBean;
import noumena.payment.model.Orders;
import noumena.payment.model.PayGame;
import noumena.payment.model.PayServer;
import noumena.payment.util.Constants;
import noumena.payment.util.StringEncrypt;

public class PhonePayServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public PhonePayServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		return;
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException
	{
		request.setCharacterEncoding("utf-8");
		response.setCharacterEncoding("utf-8");
		
		String game = request.getParameter("game");
		String server = request.getParameter("server");
		String username = request.getParameter("username");
		String usernamer = request.getParameter("usernamer");
		String info = request.getParameter("amount");
		String[] infos = info.split("#");
		float amount = 0;
		int gem = 0;
		if (game == null || server == null || username == null || !username.equals(usernamer) || info == null || infos.length < 4)
		{
			return;
		}
		try
		{
			amount = Float.parseFloat(infos[1]);
			gem = Integer.parseInt(infos[3]);
		}
		catch (Exception e)
		{
			return;
		}
		PayServerBean payServerBean = new PayServerBean();
		PayGameBean payGameBean = new PayGameBean();
		PayServer payServer = payServerBean.get(server);
		PayGame payGame = payGameBean.getGame(game);
		if (payServer == null || payGame == null)
		{
			return;
		}
		Date date = new Date();
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyyyMMddHHmmss");
		String unit = infos[2];
		String codeName = infos[0];
		String cburl = payServer.getCallbackUrl();
		String paynotify = payServer.getPayNotify();
		server = payServer.getServerId().split("_")[0];
		String Parameter = "";
		Parameter += "userid=" + URLEncoder.encode(username, "utf-8");
		Parameter += "&gameid=" + game;
		Parameter += "&serverid=" + server;
		Parameter += "&itmeid=" + codeName;
		Parameter += "&amount=" + (int)amount;
		Parameter += "&itemprice=" + gem;
		String cbsign = game+server+username+codeName+gem+(int)amount;
		cbsign = StringEncrypt.Encrypt(cbsign + Constants.P_KEY);
		if (cburl != null && !cburl.equals(""))
		{
			if (cburl.indexOf("?") == -1)
			{
				cburl += "?" + Parameter;
			}
			else
			{
				cburl += "&" + Parameter;
			}
			cburl += "&sign=" + cbsign;
		}

		String tmp = "";
		tmp = "{\"itemId\":\"";
		tmp += codeName;
		tmp += "\",\"itemNum\":\"1\"}";
		String paymemo = tmp;
		
		Orders vo = new Orders();
		
		vo.setUId(username);
		vo.setChannel(Constants.getGameIdByAppId(game) + "A0ABE00A0000000");
		vo.setAppId(game);
		vo.setAmount(amount);
		vo.setCreateTime(df1.format(date));
		vo.setPayType(Constants.PAY_TYPE_DACHENG_WEB);
		vo.setItemId(codeName);
		vo.setItemPrice(String.valueOf(gem));
		vo.setItemNum(1);
		vo.setDeviceId(unit);
		vo.setExInfo(paymemo + "#");
		vo.setCurrency(Constants.CURRENCY_RMB);
		vo.setUnit(Constants.CURRENCY_UNIT_YUAN);
		
		String imei = Constants.PAY_TYPE_DACHENG_WEB;
		try
		{
			imei += Integer.parseInt(username);
		}
		catch (Exception e) 
		{
			imei += "0000000";
		}
		vo.setImei(imei);
		
		String payid;
		OrdersBean bean = new OrdersBean();
		if (cburl == null || cburl.equals(""))
		{
			payid = bean.CreateOrder(vo);
		}
		else
		{
			if (cburl.indexOf("?") == -1)
			{
				cburl += "?pt=" + Constants.PAY_TYPE_DACHENG_WEB;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_DACHENG_WEB;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
			payid = bean.CreateOrder(vo, cburl);
		}
		
		String r = payid+game+server+username+codeName+gem+(int)amount;
//		System.out.println(r);
		String gamesign = StringEncrypt.Encrypt(r + Constants.N_KEY);
		if (paynotify != null && !paynotify.equals(""))
		{
			if (paynotify.indexOf("?") == -1)
			{
				paynotify += "?" + Parameter;
			}
			else
			{
				paynotify += "&" + Parameter;
			}
			paynotify += "&payId=" + payid;
			paynotify += "&sign=" + gamesign;
			paynotify += "&status=888";
		}
		
		String payprice = new DecimalFormat("0.00").format(amount);
		CallBackGameServBean cbean = new CallBackGameServBean();
		try
		{
//			System.out.println(paynotify);
			String res = cbean.doGet(paynotify);
//			System.out.println(res);
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
//		String url = Constants.PAY_PHONE_URL;
		int p = request.getRequestURL().lastIndexOf("/");
		String url = request.getRequestURL().substring(0, p) + "/phonepost.jsp";
		
		tmp = "" + payid;
		tmp += game;
		tmp += imei;
		tmp += payprice;
		tmp += df2.format(date);
		tmp += paymemo;
		tmp += Constants.C_KEY;
//		System.out.println("minwen ->" + tmp);
		String sign = StringEncrypt.Encrypt(tmp).toUpperCase();
		
		url += "?payTypeId=10029";
		url += "&payId=" + payid;
		url += "&paytime=" + df2.format(date);
		url += "&gameid=" + game;
		url += "&areaid=1";
		url += "&serverid=" + server;
		url += "&imei=" + imei;
		url += "&accountid=" + username;
		url += "&roleid=" + username;
		url += "&payprice=" + payprice;
		url += "&paymemo=" + URLEncoder.encode(paymemo, "utf-8");
		url += "&sign=" + sign;

//		System.out.println("phone pay url->" + url);
		response.sendRedirect(url);
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException
	{
		// Put your code here
	}

}

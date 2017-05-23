package noumena.payment.dao.servlet;

import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.bean.OrdersBean;
import noumena.payment.bean.PayGameBean;
import noumena.payment.bean.PayServerBean;
import noumena.payment.gash.GASHCharge;
import noumena.payment.gash.GASHPaytypeParams;
import noumena.payment.model.GashPayServer;
import noumena.payment.model.Orders;
import noumena.payment.model.PayGame;

public class GASHWebPayServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public GASHWebPayServlet() {
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

		String paid = request.getParameter("paidct");
		GASHPaytypeParams paytype = GASHCharge.getGashPaytypeParamsByPaid(paid);
		System.out.println("gash web pay paid->" + paid);
		String game = request.getParameter("game");
		String server = request.getParameter("server");
		String username = request.getParameter("username");
		String usernamer = request.getParameter("usernamer");
		String info = request.getParameter("amount");
		String codeName = request.getParameter("itemid");
		String unit = request.getParameter("deviceid");
		
		

		//必需的参数
		String uid = request.getParameter("uid");				//
		String pkgid = request.getParameter("pkgid");			//
		String itemid = request.getParameter("itemid");			//
		String payprice = request.getParameter("price");		//
		String cburl = request.getParameter("cburl");			//单机游戏可以不需要回调地址
		
		//选填的参数
		String imei = request.getParameter("imei");				//
		String channel = request.getParameter("channel");		//
		String device_type = request.getParameter("device_type");	//
		String device_id = request.getParameter("device_id");	//
		String gversion = request.getParameter("gversion");		//
		String osversion = request.getParameter("osversion");	//
		
		

		Date date = new Date();
		SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		float amount = 0;
		int gem = 0;
		Orders vo = new Orders();
		
		if (cburl == null || cburl.equals(""))
		{
			//没有cburl，是从页面来的
			System.out.println("amount->" + info);
			String[] infos = info.split("#");
			
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

			if (cburl == null || cburl.equals(""))
			{
				PayServerBean payServerBean = new PayServerBean();
				PayGameBean payGameBean = new PayGameBean();
				GashPayServer payServer = payServerBean.getgash(server);
				PayGame payGame = payGameBean.getGame(game);
				if (payServer == null || payGame == null)
				{
					return;
				}
				unit = infos[2];
				codeName = infos[0];
				cburl = payServer.getCallbackUrl();
				server = payServer.getServerId().split("_")[0];
				
				String Parameter = "";
				Parameter += "uid=" + URLEncoder.encode(username, "utf-8");
				Parameter += "&sid=" + codeName;
				Parameter += "&p=" + (int)amount;
				Parameter += "&c=" + 1;
				if (cburl != null && !cburl.equals("")) {
					if (cburl.indexOf("?") == -1) {
						cburl += "?" + Parameter;
					} else {
						cburl += "&" + Parameter;
					}
				}
				
				vo.setUId(username);
				vo.setChannel(server);
				vo.setAppId(game);
				vo.setAmount(amount);
				vo.setCreateTime(df1.format(date));
				vo.setPayType(paytype.getType());
				vo.setItemId(codeName);
				vo.setItemPrice(String.valueOf(gem));
				vo.setItemNum(1);
				vo.setDeviceId(unit);
				vo.setExInfo(paid);
				vo.setImei(imei);
			}
		}
		else
		{
			//存在cburl是从游戏内来的
			
			vo.setUId(uid);
			username = uid;
			vo.setChannel(channel);
			vo.setAppId(pkgid);
			amount = Float.parseFloat(payprice);
			vo.setAmount(amount);
			vo.setCreateTime(df1.format(date));
			vo.setPayType(paytype.getType());
			vo.setItemId(itemid);
			vo.setItemNum(1);
			vo.setDeviceType(device_type);
			vo.setDeviceId(device_id);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setExInfo(paid);
			vo.setImei(imei);
		}
		
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
				cburl += "?pt=" + paytype.getType();
			}
			else
			{
				cburl += "&pt=" + paytype.getType();
			}
			payid = bean.CreateOrder(vo, cburl);
		}
		
		int p = request.getRequestURL().lastIndexOf("/");
		String url = request.getRequestURL().substring(0, p) + "/post.jsp";
		System.out.println("GASH web pay servlet->" + payid + "-" + paid + "-" + amount + "-" + codeName + "-" + username);
		url += "?senddata=" + GASHCharge.getSendData(payid, paid, "" + amount, codeName, codeName, username);

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

package noumena.pay;

import java.io.IOException;
import java.net.URLEncoder;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class PayServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding("utf-8");
		String unitNum = "1";
		// 必需的参数
		String appid = request.getParameter("appid");// appid
		String account = request.getParameter("account");// 账户
		String uid = request.getParameter("uid");// 角色ID
		String packagename = request.getParameter("packagename");// 包名
		String productid = request.getParameter("productid");// 商品ID
		String productname = request.getParameter("productname");// 商品名称
		String amount = request.getParameter("amount");// 价格
		String channel = request.getParameter("channel");// 渠道号
		String productdesc = request.getParameter("productdesc");// 商品描述
		String cbgameurl = request.getParameter("cbgameurl");// 返回游戏地址
		String cburl = request.getParameter("cburl");// 服务器回调地址
		String serverName = request.getParameter("serverName");// 服务器名
		unitNum = request.getParameter("unitNum");// 服务器名
		System.out.println(cbgameurl+"wewewewewewe");
		// 计算出钱数
		float money = (Float.parseFloat(amount)) * (Float.parseFloat(unitNum)/100f);
		request.getSession().setAttribute("appid", appid);
		request.getSession().setAttribute("account", account);
		request.getSession().setAttribute("uid", uid);
		request.getSession().setAttribute("packagename", packagename);
		request.getSession().setAttribute("productid", productid);
		request.getSession().setAttribute("productname", productname);
		request.getSession().setAttribute("amount", amount);
		request.getSession().setAttribute("channel", channel);
		request.getSession().setAttribute("productdesc", productdesc);
		request.getSession().setAttribute("cbgameurl", URLEncoder.encode(cbgameurl, "UTF-8"));
		request.getSession().setAttribute("cburl", cburl);
		request.getSession().setAttribute("serverName", serverName);
		request.getSession().setAttribute("money", money);
		String url = request.getContextPath();
		String ua = request.getHeader("User-Agent");
		//在这加浏览器判断
		
		if (ua != null) {
			String mua = ua.toLowerCase();
			if(mua.indexOf("micromessenger") >= 0){
				System.out.println("weixin============================"+url);
				RequestDispatcher rd = request.getRequestDispatcher("/pay/turnwexin.jsp");  
				rd.forward(request, response);	
			}else{
				 response.sendRedirect(url+"/pay/pay.jsp");	
			}
		}	
	
	
	}

}

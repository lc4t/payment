package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.bean.OrdersBean;
import noumena.payment.kongmp.KongMPCharge;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.LogVO;
import noumena.payment.util.OSUtil;

public class KongMPServlet extends HttpServlet
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
		
		//必需的参数
		String stype = request.getParameter("model");			//请求的类型：1-得到交易id；2-查询订单状态
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

		//支付方的参数
		String phoneid = request.getParameter("phoneid");		//电话号码
		String gameid = request.getParameter("gameid");			//无线分配的游戏 标识
		
		//验证订单用参数
		String payIds = request.getParameter("payIds");			//待查询的所有订单号，以“,”分隔
		
		String ret = "";
		if (stype == null)
		{
			stype = "";
		}
		if (stype.equals("1"))
		{
			//创建订单
			Orders vo = new Orders();
			vo.setImei(imei);
			vo.setUId(uid);
			vo.setItemId(itemid);
			vo.setSign(gameid);
			vo.setItemPrice(payprice);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setDeviceId(device_id);
			vo.setDeviceType(device_type);
			vo.setChannel(channel);
			vo.setAppId(pkgid);
			vo.setAmount(Float.valueOf(payprice));
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setExInfo(phoneid);
			vo.setPayType(Constants.PAY_TYPE_KONGMP);
			vo.setCallbackUrl(cburl);
			
			ret = KongMPCharge.getTransactionId(vo);
		}
		else if (stype.equals("3"))
		{
			//获取电话号码
			Orders vo = new Orders();
			vo.setImei(imei);
			vo.setUId(uid);
			vo.setItemId(gameid);
			vo.setItemPrice(payprice);
			vo.setGversion(gversion);
			vo.setOsversion(osversion);
			vo.setDeviceId(device_id);
			vo.setDeviceType(device_type);
			vo.setChannel(channel);
			vo.setAppId(pkgid);
			vo.setCreateTime(DateUtil.getCurrentTime());
			vo.setExInfo(phoneid);
			vo.setPayType(Constants.PAY_TYPE_KONGMPID);
			vo.setCallbackUrl(cburl);
			
			ret = KongMPCharge.getTransactionPhoneId(vo);
		}
		else if (stype.equals("4"))
		{
			//发送订购短信的日志
			String orderid = request.getParameter("orderid");
			if (orderid == null)
			{
				orderid = "";
			}
			String code = request.getParameter("code");
			String message = request.getParameter("message");
			String keyword = request.getParameter("keyword");
			String rcontent = request.getParameter("rcontent");
			OrdersBean orderbean = new OrdersBean();
			Orders order = orderbean.qureyOrder(orderid);
			if (order != null)
			{
				try
				{
					//文件名：smartphone.game.smssubscribe.yyyy-mm-dd
					//日志内容格式：游戏标识|订购发起时间|订单号|手机号|运营商|归属地|指令码|目的号码|业务代码|回复内容|业务名称
					LogVO logvo = new LogVO();
					logvo.setItem1(order.getSign());
					logvo.setItem2(DateUtil.date2Str(System.currentTimeMillis(), 4));
					logvo.setItem3(Constants.ORDERID_PRE + orderid);
					logvo.setItem4(order.getExInfo());
					logvo.setItem5("");
					logvo.setItem6("");
					logvo.setItem7(message);
					logvo.setItem8(code);
					logvo.setItem9(keyword);
					logvo.setItem10(rcontent);
					logvo.setItem11("");
					OSUtil.genLog(logvo, 2);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (stype.equals("5"))
		{
			//发送确认订购短信的日志
			String orderid = request.getParameter("orderid");
			if (orderid == null)
			{
				orderid = "";
			}
			String code = request.getParameter("code");
			String message = request.getParameter("message");
			String keyword = request.getParameter("keyword");
			String rcontent = request.getParameter("rcontent");
			OrdersBean orderbean = new OrdersBean();
			Orders order = orderbean.qureyOrder(orderid);
			if (order != null)
			{
				try
				{
					//文件名：smartphone.game.smsconfirm.yyyy-mm-dd
					//日志内容格式：游戏标识|发送时间|订单号|手机号|发送的确认订购短信内容|目的号码|关键字|截获的确认短信内容
					//日志内容格式：游戏标识|确认发起时间|订单号|手机号|回复内容|目的号码|业务代码|业务名称|截获短信
					LogVO logvo = new LogVO();
					logvo.setItem1(order.getSign());
					logvo.setItem2(DateUtil.getCurrentTime());
					logvo.setItem3(orderid);
					logvo.setItem4(order.getExInfo());
					logvo.setItem5(rcontent);
					logvo.setItem6(code);
					logvo.setItem7(keyword);
					logvo.setItem8("");
					logvo.setItem9(message);
					OSUtil.genLog(logvo, 3);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		}
		else if (stype.equals("2"))
		{
			ret = KongMPCharge.checkOrdersStatus(payIds);
		}
		else
		{
			System.out.println("kongmp model invalid->" + stype);
			ret = "invalid";
		}

		System.out.println("kongmp order id->(" + uid + ")" + ret);
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
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

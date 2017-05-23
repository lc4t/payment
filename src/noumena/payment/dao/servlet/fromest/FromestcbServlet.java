package noumena.payment.dao.servlet.fromest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;
import noumena.payment.fromest.FromestCharge;
import noumena.payment.fromest.FromestOrderVO;
import noumena.payment.sougou.SougouCharge;


public class FromestcbServlet extends HttpServlet
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

		BufferedReader reader = request.getReader();
		String result = "";
		String tmp = null;
		while((tmp = reader.readLine())!=null) {
			result+=tmp;
		}
		System.out.println(result);
		JSONObject json = JSONObject.fromObject(result);
		/*
		 * {"orderid":"20160603174114862A2f","productid":"5000003","cardno":"","amount":"110000",
		 * "amountunit":"krw","ret":"1","cardstatus":"","merpriv":"merpriv",
		 * "verifystring":"YeVYtUjpvMPomTqT9jBUTszC0qH713msQmXw0wlPHa/X3v4cxU9dFSbZb79nNl5VUwW6egDcsKrW\
		 * nHSB3GrZwjog8j2UCzWmEWItW0OMSN+v3jZdEjMN2Hn/qdkYlL2AQDHrm8WAovJpzha5TI726Ijnm\noplHW3yUVO51epMCHIUbC6EG1Uw9MFF
		 * /wMfvGcq7NnZ4BsWWc1kqigMY08JqFD0Hq05+CWAgJBGh\nDj2iIySdfe02cemJtzRMq2XhKQv/TbkoRZRqMns0USqgiXNiLwty4yiAP4PBT8
		 * bWDcZBnJFnjywL\nUbYJvLtwL332GHVoWtkyoWeObYz2D1QDD9fiqw=="}
		 */
		FromestOrderVO vo = new FromestOrderVO();
		vo.setFmorderid(json.get("fmorderid").toString());
		vo.setOrderid(json.get("orderid").toString());
		vo.setProductid(json.get("productid").toString());
		vo.setCardno(json.get("cardno").toString());
		vo.setAmount(json.get("amount").toString());
		vo.setAmountunit(json.get("amountunit").toString());
		vo.setRet(json.get("ret").toString());
		vo.setCardStatus(json.get("cardstatus").toString());
		vo.setMerPriv(json.get("merpriv").toString());
		vo.setVerifystring(json.get("verifystring").toString());
		
		String ret = FromestCharge.getCallbackFromFromest(vo);

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

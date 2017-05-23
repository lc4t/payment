package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.cmgc.CMGCCharge;
import noumena.payment.cmgc.CMGCOrderDataVO;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

public class CMGCcbServlet extends HttpServlet
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
//		BufferedReader in = new BufferedReader(new InputStreamReader(request.getInputStream()));
//		String res = in.readLine();
//		System.out.println("cmgc cb->" + res);
		
		//<?xml version="1.0" encoding="UTF-8"?><request><userId>1152998711</userId><cpServiceId>601810059326</cpServiceId><consumeCode>000059325001</consumeCode><cpParam>0000000000000475</cpParam><hRet>0</hRet><status>1101</status><transIDO>3173411PONE30087F</transIDO><versionId>100</versionId></request>

		CMGCOrderDataVO vo = new CMGCOrderDataVO();
		try
		{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(request.getInputStream());
			Element root = doc.getRootElement();
			
			List<?> childrens = root.getChildren("userId");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setUserId(str);
			}
			childrens = root.getChildren("cpServiceId");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setCpServiceId(str);
			}
			childrens = root.getChildren("consumeCode");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setConsumeCode(str);
			}
			childrens = root.getChildren("cpParam");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setCpParam(str);
			}
			childrens = root.getChildren("hRet");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.sethRet(str);
			}
			childrens = root.getChildren("status");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setStatus(str);
			}
			childrens = root.getChildren("transIDO");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setTransIDO(str);
			}
			childrens = root.getChildren("versionId");
			if (childrens.size() > 0)
			{
				Element children = (Element) childrens.get(0);
				String str = children.getText();
				vo.setVersionId(str);
			}
			
			String path = OSUtil.getRootPath() + "../../logs/cmgccb/" + DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + vo.getUserId() + "_" + vo.getCpParam();
			OSUtil.saveXML(root, filename);
		
			String ret = CMGCCharge.getCallbackCMGC(vo, OSUtil.XML2String(doc));
			
			PrintWriter out = response.getWriter();
			out.println(ret);
			out.flush();
			out.close();
		}
		catch (Exception e) 
		{
			e.printStackTrace();
		}
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

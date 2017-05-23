package noumena.payment.dao.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.List;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.mycardtw.MyCardTWCharge;
import noumena.payment.mycardtw.MyCardTWOrderDataVO;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class MyCardTWcbnServlet extends HttpServlet
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * The doGet method of the servlet. <br>
	 * 
	 * MyCard台湾Billing模式补充订单回调
	 * 
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
		/*
		 * <BillingApplyRq>
		 * 	<FatoryId>xxxxxx</FatoryId>
		 * 	<TotalNum>2</TotalNum>
		 * 	<Records>
		 * 		<Record>
		 * 			<ReturnMsgNo>1</ReturnMsgNo>
		 * 			<ReturnMsg></ReturnMsg>
		 * 			<TradeSeq>00001</TradeSeq>
		 * 		</Record>
		 * 		<Record>
		 * 			<ReturnMsgNo>1</ReturnMsgNo>
		 * 			<ReturnMsg></ReturnMsg>
		 * 			<TradeSeq>00002</TradeSeq>
		 * 		</Record>
		 * 	</Records>
		 * </BillingApplyRq>
		 */
		Vector<MyCardTWOrderDataVO> vos = new Vector<MyCardTWOrderDataVO>();
		MyCardTWOrderDataVO vo = null;
		String s = "_";
		String cbdata = request.getParameter("data");
		
		if (cbdata != null && !cbdata.equals(""))
		{
			System.out.println("mycard cb notify->" + cbdata);
	
			try
			{
				StringReader read = new StringReader(cbdata);
				InputSource source = new InputSource(read);
				SAXBuilder builder = new SAXBuilder();
				Document doc = builder.build(source);
				Element root = doc.getRootElement();
				List<?> childrens = root.getChildren("Records");
				if (childrens.size() > 0)
				{
					Element e_records = (Element) childrens.get(0);
					List<?> l_records = e_records.getChildren("Record");
					for (int i = 0 ; i < l_records.size() ; i++)
					{
						vo = new MyCardTWOrderDataVO();
						Element e_recorddata = (Element) l_records.get(i);
						List<?> l_recorddata = e_recorddata.getChildren("ReturnMsgNo");
						if (l_recorddata.size() > 0)
						{
							Element data = (Element) l_recorddata.get(0);
							String str = data.getText();
							vo.setReturnMsgNo(str);
						}
						l_recorddata = e_recorddata.getChildren("ReturnMsg");
						if (l_recorddata.size() > 0)
						{
							Element data = (Element) l_recorddata.get(0);
							String str = data.getText();
							vo.setReturnMsg(str);
						}
						l_recorddata = e_recorddata.getChildren("TradeSeq");
						if (l_recorddata.size() > 0)
						{
							Element data = (Element) l_recorddata.get(0);
							String str = data.getText();
							vo.setTradeSeq(str);
							s += str + "_";
						}
						vos.add(vo);
					}
				}
				
				String path = OSUtil.getRootPath() + "../../logs/mycardtwcbn/" + DateUtil.getCurTimeStr().substring(0, 8);
				OSUtil.makeDirs(path);
				String filename = path + "/" + s;
				OSUtil.saveXML(root, filename);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		
			MyCardTWCharge.getCallbackRecordsFromMyCardTW(vos);
		}
		
		PrintWriter out = response.getWriter();
		out.println("SUCCESS");
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

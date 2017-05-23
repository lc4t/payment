package noumena.payment.dao.servlet.huawei;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import noumena.payment.huawei.HuaweiCharge;

public class HuaweicbServlet extends HttpServlet
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

		Map<String,Object> params = new HashMap<String,Object>();
		params = getValue(request);

		String ret = HuaweiCharge.getCallbackFromHuawei(params);

		response.setContentType("application/json");
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
	
	 /**
     * @param request
     * @return
     *         本接口Content-Type是：application/x-www-form-urlencoded，对所有参数，会自动进行编码，接收端收到消息也会自动根据Content-Type进行解码。
     *         同时，接口中参数在发送端并没有进行单独的URLEncode (sign和extReserved、sysReserved参数除外)，所以，在接收端根据Content-Type解码后，即为原始的参数信息。
     *         但是HttpServletRequest的getParameter()方法会对指定参数执行隐含的URLDecoder.decode(),所以，相应参数中如果包含比如"%"，就会发生错误。
     *         因此，我们建议通过如下方法获取原始参数信息。
     * 
     *         注：使用如下方法必须在原始ServletRequest未被处理的情况下进行，否则无法获取到信息。比如，在Struts情况，由于struts层已经对参数进行若干处理，
     *         http中InputStream中其实已经没有信息，因此，本方法不适用。要获取原始信息，必须在原始的，未经处理的ServletRequest中进行。
     */
    public Map<String, Object> getValue(HttpServletRequest request)
    {
        
        String line = null;
        StringBuffer sb = new StringBuffer();
        try
        {
            request.setCharacterEncoding("UTF-8");
            
            InputStream stream = request.getInputStream();
            InputStreamReader isr = new InputStreamReader(stream);
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null)
            {
                sb.append(line).append("\r\n");
            }
            br.close();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
        
        String str = sb.toString();
        Map<String, Object> valueMap = new HashMap<String, Object>();
        if (null == str || "".equals(str))
        {
            return valueMap;
        }
        
        String[] valueKey = str.split("&");
        for (String temp : valueKey)
        {
            String[] single = temp.split("=");
            valueMap.put(single[0], single[1]);
        }
        
        // 接口中，如下参数sign和extReserved、sysReserved是URLEncode的，所以需要decode，其他参数直接是原始信息发送，不需要decode
        try
        {
            String sign = (String)valueMap.get("sign");
            String extReserved = (String)valueMap.get("extReserved");
            String sysReserved = (String)valueMap.get("sysReserved");
            
            if (null != sign)
            {
                sign = URLDecoder.decode(sign, "utf-8");
                valueMap.put("sign", sign);
            }
            if (null != extReserved)
            {
                extReserved = URLDecoder.decode(extReserved, "utf-8");
                valueMap.put("extReserved", extReserved);
            }
            
            if (null != sysReserved)
            {
                sysReserved = URLDecoder.decode(sysReserved, "utf-8");
                valueMap.put("sysReserved", sysReserved);
            }
            
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return valueMap;
        
    }

}

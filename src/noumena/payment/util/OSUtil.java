package noumena.payment.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import noumena.mgsplus.logs.bean.GameLogsBean;
import noumena.mgsplus.logs.model.ExNewLog;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class OSUtil {

	public static String formatString(String old, int num, String value,
			boolean tag) {

		String str = "";

		if (old.length() >= num) {
			return old;
		} else {
			if (tag) {
				for (int i = 1; i <= num - old.length(); i++) {
					str = str + value;
				}
				str = str + old;
			} else {
				str = old;
				for (int i = 1; i <= num - old.length(); i++) {
					str = str + value;
				}
			}

			return str;
		}
	}

	public static String cutString(String str, int len) {

		String tmp = null;

		try {
			int count = 0;
			byte[] b = str.getBytes("GBK");

			if (b.length <= len) {
				tmp = str;
			} else {
				for (int i = 0; i < len; i++) {
					if (b[i] < 0) {
						count++;
					}
				}

				if (count % 2 == 0) {
					tmp = new String(b, 0, len, "GBK");
				} else {
					tmp = new String(b, 0, len - 1, "GBK");
				}
			}
		} catch (UnsupportedEncodingException e) {
		}

		return tmp;
	}

	public static String filterString(String target) {

		for (int i = 0; i < Symbol.SYMBOL.length; i++) {
			target = target
					.replaceAll(Symbol.SYMBOL[i][0], Symbol.SYMBOL[i][1]);
		}

		return target;
	}

	public static String getRequestURL(HttpServletRequest request) {

		boolean flag = true;
		String paramName, paramValue;
		StringBuffer requestURL = request.getRequestURL();
		Enumeration paramNames = request.getParameterNames();

		while (paramNames.hasMoreElements()) {
			if (flag) {
				flag = false;
				requestURL.append("?");
			} else {
				requestURL.append("&");
			}

			paramName = (String) paramNames.nextElement();

			try {
				paramValue = URLEncoder.encode(request.getParameter(paramName),
						"utf-8");
				requestURL.append(paramName).append("=").append(paramValue);
			} catch (UnsupportedEncodingException e) {
			}
		}

		return requestURL.toString();
	}

	public static String encodeURL(String destURL) {

		try {
			destURL = URLEncoder.encode(destURL, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}

		return destURL;
	}

	public static String decodeURL(String destURL) {

		try {
			destURL = URLDecoder.decode(destURL, "utf-8");
		} catch (UnsupportedEncodingException e) {
		}

		return destURL;
	}

	public static boolean makeDirs(String realPath) {

		File dir = new File(realPath);
		boolean flag = false;

		try {
			if (!dir.exists()) {
				flag = dir.mkdirs();
			}
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	public static boolean delFile(String realPath) {

		File target = new File(realPath);
		boolean flag = false;

		try {
			if (target.exists()) {
				flag = target.delete();
			}
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	public static boolean delDir(String realPath) {

		boolean flag = false;
		File dir = new File(realPath);

		if (dir.isDirectory()) {
			flag = delDir(dir);
		}

		return flag;
	}

	public static boolean delDir(File dir) {

		boolean flag = false;

		try {
			File[] files = dir.listFiles();

			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					delDir(files[i]);
				} else {
					files[i].delete();
				}
			}

			flag = dir.delete();
		} catch (Exception e) {
			flag = false;
		}

		return flag;
	}

	public static long getFileSize(String filePath) {

		File file = new File(filePath);

		return file.length();
	}

	public static String getFileTime(String filePath) {

		File file = new File(filePath);

		return DateUtil.getTime(file.lastModified());
	}

	public static int setFlag(int value, int pos, boolean flag) {

		if (flag) {
			value = value | pos;
		} else {
			value = ~(~value | pos);
		}

		return value;
	}

	public static boolean getFlag(int value, int pos) {

		boolean flag;

		if ((value & pos) != 0) {
			flag = true;
		} else {
			flag = false;
		}

		return flag;
	}

	public static String[] split(String str) {

		StringTokenizer tokenizer = new StringTokenizer(str);
		String[] array = new String[tokenizer.countTokens()];

		for (int i = 0; i < array.length; i++) {
			array[i] = tokenizer.nextToken();
		}

		return array;
	}

	public static String assembleSQL(String fieldName, String[] array,
			String logic) {

		String str = fieldName + " LIKE '%" + array[0] + "%'";

		if (array.length > 1) {
			for (int i = 1; i < array.length; i++) {
				str = str + " " + logic + " " + fieldName + " LIKE '%"
						+ array[i] + "%'";
			}
		}

		return "(" + str + ")";
	}

	public static String getCookieValue(Cookie[] cookies, String name) {

		for (int i = 0; i < cookies.length; i++) {
			if (cookies[i].getName().equals(name)) {
				return cookies[i].getValue();
			}
		}

		return null;
	}

	public static void saveXML(Element element, String filename)
	{
		Format format = Format.getCompactFormat();
		format.setIndent("    ");
		format.setEncoding("UTF8");
		XMLOutputter outputter = new XMLOutputter(format);
		try
		{
			Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF8"));
			outputter.output(element, out);
			out.close();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}

	public static void saveFile(String filename, String content)
	{
		try
		{
			BufferedWriter out = null;
			out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(filename), "UTF-8"));
			if (out != null)
			{
				out.append(content);
				out.close();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public static String XML2String(Document doc)
	{
		Format format =Format.getPrettyFormat();      
		format.setEncoding("UTF-8");//设置编码格式   

		StringWriter out=null; //输出对象  
		String sReturn =""; //输出字符串  
		XMLOutputter outputter =new XMLOutputter();   
		out=new StringWriter();   
		try
		{
			outputter.output(doc,out);  
		}
		catch (IOException e)
		{
			e.printStackTrace();  
		}
		sReturn=out.toString();
		
		return sReturn;
	}
	
	public static String getRootPath()
	{
		return OSUtil.class.getResource("/").getPath();
		//util.rootpath = System.getProperty("user.dir");
	}
	
	public static void genLog(LogVO vo)
	{
		genLog(vo, 0);
	}
	
	public static void genLog(LogVO vo, int type)
	{
		ExNewLog newlog = new ExNewLog();
		String filename = "";
		if (type == 0)
		{
			//下单日志
			//文件名：smartphone.game. order.yyyy-mm-dd
			//日志内容格式：下单时间|下单渠道|订单号|设备名|设备序列号|设备物理地址|设备分辨率|设备OS|OS版本|游戏ID |游戏版本|游戏渠道|用户ID |支付渠道|订单项目列表
			newlog.getList().add(vo.getItem1());
			newlog.getList().add(vo.getItem2());
			newlog.getList().add(vo.getItem3());
			newlog.getList().add(vo.getItem4());
			newlog.getList().add(vo.getItem5());
			newlog.getList().add(vo.getItem6());
			newlog.getList().add(vo.getItem7());
			newlog.getList().add(vo.getItem8());
			newlog.getList().add(vo.getItem9());
			newlog.getList().add(vo.getItem10());
			newlog.getList().add(vo.getItem11());
			newlog.getList().add(vo.getItem12());
			newlog.getList().add(vo.getItem13());
			newlog.getList().add(vo.getItem14());
			newlog.getList().add(vo.getItem15());
			
			filename = "smartphone.game.order.";
		}
		else if (type == 1)
		{
			//支付日志
			//文件名：smartphone.game. payment.yyyy-mm-dd
			//日志内容格式：支付时间|支付订单号|支付金额|支付状态|状态码|交易流水号|货币类型|货币单位
			newlog.getList().add(vo.getItem1());
			newlog.getList().add(vo.getItem2());
			newlog.getList().add(vo.getItem3());
			newlog.getList().add(vo.getItem4());
			newlog.getList().add(vo.getItem5());
			newlog.getList().add(vo.getItem6());
			newlog.getList().add(vo.getItem7());
			newlog.getList().add(vo.getItem8());
			
			filename = "smartphone.game.payment.";
		}
		else if (type == 2)
		{
			//发送订购短信的日志
			//文件名：smartphone.game.smssubscribe.yyyy-mm-dd
			//日志内容格式：游戏标识|订购发起时间|订单号|手机号|运营商|归属地|指令码|目的号码|业务代码|回复内容|业务名称
			newlog.getList().add(vo.getItem1());
			newlog.getList().add(vo.getItem2());
			newlog.getList().add(vo.getItem3());
			newlog.getList().add(vo.getItem4());
			newlog.getList().add(vo.getItem5());
			newlog.getList().add(vo.getItem6());
			newlog.getList().add(vo.getItem7());
			newlog.getList().add(vo.getItem8());
			newlog.getList().add(vo.getItem9());
			newlog.getList().add(vo.getItem10());
			newlog.getList().add(vo.getItem11());
			
			filename = "smartphone.game.smssubscribe.";
		}
		else if (type == 3)
		{
			//发送确认订购短信的日志
			//文件名：smartphone.game.smssubscribe.yyyy-mm-dd
			//日志内容格式：游戏标识|确认发起时间|订单号|手机号|回复内容|目的号码|业务代码|业务名称|截获短信
			newlog.getList().add(vo.getItem1());
			newlog.getList().add(vo.getItem2());
			newlog.getList().add(vo.getItem3());
			newlog.getList().add(vo.getItem4());
			newlog.getList().add(vo.getItem5());
			newlog.getList().add(vo.getItem6());
			newlog.getList().add(vo.getItem7());
			newlog.getList().add(vo.getItem8());
			newlog.getList().add(vo.getItem9());
			
			filename = "smartphone.game.smsconfirm.";
		}
		else
		{
			filename = "smartphone.game.unknow.";
		}
		GameLogsBean.saveExNewLog(newlog, true, "logs/exlog2/", filename);
	}
	
	public static void genChannelLog(LogVO vo, String type)
	{
		ExNewLog newlog = new ExNewLog();
		String filename = "";
		if (type == null) 
		{
			type = "";
		}
		if (type.equals("efun"))
		{
			//efun的支付日志
			//文件名：efun.yyyy-mm-dd
			//日志内容格式：支付时间|游戏服|渠道用户ID|渠道订单号|空中订单号|币种类型|充值金额|游戏币|对账月|系统
			newlog.getList().add(vo.getItem1());
			newlog.getList().add(vo.getItem2());
			newlog.getList().add(vo.getItem3());
			newlog.getList().add(vo.getItem4());
			newlog.getList().add(vo.getItem5());
			newlog.getList().add(vo.getItem6());
			newlog.getList().add(vo.getItem7());
			newlog.getList().add(vo.getItem8());
			newlog.getList().add(vo.getItem9());
			newlog.getList().add(vo.getItem10());
			filename = "efun.";
		}
		else
		{
			filename = "unknow.";
		}
		GameLogsBean.saveExNewLog(newlog, true, "logs/exlog3/", filename);
	}
	
	public static String[][] sortHttpRequestMap(Map<String, String> map)
	{
		String[][] ret = new String[map.size()][2];
		Map<String, String> sort = new TreeMap<String, String>();
		for (String key : map.keySet())
		{
			sort.put(key, map.get(key));
		}
		List<Map.Entry<String, String>> infos = new ArrayList<Map.Entry<String, String>>(sort.entrySet());
		for (int i = 0; i < infos.size(); i++)
		{
			ret[i][0] = infos.get(i).getKey();
			ret[i][1] = infos.get(i).getValue();
		}
		return ret;
	}
	
	public static String addZero(String content, int len)
	{
		if (content == null)
		{
			return content;
		}
		
		int length = content.length();
		if (length < len)
		{
			int padding = len - length;
			while (padding > 0)
			{
				content = "0" + content;
				padding--;
			}
		}
		return content;
	}
	
	public static String rmvZero(String content)
	{
		if (content == null || content.length() <= 0)
		{
			return content;
		}
		
		String str = content.substring(0, 1);
		while (str.equals("0"))
		{
			content = content.substring(1, content.length());
			if (content.length() <= 0)
			{
				break;
			}
			str = content.substring(0, 1);
		}
		return content;
	}
}
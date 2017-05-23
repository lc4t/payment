package noumena.payment.userverify;

import java.net.URL;

import net.sf.json.JSONObject;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;

/**
 * @author LiangJun
 *
 */
public class CLenovoVerify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
		String ret = "";
		switch (model)
		{
		case 0:
			//正常参数验证，返回合法id
			ret = getIdFrom(vo);
			break;
		case 1:
			//正常参数验证，返回json格式状态
			ret = getIdFrom(vo);
			break;
		case 2:
			//json参数验证，返回合法id
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setToken(json.getString("token"));
			vo.setAppid(json.getString("appid"));
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
//		String urlstr = "http://passport.lenovo.com/interserver/authen/1.2/getaccountid?%s";
//		urlstr = String.format(urlstr, token);
		
		String urlstr = "http://passport.lenovo.com/interserver/authen/1.2/getaccountid?lpsust=%s&realm=%s";
		urlstr = String.format(urlstr, vo.getToken(), vo.getAppid());
		
		ChannelVerify.GenerateLog("lenovo get user info url ->" + urlstr);

		try
		{
			SAXBuilder builder = new SAXBuilder();
			Document doc = builder.build(new URL(urlstr));
			Element root = doc.getRootElement();
			Element e = root.getChild("AccountID");
			id = e.getTextTrim();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

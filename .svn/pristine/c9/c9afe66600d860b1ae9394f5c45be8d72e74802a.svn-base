package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CBaiduVerify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
//		String id = getIdFrom(vo);
//		return id;
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
			vo.setUid(json.getString("uid"));
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("sessionid"));
			vo.setExinfo("");
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		try
		{
			String uid = vo.getUid();
			String sessionid = URLDecoder.decode(vo.getToken(), "utf-8");
			
			String minwen = "";
			minwen = vo.getAppid();
			minwen += getAppKeyById(vo.getAppid());
			minwen += uid;
			minwen += sessionid;
			minwen += getAppSecretById(vo.getAppid());
			String miwen = StringEncrypt.Encrypt(minwen);
			
			String urlstr = "http://sdk.m.duoku.com/openapi/sdk/checksession";
			urlstr += "?appid=" + vo.getAppid();
			urlstr += "&appkey=" + getAppKeyById(vo.getAppid());
			urlstr += "&uid=" + uid;
			urlstr += "&sessionid=" + sessionid;
			urlstr += "&clientsecret=" + miwen;
			
			ChannelVerify.GenerateLog("baidu get user info url ->" + urlstr);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}

			connection.disconnect();

			ChannelVerify.GenerateLog("baidu get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("error_code").equals("0"))
			{
				id = uid;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
	
	private static String getAppKeyById(String appid)
	{
		return Util.getDuokooKey(appid);
//		if (appid.equals("1452"))
//		{
//			//t6
//			return "694e59e14f9abaef4e065983c62af405";
//		}
//		return "";
	}
	
	private static String getAppSecretById(String appid)
	{
		return Util.getDuokooSecret(appid);
//		if (appid.equals("1452"))
//		{
//			//t6
//			return "4aba057a5ec082fc2ade563d04ba1bf2";
//		}
//		return "";
	}
}

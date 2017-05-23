package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.HmacSHA1Encryption;
import noumena.payment.userverify.util.Util;

public class CMiVerify
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
			vo.setToken(json.getString("session"));
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
//			String uid = vo.getUid();
//			String sessionid = URLDecoder.decode(vo.getToken(), "utf-8");
			
			String minwen = "";
			minwen += "appId=";
			minwen += vo.getAppid();
			minwen += "&session=";
			minwen += vo.getToken();
			minwen += "&uid=";
			minwen += vo.getUid();
			String miwen = HmacSHA1Encryption.HmacSHA1Encrypt(minwen, getAppKeyById(vo.getAppid()));
			
			String urlstr = "http://mis.migc.xiaomi.com/api/biz/service/verifySession.do";
			urlstr += "?appId=" + vo.getAppid();
			urlstr += "&session=" + URLEncoder.encode(vo.getToken(), "utf-8");
			urlstr += "&uid=" + vo.getUid();
			urlstr += "&signature=" + miwen;
			
			ChannelVerify.GenerateLog("xiaomi get user info url ->" + urlstr);

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

			ChannelVerify.GenerateLog("xiaomi get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("errcode").equals("200"))
			{
				id = vo.getUid();
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
		return Util.getXiaomiKey(appid);
//		if (appid.equals("1452"))
//		{
//			//t6
//			return "694e59e14f9abaef4e065983c62af405";
//		}
//		return "";
	}
}

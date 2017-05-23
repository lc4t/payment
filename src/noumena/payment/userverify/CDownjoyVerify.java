package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CDownjoyVerify
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
			vo.setAppid(json.getString("app_id"));
			vo.setUid(json.getString("mid"));
			vo.setToken(json.getString("token"));
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
			String appid = vo.getAppid();
			String mid = vo.getUid();
			String token = vo.getToken();
			String key = Util.getDownjoyKey(appid);
			String minwen = token + "|" + key;
			String miwen = StringEncrypt.Encrypt(minwen);
			
			String urlstr = "http://connect.d.cn/open/member/info/";
			urlstr += "?app_id=" + appid;
			urlstr += "&mid=" + mid;
			urlstr += "&token=" + token;
			urlstr += "&sig=" +  miwen;

			ChannelVerify.GenerateLog("downjoy get user info minwen ->" + minwen);
			ChannelVerify.GenerateLog("downjoy get user info url ->" + urlstr);
	
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
//			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.connect();
			
//			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
//			outs.write("");
			
//			outs.flush();
//			outs.close();
			
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
	
			ChannelVerify.GenerateLog("downjoy get user info ret ->" + res);
	
//			{
//				"memberId":32608510,
//				"username":"ym1988ym",
//				"nickname":"当乐_小牧",
//				"gender":"男",
//				"level":11, "avatar_url":"http://d.cn/images/item/35/002.gif",
//				"created_date":1346140985873,
//				"token":"F9A0F6A0E0D4564F56C483165A607735FA4F324",
//				"error_code":0
//			}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("error_code");
			if (id.equals("0"))
			{
				id = json.getString("memberId");
			}
			else
			{
				id = "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

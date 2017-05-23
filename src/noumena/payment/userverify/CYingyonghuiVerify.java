package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.Util;

public class CYingyonghuiVerify
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
			//json内容： {"type":"yingyonghui","app_id":"","ticket":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setToken(json.getString("ticket"));
			vo.setAppid(json.getString("app_id"));
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
			String urlstr = "http://api.appchina.com/appchina-usersdk/user/get.json?app_id=%s&app_key=%s&ticket=%s";
			urlstr = String.format(urlstr, appid, Util.getYingyonghuiKey(appid), vo.getToken());
			
			ChannelVerify.GenerateLog("yingyonghui get user info url ->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream(),"utf-8")
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}

			connection.disconnect();
			
			ChannelVerify.GenerateLog("yingyonghui get user info ret ->" + res);
			
//			{"status": 0,"data":{"user_name": "","user_id":""}}
			JSONObject json = JSONObject.fromObject(res);
			if (json.getInt("status") == 0) 
			{
				Object data = json.get("data");
				json.clear();
				json = JSONObject.fromObject(data);
				id = json.getString("user_name");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}

}

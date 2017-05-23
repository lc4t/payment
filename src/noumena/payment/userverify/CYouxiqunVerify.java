package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CYouxiqunVerify
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
			vo.setToken(json.getString("access_token"));
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
			String urlstr = "http://open.xmwan.com/v2/users/me?access_token=" + vo.getToken();
			
			ChannelVerify.GenerateLog("youxiqun get user info url ->" + urlstr);
			
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
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}

			connection.disconnect();
			
			ChannelVerify.GenerateLog("youxiqun get user info ret ->" + res);
			
//			{"xmw_open_id":"","nickname":"","avatar":"","gender":""}
			JSONObject json = JSONObject.fromObject(res);
			String uid = json.getString("xmw_open_id");
			if (uid != null) 
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

}

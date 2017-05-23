package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CDuowanVerify
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
			vo.setUid(json.getString("open_id"));
			vo.setAppid(json.getString("g_appid"));
			vo.setToken(json.getString("open_token"));
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
			String urlstr = "http://api.5253.com/open/checkLogin.do?open_id=%s&open_token=%s&g_appid=%s";
//			String urlstr = "http://sandbox.api.5253.com/open/checkLogin.do?open_id=%s&open_token=%s&g_appid=%s";

			urlstr = String.format(urlstr, vo.getUid(), vo.getToken(), vo.getAppid());
			ChannelVerify.GenerateLog("duowan get user info url ->" + urlstr);
	
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
	
			ChannelVerify.GenerateLog("duowan get user info ret ->" + res);
//			{"result":"1" ,"msg":"登录成功"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("result");
			if (id.equals("1"))
			{
				id = vo.getUid();
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

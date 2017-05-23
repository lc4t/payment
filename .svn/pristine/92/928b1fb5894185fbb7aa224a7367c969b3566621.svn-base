package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CSnailVerify
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
			String minwen = "";
			minwen += vo.getAppid();
			minwen += "4";
			minwen += vo.getUid();
			minwen += vo.getToken();
			minwen += Util.getSnailKey(vo.getAppid());
			ChannelVerify.GenerateLog("snail mingwen ->" + minwen);
			String miwen = StringEncrypt.Encrypt(minwen);
			
			String urlstr = "http://api.app.snail.com/store/platform/sdk/ap";
			urlstr += "?AppId=" + vo.getAppid();
			urlstr += "&Act=4";
			urlstr += "&Uin=" + vo.getUid();
			urlstr += "&SessionId=" + vo.getToken();
			urlstr += "&Sign=" + miwen;
			
			ChannelVerify.GenerateLog("snail get user info url ->" + urlstr);
	
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
//			outs.write(content);
			
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
	
			ChannelVerify.GenerateLog("snail get user info ret ->" + res);
//			{"ErrorCode":"1","ErrorDesc":"有效","Account":"xxx"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("ErrorCode");
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

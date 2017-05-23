package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CBaiduyunVerify
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
			String minwen = "";
			minwen += vo.getAppid();
			minwen += vo.getToken();
			minwen += Util.getBaiduyunKey(vo.getAppid());
			ChannelVerify.GenerateLog("baiduyun mingwen ->" + minwen);
			String miwen = StringEncrypt.Encrypt(minwen);
			String content = "";
			
			String urlstr = "http://querysdkapi.91.com/LoginStateQuery.ashx";
			content += "AppID=" + vo.getAppid();
			content += "&AccessToken=" + vo.getToken();
			content += "&Sign=" + miwen;
			
			ChannelVerify.GenerateLog("baiduyun get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("baiduyun get user info content ->" + content);
	
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
//			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream(), "utf8")
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
	
			connection.disconnect();
	
			ChannelVerify.GenerateLog("baiduyun get user info ret ->" + res);
//			{"ResultCode":-1,"ResultMsg":"参数错误"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("ResultCode");
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

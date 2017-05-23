package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CLineVerify
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
			vo.setAppid(json.getString("appId"));
			vo.setUid("");
			vo.setToken(json.getString("reqStr"));
			vo.setExinfo(json.getString("env"));
			
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
			String type = vo.getExinfo();
			String appid = vo.getAppid();
			String sessionid = vo.getToken();
			//{"txid":"123456789","appId":"LGSAMPLE","reqStr":"abcdefghijklmnopqrstu123456"}
			String content = "";
			content += "{";
			content += "\"txid\":\"" + System.currentTimeMillis() + "\",";
			content += "\"appId\":\"" + appid + "\",";
			content += "\"reqStr\":\"" + sessionid + "\"";
			content += "}";
			
			String urlstr = "";
			if (type.equals("sandbox"))
			{
				urlstr = "http://sdbx-gw.line-apps.com:80/Verify/login/verify";
			}
			else if (type.equals("test"))
			{
				urlstr = "http://proxy-sdbx.line-alpha.me:10080/Verify/login/verify";
			}
			else
			{
				urlstr = "http://xxx/Verify/login/verify";
			}
			
			ChannelVerify.GenerateLog("line get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("line get user info content ->" + content);
	
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			
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
	
			ChannelVerify.GenerateLog("line get user info ret ->" + res);
	
//			{
//			  "statusCoode":0,
//			  "statusMessage":"",
//			  "txid":"123456789",
//			  "data":{
//			    "mid":"midmidmidmidmidmid"
//			  }
//			}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("statusCode");
			if (id.equals("0"))
			{
				json = JSONObject.fromObject(json.getString("data"));
				id = json.getString("mid");
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

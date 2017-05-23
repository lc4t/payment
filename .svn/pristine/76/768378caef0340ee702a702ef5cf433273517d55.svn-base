package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CWeixinVerify
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
			//json内容： {"type":"weixin","openid":"","appid":"","accessToken":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("openid"));
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("accessToken"));
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
			long timestamp = Calendar.getInstance().getTimeInMillis()/1000;
			String appid = vo.getAppid();
			String openid = vo.getUid();
			String sig = StringEncrypt.Encrypt(Util.getWeixinKey(appid) + timestamp);
			
			String urlstr = "http://msdk.qq.com/auth/check_token/?timestamp=%s&appid=%s&sig=%s&openid=%s&encode=1";
			urlstr = String.format(urlstr, timestamp+"", appid, sig, openid);
			
			String content = "{";
			content += "\"accessToken\":\"" + vo.getToken() + "\",";
			content += "\"openid\":\"" + openid + "\"";
			content += "}";
			
			ChannelVerify.GenerateLog("weixin get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("weixin get user info url content ->" + content);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(content);
			wr.flush();
			wr.close();
			
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
			
			ChannelVerify.GenerateLog("weixin get user info ret ->" + res);
			
			//{"ret":0,"msg":"ok"}
			JSONObject json = JSONObject.fromObject(res);
			if (json.getInt("ret")==0) 
			{
				id = openid;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

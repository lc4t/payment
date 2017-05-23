package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CDouyuVerify
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
			//json内容： {"type":"douyu","sid":"","appid":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("sid"));
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
			int timestamp = (int)(System.currentTimeMillis()/1000);
			String appid = vo.getAppid();
			String sid = vo.getToken();
			
			String content = sid + "&" + timestamp + "&" + Util.getDouyuKey(appid);
			String sign = StringEncrypt.Encrypt(content);
			
			String urlstr = "http://www.douyutv.com/api/wg_andriod/check_sn";
//			String urlstr = "http://dy.dz11.com:88/api/wg_andriod/check_sn";
			
			String urlParameters = "";
			urlParameters += "game_id=" + appid;
			urlParameters += "&sid=" + sid;
			urlParameters += "&timestamp=" + timestamp;
			urlParameters += "&sign=" + sign;
			
			ChannelVerify.GenerateLog("douyu get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("douyu get user info urlParameters ->" + urlParameters);
			
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
			wr.writeBytes(urlParameters);
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
			
			ChannelVerify.GenerateLog("douyu get user info ret ->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			if (json.getInt("error") == 0) 
			{
				id = json.getString("data");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
}

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;

public class CSinaVerify
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
			//json内容： {"type":"sina","uid":"","token":"","machineid":"","ip":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("uid"));
			vo.setToken(json.getString("token"));
			vo.setAppid(json.getString("machineid"));
			vo.setExinfo(json.getString("ip"));
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
			String i = "chktoken";
			String p = "d940a1ab0429ce60292187d9d70bae34";
			String m = StringEncrypt.Encrypt(vo.getToken()+p);
			
			String urlstr = "http://game.weibo.cn/sso.php?i=%s&uid=%s&token=%s&machineid=%s&ip=%s&m=%s";
			urlstr = String.format(urlstr, i, vo.getUid(), vo.getToken(), vo.getAppid(), vo.getExinfo(), m);
			
			ChannelVerify.GenerateLog("sina get user info url ->" + urlstr);
			
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
			
			ChannelVerify.GenerateLog("sina get user info ret ->" + res);
			
//			{"result":"succ","data":{"token":"","uid":""}}
			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("result").equals("succ")) 
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

}

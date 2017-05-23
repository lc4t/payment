package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class C4399Verify
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
			//json内容： {"type":"4399","uid":"","key":"","state":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("uid"));
			vo.setAppid(json.getString("key"));
			vo.setToken(json.getString("state"));
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
			String uid = vo.getUid();
			String state = vo.getToken();
			String key = vo.getAppid();
			String content = key+uid+state+Util.get4399Key(key);
			String sign = StringEncrypt.Encrypt(content);
			
			String urlstr = "http://m.4399api.com/openapi/oauth-check.html?uid=%s&state=%s&key=%s&sign=%s";
			urlstr = String.format(urlstr, uid, state, key, sign);
			
			ChannelVerify.GenerateLog("4399 get user info url ->" + urlstr);
			
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
			
			ChannelVerify.GenerateLog("4399 get user info ret ->" + res);
			System.out.println(res);
//			{"code":100,"result":null,"message":"验证成功"}
			JSONObject json = JSONObject.fromObject(res);
			if (json.getInt("code")==100) 
			{
				id = uid;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			id = "";
		}

		return id;
	}
}

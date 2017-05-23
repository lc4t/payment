package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CJufenghudongVerify
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
		String minwen = "";
		String sign = "";
		
		try
		{
			String urlstr = "http://ucenter.iiapple.com/foreign/oauth/verification.php?user_id=%s&session=%s&game_id=%s&_sign=%s";
				
			minwen += "game_id=";
			minwen += vo.getAppid();
			minwen += "&session=";
			minwen += vo.getToken();
			minwen += "&user_id=";
			minwen += vo.getUid();
			sign = StringEncrypt.Encrypt(minwen);
			sign = StringEncrypt.Encrypt(sign+Util.getJufenghudongKey(vo.getAppid()));

			urlstr = String.format(urlstr, vo.getUid(), vo.getToken(), vo.getAppid(),sign);
			ChannelVerify.GenerateLog("jufenghudong get user info url ->" + urlstr);
	
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
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
	
			ChannelVerify.GenerateLog("jufenghudong get user info ret ->" + res);
//			{"status":"1" ,"desc":"操作成功"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("status");
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

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CXxzsVerify
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
			//json内容： {"type":"xxzs","game_uin":"","appid":"","token":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("game_uin"));
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
			long t = Calendar.getInstance().getTimeInMillis()/1000;
			String content = vo.getUid() + vo.getAppid() + t + Util.getXxzsKey(vo.getAppid());
			String sign = StringEncrypt.Encrypt(content);
			
			String urlstr = "http://guopan.cn/gamesdk/verify/?game_uin=%s&appid=%s&token=%s&t=%s&sign=%s";
			urlstr = String.format(urlstr, vo.getUid(), vo.getAppid(), vo.getToken(), t, sign);
			
			ChannelVerify.GenerateLog("xxzs get user info url ->" + urlstr);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setUseCaches(false);
			connection.setDoInput(true);
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
	
			ChannelVerify.GenerateLog("xxzs get user info ret ->" + res);
			
			if (res.equals("true"))
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

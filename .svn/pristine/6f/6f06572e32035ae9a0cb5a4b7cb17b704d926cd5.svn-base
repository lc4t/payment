package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CLengjingVerify
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
			//{"type":"lengjing","userId":"","channel":"","token":"","productCode":"","channelLabel":"","channelUserId":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("userId"));
			vo.setAppid(json.getString("productCode"));
			vo.setToken(json.getString("token"));

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
			JSONObject json = JSONObject.fromObject(vo.getExinfo());
			String channelUserId = json.getString("channelUserId");
			String channel = json.getString("channel");
			String channelLabel = json.getString("channelLabel");
			
			String urlstr = "http://gameproxy.xinmei365.com/game_agent/checkLogin?userId=%s&channel=%s&token=%s&productCode=%s&channelLabel=%s&channelUserId=%s";
			urlstr = String.format(urlstr, vo.getUid(), channel, vo.getToken(),vo.getAppid(),channelLabel,channelUserId);
			
			ChannelVerify.GenerateLog("lengjing get user info url ->" + urlstr);
	
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
	
			ChannelVerify.GenerateLog("lengjing get user info ret ->" + res);
			//true/false
			if (res.equals("true")) {
				id = channelUserId;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.Base64;
import noumena.payment.userverify.util.Util;

public class CAnzhiVerify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
//		String id = getIdFrom(vo);
//		return id;
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
			String uid = vo.getUid();
			String sessionid = vo.getToken();
			String appid = vo.getAppid();
			
			String minwen = "";
			minwen += appid;
			minwen += uid;
			minwen += sessionid;
			minwen += getAppKeyById(appid);
			String miwen = Base64.encodeToString(minwen);
			
			String urlstr = "http://user.anzhi.com/web/api/sdk/third/1/queryislogin";
			String poststr = "";
			poststr += "time=" + Util.getCurTimeStr();
			poststr += "&appkey=" + appid;
			poststr += "&account=" + uid;
			poststr += "&sid=" + sessionid;
			poststr += "&sign=" + miwen;
			
			ChannelVerify.GenerateLog("anzhi get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("anzhi get user info posturl ->" + poststr);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(poststr);
			
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

			ChannelVerify.GenerateLog("anzhi get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("sc").equals("1"))
			{
				id = json.getString("msg");
				id = Base64.decode(id, "UTF-8");
				json = JSONObject.fromObject(id);
//				id = json.getString("nickname");
				id = json.getString("uid");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
	
	private static String getAppKeyById(String appid)
	{
		return Util.getAnzhiKey(appid);
	}
}

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CQidianVerify
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
			vo.setUid(json.getString("packagename"));
			vo.setAppid(json.getString("gameid"));
			vo.setToken(json.getString("tkey"));
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
			String urlstr = "http://shouyousdk.game.qidian.com/HubService.asmx/GameCheckLoginToken";
			String content = "";
			
			content += "tkey=" + vo.getToken();
			content += "&GameId=" + vo.getAppid();
			content += "&PackageName=" + vo.getUid();

			urlstr = String.format(urlstr, vo.getAppid(), vo.getUid(), vo.getToken());
			ChannelVerify.GenerateLog("qidian get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("qidian get user info content ->" + content);
	
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
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
	
			connection.disconnect();
	
			ChannelVerify.GenerateLog("qidian get user info ret ->" + res);
//			{value:{Code:0,LoginMessage:'登录成功',NickName:'书友140102202107755',SndaId:'13641617457',UserId:'174427204'}}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("value");
			if (id != null && !id.equals(""))
			{
				json = JSONObject.fromObject(id);
				id = json.getString("Code");
				if (id.equals("0"))
				{
					id = json.getString("UserId");
				}
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

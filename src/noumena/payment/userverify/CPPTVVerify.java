package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CPPTVVerify
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
			vo.setToken(json.getString("sessionid"));
			vo.setUid(json.getString("username"));
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
			String urlstr = "http://api.user.vas.pptv.com/c/v2/cksession.php";
			urlstr += "?type=login";
			urlstr += "&app=mobgame";
			urlstr += "&sessionid=" + vo.getToken();
			urlstr += "&username=" + vo.getUid();
			
			ChannelVerify.GenerateLog("pptv get user info url ->" + urlstr);

			HttpURLConnection connection = (HttpURLConnection) new URL(urlstr).openConnection();
//			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
//			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.connect();
			
//			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
//			outs.write(content);
			
//			outs.flush();
//			outs.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream(), "utf8")
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
	
			connection.disconnect();
	
			ChannelVerify.GenerateLog("pptv get user info ret ->" + res);
//			{"status":1,"message":""}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("status");
			if (id != null && id.equals("1"))
			{
				id = vo.getUid();
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

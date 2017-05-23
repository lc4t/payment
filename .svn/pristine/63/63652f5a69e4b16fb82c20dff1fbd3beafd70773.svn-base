package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class C3GGateVerify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
		String id = getIdFrom(vo);
		return id;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		try
		{
			String uid = vo.getUid();
			String appid = vo.getAppid();
			String sid = vo.getToken();
			String cpid = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(cpid);
			cpid = json.getString("cpid");
			String urlstr = "http://2324.cn/User/userverify.php";
			String transdata = "";
			transdata += "cpid=" + cpid;
			transdata += "&gameid=" + appid;
			transdata += "&sid=" + sid;
			transdata += "&token=" + uid;
			
			ChannelVerify.GenerateLog("3ggate get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("3ggate get user info params ->" + transdata);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(transdata);
			
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

			ChannelVerify.GenerateLog("3ggate get user info ret ->" + res);

			json = JSONObject.fromObject(res);
			id = json.getString("code");
			if (id.equals("1"))
			{
				id = uid;
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

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CKunlunVerify
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
			String klsso = vo.getToken();
			String urlstr = "http://login.kimi.com.tw/verifyklsso.php?klsso=";
			urlstr += klsso;
			
			ChannelVerify.GenerateLog("kunlun get user info url ->" + urlstr);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write("");
			
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

			ChannelVerify.GenerateLog("kunlun get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("retcode").equals("0"))
			{
				id = json.getString("data");
				json = JSONObject.fromObject(id);
				id = json.getString("uid");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

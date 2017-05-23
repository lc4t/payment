package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CBaiduqianbaoVerify
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
			String appid = vo.getAppid();
			String code = vo.getToken();
			String urlstr = "http://gameopen.baidu.com/index.php";
			String content = "r=FromIapppayToUserAction&m=domethod2";
			String transdata = "";
			transdata += "{\"appid\":\"";
			transdata += appid;
			transdata += "\",\"code\":\"";
			transdata += code;
			transdata += "\"}";
			String key = Util.getBaiduqianbaoKey(appid);
			String sign = StringEncrypt.Encrypt(transdata + key);
			content += "&transdata=";
			content += transdata;
			content += "&sign=";
			content += sign;
			
			ChannelVerify.GenerateLog("baiduqianbao get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("baiduqianbao get user info params ->" + content);

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

			ChannelVerify.GenerateLog("baiduqianbao get user info ret ->" + res);

			int pos = 0;
			pos = res.indexOf("{");
			transdata = res.substring(pos);
			pos = transdata.indexOf("}");
			transdata = transdata.substring(0, pos + 1);
			JSONObject json = JSONObject.fromObject(transdata);
			id = json.getString("userid");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

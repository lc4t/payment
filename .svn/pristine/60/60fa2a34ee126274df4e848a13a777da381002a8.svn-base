package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CKuaiyongVerify
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
			String appid = vo.getAppid();
			String token = vo.getToken();
			String minwen = "";
			String sign = "";
			
			minwen = Util.getKuaiyongKey(appid) + token;
			sign = StringEncrypt.Encrypt(minwen);
			
			String urlstr = "http://f_signin.bppstore.com/loginCheck.php";
			urlstr += "?tokenKey=" + token;
			urlstr += "&sign=" + sign;
			
			ChannelVerify.GenerateLog("kuaiyong get user info url ->" + urlstr);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.connect();
			
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

			ChannelVerify.GenerateLog("kuaiyong get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("code");
			if (id.equals("0"))
			{
				id = json.getString("data");
				json = JSONObject.fromObject(id);
				id = json.getString("guid");
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

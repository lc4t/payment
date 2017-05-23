package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CSharejoyVerify
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
			//json内容： {"type":"sharejoy","game_id":"","merchant_id":"","server_id":"","access_key":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setAppid(json.getString("game_id"));
			vo.setToken(json.getString("access_key"));

			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		String minwen = "";
		String miwen = "";
		int ts = (int)(Calendar.getInstance().getTimeInMillis());
		JSONObject jsob = null;
		try {
			jsob = JSONObject.fromObject(vo.getExinfo());
			
			String access_key = vo.getToken();
			String appid = vo.getAppid();
			String merchant_id = jsob.getString("merchant_id");
			String server_id = jsob.getString("server_id");
			
			minwen = access_key;
			minwen += appid;
			minwen += merchant_id;
			minwen += server_id;
			minwen += ts;
			minwen += "1";
			minwen += Util.getSharejoyKey(appid);
			
			miwen = StringEncrypt.Encrypt(minwen);
			
			String urlstr ="http://p.biligame.com/api/server/session.verify";
			
			String urlParameters = "";
			urlParameters += "access_key=" + access_key;
			urlParameters += "&game_id=" + appid;
			urlParameters += "&merchant_id=" + merchant_id;
			urlParameters += "&version=1";
			urlParameters += "&timestamp=" + ts;
			urlParameters += "&server_id=" + server_id;
			urlParameters += "&sign="+miwen;
			
			ChannelVerify.GenerateLog("sharejoy get user info urlParameters ->"+ urlParameters);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("User-Agent", "Mozilla/5.0 GameServer");
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null) {
				res += line;
			}
			connection.disconnect();
			System.out.println(res);
			ChannelVerify.GenerateLog("sharejoy get user info res ->" + res);

			// {"code":"0" ,"timestamp":"","open_id":"","uname":""}
			JSONObject json = JSONObject.fromObject(res);
			
			if (json.getString("code").equals("0")) 
			{
				id = json.getString("open_id");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}
}


package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CKuaifaVerify
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
			//json内容： {"type":"kuaifa","openid":"","gamekey":"","token":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("openid"));
			vo.setAppid(json.getString("gamekey"));
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
			long ts = Calendar.getInstance().getTimeInMillis()/1000;
			String content = "";
			String sign = "";
			
			content += "gamekey=" + URLEncoder.encode(vo.getAppid(), "utf-8");
			content += "&openid=" + URLEncoder.encode(vo.getUid(), "utf-8");
			content += "&timestamp=" + ts;
			content += "&token=" + URLEncoder.encode(vo.getToken(), "utf-8");
			sign = StringEncrypt.Encrypt(content);
			sign = StringEncrypt.Encrypt(sign + Util.getKuaifaKey(vo.getAppid()));
			
			String urlParameters = "";
			urlParameters += "gamekey=" + vo.getAppid();
			urlParameters += "&openid=" + vo.getUid();
			urlParameters += "&timestamp=" + ts;
			urlParameters += "&token=" + vo.getToken();
			urlParameters += "&_sign=" + sign;
			
			String urlstr = "http://z.kuaifazs.com/foreign/oauth/verification2.php";
			
			ChannelVerify.GenerateLog("kuaifa get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("kuaifa get user info urlParameters ->" + urlParameters);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
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
			
			ChannelVerify.GenerateLog("kuaifa get user info ret ->" + res);
			
//			{"result":"0","result_desc":"ok"}
			JSONObject json = JSONObject.fromObject(res);
			
			if (json.getString("result").equals("0"))
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

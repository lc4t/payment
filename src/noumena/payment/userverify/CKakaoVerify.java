package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.TrustAnyTrustManager;

public class CKakaoVerify
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
			//json内容： {"type":"kakao","user_id":"","access_token":"","client_id":"","sdkver":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("user_id"));
			vo.setToken(json.getString("access_token"));
			vo.setAppid(json.getString("client_id"));
			vo.setExinfo(json.getString("sdkver"));
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
			String urlstr = "https://api.kakao.com/v1/token/check.json?user_id=%s&access_token=%s&client_id=%s&sdkver=%s";
			urlstr = String.format(urlstr,vo.getUid(), vo.getToken(), vo.getAppid(), vo.getExinfo());
			ChannelVerify.GenerateLog("kakao get user info url ->" + urlstr);
			
//			URL url = new URL(urlstr);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(false);
//			connection.setDoInput(true);
//			connection.setUseCaches(false);
//			connection.setRequestMethod("GET");
//			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
//			connection.setRequestProperty("charset", "utf-8");
//			
//			connection.connect();
			
			SSLContext context = SSLContext.getInstance("SSL");
			context.init(null, new TrustManager[]{ new TrustAnyTrustManager() }, new java.security.SecureRandom());   
			HttpsURLConnection connection = (HttpsURLConnection)new URL(urlstr).openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
			(
				new HostnameVerifier()
				{
					@Override
					public boolean verify(String arg0, SSLSession arg1)
					{
						//return true; //不验证
						return false; //验证
					}
				}
			);
			
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
			ChannelVerify.GenerateLog("kakao get user info ret ->" + res);
			
//			{"status":0}
			JSONObject json = JSONObject.fromObject(res);
			String status = json.getString("status");
			if (status.equals("0")) 
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

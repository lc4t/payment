package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.TrustAnyTrustManager;
import noumena.payment.userverify.util.Util;

public class CIGameVerify
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
			//json内容： {"type":"igame","client_id":"","code":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setAppid(json.getString("client_id"));
			vo.setToken(json.getString("code"));
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
			long timestamp = Calendar.getInstance().getTimeInMillis();
			String client_id = vo.getAppid();
			String client_secret = Util.getIGameKey(client_id);
			String sign_method = "MD5";
			String version = "v1.0";
			String sign_sort = "client_id&client_secret&sign_method&timestamp&version";
			String signature = StringEncrypt.Encrypt(client_id + client_secret + sign_method + timestamp + version).toUpperCase();
			
			String urlstr = "https://open.play.cn/oauth/token";
			String content = "client_id=" + client_id;
			content += "&sign_method=" + sign_method;
			content += "&version=" + version;
			content += "&timestamp=" + timestamp;
			content += "&sign_sort=" + URLEncoder.encode(sign_sort,"utf-8");
			content += "&signature=" + signature;
			content += "&client_secret=" + client_secret;
			content += "&code=" + vo.getToken();
			content += "&grant_type=authorization_code";
			
			ChannelVerify.GenerateLog("igame get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("igame get user info content ->" + content);
			

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
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
			connection.setRequestProperty("User-Agent", "Mozilla/4.0");
			
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			
			connection.disconnect();

			System.out.println(res);
			ChannelVerify.GenerateLog("igame get user info ret ->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			
			if (json.getString("user_id")!=null) 
			{
				id = json.getString("user_id");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

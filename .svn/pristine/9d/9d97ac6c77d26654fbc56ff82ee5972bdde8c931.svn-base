package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.TrustAnyTrustManager;
import noumena.payment.userverify.util.Util;

public class C360Verify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
//		String token = vo.getToken();
//		if (model == 1)
//		{
//			token = getTokenFrom360(token, vo.getAppid());
//		}
//		String id = "";
//		if (token == null || token.equals(""))
//		{
//			if (model == 1)
//			{
//				id = "{\"ret\"=0}";
//			}
//		}
//		else
//		{
//			id = getIdFrom(model, token);
//		}
//		return id;
		String ret = "";
		String token = vo.getToken();
		switch (model)
		{
		case 1:
			//需要先换取token的验证方法
			token = getTokenFrom360(token, vo.getAppid());
			if (token == null || token.equals(""))
			{
				if (model == 1)
				{
					ret = "{\"ret\"=0}";
				}
			}
		case 0:
			//正常参数验证，返回合法id
			ret = getIdFrom(model, token);
			break;
		case 2:
			//json参数验证，返回合法id
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			ret = getIdFrom(model, json.getString("token"));
			break;
		}
		return ret;
	}

	private static String getTokenFrom360(String code, String appid)
	{
		String token = "";
		String urlstr = "https://openapi.360.cn/oauth2/access_token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=oob";
		String appkey = Util.getQhKey(appid);
		String secretkey = Util.getQhSecretKey(appid);
		if (secretkey == null || secretkey.equals(""))
		{
			return "";
		}
		urlstr = String.format(urlstr, code, appkey, secretkey);
		ChannelVerify.GenerateLog("360 get token url ->" + urlstr);

		try
		{
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

			InputStream is = connection.getInputStream();
			InputStreamReader isr = new InputStreamReader(is);
			BufferedReader in = new BufferedReader(isr);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			
			connection.disconnect();
			
			ChannelVerify.GenerateLog("360 get token ret ->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			token = json.getString("access_token");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return token;
	}

	private static String getIdFrom(int model, String token)
	{
		String id = "";
		String urlstr = "https://openapi.360.cn/user/me.json?access_token=%s&fields=id";
		urlstr = String.format(urlstr, token);
		
		ChannelVerify.GenerateLog("360 get user info url ->" + urlstr);

		try
		{
			SSLContext context = SSLContext.getInstance("SSL");
			context.init
				(
					null,
					new TrustManager[]
					{
						new TrustAnyTrustManager()
					},
					new java.security.SecureRandom()
				);
			HttpsURLConnection connection = (HttpsURLConnection) new URL(urlstr).openConnection();
			connection.setSSLSocketFactory(context.getSocketFactory());
			connection.setHostnameVerifier
				(
					new HostnameVerifier()
					{
						@Override
						public boolean verify(String arg0, SSLSession arg1)
						{
							//return true; //不验证
							return false; // 验证
						}
					}
				);

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

			ChannelVerify.GenerateLog("360 get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);

			id = json.getString("id");
			if (model == 1)
			{
				json.clear();
				json.accumulate("ret", 1);
				json.accumulate("uid", id);
				json.accumulate("token", token);
				id = json.toString();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			if (model == 1)
			{
				id = "{\"ret\"=0}";
			}
		}

		return id;
	}
}

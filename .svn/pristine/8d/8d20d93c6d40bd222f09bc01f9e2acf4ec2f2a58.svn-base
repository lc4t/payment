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

public class CCoolpadVerify
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
			vo.setUid(json.getString("openid"));
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("access_token"));
			vo.setExinfo("");
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		String uid = vo.getUid();
		try
		{
			String urlstr = "https://openapi.coolyun.com/oauth2/api/get_user_info?access_token=%s&oauth_consumer_key=%s&openid=%s";
			urlstr = String.format(urlstr,vo.getToken(),vo.getAppid(),uid);
			
			ChannelVerify.GenerateLog("coolpad get user info url ->" + urlstr);

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

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			
			connection.disconnect();

			ChannelVerify.GenerateLog("coolpad get user info ret ->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			
			//{“rtn_code”:“0”}
			if (json.getString("rtn_code").equals("0")) {
				id = uid;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
	
	
	
}

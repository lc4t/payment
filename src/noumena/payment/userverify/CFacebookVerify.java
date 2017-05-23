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

public class CFacebookVerify
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
			vo.setToken(json.getString("token"));
			vo.setExinfo("");
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	public static String getIdFrom(String token)
	{
		ChannelInfoVO vo = new ChannelInfoVO();
		vo.setToken(token);
		return getIdFrom(vo);
	}

	public static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		String urlstr = "https://graph.facebook.com/me?fields=id&access_token=" + vo.getToken();

		try
		{
			ChannelVerify.GenerateLog("facebook get user info url ->" + urlstr);

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

//			URL url = new URL(urlstr);
//			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//			connection.setDoOutput(true);
//			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
//			
//			outs.flush();
//			outs.close();
			
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
	
			ChannelVerify.GenerateLog("facebook get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("id");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

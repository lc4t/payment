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

public class CToutiaoVerify
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
			//json内容： {"type":"toutiao","client_id":"","uid":"","access_token":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setAppid(json.getString("client_id"));
			vo.setUid(json.getString("uid"));
			vo.setToken(json.getString("access_token"));
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
			String uid = vo.getUid();
			
			String urlstr = "https://open.snssdk.com/partner_sdk/check_user/?client_key=%s&uid=%s&access_token=%s";
			urlstr = String.format(urlstr, vo.getAppid(), uid, vo.getToken());
			
			ChannelVerify.GenerateLog("toutiao get user info url ->" + urlstr);
			
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
			
			ChannelVerify.GenerateLog("toutiao get user info ret ->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("message").equals("success")) 
			{
				json = json.getJSONObject("data");
				if (json.getInt("verify_result") == 1)
				{
					id = uid;
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
}

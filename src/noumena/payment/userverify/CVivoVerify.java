package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.TrustAnyTrustManager;

public class CVivoVerify
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

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		try
		{
			String urlstr = "https://usrsys.inner.bbk.com/auth/user/info";
			String content = "access_token=" + vo.getToken();
			
			ChannelVerify.GenerateLog("vivo get user info url ->" + urlstr);
			ChannelVerify.GenerateLog("vivo get user info content ->" + content);

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
							return true; //不验证
							//return false; // 验证
						}
					}
				);

			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
//			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader
				(
					new InputStreamReader(connection.getInputStream(), "utf8")
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
	
			connection.disconnect();
	
			ChannelVerify.GenerateLog("vivo get user info ret ->" + res);
//			{"uid":"","email":"","stat":"","msg":""}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("uid");
			if (id == null)
			{
				id = "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			id = "";
		}

		return id;
	}
}

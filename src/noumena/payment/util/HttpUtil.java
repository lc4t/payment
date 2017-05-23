package noumena.payment.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import noumena.payment.userverify.util.TrustAnyTrustManager;


public class HttpUtil
{
	public static String doHttpsPost(String urlstr, String content)
	{
		String ret = "";
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
							return true; //不验证
							//return false; // 验证
						}
					}
				);
	
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
	//		connection.setRequestProperty("Content-type", "application/json");
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
			ret = res;
	
			connection.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
		return ret;
	}
	
	public static String doHttpPost(String urlstr, String content)
	{
		String ret = "";
		try
		{
			HttpURLConnection connection = (HttpURLConnection) new URL(urlstr).openConnection();
			
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
	//		connection.setRequestProperty("Content-type", "application/json");
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
			ret = res;
	
			connection.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
	
		return ret;
	}
}
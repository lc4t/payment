package cn.i4.pay.sdk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

public class HttpUtils {
	/**
	 * 
	 * @param url
	 * @param params
	 * @return
	 */
	public static String postHttps(String url, String params) {
		InputStream urlStream = null;
		BufferedReader reader = null;
		try {
			verifierHostname();
			URLConnection urlCon = (new URL(url)).openConnection();
			urlCon.setDoOutput(true);
			OutputStreamWriter out = new OutputStreamWriter(urlCon
					.getOutputStream(), "utf-8");
			out.write(params);
			// remember to clean up
			out.flush();
			out.close();
			// 一旦发送成功，用以下方法就可以得到服务器的回应：
			StringBuilder sTotalString = new StringBuilder();
			String sCurrentLine = "";
			urlStream = urlCon.getInputStream();
			reader = new BufferedReader(new InputStreamReader(urlStream));
			while ((sCurrentLine = reader.readLine()) != null) {
				sTotalString.append(sCurrentLine);
			}
			return sTotalString.toString();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (urlStream != null) {
				try {
					urlStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return "";
	}

	private static void verifierHostname() throws NoSuchAlgorithmException,
			KeyManagementException {
		SSLContext sslContext = null;
		sslContext = SSLContext.getInstance("TLS");
		X509TrustManager xtm = new X509TrustManager() {
			public void checkClientTrusted(X509Certificate[] chain,
					String authType) {
			}

			public void checkServerTrusted(X509Certificate[] chain,
					String authType) {
			}

			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		X509TrustManager[] xtmArray = new X509TrustManager[] { xtm };
		sslContext.init(null, xtmArray, new java.security.SecureRandom());
		if (sslContext != null) {
			HttpsURLConnection.setDefaultSSLSocketFactory(sslContext
					.getSocketFactory());
		}
		HostnameVerifier hnv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return true;
			}
		};
		HttpsURLConnection.setDefaultHostnameVerifier(hnv);
	}
}

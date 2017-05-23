package noumena.payment.userverify;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Set;
import java.util.UUID;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.GioneeBase64;
import noumena.payment.userverify.util.TrustAnyTrustManager;
import noumena.payment.userverify.util.Util;

public class CGioneeVerify
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
			vo.setUid(json.getString("uid"));
			vo.setAppid(json.getString("id"));
			vo.setToken(json.getString("AmigoToken"));
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
		
		String port = "443";
		String verify_url = "https://id.gionee.com:"+port+"/account/verify.do";
		String host = "id.gionee.com";
		String url = "/account/verify.do";
		String method = "POST";
		
		Long ts = System.currentTimeMillis() / 1000;
		//随机字符串
		String nonce = uuidToString(UUID.randomUUID()).substring(0, 8);
		//mac签名
		StringBuffer buffer = new StringBuffer();
		buffer.append(ts).append("\n");
		buffer.append(nonce).append("\n");
		buffer.append(method.toUpperCase()).append("\n");
		buffer.append(url).append("\n");
		buffer.append(host.toLowerCase()).append("\n");
		buffer.append(port).append("\n");
		buffer.append("\n");
		String text = buffer.toString();

		String mac = "";
		byte[] ciphertext = null;
		try {
			ciphertext = hmacSHA1Encrypt(Util.getGioneeKey(vo.getAppid()), text);
			mac = GioneeBase64.encodeToString(ciphertext, GioneeBase64.DEFAULT);
		} catch (Throwable e) {
			e.printStackTrace();
			id = "";
		}
		mac = mac.replace("\n", "");
		StringBuilder authStr = new StringBuilder();
		authStr.append("MAC ");
		authStr.append(String.format("id=\"%s\"", vo.getAppid()));
		authStr.append(String.format(",ts=\"%s\"", ts));
		authStr.append(String.format(",nonce=\"%s\"", nonce));
		authStr.append(String.format(",mac=\"%s\"", mac));
		ChannelVerify.GenerateLog("gionee get user info url ->" + verify_url + "?" + authStr.toString() );
		
		TrustManager[] tm = {new TrustAnyTrustManager()};
		try {
			SSLContext sslContext = SSLContext.getInstance("SSL", "SunJSSE");
			sslContext.init(null, tm, new java.security.SecureRandom());
			// 从上述SSLContext对象中得到SSLSocketFactory对象
			SSLSocketFactory ssf = sslContext.getSocketFactory();
			URL sendUrl = new URL(verify_url);
			HttpsURLConnection connection = (HttpsURLConnection) sendUrl.openConnection();
			connection.setSSLSocketFactory(ssf);
			connection.setDoInput(true); // true表示允许获得输入流,读取服务器响应的数据,该属性默认值为true
			connection.setDoOutput(true); // true表示允许获得输出流,向远程服务器发送数据,该属性默认值为false
			connection.setUseCaches(false); // 禁止缓存
			int timeout = 30000;
			connection.setReadTimeout(timeout); // 30秒读取超时
			connection.setConnectTimeout(timeout); // 30秒连接超时
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/json");
			connection.setRequestProperty("Authorization", authStr.toString());
			OutputStream out = connection.getOutputStream();
			out.write(vo.getToken().getBytes());
			out.flush();
			out.close();
			InputStream in = connection.getInputStream();
			ByteArrayOutputStream bytebuffer = new ByteArrayOutputStream();
			byte[] buff = new byte[1024];
			int len = -1;
			while ((len = in.read(buff)) != -1) {
				bytebuffer.write(buff, 0, len);
			}
			String res = bytebuffer.toString();
			ChannelVerify.GenerateLog("gionee get user info ret ->" + res);
			JSONObject json = JSONObject.fromObject(res);
			
			id = uid;
			Set set = json.keySet();
			for (Object obj : set) {
				if (obj.equals("r")) {
					if (json.get(obj).equals("0")) {
						id = uid;
					}else {
						id = "";
					}
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			id = "";
		}
		
		return id;
	}
	
	public static String uuidToString(UUID uuid) {
		int SizeOfUUID = 16;
		int SizeOfLong = 8;
		int BitsOfByte = 8;
		int MBLShift = (SizeOfLong - 1) * BitsOfByte;

		char[] HEX_CHAR_TABLE = { '0', '1', '2', '3', '4', '5', '6', '7', '8','9', 'A', 'B', 'C', 'D', 'E', 'F' };

		long[] ll = { uuid.getMostSignificantBits(),
				uuid.getLeastSignificantBits() };
		StringBuilder str = new StringBuilder(SizeOfUUID * 2);
		for (int m = 0; m < ll.length; ++m) {
			for (int i = MBLShift; i > 0; i -= BitsOfByte) {
				str.append(HEX_CHAR_TABLE[((byte) (ll[m] >>> i) >>> 4) & 0x0F]);
				str.append(HEX_CHAR_TABLE[(byte) (ll[m] >>> i) & 0x0F]);
			}
			str.append(HEX_CHAR_TABLE[((byte) (ll[m]) >>> 4) & 0x0F]);
			str.append(HEX_CHAR_TABLE[(byte) (ll[m]) & 0x0F]);
		}
		return str.toString();
	}
	
	public static byte[] hmacSHA1Encrypt(String encryptKey, String encryptText) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(encryptKey.getBytes("UTF-8"), "HmacSHA1"));
		return mac.doFinal(encryptText.getBytes("UTF-8"));
	}

}
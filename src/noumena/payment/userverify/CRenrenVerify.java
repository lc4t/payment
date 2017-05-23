package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.Base64;

public class CRenrenVerify
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
			//json内容： {"type":"renren","userId":"","access_token":"","mac_key":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("userId"));
			vo.setToken(json.getString("access_token"));
			vo.setExinfo(json.getString("mac_key"));
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		String userId = vo.getUid();
		
		String port = "80";
		String verify_url = "http://api.renren.com/v2/user/get?userId="+userId;
		String host = "api.renren.com";
		String url = "/v2/user/get?userId="+userId;
		String method = "GET";
		try {
			Long ts = System.currentTimeMillis() / 1000;
			//随机字符串
			String nonce = getRandomString();
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
			mac = hmacSHA1Encrypt(vo.getExinfo(),text);
			
			StringBuilder authStr = new StringBuilder();
			authStr.append("MAC ");
			authStr.append(String.format("id=\"%s\"", vo.getToken()));
			authStr.append(String.format(",ts=\"%s\"", ts));
			authStr.append(String.format(",nonce=\"%s\"", nonce));
			authStr.append(String.format(",mac=\"%s\"", mac));
			
			ChannelVerify.GenerateLog("renren get user info url ->" + verify_url + "&" + authStr.toString());
			  
			HttpURLConnection connection = (HttpURLConnection)new URL(verify_url).openConnection();
			connection.setUseCaches(false);
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestMethod("GET");
			connection.addRequestProperty("Authorization", authStr.toString());
			connection.connect();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"utf-8"));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			
			connection.disconnect();
			
			ChannelVerify.GenerateLog("renren get user info ret ->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			
			if (json.get("response")!=null) 
			{
				String response = json.get("response")+"";
				json.clear();
				json = JSONObject.fromObject(response);
				id = json.getLong("id")+"";
			}			

		} catch (Exception e) {
			e.printStackTrace();
			id = "";
		}
		return id;
	}
	
    public static String getRandomString() {
    	String CHAR_LIST = "abcdefghigklmnopkrstuvwxyzABCDEFGHIGKLMNOPQRSTUVWXYZ0123456789";
        Random random = new Random();
        StringBuffer sf = new StringBuffer();
        for (int i = 0; i < 8; i++) {
            int number = random.nextInt(62);
            sf.append(CHAR_LIST.charAt(number));
        }
        return sf.toString();
    }
	
	public static String hmacSHA1Encrypt(String encryptKey, String encryptText) throws InvalidKeyException, NoSuchAlgorithmException, IllegalStateException, UnsupportedEncodingException {
		Mac mac = Mac.getInstance("HmacSHA1");
		mac.init(new SecretKeySpec(encryptKey.getBytes("UTF-8"), "HmacSHA1"));
		byte[] digest = mac.doFinal(encryptText.getBytes("UTF-8"));
		return new String(Base64.encodeToString(digest));
	}
}
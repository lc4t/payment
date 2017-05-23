package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Random;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.Util;

public class COppoVerify
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
			vo.setAppid(json.getString("appid"));
			vo.setExinfo(json.getString("ssoid"));
			vo.setToken(json.getString("token"));
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	/*private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		try
		{
			String gcUserInfo = AccountAgent.getInstance().getGCUserInfo(new AccessToken(vo.getToken(), Util.getOppoKey(vo.getAppid())));
			ChannelVerify.GenerateLog("oppo get user info ->" + gcUserInfo);

//			{"BriefUser" : {"id":"49016734","sex":"true","profilePictureUrl":"http://gcfs.nearme.com.cn/avatar/940/959/11dcee105ee94e10845dd1d386cd810c.jpg","name":"winnn","userName":"winnn","emailStatus":"false","mobileStatus":"false","status":"VISITOR_LOCKED","mobile":"","email":"","gameBalance":"0"}}
			JSONObject json = JSONObject.fromObject(gcUserInfo);
			id = json.getString("BriefUser");
			if (id != null)
			{
				json = JSONObject.fromObject(id);
				id = json.getString("id");
			}
			else
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
	}*/
	
	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		try
		{
			StringBuffer baseStr = new StringBuffer();
			baseStr.append("oauthConsumerKey=").append(URLEncoder.encode(Util.getOppoKey(vo.getAppid()),"UTF-8")).append("&");
			baseStr.append("oauthToken=").append(URLEncoder.encode(vo.getToken(),"UTF-8")).append("&");
			baseStr.append("oauthSignatureMethod=").append(URLEncoder.encode("HMAC-SHA1","UTF-8")).append("&");
			baseStr.append("oauthTimestamp=").append(URLEncoder.encode(new Date().getTime()+"","UTF-8")).append("&");
			baseStr.append("oauthNonce=").append(URLEncoder.encode(new Random(100).nextInt()+"","UTF-8")).append("&");
			baseStr.append("oauthVersion=").append(URLEncoder.encode("1.0","UTF-8")).append("&");
			String url="fileId="+URLEncoder.encode(vo.getExinfo(),"UTF-8")+"&token="+URLEncoder.encode(vo.getToken(),"UTF-8");
			//ChannelVerify.GenerateLog("oppo send url ->" + url);
			String gcUserInfo = COppoVerify.sendToken(url.toString(),baseStr.toString(),generateSign(baseStr.toString(),vo.getAppid()));
			ChannelVerify.GenerateLog("oppo get user info ->" + gcUserInfo);

//			{"BriefUser" : {"id":"49016734","sex":"true","profilePictureUrl":"http://gcfs.nearme.com.cn/avatar/940/959/11dcee105ee94e10845dd1d386cd810c.jpg","name":"winnn","userName":"winnn","emailStatus":"false","mobileStatus":"false","status":"VISITOR_LOCKED","mobile":"","email":"","gameBalance":"0"}}
			JSONObject json = JSONObject.fromObject(gcUserInfo);
			id = json.getString("BriefUser");
			String resultCode = json.getString("resultCode");
			String ssoid = json.getString("ssoid");
			if ("200".equals(resultCode) && vo.getExinfo().equals(ssoid))
			{
				//json = JSONObject.fromObject(id);
				//id = json.getString("id");
				id = ssoid;
				
			}
			else
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
	
	public static String sendToken(String urls,String param,String oauthSignature){
		String res = "";
		try {
		URL url = new URL("http://i.open.game.oppomobile.com/gameopen/user/fileIdInfo?"+urls);
		
		HttpURLConnection connection = (HttpURLConnection) url.openConnection();
		connection.setUseCaches(false);
		connection.setDoInput(true);
		connection.setRequestProperty("Connection", "Keep-Alive");
		connection.setRequestMethod("GET");
		connection.setRequestProperty("param", param);
		connection.setRequestProperty("oauthSignature", oauthSignature);
		connection.connect();
		
		BufferedReader in = new BufferedReader
			(
				new InputStreamReader(connection.getInputStream())
			);
		String line = null;
		while ((line = in.readLine()) != null)
		{
			res += line;
		}
		connection.disconnect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
	}
	
	public static String generateSign(String baseStr,String appid){
		 byte[] byteHMAC = null;
		try {
		Mac mac = Mac. getInstance("HmacSHA1");
		SecretKeySpec spec = null;
		//String oauthSignatureKey =Util.getOppoSecretKey(appid) + "&";
		String oauthSignatureKey ="934621f96651b91dc1Af02246F3A86cE" + "&";
		spec = new SecretKeySpec(oauthSignatureKey.getBytes(), "HmacSHA1");
		mac.init(spec);
		byteHMAC = mac.doFinal(baseStr.getBytes());
		} catch (InvalidKeyException e) {
		e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
		e.printStackTrace();
		}
		try {
			return URLEncoder.encode(String. valueOf(base64Encode(byteHMAC) ) ,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
		}
	
	public static char[] base64Encode(byte[] data) {
		final char[] alphabet =
		"ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/="
		.toCharArray();
		char[] out = new char[((data.length + 2) / 3) * 4];
		for (int i = 0, index = 0; i < data.length; i += 3, index += 4) {
		boolean quad = false;
		boolean trip = false;
		int val = (0xFF & (int) data[i]);
		val <<= 8;
		if ((i + 1) < data.length) {
		val |= (0xFF & (int) data[i + 1]);
		trip = true;
		}
		val <<= 8;
		if ((i + 2) < data.length) {
		val |= (0xFF & (int) data[i + 2]);
		quad = true;
		}
		out[index + 3] = alphabet[(quad ? (val & 0x3F) : 64)];
		val >>= 6;
		out[index + 2] = alphabet[(trip ? (val & 0x3F) : 64)];
		val >>= 6;
		out[index + 1] = alphabet[val & 0x3F];
		val >>= 6;
		out[index + 0] = alphabet[val & 0x3F];
		}
		return out;
		}
	/*public static void main(String[] args){
		String ssoid = "84600982";
		String token= "TOKEN_gpsYpg5LSZFMc7wNIESECKg348Dzz5capQnbktPxgDvGCqFfeR72Lg==";
		String url = "";
		StringBuffer baseStr = new StringBuffer();
		try {
		baseStr.append("oauthConsumerKey=").append(URLEncoder.encode("77xRCa4uw7404sK8CsS4ooG4S","UTF-8")).append("&");
		baseStr.append("oauthToken=").append(URLEncoder.encode(token,"UTF-8")).append("&");
		baseStr.append("oauthSignatureMethod=").append(URLEncoder.encode("HMAC-SHA1","UTF-8")).append("&");
		baseStr.append("oauthTimestamp=").append(URLEncoder.encode(new Date().getTime()+"","UTF-8")).append("&");
		baseStr.append("oauthNonce=").append(URLEncoder.encode(new Random(100).nextInt()+"","UTF-8")).append("&");
		baseStr.append("oauthVersion=").append(URLEncoder.encode("1.0","UTF-8")).append("&");
			url = "fileId="+URLEncoder.encode(ssoid,"UTF-8")+"&token="+URLEncoder.encode(token,"UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.out.println(url);
		String gcUserInfo = COppoVerify.sendToken(url.toString(),baseStr.toString(),generateSign(baseStr.toString(),"3862"));
		System.out.println(gcUserInfo);
	}*/
}

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import sun.misc.BASE64Encoder;

public class CKingnetVerify
{
	private static String KEY = "18";
	private static String PKEY = "kingnet_mobile_" + KEY;
	
	public static String verify(int model, ChannelInfoVO vo)
	{
		String id = getIdFrom(vo.getUid(), vo.getToken(), vo.getExinfo());
		return id;
	}

	private static String getIdFrom(String account, String password, String logintype)
	{
		String id = "";
		String urlstr = "http://tw.kingnet.com/";
		String params = "";
		String minwen = "";

		try
		{
			if (logintype.equals("kingnet"))
			{
				minwen += "a=login";
				params += "a=login";
				minwen += "&account=" + account;
				params += "&account=" + URLEncoder.encode(account, "utf8");
				minwen += "&c=mobileauth";
				params += "&c=mobileauth";
				
				BASE64Encoder base64Encoder = new BASE64Encoder();
				password = base64Encoder.encode(password.getBytes());
				
				minwen += "&password=" + password;
				params += "&password=" + URLEncoder.encode(password, "utf8");
				minwen += "&pk=" + KEY;
				params += "&pk=" + KEY;
			}
			else if (logintype.equals("facebook"))
			{
				String fbid = CFacebookVerify.getIdFrom(password);
				minwen += "a=thirdLogin";
				params += "a=thirdLogin";
				minwen += "&account=" + fbid;
				params += "&account=" + URLEncoder.encode(fbid, "utf8");
				minwen += "&c=mobileauth";
				params += "&c=mobileauth";
				minwen += "&login_type=3";
				params += "&login_type=3";
				minwen += "&pk=" + KEY;
				params += "&pk=" + KEY;
			}
			else if (logintype.equals("register"))
			{
				minwen += "a=register";
				params += "a=register";
				minwen += "&account=" + account;
				params += "&account=" + URLEncoder.encode(account, "utf8");
				minwen += "&c=mobileauth";
				params += "&c=mobileauth";
				
				BASE64Encoder base64Encoder = new BASE64Encoder();
				password = base64Encoder.encode(password.getBytes());
				
				minwen += "&password=" + password;
				params += "&password=" + URLEncoder.encode(password, "utf8");
				minwen += "&pk=" + KEY;
				params += "&pk=" + KEY;
			}
			params += "&noaes=1";
			params += "&sig=" + URLEncoder.encode(getSign(minwen, PKEY), "utf8");
			urlstr += "?" + params;
			
			ChannelVerify.GenerateLog("kingnet get user info url ->" + urlstr);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			outs.flush();
			outs.close();
			
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
	
			ChannelVerify.GenerateLog("kingnet get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("is_success").equals("1"))
			{
				id = json.getString("msg");
				json = JSONObject.fromObject(id);
				id = json.getString("uid");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}

	private static String getSign(String params, String key)
	{
		params = key + params;
		String sign = StringEncrypt.Encrypt(params);
		BASE64Encoder base64Encoder = new BASE64Encoder();
		sign = base64Encoder.encode(sign.getBytes());
		
		return sign;
	}
}

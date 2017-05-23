package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;

public class CMobojoyVerify
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
			vo.setUid(json.getString("uin"));
			vo.setToken(json.getString("sessionid"));
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
			String appid = vo.getAppid();
			String sessionid = vo.getToken();
			String uid = vo.getUid();
			String minwen = "";
			String sign = "";
			String content = "";
			//Act=3&AppId=9&SessionId=d 891b6f03f361128b10c69d440c92c 34&Uin=1326&Version=1.07a1234 56789b123456789c123456789d1
			minwen += "Act=3";
			minwen += "&AppId=" + appid;
			minwen += "&SessionId=" + sessionid;
			minwen += "&Uin=" + uid;
			minwen += "&Version=1.07";
			content = minwen;
			minwen += "537fa1a83c58e003bf252f83478cb828";
			sign = StringEncrypt.Encrypt(minwen);
			content += "&Sign=" + sign;
			
			String urlstr = "http://api.sdk.moborobo.com/phone/index.php/DeveloperServer/Index";
			
			ChannelVerify.GenerateLog("mobojoy get user info url ->" + urlstr);
//			ChannelVerify.GenerateLog("mobojoy get user info minwen ->" + minwen);
			ChannelVerify.GenerateLog("mobojoy get user info content ->" + content);
	
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setRequestProperty("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.connect();
			
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			outs.write(content);
			
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
	
			ChannelVerify.GenerateLog("mobojoy get user info ret ->" + res);
	
			//{"Error_Code":1,"Sign":"34075e646605de6d9c2b695e3184771b"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("Error_Code");
			if (id.equals("0"))
			{
				id = uid;
			}
			else
			{
				id = "";
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class C91Verify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
//		String id = getIdFrom(vo.getUid(), vo.getAppid(), vo.getToken());
//		return id;
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
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("token"));
			vo.setExinfo("");
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String uid = vo.getUid();
		String appid = vo.getAppid();
		String token = vo.getToken();
		String id = "";
		
		String AppID = appid;
		String Act = "4";
		String Uin = uid;
		String SessionId = token;
		String AppKey = Util.getNdpayKey(appid);
		
		try
		{
			StringBuilder strSign = new StringBuilder();
			strSign.append(AppID);
			strSign.append(Act);
			strSign.append(Uin);
			strSign.append(SessionId);
			strSign.append(AppKey);
	
			String miwen = StringEncrypt.Encrypt(strSign.toString()).toLowerCase();
			
			StringBuilder params = new StringBuilder();
			params.append("AppId=");
			params.append(AppID);
			params.append("&Act=");
			params.append(Act);
			params.append("&Uin=");
			params.append(Uin);
			params.append("&Sign=");
			params.append(miwen);
			params.append("&SessionId=");
			params.append(SessionId);
	
			String urlstr = "http://service.sj.91.com/usercenter/AP.aspx?";
			urlstr += params.toString();
			ChannelVerify.GenerateLog("91 get user info url ->" + urlstr);
	
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
	
			JSONObject json = JSONObject.fromObject(res);
			if (json.getString("ErrorCode").equals("1"))
			{
				id = Uin;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
}

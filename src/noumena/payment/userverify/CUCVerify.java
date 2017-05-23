package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CUCVerify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
//		String id = getIdFrom(vo.getAppid(), vo.getToken());
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
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("sessionid"));
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String uid = "";
		String token = vo.getToken(), appid = vo.getAppid();
		
		//true - 新游戏调新接口， false - 旧游戏调旧接口
		boolean flag = true;
		try
		{
			String[] gameids = {"517617","515118","536301","537853","538115","544780","543418","549726","550081","550823","551089"};
			for (int i = 0; i < gameids.length; i++) 
			{
				if (appid.equals(gameids[i])) 
				{
					flag = false;
				}
			}
			
			String urlstr = "";
			String id = System.currentTimeMillis() + "";
			String service = "";
			String data = "{\"sid\":\"" + token + "\"}";
			String game = "";
			String minwen = "";
			if (flag) 
			{
				urlstr = "http://sdk.g.uc.cn/cp/account.verifySession";
				game = "{\"gameId\":\""+appid+"\"}";
				minwen = "sid=" + token + Util.getUCKey(appid);
			}
			else
			{
				urlstr = "http://sdk.g.uc.cn/ss/";
				service = "\"service\":\"ucid.user.sidInfo\",";
				game = "{\"cpId\":" + Util.getUCCpid(appid) + ",\"gameId\":" + appid + ",\"channelId\":\"" + "" + "\",\"serverId\":" + Util.getUCSid(appid) + "}";
				minwen = Util.getUCCpid(appid) + "sid=" + token + Util.getUCKey(appid);
			}
			String miwen = StringEncrypt.Encrypt(minwen);
			String body = "{";
			body += "\"id\":" + id + ",";
			body += service;
			body += "\"data\":" + data + ",";
			body += "\"game\":" + game + ",";
			body += "\"sign\":\"" + miwen + "\"";
			body += "}";
			
			ChannelVerify.GenerateLog("uc get user info urlstr ->" + urlstr);
			ChannelVerify.GenerateLog("uc get user info body ->" + body);
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			outs.flush();
			outs.write(body);
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
	
			ChannelVerify.GenerateLog("uc get user info ret ->" + res);
			
			JSONObject json = JSONObject.fromObject(res);
			String retstate = json.getString("state");
			String retdata = json.getString("data");
			json = JSONObject.fromObject(retstate);
			String retcode = json.getString("code");
			if (retcode.equals("1"))
			{
				json = JSONObject.fromObject(retdata);
				if (flag) 
				{
					uid = json.getString("accountId");
				}
				else
				{
					uid = json.getString("ucid");
				}
				
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return uid;
	}
}

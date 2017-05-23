package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CYouxinVerify
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
			vo.setUid(json.getString("openid"));
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
			String minwen = "";
			String miwen = "";
			minwen += vo.getAppid();
			minwen += vo.getUid();
			minwen += Util.getYouxinKey(vo.getAppid());
			miwen = StringEncrypt.Encrypt(minwen);
			
			String urlstr = "http://gos.uxin.com/gameopt/gameuser/checkOpenid.do";
			urlstr += "?appid=" + vo.getAppid();
			urlstr += "&openid=" + vo.getUid();
			urlstr += "&sign=" + miwen;
			
			ChannelVerify.GenerateLog("youxin get user info url ->" + urlstr);

			HttpURLConnection connection = (HttpURLConnection) new URL(urlstr).openConnection();
//			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
//			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("GET");
			connection.connect();
			
//			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
//			outs.write(content);
			
//			outs.flush();
//			outs.close();
			
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
	
			ChannelVerify.GenerateLog("youxin get user info ret ->" + res);
//			{"result":"0"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("result");
			if (id != null && id.equals("0"))
			{
				id = vo.getUid();
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
}

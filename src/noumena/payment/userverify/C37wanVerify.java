package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class C37wanVerify
{
	private static String PARTNERID = "1";
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
			vo.setAppid(json.getString("gameid"));
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
			String time = (System.currentTimeMillis() / 1000) + "";
			String minwen = "";
			minwen += vo.getAppid();
			minwen += PARTNERID;
			minwen += vo.getToken();
			minwen += time;
			minwen += Util.get37wanKey(vo.getAppid());
			String miwen = "";
			miwen = StringEncrypt.Encrypt(minwen);
			
			String urlstr = "http://sy.api.37wan.cn/sdk/checklogin/";
			urlstr += "?gameid=" + vo.getAppid();
			urlstr += "&partner=" + PARTNERID;
			urlstr += "&sessionid=" + vo.getToken();
			urlstr += "&time=" + time;
			urlstr += "&sign=" + miwen.toLowerCase();
			
			ChannelVerify.GenerateLog("37wan get user info url ->" + urlstr);
	
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
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
					new InputStreamReader(connection.getInputStream())
				);
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
	
			connection.disconnect();
	
			ChannelVerify.GenerateLog("37wan get user info ret ->" + res);
//			{"code":1,"data":{"uid":"123456789","passport":"apollosaar045","partner":1,"referer":"37wan","gameid":"1000001"},"msg":"成功"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("code");
			if (id.equals("1"))
			{
				id = json.getString("data");
				json = JSONObject.fromObject(id);
				id = json.getString("uid");
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

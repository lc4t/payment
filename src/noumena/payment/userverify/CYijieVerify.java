package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;

public class CYijieVerify
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
			//json内容： {"type":"yijie","uin":"","app":"","sess":"","sdk":"","mark":"zijian"}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("uin"));
			vo.setAppid(json.getString("app"));
			vo.setToken(json.getString("sess"));
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
			JSONObject json = JSONObject.fromObject(vo.getExinfo());
			String sdk = json.getString("sdk");
			String uin = URLEncoder.encode(vo.getUid(),"UTF-8");
			String sess = URLEncoder.encode(vo.getToken(),"UTF-8");
			String urlstr = "";
			if (json.containsKey("mark")) 
			{
				urlstr = "http://yjserver.ko.cn:8080/yjcpsrv/user/%s/loginCheck.html?uin=%s&app=%s&sess=%s&sdk=%s";
				urlstr = String.format(urlstr, sdk, uin, vo.getAppid(), sess, sdk);
			}
			else
			{
				urlstr = "http://sync.1sdk.cn/login/check.html?uin=%s&app=%s&sess=%s&sdk=%s";
				urlstr = String.format(urlstr, uin, vo.getAppid(), sess, sdk);
			}
			ChannelVerify.GenerateLog("yijie get user info url ->" + urlstr);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(false);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("GET");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "UTF-8");
			
			connection.connect();
			
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
			
			ChannelVerify.GenerateLog("yijie get user info ret ->" + res);
			
			if (res.equals("0")) {
				id = vo.getUid();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
}

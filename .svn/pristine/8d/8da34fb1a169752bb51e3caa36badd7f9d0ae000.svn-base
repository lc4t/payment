package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Calendar;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CMeizuVerify
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
			vo.setAppid(json.getString("gid"));
			vo.setToken(json.getString("token"));
			vo.setExinfo(json.getString("pid"));
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
			int ts = (int)(Calendar.getInstance().getTimeInMillis()/1000);
			String content = "";
			String sign = "";
			
			content = vo.getAppid()+ts+Util.getMeizuKey(vo.getAppid());
			sign = StringEncrypt.Encrypt(content).toLowerCase();
			
			String urlParameters = "";
			urlParameters = "pid="+vo.getExinfo();
			urlParameters += "&gid="+vo.getAppid();
			urlParameters += "&time="+ts;
			urlParameters += "&sign="+sign;
			urlParameters += "&token="+URLEncoder.encode(vo.getToken(),"utf-8");
			
			String urlstr = "http://vt.api.m.37.com/verify/token/";
			
			ChannelVerify.GenerateLog("meizu get user info urlParameters ->" + urlParameters);
			
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();
			
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
			
			ChannelVerify.GenerateLog("meizu get user info ret ->" + res);
			
//			{"state":"1" ,"data":"","msg":"成功"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("state");
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

package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CDuokuVerify
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
			vo.setUid(json.getString("uid"));
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("accesstoken"));
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
			String uid = vo.getUid();
			String accesstoken = URLDecoder.decode(vo.getToken(), "utf-8");
			
			String minwen = "";
			minwen = vo.getAppid();
			minwen += accesstoken;
			minwen += Util.getDuokuKey(vo.getAppid());
			String miwen = StringEncrypt.Encrypt(minwen).toLowerCase();
			
			String urlstr = "http://querysdkapi.91.com/LoginStateQuery.ashx";
			String urlParameters ="AppID="+vo.getAppid()+"&AccessToken="+accesstoken+"&Sign="+miwen;
			
			ChannelVerify.GenerateLog("duoku get user info urlParameters ->" + urlParameters);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			
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

			ChannelVerify.GenerateLog("duoku get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			//{ ResultCode: "1" , ResultMsg :" " }
			if (json.getString("ResultCode").equals("1"))
			{
				id = uid;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return id;
	}
	
}

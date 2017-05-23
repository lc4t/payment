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

public class CLinyouVerify
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
			vo.setAppid(json.getString("gamekey"));
			vo.setToken(json.getString("token"));
			vo.setExinfo(json.getString("cp"));
			
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
			String ts = (Calendar.getInstance().getTimeInMillis() / 1000)+"";
			
			String minwen = "";
			String miwen = "";
			minwen  = "cp=";
			minwen += URLEncoder.encode(vo.getExinfo(),"utf-8");
			minwen += "&gamekey=";
			minwen += URLEncoder.encode(vo.getAppid(),"utf-8");
			minwen += "&timestamp=";
			minwen += URLEncoder.encode(ts,"utf-8");
			minwen += "&token=";
			minwen += URLEncoder.encode(vo.getToken(),"utf-8");
			
			miwen = StringEncrypt.Encrypt(minwen);
			miwen = StringEncrypt.Encrypt(miwen+Util.getLinyouKey(vo.getAppid()));
			
			String urlstr = "http://anyapi.mobile.youxigongchang.com/foreign/oauth/verification.php";
			String urlParameters ="token="+vo.getToken()+"&cp="+vo.getExinfo()+"&timestamp="+ts+"&gamekey="+vo.getAppid()+"&_sign="+miwen;
			
			ChannelVerify.GenerateLog("linyou get user info urlParameters ->" + urlParameters);

			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setUseCaches(false);
			connection.setRequestMethod("POST"); 
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

			ChannelVerify.GenerateLog("linyou get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			//{“result”:“0”,”result_desc”:“ok”}
			if (json.getString("result").equals("0"))
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

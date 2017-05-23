package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CKudongVerify
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
			vo.setToken(json.getString("token"));
			vo.setUid(json.getString("uid"));
			vo.setExinfo(json.getString("env"));
			
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
			String urlstr = "";
			if (vo.getExinfo().equals("test"))
			{
				urlstr = "http://test.17mogu.com:8080/mogoo2/gameUser/checkToken.do";
			}
			else
			{
				urlstr = "http://app.17mogu.com:8080/mogoo2/gameUser/checkToken.do";
			}
			
			ChannelVerify.GenerateLog("kudong get user info url ->" + urlstr);

			HttpURLConnection connection = (HttpURLConnection) new URL(urlstr).openConnection();
//			connection.setDoOutput(true);
			connection.setUseCaches(false);
			connection.setDoInput(true);
//			connection.setRequestProperty("Content-type", "application/json");
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestProperty("Cookie", "JSESSIONID=" + vo.getToken());
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
	
			ChannelVerify.GenerateLog("kudong get user info ret ->" + res);
//			{"head":null,"body":null,"res_info":{"response_code":"000","response_msg":"该用户已经登陆!"}}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("res_info");
			if (id != null)
			{
				json = JSONObject.fromObject(id);
				id = json.getString("response_code");
				if (id != null && id.equals("000"))
				{
					id = vo.getUid();
				}
				else
				{
					id = "";
				}
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

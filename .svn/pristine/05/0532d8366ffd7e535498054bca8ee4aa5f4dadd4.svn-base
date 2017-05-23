package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;

public class CCMCCVerify {
	public static String verify(int model, ChannelInfoVO vo) {
		String ret = "";
		switch (model) {
		case 0:
			// 正常参数验证，返回合法id
			ret = getIdFrom(vo);
			break;
		case 1:
			// 正常参数验证，返回json格式状态
			ret = getIdFrom(vo);
			break;
		case 2:
			// json参数验证，返回合法id
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setToken(json.getString("token"));

			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";
		String urlstr = "http://read.wap777.net:5080/examples/test2.jsp?cpparam=" + vo.getToken();
		ChannelVerify.GenerateLog("cmcc get user info url ->"+ urlstr);
		try
		{
			int c = 2;
			
			while (c > 0)
			{
				URL url = new URL(urlstr);
				HttpURLConnection connection = (HttpURLConnection) url.openConnection();
				
	//			connection.setDoOutput(true);
				connection.setDoInput(true);
				connection.setInstanceFollowRedirects(false);
				connection.setUseCaches(false);
				connection.setRequestProperty("Connection", "Keep-Alive");
				connection.setRequestMethod("GET");
				connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
				connection.setRequestProperty("charset", "utf-8");
				
				connection.connect();
				
	//			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
	//			wr.writeBytes("");
	//			wr.flush();
	//			wr.close();
	
				BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
				String res = "", line = null;
				while ((line = in.readLine()) != null) {
					res += line;
				}
	
				connection.disconnect();
	
				ChannelVerify.GenerateLog("cmcc get user info(" + (2 - c + 1) + ") res ->" + res);
				//12345678|null|null|null
				String[] tmp = res.split("\\|");
				if (tmp.length > 0)
				{
					id = tmp[0];
					if (id.equals("") || id.equals("null"))
					{
						id = "";
					}
					else
					{
						break;
					}
				}
				
				c--;
				Thread.sleep(2 * 1000);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}
}

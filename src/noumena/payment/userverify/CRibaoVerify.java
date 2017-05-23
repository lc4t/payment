package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.UnicodeDecoder;

public class CRibaoVerify {
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
			//json内容： {"type":"ribao","uid":"","appid":"","token":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setAppid(json.getString("appid"));
			vo.setUid(json.getString("uid"));
			vo.setToken(json.getString("token"));
			
			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}
	public static void main(String[] args) {
		ChannelInfoVO vo = new ChannelInfoVO();
		vo.setAppid("1898");
		vo.setToken("43b884bfb856f1f3bf750cda358b539a");
		System.out.println(getIdFrom(vo));
	}
	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = vo.getUid();
		try
		{
			String urlstr = "http://sdk.game.kugou.com/index.php?r=ValidateIsLogined/CheckToken";
			String urlParameters = "&token="+URLEncoder.encode(vo.getToken(),"utf-8");
			
			ChannelVerify.GenerateLog("ribao get user info urlParameters ->" + urlParameters);
			
			URL url = new URL(urlstr + urlParameters);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(connection.getOutputStream());
			
			outs.flush();
			outs.close();
			
			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
			String res = "", line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			res = UnicodeDecoder.decode(res);
			connection.disconnect();
			
			ChannelVerify.GenerateLog("ribao get user info ret ->" + res);
			
//			eg:{"response":{"code":"0","message":"Success","message_cn":"token合法","prompt":"token合法."}}
			JSONObject json = JSONObject.fromObject(res);
			String code = JSONObject.fromObject(json.getString("response")).getString("code");
			if (!code.equals("0"))
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

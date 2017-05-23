package noumena.payment.userverify;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class COMGVerify {
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
			vo.setUid(json.getString("uid"));
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("token"));

			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}

	private static String getIdFrom(ChannelInfoVO vo) {
		String id = "";
		int ts = (int)(Calendar.getInstance().getTimeInMillis() / 1000);

		String hash = "{\"gamebarid\":\"" + vo.getAppid() + "\",\"aid\":\""+ vo.getUid() + "\",\"ts\":\"" + ts + "\"}";
		hash = StringEncrypt.encryptAESIV(hash, Util.getOMGKey(vo.getAppid()),Util.getOMGIVKey(vo.getAppid()));

		// 正式地址
		 String urlstr ="http://member.app-master.com.tw/gamebarV2/api/authaccount.ashx";
		// 测试地址
//		String urlstr = "http://vendortest.app-master.com.tw/gamebarV2/api/authaccount.ashx";

		String urlParameters = "gamebarid=" + vo.getAppid() + "&auth="+ vo.getToken() + "&hash=" + hash;
		ChannelVerify.GenerateLog("omg get user info urlParameters ->"+ urlParameters);
		try {
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();
			
			connection.setDoOutput(true);
			connection.setDoInput(true);
			connection.setInstanceFollowRedirects(false);
			connection.setUseCaches(false);
			connection.setRequestProperty("Connection", "Keep-Alive");
			connection.setRequestMethod("POST");
			connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded"); 
			connection.setRequestProperty("charset", "utf-8");
			connection.setRequestProperty("Content-Length", "" + Integer.toString(urlParameters.getBytes().length));
			
			connection.connect();
			
			DataOutputStream wr = new DataOutputStream(connection.getOutputStream ());
			wr.writeBytes(urlParameters);
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null) {
				res += line;
			}

			connection.disconnect();

			ChannelVerify.GenerateLog("omg get user info res ->" + res);
			// {"loginstatus":"1" ,"msg":"成功"}
			JSONObject json = JSONObject.fromObject(res);
			id = json.getString("loginstatus");
			if (id.equals("1")) {
				id = vo.getUid();
			} else {
				id = "";
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return id;
	}
}

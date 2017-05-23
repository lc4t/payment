package noumena.payment.userverify;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CPPSVerify
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
			vo.setAppid(json.getString("app_id"));
			vo.setUid(json.getString("uid"));
			vo.setToken(json.getString("sign"));
			vo.setExinfo(json.getString("timestamp"));
			
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
			String appid = vo.getAppid();
			String uid = vo.getUid();
			String sign = vo.getToken();
			String timestamp = vo.getExinfo();
			String key = Util.getPPSKey(appid);
			String minwen = uid + "&" + timestamp + "&" + key;
			String miwen = StringEncrypt.Encrypt(minwen);

			ChannelVerify.GenerateLog("pps get user info minwen ->" + minwen + "&" + sign);
			ChannelVerify.GenerateLog("pps get user info kongzhong sign ->" + miwen);
			
			if (miwen.equals(sign))
			{
				id = uid;
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

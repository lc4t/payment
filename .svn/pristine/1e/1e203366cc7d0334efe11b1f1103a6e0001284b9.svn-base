package noumena.payment.userverify;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CLiebaoVerify
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
			//json内容： {"type":"liebao","username":"","appid":"","sign":"","logintime":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setUid(json.getString("username"));
			vo.setAppid(json.getString("appid"));
			vo.setToken(json.getString("sign"));
			vo.setExinfo(json.getString("logintime"));
			
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
			String sign = vo.getToken();
			String minwen = "";
			String miwen = "";
			
			minwen += "username=" + uid;
			minwen += "&appkey=" + Util.getLiebaoKey(vo.getAppid());
			minwen += "&logintime=" + vo.getExinfo();
			
			miwen = StringEncrypt.Encrypt(minwen);
			
			if (miwen.equals(sign)) 
			{
				id = uid;
			}
			else
			{
				ChannelVerify.GenerateLog("liebao get user info ->(appid:" + vo.getAppid() + "),(content:" + minwen + "),(sign:" + miwen +")");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
}

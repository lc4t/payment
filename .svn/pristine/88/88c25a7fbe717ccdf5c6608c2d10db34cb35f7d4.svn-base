package noumena.payment.userverify;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CEfunVerify
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
			//json内容： {"type":"efun","appid":"","userid":"","timestamp":"","sign":""}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setAppid(json.getString("appid"));
			vo.setUid(json.getString("userid"));
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
			String uid = vo.getUid();
			String sign = vo.getToken();
			String minwen = "";
			String miwen = "";
			
			minwen += Util.getEfunKey(vo.getAppid());
			minwen += uid;
			minwen += vo.getExinfo();
			
			miwen = StringEncrypt.Encrypt(minwen).toUpperCase();
			
			if (miwen.equals(sign)) 
			{
				id = uid;
			}
			else
			{
				ChannelVerify.GenerateLog("efun get user info ->(appid:" + vo.getAppid() + "),(content:" + minwen + "),(sign:" + miwen +")");
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return id;
	}
}

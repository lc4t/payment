package noumena.payment.userverify;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CXingshangVerify
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
			//json内容： {"type":"xingshang","username":"","appid":"","sign":"","logintime":""}
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
		try {
			String content = "";
			content += "username="+vo.getUid();
			content += "&appkey="+Util.getXingshangKey(vo.getAppid());
			content += "&logintime="+vo.getExinfo();
			String sign = StringEncrypt.Encrypt(content);
			if (sign.equals(vo.getToken()))
			{
				id = vo.getUid();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return id;
	}
}

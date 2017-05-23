package noumena.payment.userverify;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;
import noumena.payment.userverify.util.Util;

public class CIGSVerify
{
	public static String verify(int model, ChannelInfoVO vo)
	{
		String id = getIdFrom(vo);
		return id;
	}

	private static String getIdFrom(ChannelInfoVO vo)
	{
		String token = vo.getToken();
		String appid = vo.getAppid();
		String id = "";
		
		try
		{
			String jsondata = "";
			String igssign = "";
			int pos = 0;
			pos = token.lastIndexOf(",");
			if (pos >= 0)
			{
				jsondata = token.substring(0, pos);
				igssign = token.substring(pos + 1);
			}
			String minwen = "";
			String sign = "";
			
			minwen = jsondata + Util.getIGSKey(appid);
			sign = StringEncrypt.sha(minwen);
			
			pos = sign.length();
			String stre = sign.substring(pos - 1, pos);
			int count = 0;
			while (stre.equals("=") && count < 5)
			{
				sign = sign.substring(0, pos - 1);
				pos = sign.length();
				stre = sign.substring(pos - 1, pos);
				count++;
			}
			
			ChannelVerify.GenerateLog("igs minwen ->" + minwen);
			ChannelVerify.GenerateLog("igs kong sign ->" + sign);
			ChannelVerify.GenerateLog("igs sign ->" + igssign);

			JSONObject json = JSONObject.fromObject(jsondata);
			if (sign.equals(igssign))
			{
				id = json.getString("AccountID");
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

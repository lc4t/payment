package noumena.payment.userverify;

import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.StringEncrypt;

import org.codehaus.xfire.client.Client;

public class CKongzhongVerify
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
			//json内容： {"type":"kongzhong","token":"","sdk":"new"}
			String info = vo.getExinfo();
			JSONObject json = JSONObject.fromObject(info);
			vo.setToken(json.getString("token"));
			String sdk = "";
			try
			{
				sdk = json.getString("sdk");
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
			vo.setAppid(sdk);

			ret = getIdFrom(vo);
			break;
		}
		return ret;
	}
public static void main(String[] args) {
	ChannelInfoVO vo = new ChannelInfoVO();
	vo.setToken("bd1903bc-6cf8-4c36-98b7-e59772b4c136");
	vo.setAppid("new");
	System.out.println(CKongzhongVerify.getIdFrom(vo));
}
	private static String getIdFrom(ChannelInfoVO vo)
	{
		String id = "";

		try
		{
			//sdk内容如果为new是新版空中用户中心验证，超神之后的游戏都是用的新版，公主、狂斩用的是旧版
			String wsUrl = "";
			String key = "";
			
			if (vo.getAppid().equals("new"))
			{
//				wsUrl = "http://webservice-test.kong.net/service/WSCommon?WSDL";
				wsUrl = "http://webservice.kong.net/service/WSCommon?WSDL";
				key = "Kongz3dkeyCWz@KZC3";
				
				String sign = vo.getToken();
				sign = StringEncrypt.Encrypt(sign).toLowerCase();
				sign = sign + key;
				sign = StringEncrypt.Encrypt(sign).toLowerCase();

				ChannelVerify.GenerateLog("kongzhong get user url ->" + wsUrl);
				ChannelVerify.GenerateLog("kongzhong get user content ->(" + vo.getToken() + ")(" + sign + ")");
				
				Client client = new Client(new URL(wsUrl));
				Object[] ret = client.invoke("getRoleId", new Object[] { vo.getToken(), sign });
				
				if (ret != null && ret.length > 0)
				{
					ChannelVerify.GenerateLog("kongzhong get user info ret ->" + ret.length + "-" + ret[0]);
					//{"code":0,"msg":"790787"}
					JSONObject json = JSONObject.fromObject(ret[0]);
					if (json.getString("code").equals("0"))
					{
						id = json.getString("msg");
					}
				}
				else
				{
					ChannelVerify.GenerateLog("kongzhong get user info ret -> null");
				}
			}
			else
			{
//				String wsUrl = "http://webservice-stage.kongzhong.com/PassportWebService/services/CommonWS?wsdl";
				wsUrl = "http://webservice.kongzhong.com/PassportWebService/services/CommonWS?wsdl";
				key = "key*commonws";
				
				int areaId = 0;
				String roleId = "0";
				
				String sign = areaId + roleId + vo.getToken();
				sign = StringEncrypt.Encrypt(sign).toLowerCase();
				sign = sign + key;
				sign = StringEncrypt.Encrypt(sign).toLowerCase();

				ChannelVerify.GenerateLog("kongzhong get user url ->" + wsUrl);
				ChannelVerify.GenerateLog("kongzhong get user content ->(" + vo.getToken() + ")(" + sign + ")");
				
				Client client = new Client(new URL(wsUrl));
				Object[] ret = client.invoke("getRoleId", new Object[] { areaId, roleId, vo.getToken(), sign });
				
				if (ret != null && ret.length > 0)
				{
					ChannelVerify.GenerateLog("kongzhong get user info ret ->" + ret.length + "-" + ret[0]);
					//{"code":0,"msg":"790787"}
					JSONObject json = JSONObject.fromObject(ret[0]);
					if (json.getString("code").equals("0"))
					{
						id = json.getString("msg");
					}
				}
				else
				{
					ChannelVerify.GenerateLog("kongzhong get user info ret -> null");
				}
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return id;
	}
}

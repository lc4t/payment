package noumena.payment.c49you;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class C49youParams extends ChannelParams
{
	public static String CHANNEL_ID		= "49you";
	
	public C49youParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new C49youParamsVO();
		}
		else
		{
			return (C49youParamsVO) vo;
		}
	}
	
}

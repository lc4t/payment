package noumena.payment.c921sdk;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class C921Params extends ChannelParams
{
	public static String CHANNEL_ID		= "c921";
	
	public C921VO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new C921VO();
		}
		else
		{
			return (C921VO) vo;
		}
	}
	
}

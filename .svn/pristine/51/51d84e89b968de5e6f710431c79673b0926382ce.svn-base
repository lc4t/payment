package noumena.payment.bluepay;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class BluepayParams extends ChannelParams
{
	public static String CHANNEL_ID		= "bluepay";
	
	public BluepayParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new BluepayParamsVO();
		}
		else
		{
			return (BluepayParamsVO) vo;
		}
	}
	
}

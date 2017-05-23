package noumena.payment.youwan;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class YouwanParams extends ChannelParams
{
	public static String CHANNEL_ID		= "youwan";
	
	public YouwanParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new YouwanParamsVO();
		}
		else
		{
			return (YouwanParamsVO) vo;
		}
	}
	
}

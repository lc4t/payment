package noumena.payment.now;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class NowParams extends ChannelParams
{
	public static String CHANNEL_ID		= "now";
	
	public NowParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new NowParamsVO();
		}
		else
		{
			return (NowParamsVO) vo;
		}
	}
	
}

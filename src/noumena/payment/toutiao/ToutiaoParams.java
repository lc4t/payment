package noumena.payment.toutiao;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class ToutiaoParams extends ChannelParams
{
	public static String CHANNEL_ID		= "toutiao";
	
	public ToutiaoParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new ToutiaoParamsVO();
		}
		else
		{
			return (ToutiaoParamsVO) vo;
		}
	}
	
}

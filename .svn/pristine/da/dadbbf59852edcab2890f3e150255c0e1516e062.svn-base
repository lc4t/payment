package noumena.payment.durain;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class DurainParams extends ChannelParams
{
	public static String CHANNEL_ID		= "durain";
	
	public DurainVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new DurainVO();
		}
		else
		{
			return (DurainVO) vo;
		}
	}
	
}

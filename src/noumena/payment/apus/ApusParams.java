package noumena.payment.apus;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class ApusParams extends ChannelParams
{
	public static String CHANNEL_ID		= "apus";
	
	public ApusParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new ApusParamsVO();
		}
		else
		{
			return (ApusParamsVO) vo;
		}
	}
	
}

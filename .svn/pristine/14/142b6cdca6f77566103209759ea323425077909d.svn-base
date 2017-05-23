package noumena.payment.fromest;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class FromestParams extends ChannelParams
{
	public static String CHANNEL_ID	= "fromest";
	
	public FromestParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new FromestParamsVO();
		}
		else
		{
			return (FromestParamsVO) vo;
		}
	}
	
}

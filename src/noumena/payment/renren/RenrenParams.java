package noumena.payment.renren;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class RenrenParams extends ChannelParams
{
	public static String CHANNEL_ID		= "renren";
	
	public RenrenParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new RenrenParamsVO();
		}
		else
		{
			return (RenrenParamsVO) vo;
		}
	}
	
}

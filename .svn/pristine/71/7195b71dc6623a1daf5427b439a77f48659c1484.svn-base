package noumena.payment.anqu;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class AnquParams extends ChannelParams
{
	public static String CHANNEL_ID		= "anqu";
	
	public AnquParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new AnquParamsVO();
		}
		else
		{
			return (AnquParamsVO) vo;
		}
	}
	
}

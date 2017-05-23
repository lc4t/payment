package noumena.payment.linyou;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class LinyouParams extends ChannelParams
{
	public static String CHANNEL_ID		= "linyou";
	
	public LinyouParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new LinyouParamsVO();
		}
		else
		{
			return (LinyouParamsVO) vo;
		}
	}
	
}

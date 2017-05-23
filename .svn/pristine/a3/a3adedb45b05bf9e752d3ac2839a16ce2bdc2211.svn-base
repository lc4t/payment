package noumena.payment.efun;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class EfunParams extends ChannelParams
{
	public static String CHANNEL_ID		= "efun";
	
	public EfunParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new EfunParamsVO();
		}
		else
		{
			return (EfunParamsVO) vo;
		}
	}
	
}

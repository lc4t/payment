package noumena.payment.tutu;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class TutuParams extends ChannelParams
{
	public static String CHANNEL_ID		= "tutu";
	
	public TutuParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new TutuParamsVO();
		}
		else
		{
			return (TutuParamsVO) vo;
		}
	}
	
}

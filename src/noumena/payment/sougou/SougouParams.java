package noumena.payment.sougou;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class SougouParams extends ChannelParams
{
	public static String CHANNEL_ID		= "sougou";
	
	public SougouParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new SougouParamsVO();
		}
		else
		{
			return (SougouParamsVO) vo;
		}
	}
	
}

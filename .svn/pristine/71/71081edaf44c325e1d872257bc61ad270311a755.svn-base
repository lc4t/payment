package noumena.payment.NicePlayV3;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class NicePlayParams extends ChannelParams
{
	public static String CHANNEL_ID		= "nicePlay";
	
	public NicePlayVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new NicePlayVO();
		}
		else
		{
			return (NicePlayVO) vo;
		}
	}
	
}

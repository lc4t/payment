package noumena.payment.pengyouwan;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class PengYouWanParams extends ChannelParams
{
	public static String CHANNEL_ID		= "pengyouwan";
	
	public PengYouWanVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new PengYouWanVO();
		}
		else
		{
			return (PengYouWanVO) vo;
		}
	}
	
}

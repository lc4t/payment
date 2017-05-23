package noumena.payment.haima;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class HaimaParams extends ChannelParams
{
	public static String CHANNEL_ID		= "haima";
	
	public HaimaParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new HaimaParamsVO();
		}
		else
		{
			return (HaimaParamsVO) vo;
		}
	}
	
}

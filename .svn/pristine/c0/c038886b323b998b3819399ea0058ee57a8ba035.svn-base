package noumena.payment.douyu;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class DouyuParams extends ChannelParams
{
	public static String CHANNEL_ID		= "douyu";
	
	public DouyuParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new DouyuParamsVO();
		}
		else
		{
			return (DouyuParamsVO) vo;
		}
	}
	
}

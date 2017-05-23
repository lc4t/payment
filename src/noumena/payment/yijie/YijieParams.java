package noumena.payment.yijie;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class YijieParams extends ChannelParams
{
	public static String CHANNEL_ID		= "yijie";
	
	public YijieParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new YijieParamsVO();
		}
		else
		{
			return (YijieParamsVO) vo;
		}
	}
	
}

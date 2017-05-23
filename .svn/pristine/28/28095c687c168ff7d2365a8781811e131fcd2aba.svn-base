package noumena.payment.iapppay;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class IappayParams extends ChannelParams
{
	public static String CHANNEL_ID		= "iappay";
	
	public IappayParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new IappayParamsVO();
		}
		else
		{
			return (IappayParamsVO) vo;
		}
	}
	
}

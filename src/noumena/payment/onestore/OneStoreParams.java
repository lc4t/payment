package noumena.payment.onestore;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class OneStoreParams extends ChannelParams
{
	public static String CHANNEL_ID		= "onestore";
	
	public OneStoreParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new OneStoreParamsVO();
		}
		else
		{
			return (OneStoreParamsVO) vo;
		}
	}
	
}

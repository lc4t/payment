package noumena.payment.quick;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class QuickParams extends ChannelParams
{
	public static String CHANNEL_ID		= "quick";
	
	public QuickParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new QuickParamsVO();
		}
		else
		{
			return (QuickParamsVO) vo;
		}
	}
	
}

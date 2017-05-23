package noumena.payment.gmobi;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class GmobiParams extends ChannelParams
{
	public static String CHANNEL_ID		= "gmobi";
	
	public GmobiParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new GmobiParamsVO();
		}
		else
		{
			return (GmobiParamsVO) vo;
		}
	}
	
}

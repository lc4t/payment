package noumena.payment.jusdk;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class JusdkParams extends ChannelParams
{
	public static String CHANNEL_ID		= "jusdk";
	
	public JusdkParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new JusdkParamsVO();
		}
		else
		{
			return (JusdkParamsVO) vo;
		}
	}
	
}

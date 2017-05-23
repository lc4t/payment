package noumena.payment.nduo;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class NduoParams extends ChannelParams
{
	public static String CHANNEL_ID		= "nduo";
	
	public NduoParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new NduoParamsVO();
		}
		else
		{
			return (NduoParamsVO) vo;
		}
	}
	
}

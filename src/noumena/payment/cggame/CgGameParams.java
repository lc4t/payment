package noumena.payment.cggame;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class CgGameParams extends ChannelParams
{
	public static String CHANNEL_ID		= "cggame";
	
	public CgGameVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new CgGameVO();
		}
		else
		{
			return (CgGameVO) vo;
		}
	}
	
}

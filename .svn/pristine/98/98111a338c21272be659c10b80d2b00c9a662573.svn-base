package noumena.payment.igame;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class IGameParams extends ChannelParams
{
	public static String CHANNEL_ID		= "igame";
	
	public IGameParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new IGameParamsVO();
		}
		else
		{
			return (IGameParamsVO) vo;
		}
	}
	
}

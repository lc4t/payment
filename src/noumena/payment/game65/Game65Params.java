package noumena.payment.game65;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class Game65Params extends ChannelParams
{
	public static String CHANNEL_ID		= "game65";
	
	public Game65ParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new Game65ParamsVO();
		}
		else
		{
			return (Game65ParamsVO) vo;
		}
	}
	
}

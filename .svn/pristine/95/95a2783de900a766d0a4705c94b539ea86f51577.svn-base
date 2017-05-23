package noumena.payment.lumi;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class LumiParams extends ChannelParams
{
	public static String CHANNEL_ID		= "lumi";
	
	public LumiVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new LumiVO();
		}
		else
		{
			return (LumiVO) vo;
		}
	}
	
}

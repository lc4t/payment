package noumena.payment.liebao;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class LiebaoParams extends ChannelParams
{
	public static String CHANNEL_ID		= "liebao";
	
	public LiebaoParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new LiebaoParamsVO();
		}
		else
		{
			return (LiebaoParamsVO) vo;
		}
	}
	
}

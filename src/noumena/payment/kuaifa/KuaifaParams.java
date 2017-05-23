package noumena.payment.kuaifa;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class KuaifaParams extends ChannelParams
{
	public static String CHANNEL_ID		= "kuaifa";
	
	public KuaifaParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new KuaifaParamsVO();
		}
		else
		{
			return (KuaifaParamsVO) vo;
		}
	}
	
}

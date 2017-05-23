package noumena.payment.caishen;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class CaishenParams extends ChannelParams
{
	public static String CHANNEL_ID		= "caishen";
	
	public CaishenParamsVO getParams(String appid)
	{
		System.out.println(appid);
		ChannelParamsVO vo = super.getParamsVO(appid);
		System.out.println(vo);
		if (vo == null)
		{
			return new CaishenParamsVO();
		}
		else
		{
			return (CaishenParamsVO) vo;
		}
	}
	
}

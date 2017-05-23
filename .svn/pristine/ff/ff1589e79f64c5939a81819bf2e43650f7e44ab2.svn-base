package noumena.payment.c185sy;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class C185syParams extends ChannelParams
{
	public static String CHANNEL_ID		= "c185";
	
	public C185syKVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new C185syKVO();
		}
		else
		{
			return (C185syKVO) vo;
		}
	}
	
}

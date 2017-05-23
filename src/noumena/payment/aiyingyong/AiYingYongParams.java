package noumena.payment.aiyingyong;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class AiYingYongParams extends ChannelParams
{
	public static String CHANNEL_ID		= "aiyingyong";
	
	public AiYingYongParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new AiYingYongParamsVO();
		}
		else
		{
			return (AiYingYongParamsVO) vo;
		}
	}
	
}

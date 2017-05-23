package noumena.payment.kugou.ribao;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class RibaoParams extends ChannelParams
{
	public static String CHANNEL_ID		= "ribao";
	
	public RibaoParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new RibaoParamsVO();
		}
		else
		{
			return (RibaoParamsVO) vo;
		}
	}
	
}

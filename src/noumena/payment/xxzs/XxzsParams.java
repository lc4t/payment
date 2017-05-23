package noumena.payment.xxzs;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class XxzsParams extends ChannelParams
{
	public static String CHANNEL_ID		= "xxzs";
	
	public XxzsParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new XxzsParamsVO();
		}
		else
		{
			return (XxzsParamsVO) vo;
		}
	}
	
}

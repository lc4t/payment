package noumena.payment.qianbao;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class QianbaoParams extends ChannelParams
{
	public static String CHANNEL_ID		= "qianbao";
	
	public QianbaoParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new QianbaoParamsVO();
		}
		else
		{
			return (QianbaoParamsVO) vo;
		}
	}
	
}

package noumena.payment.yingyonghui;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class YingyonghuiParams extends ChannelParams
{
	public static String CHANNEL_ID		= "yingyonghui";
	
	public YingyonghuiParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new YingyonghuiParamsVO();
		}
		else
		{
			return (YingyonghuiParamsVO) vo;
		}
	}
	
}

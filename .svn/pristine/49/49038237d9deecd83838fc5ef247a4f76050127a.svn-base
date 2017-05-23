package noumena.payment.vivonew;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class VivoNewParams extends ChannelParams
{
	public static String CHANNEL_ID	= "vivonew";
	
	public static String VIVO_VER				= "1.0.0";
	public static String VIVO_SIGN				= "MD5";
	public static String CALLBACK_URL_RELEASE	= "http://p.ko.cn/pay/vivonewcb";
	public static String CALLBACK_URL_TEST		= "http://paystage.ko.cn:6001/paymentsystem/vivonewcb";
	
	public VivoNewParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new VivoNewParamsVO();
		}
		else
		{
			return (VivoNewParamsVO) vo;
		}
	}
}

package noumena.payment.youxiqun;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class YouxiqunParams extends ChannelParams
{
	public static String CHANNEL_ID				= "youxiqun";
	
	public static String CREATE_ORDER_URL  		= "http://open.xmwan.com/v2/purchases";
	public static String GET_TOKEN_URL 	 		= "http://open.xmwan.com/v2/oauth2/access_token";
	public static String CHECK_ORDER_URL		= "http://open.xmwan.com/v2/purchases/verify";
	public static String CALLBACK_URL_TEST		= "http://paystage.ko.cn:6001/paymentsystem/youxiquncb";
	public static String CALLBACK_URL_RELEASE	= "http://p.ko.cn/pay/youxiquncb";
	public static String GRANT_TYPE				= "authorization_code";
	
	public YouxiqunParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new YouxiqunParamsVO();
		}
		else
		{
			return (YouxiqunParamsVO) vo;
		}
	}
	
}

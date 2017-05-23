package noumena.payment.tencent;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class TencentParams extends ChannelParams
{
	public static String CHANNEL_ID	= "tencent";
	
	public static String METHOD = "GET";
	public static String QQ_SESSION_ID				= "openid";
	public static String WEIXIN_SESSION_ID			= "hy_gameid";
	public static String QQ_SESSION_TYPE			= "kp_actoken";
	public static String WEIXIN_SESSION_TYPE		= "wc_actoken";
	
	public static String PURCHASE_URI				= "/mpay/pay_m";
	public static String GETBALANCE_URI				= "/mpay/get_balance_m";
	
	public static String PURCHASE_URL_TEST  		= "http://msdktest.qq.com/mpay/pay_m";
	//public static String PURCHASE_URL_RELEASE  		= "http://msdktest.qq.com/mpay/pay_m";
	public static String PURCHASE_URL_RELEASE  		= "http://msdk.qq.com/mpay/pay_m";
	public static String GETBALANCE_URL_TEST  		= "http://msdktest.qq.com/mpay/get_balance_m";
	//public static String GETBALANCE_URL_RELEASE		= "http://msdktest.qq.com/mpay/get_balance_m";
	public static String GETBALANCE_URL_RELEASE		= "http://msdk.qq.com/mpay/get_balance_m";
	
	public TencentParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new TencentParamsVO();
		}
		else
		{
			return (TencentParamsVO) vo;
		}
	}
	
}

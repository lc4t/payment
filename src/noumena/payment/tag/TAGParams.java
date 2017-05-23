package noumena.payment.tag;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class TAGParams extends ChannelParams
{
	public static String CHANNEL_ID	= "tag";
	
	public static String TAG_VERIFY_URL_TEST	= "http://211.63.6.225/tagmkp/promotion/get/%s/%s";
	public static String TAG_VERIFY_URL_RELEASE	= "http://tagmkp.theappsgames.com/promotion/get/%s/%s";
	
	public TAGParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new TAGParamsVO();
		}
		else
		{
			return (TAGParamsVO) vo;
		}
	}
	
}

package noumena.payment.heepay;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class HeepayParams extends ChannelParams
{
	public static String CHANNEL_ID		= "heepay";
	
	public static String NOTIFY_URL_RELEASE		= "http://p.ko.cn/pay/heepaycb";
	public static String NOTIFY_URL_TEST		= "http://paystage.ko.cn:6001/paymentsystem/heepaycb";
	public static String CREATE_ORDER_URL = "https://pay.heepay.com/Phone/SDK/PayInit.aspx";
	
	public HeepayParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new HeepayParamsVO();
		}
		else
		{
			return (HeepayParamsVO) vo;
		}
	}
	
}

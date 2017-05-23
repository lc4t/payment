package noumena.payment.sina;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class SinaParams extends ChannelParams
{
	public static String CHANNEL_ID				= "sina";
	
	public static String CHECK_ORDER_URL		= "http://i.game.weibo.cn/pay.php?method=query&order_id=%s";;
	
	public SinaParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new SinaParamsVO();
		}
		else
		{
			return (SinaParamsVO) vo;
		}
	}
	
}

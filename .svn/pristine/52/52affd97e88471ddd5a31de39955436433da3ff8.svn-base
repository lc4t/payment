package noumena.payment.xingshang;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class XingshangParams extends ChannelParams
{
	public static String CHANNEL_ID		= "xingshang";
	
	public XingshangParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new XingshangParamsVO();
		}
		else
		{
			return (XingshangParamsVO) vo;
		}
	}
	
}

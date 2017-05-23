package noumena.payment.xiaoqi;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class CXiaoQiParams extends ChannelParams
{
	public static String CHANNEL_ID		= "xiaoqi";
	
	public CXiaoQinVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new CXiaoQinVO();
		}
		else
		{
			return (CXiaoQinVO) vo;
		}
	}
	
}

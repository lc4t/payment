package noumena.payment.uc;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class UCParams extends ChannelParams {
	public static String CHANNEL_ID = "uc";

	public UCParamsVO getParams(String appid) {
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null) {
			return new UCParamsVO();
		} else {
			return (UCParamsVO) vo;
		}
	}

}

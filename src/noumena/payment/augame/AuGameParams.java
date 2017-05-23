package noumena.payment.augame;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class AuGameParams extends ChannelParams {
	public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsu/423282eKUgXbSGIrl9URREFSM6EUfNp+ZYZhMInCwJVFcMRtlzSEYRZnESkNS+cu97yQfTAUzBDv3gJGfvriSyxT5qTYMOHWE0X4K+A7WbSnygKfFnHsp5TgnghMcoYWewBtHmC0OJCZXreTJmS43NjyDAy8RGvLtEpmtzzmPh4DSa0bQghvWprNYWjwKIIKHghWBy2ErhQX1EzlVzkM4w8oQxelFuohaFB8BxjxAwI5Ss0xxuiY9k1WPaDjw9W3MiftVKOuGM4U+MV7sn/QTcN+ZpVh5dtTX4rTIu0kpa58bqB94BGfeUhs9eK4rUhfOx711CASWZW007D5JKwIDAQAB";
	public static String CHANNEL_ID = "augame";

	public AuGameParamsVO getParams(String appid) {
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null) {
			return new AuGameParamsVO();
		} else {
			return (AuGameParamsVO) vo;
		}
	}

}

package noumena.payment.kuaiyong;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class KuaiyongParams extends ChannelParams
{
	public static String CHANNEL_ID		= "kuaiyong";
	
	public KuaiyongParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new KuaiyongParamsVO();
		}
		else
		{
			return (KuaiyongParamsVO) vo;
		}
	}
//	public static String KONG_PRIVATE_KEY_PKCS8	= "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAL/4wVKQTjOxj1RASw8KIovuZYv3Mff+9rft7aPI/NEDphdt1gocS8uVs984pVsQxXe9H/56698Ic0mT/f19byhUgDyOShBRgJYI6ZD+WL1CRtKaIiBTgUpvB+5hSquTHxuIQRfwVY80E5sJ9TYs1ITx9sd7c4mowva+EtIKJxNZAgMBAAECgYEAioT7MhEwbHqN2pQ91M9SWDggC9vFsZXG/rbvB4TKcCM64KL/k/IQ+wi9bq+YGVrDsf/Eq0KVGi0lywy/c/7jii0ypwU1qWp0XtbFUnM9j/B4VZzjU9b52Lge7qSKVw27l9d89pWWYrszsaESg/ltOI6H04+6UHLDH0t/UfpU0VECQQDl/5dkndAo97pdIOaQhVgIBvUF+0hgHM3irx3u2OKKLRR+BMQY95HUmh4RzZdlNqsYjtH6Shl0KqMEIx4oCQEtAkEA1ayg+/qikI/d9GvuUSJQEN9Y8NpqvJqeMugYXlgbEjAutFRc9yqUypbwShk0KorPzDSyp8b1wbCgx23NevP+XQJBAMowh9mW9mCdbsLh4m7YGkxsOUh53+VGTSRjR9Ap7/UCrkoeTHmaYiPhHGAQOXBrv7uGCkeajofV15sSeDc/uz0CQGieVVQKq33McwgxsEXZXwpj0NBcoNYnfj2+le/bg6WsIbH5fGsoViG7NXuSS5Bo1sVNKAwtxjZHnPEIOgtxW6ECQEYo8y2Fgfl7D1jc6tpKqJ1upVewvXYT419Jg9dFHU9l4AUZP+Kd68mGJUo8+sFXzwsHvXRmyAWZCYstRTncMDM=";
//	public static String KONG_PRIVATE_KEY		= "MIICXQIBAAKBgQC/+MFSkE4zsY9UQEsPCiKL7mWL9zH3/va37e2jyPzRA6YXbdYKHEvLlbPfOKVbEMV3vR/+euvfCHNJk/39fW8oVIA8jkoQUYCWCOmQ/li9QkbSmiIgU4FKbwfuYUqrkx8biEEX8FWPNBObCfU2LNSE8fbHe3OJqML2vhLSCicTWQIDAQABAoGBAIqE+zIRMGx6jdqUPdTPUlg4IAvbxbGVxv627weEynAjOuCi/5PyEPsIvW6vmBlaw7H/xKtClRotJcsMv3P+44otMqcFNalqdF7WxVJzPY/weFWc41PW+di4Hu6kilcNu5fXfPaVlmK7M7GhEoP5bTiOh9OPulBywx9Lf1H6VNFRAkEA5f+XZJ3QKPe6XSDmkIVYCAb1BftIYBzN4q8d7tjiii0UfgTEGPeR1JoeEc2XZTarGI7R+koZdCqjBCMeKAkBLQJBANWsoPv6opCP3fRr7lEiUBDfWPDaaryanjLoGF5YGxIwLrRUXPcqlMqW8EoZNCqKz8w0sqfG9cGwoMdtzXrz/l0CQQDKMIfZlvZgnW7C4eJu2BpMbDlIed/lRk0kY0fQKe/1Aq5KHkx5mmIj4RxgEDlwa7+7hgpHmo6H1debEng3P7s9AkBonlVUCqt9zHMIMbBF2V8KY9DQXKDWJ349vpXv24OlrCGx+XxrKFYhuzV7kkuQaNbFTSgMLcY2R5zxCDoLcVuhAkBGKPMthYH5ew9Y3OraSqidbqVXsL12E+NfSYPXRR1PZeAFGT/inevJhiVKPPrBV88LB710ZsgFmQmLLUU53DAz";
//	public static String KONG_PUBLIC_KEY		= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC/+MFSkE4zsY9UQEsPCiKL7mWL9zH3/va37e2jyPzRA6YXbdYKHEvLlbPfOKVbEMV3vR/+euvfCHNJk/39fW8oVIA8jkoQUYCWCOmQ/li9QkbSmiIgU4FKbwfuYUqrkx8biEEX8FWPNBObCfU2LNSE8fbHe3OJqML2vhLSCicTWQIDAQAB";
//	public static String KUAIYONG_PUBLIC_KEY	= "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDPtxJwSjpyMd23NhJqVR3o7QvA/0ZXhNBxmZ29FBsH/DwjxuhcYYz08GIWRF6AT0Watxu2In9M7Rnbecyy4uyT9gJG4WWbBCPa592+N7i88/zpP18equvHUT+US833pjw4jBebFOEAgGATC4rTbqjNiH5TXbcZrfFHkxapNsuYiwIDAQAB";

//	private Vector<KuaiyongParamApp> apps = new Vector<KuaiyongParamApp>();
//
//	public Vector<KuaiyongParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(KuaiyongParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		KuaiyongParamApp app = new KuaiyongParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (KuaiyongParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

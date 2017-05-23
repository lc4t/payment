package noumena.payment.naver;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class NaverParams extends ChannelParams
{
	public static String CHANNEL_ID		= "naver";
	
	public static String NAVER_VERIFY_URL		= "http://apis.naver.com/%s/appStore/receipt.json";
	public static String NAVER_VERIFY_URL_NEW	= "http://apis.naver.com/%s/appStore/receiptV2.json";
	
	public NaverParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new NaverParamsVO();
		}
		else
		{
			return (NaverParamsVO) vo;
		}
	}
	
////	public static String NAVER_CPID			= "AD_1002599";
////	public static String NAVER_KEY_TEST		= "m8NrbkmSNI";
////	public static String NAVER_KEY_RELEASE	= "vWZLliGY6P";

//	private Vector<NaverParamApp> apps = new Vector<NaverParamApp>();
//
//	public Vector<NaverParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(NaverParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String cpid, String testkey, String appkey, String privatekey)
//	{
//		NaverParamApp app = new NaverParamApp();
//		app.setAppname(appname);
//		app.setCpid(cpid);
//		app.setAppid(appid);
//		app.setTestkey(testkey);
//		app.setAppkey(appkey);
//		app.setPrivatekey(privatekey);
//		this.getApps().add(app);
//	}
//	
//	public String getCpidById(String appid)
//	{
//		for (NaverParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getCpid();
//			}
//		}
//		return null;
//	}
//	
//	public String getTestKeyById(String appid)
//	{
//		for (NaverParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getTestkey();
//			}
//		}
//		return null;
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (NaverParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
//	
//	public String getPrivateKeyById(String appid)
//	{
//		for (NaverParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getPrivatekey();
//			}
//		}
//		return null;
//	}
}

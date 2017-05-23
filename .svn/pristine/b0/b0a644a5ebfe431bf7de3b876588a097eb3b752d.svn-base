package noumena.payment.google;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class GoogleParams extends ChannelParams
{
	public static final String SIGNATURE_ALGORITHM 	= "SHA1withRSA";
	public static final String GOOGLEPLAY 			= "GooglePlay";
	public static String CHANNEL_ID		= "google";
	
	public GoogleParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new GoogleParamsVO();
		}
		else
		{
			return (GoogleParamsVO) vo;
		}
	}
	
//	private Vector<GoogleParamApp> apps = new Vector<GoogleParamApp>();
//
//	public Vector<GoogleParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(GoogleParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		GoogleParamApp app = new GoogleParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (GoogleParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

package noumena.payment.lengjing;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class LengjingParams extends ChannelParams
{
	public static final String SIGN_TYPE = "md5";
	public static String CHANNEL_ID		= "lengjing";
	
	public LengjingParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new LengjingParamsVO();
		}
		else
		{
			return (LengjingParamsVO) vo;
		}
	}
	
//	private Vector<LengjingParamApp> apps = new Vector<LengjingParamApp>();
//
//	public Vector<LengjingParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(LengjingParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		LengjingParamApp app = new LengjingParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (LengjingParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

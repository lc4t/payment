package noumena.payment.c37wan;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class C37wanParams extends ChannelParams
{
	public static final String SIGN_TYPE = "md5";
	public static String CHANNEL_ID		= "37wan";
	
	public C37wanParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new C37wanParamsVO();
		}
		else
		{
			return (C37wanParamsVO) vo;
		}
	}
	
//	private Vector<MeizuParamApp> apps = new Vector<MeizuParamApp>();
//
//	public Vector<MeizuParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(MeizuParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		MeizuParamApp app = new MeizuParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (MeizuParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

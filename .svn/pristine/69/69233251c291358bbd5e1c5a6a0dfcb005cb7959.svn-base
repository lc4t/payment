package noumena.payment.pptv;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class PPTVParams extends ChannelParams
{
	public static String CHANNEL_ID		= "pptv";
	
	public PPTVParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new PPTVParamsVO();
		}
		else
		{
			return (PPTVParamsVO) vo;
		}
	}
//	private Vector<PPTVParamApp> apps = new Vector<PPTVParamApp>();
//
//	public Vector<PPTVParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(PPTVParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		PPTVParamApp app = new PPTVParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (PPTVParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

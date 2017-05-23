package noumena.payment.duoku;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class DuokuParams extends ChannelParams
{
	public static String CHANNEL_ID		= "duoku";
	
	public DuokuParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new DuokuParamsVO();
		}
		else
		{
			return (DuokuParamsVO) vo;
		}
	}
	
//	private Vector<DuokuParamApp> apps = new Vector<DuokuParamApp>();
//
//	public Vector<DuokuParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(DuokuParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		DuokuParamApp app = new DuokuParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (DuokuParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

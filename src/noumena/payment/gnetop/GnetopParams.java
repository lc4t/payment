package noumena.payment.gnetop;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class GnetopParams extends ChannelParams
{
	public static String CHANNEL_ID		= "gnetop";
	
	public GnetopVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new GnetopVO();
		}
		else
		{
			return (GnetopVO) vo;
		}
	}
//	public static String MOBOJOY_KEY	= "537fa1a83c58e003bf252f83478cb828";
//
//	private Vector<PPSParamApp> apps = new Vector<PPSParamApp>();
//
//	public Vector<PPSParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(PPSParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		PPSParamApp app = new PPSParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (PPSParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

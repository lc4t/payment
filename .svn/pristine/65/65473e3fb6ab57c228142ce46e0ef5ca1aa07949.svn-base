package noumena.payment.anzhi;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class AnzhiParams extends ChannelParams
{
	public static String CHANNEL_ID			= "anzhi";
	public static final String SUCCESS		= "success";
	
	public AnzhiParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new AnzhiParamsVO();
		}
		else
		{
			return (AnzhiParamsVO) vo;
		}
	}

//	private Vector<AnzhiParamApp> apps = new Vector<AnzhiParamApp>();
//
//	public Vector<AnzhiParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(AnzhiParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		AnzhiParamApp app = new AnzhiParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (AnzhiParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

package noumena.payment.i4;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class I4Params extends ChannelParams
{
	public static String CHANNEL_ID		= "I4";
	
	public I4ParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new I4ParamsVO();
		}
		else
		{
			return (I4ParamsVO)vo;
		}
	}
	
//	public static String MOBOJOY_KEY	= "537fa1a83c58e003bf252f83478cb828";
//
//	private Vector<I4ParamApp> apps = new Vector<I4ParamApp>();
//
//	public Vector<I4ParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(I4ParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		I4ParamApp app = new I4ParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (I4ParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return this.getApps().get(0).getAppkey();
//	}
}

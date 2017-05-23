package noumena.payment.tongbu;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class TongbuParams extends ChannelParams
{
	public static final String SUCCESS	= "success";
	public static String CHANNEL_ID		= "tongbu";
	
	public TongbuParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new TongbuParamsVO();
		}
		else
		{
			return (TongbuParamsVO) vo;
		}
	}

//	private Vector<TongbuParamApp> apps = new Vector<TongbuParamApp>();
//
//	public Vector<TongbuParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(TongbuParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		TongbuParamApp app = new TongbuParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (TongbuParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

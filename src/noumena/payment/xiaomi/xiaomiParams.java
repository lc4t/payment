package noumena.payment.xiaomi;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class xiaomiParams extends ChannelParams
{
	public static String CHECK_ORDER_STATUS_URL		= "http://mis.migc.xiaomi.com/api/biz/service/queryOrder.do";
	public static String CHANNEL_ID		= "xiaomi";
	
	public xiaomiParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new xiaomiParamsVO();
		}
		else
		{
			return (xiaomiParamsVO) vo;
		}
	}
	
//	private Vector<xiaomiParamApp> apps = new Vector<xiaomiParamApp>();
//	
//	public Vector<xiaomiParamApp> getApps()
//	{
//		return apps;
//	}
//
//	public void addXiaomiApp(xiaomiParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addXiaomiApp(String appname, String appid, String appkey)
//	{
//		xiaomiParamApp app = new xiaomiParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (xiaomiParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
//	
//	//AppId：3873
//	//AppKey：4825bf27-371e-1358-2cad-50a08df44c4c
}

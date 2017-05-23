package noumena.payment.wandoujia;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class WandoujiaParams extends ChannelParams
{
	//public static String APP_KEY		= "REU5NDAzREMwMUU4ODQ4OEJEQTVFNzUyQ0RCQUM1QjIyM0UwNzVEN01USXdOelkyTnprMk1ESTRNRFUzTkRReU9URXJNalk0TWpRNU1EZzVOekE0TVRJME1qQXdNakF3TVRFMk1EUXlNRFl6T1RRME1EQTBPRFkz";
	public static final String SUCCESS	= "SUCCESS";
	public static final String FAILURE	= "FAILURE";
	public static String CHANNEL_ID		= "wandoujia";
	
	public WandoujiaParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new WandoujiaParamsVO();
		}
		else
		{
			return (WandoujiaParamsVO) vo;
		}
	}

//	private Vector<WandoujiaParamApp> apps = new Vector<WandoujiaParamApp>();
//
//	public Vector<WandoujiaParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(WandoujiaParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		WandoujiaParamApp app = new WandoujiaParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (WandoujiaParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

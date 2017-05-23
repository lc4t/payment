package noumena.payment.lenovo;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class LenovoParams extends ChannelParams
{
	//public static String APP_KEY		= "REU5NDAzREMwMUU4ODQ4OEJEQTVFNzUyQ0RCQUM1QjIyM0UwNzVEN01USXdOelkyTnprMk1ESTRNRFUzTkRReU9URXJNalk0TWpRNU1EZzVOekE0TVRJME1qQXdNakF3TVRFMk1EUXlNRFl6T1RRME1EQTBPRFkz";
	public static final String SUCCESS	= "SUCCESS";
	public static final String FAILURE	= "FAILURE";

	public static String CHANNEL_ID		= "cnlenovo";
	
	public LenovoParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new LenovoParamsVO();
		}
		else
		{
			return (LenovoParamsVO) vo;
		}
	}
	
	
//	private Vector<LenovoParamApp> apps = new Vector<LenovoParamApp>();
//
//	public Vector<LenovoParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(LenovoParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		LenovoParamApp app = new LenovoParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (LenovoParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return "";
//	}
}

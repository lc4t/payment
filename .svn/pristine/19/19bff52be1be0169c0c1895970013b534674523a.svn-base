package noumena.payment.pp;

import java.util.Vector;

public class PPParams
{
	//public static String APP_KEY		= "REU5NDAzREMwMUU4ODQ4OEJEQTVFNzUyQ0RCQUM1QjIyM0UwNzVEN01USXdOelkyTnprMk1ESTRNRFUzTkRReU9URXJNalk0TWpRNU1EZzVOekE0TVRJME1qQXdNakF3TVRFMk1EUXlNRFl6T1RRME1EQTBPRFkz";
	public static final String SUCCESS	= "success";
	public static final String FAILURE	= "fail";

	private Vector<PPParamApp> apps = new Vector<PPParamApp>();

	public Vector<PPParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(PPParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		PPParamApp app = new PPParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (PPParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

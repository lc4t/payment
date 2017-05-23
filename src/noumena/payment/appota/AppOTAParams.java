package noumena.payment.appota;

import java.util.Vector;

public class AppOTAParams
{
	public static final String SUCCESS		= "SUCCESS";
	public static final String ERR_SIGN		= "ERROR_SIGN";
	public static final String ERR_REPEAT	= "ERROR_REPEAT";
	public static final String ERR_NOORDER	= "ERROR_USER";
	public static final String FAILURE		= "ERROR_FAIL";

	private Vector<AppOTAParamApp> apps = new Vector<AppOTAParamApp>();

	public Vector<AppOTAParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(AppOTAParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		AppOTAParamApp app = new AppOTAParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (AppOTAParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

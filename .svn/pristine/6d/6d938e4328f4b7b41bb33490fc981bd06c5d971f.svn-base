package noumena.payment.gfan;

import java.util.Vector;

public class GfanParams
{
	public static String GFAN_ACCOUNT_ID	= "22310986";
	public static String VERIFY_URL			= "http://api.gfan.com/sdk/pay/queryAppPayLog";

	private Vector<GfanParamApp> apps = new Vector<GfanParamApp>();
	
	public Vector<GfanParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(GfanParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		GfanParamApp app = new GfanParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (GfanParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

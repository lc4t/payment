package noumena.payment.googleplay;

import java.util.Vector;

public class GooglePlayParams
{
	public static final String SIGNATURE_ALGORITHM 	= "SHA1withRSA";
	public static final String GOOGLEPLAY 			= "GooglePlay";
	
	private Vector<GooglePlayParamApp> apps = new Vector<GooglePlayParamApp>();

	public Vector<GooglePlayParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(GooglePlayParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		GooglePlayParamApp app = new GooglePlayParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (GooglePlayParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

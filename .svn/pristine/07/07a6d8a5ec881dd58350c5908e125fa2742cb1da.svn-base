package noumena.payment.kudong;

import java.util.Vector;

public class KudongParams
{
	public static final String SECRETKEY	= "";
	public static final String GAMEID 		= "10024";
	
	private Vector<KudongParamApp> apps = new Vector<KudongParamApp>();

	public Vector<KudongParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(KudongParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		KudongParamApp app = new KudongParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (KudongParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

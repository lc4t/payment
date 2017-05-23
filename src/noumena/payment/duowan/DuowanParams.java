package noumena.payment.duowan;

import java.util.Vector;

public class DuowanParams
{
	public static String MOBOJOY_KEY	= "537fa1a83c58e003bf252f83478cb828";

	private Vector<DuowanParamApp> apps = new Vector<DuowanParamApp>();

	public Vector<DuowanParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(DuowanParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		DuowanParamApp app = new DuowanParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (DuowanParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

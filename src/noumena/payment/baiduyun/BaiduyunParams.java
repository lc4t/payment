package noumena.payment.baiduyun;

import java.util.Vector;

public class BaiduyunParams
{
	private Vector<BaiduyunParamApp> apps = new Vector<BaiduyunParamApp>();

	public Vector<BaiduyunParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(BaiduyunParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		BaiduyunParamApp app = new BaiduyunParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (BaiduyunParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

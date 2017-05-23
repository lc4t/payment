package noumena.payment.snail;

import java.util.Vector;

public class SnailParams
{
	private Vector<SnailParamApp> apps = new Vector<SnailParamApp>();

	public Vector<SnailParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(SnailParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		SnailParamApp app = new SnailParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (SnailParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

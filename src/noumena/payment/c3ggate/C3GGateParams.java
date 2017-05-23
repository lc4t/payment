package noumena.payment.c3ggate;

import java.util.Vector;

public class C3GGateParams
{
	private Vector<C3GGateParamApp> apps = new Vector<C3GGateParamApp>();

	public Vector<C3GGateParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(C3GGateParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		C3GGateParamApp app = new C3GGateParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (C3GGateParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

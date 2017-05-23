package noumena.payment.itools;

import java.util.Vector;

public class CiToolsParams
{
	private Vector<CiToolsParamApp> apps = new Vector<CiToolsParamApp>();

	public Vector<CiToolsParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(CiToolsParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		CiToolsParamApp app = new CiToolsParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (CiToolsParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

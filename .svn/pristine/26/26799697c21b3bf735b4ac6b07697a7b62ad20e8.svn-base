package noumena.payment.kunlun;

import java.util.Vector;

public class KunlunParams
{
	public static final String KEY		= "94792e8e8307affd27ec5c0c6cf49413";

	private Vector<KunlunParamApp> apps = new Vector<KunlunParamApp>();

	public Vector<KunlunParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(KunlunParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		KunlunParamApp app = new KunlunParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (KunlunParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

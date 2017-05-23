package noumena.payment.mobojoy;

import java.util.Vector;

public class MobojoyParams
{
	public static String MOBOJOY_KEY	= "537fa1a83c58e003bf252f83478cb828";

	private Vector<MobojoyParamApp> apps = new Vector<MobojoyParamApp>();

	public Vector<MobojoyParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(MobojoyParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		MobojoyParamApp app = new MobojoyParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (MobojoyParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

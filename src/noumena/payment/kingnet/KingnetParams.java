package noumena.payment.kingnet;

import java.util.Vector;

public class KingnetParams
{
	public static final int SUCCESS		= 0;
	public static final int ERR_PARAMS	= -8;
	public static final int ERR_SIGN	= -5;
	public static final int ERR_REPEAT	= -6;
	public static final int ERR_OTHER	= -7;

	private Vector<KingnetParamApp> apps = new Vector<KingnetParamApp>();

	public Vector<KingnetParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(KingnetParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		KingnetParamApp app = new KingnetParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (KingnetParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

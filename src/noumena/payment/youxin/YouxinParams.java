package noumena.payment.youxin;

import java.util.Vector;

public class YouxinParams
{
	public static final String SECRETKEY	= "";
	public static final String GAMEID 		= "10024";
	
	private Vector<YouxinParamApp> apps = new Vector<YouxinParamApp>();

	public Vector<YouxinParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(YouxinParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		YouxinParamApp app = new YouxinParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (YouxinParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

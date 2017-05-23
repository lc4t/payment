package noumena.payment.winner;

import java.util.Vector;

public class WinnerParams
{
	public static final String SECRETKEY	= "HnG.win.th";
	public static final String GAMEID 		= "10024";
	
	private Vector<WinnerParamApp> apps = new Vector<WinnerParamApp>();

	public Vector<WinnerParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(WinnerParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		WinnerParamApp app = new WinnerParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (WinnerParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

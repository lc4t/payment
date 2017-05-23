package noumena.payment.bilibili;

import java.util.Vector;

public class BilibiliParams
{

	private Vector<BilibiliParamApp> apps = new Vector<BilibiliParamApp>();

	public Vector<BilibiliParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(BilibiliParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		BilibiliParamApp app = new BilibiliParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (BilibiliParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

package noumena.payment.sharejoy;

import java.util.Vector;

public class SharejoyParams
{

	private Vector<SharejoyParamApp> apps = new Vector<SharejoyParamApp>();

	public Vector<SharejoyParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(SharejoyParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		SharejoyParamApp app = new SharejoyParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (SharejoyParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

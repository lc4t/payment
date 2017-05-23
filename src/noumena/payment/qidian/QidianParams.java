package noumena.payment.qidian;

import java.util.Vector;

public class QidianParams
{
	private Vector<QidianParamApp> apps = new Vector<QidianParamApp>();

	public Vector<QidianParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(QidianParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		QidianParamApp app = new QidianParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (QidianParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

package noumena.payment.xunlei;

import java.util.Vector;

public class XunleiParams
{
	private Vector<XunleiParamApp> apps = new Vector<XunleiParamApp>();

	public Vector<XunleiParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(XunleiParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		XunleiParamApp app = new XunleiParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (XunleiParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

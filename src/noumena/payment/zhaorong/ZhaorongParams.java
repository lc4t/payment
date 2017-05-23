package noumena.payment.zhaorong;

import java.util.Vector;

public class ZhaorongParams
{
	public static final String KEY = "kong.zhaorong.game"; 
	private Vector<ZhaorongParamApp> apps = new Vector<ZhaorongParamApp>();

	public Vector<ZhaorongParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(ZhaorongParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		ZhaorongParamApp app = new ZhaorongParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (ZhaorongParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

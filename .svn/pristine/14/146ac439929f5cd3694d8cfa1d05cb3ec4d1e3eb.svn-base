package noumena.payment.baifubao;

import java.util.Vector;

public class BaifubaoParams
{
	private Vector<BaifubaoParamApp> apps = new Vector<BaifubaoParamApp>();

	public Vector<BaifubaoParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(BaifubaoParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		BaifubaoParamApp app = new BaifubaoParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (BaifubaoParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

package noumena.payment.baiduqianbao;

import java.util.Vector;

public class BaiduqianbaoParams
{
	private Vector<BaiduqianbaoParamApp> apps = new Vector<BaiduqianbaoParamApp>();

	public Vector<BaiduqianbaoParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(BaiduqianbaoParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		BaiduqianbaoParamApp app = new BaiduqianbaoParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (BaiduqianbaoParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

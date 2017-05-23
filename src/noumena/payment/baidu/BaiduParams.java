package noumena.payment.baidu;

import java.util.Vector;

public class BaiduParams
{
	public static final String SUCCESS		= "SUCCESS";
	public static final String ERR_SIGN		= "ERROR_SIGN";
	public static final String ERR_REPEAT	= "ERROR_REPEAT";
	public static final String ERR_NOORDER	= "ERROR_USER";
	public static final String FAILURE		= "ERROR_FAIL";

	private Vector<BaiduParamApp> apps = new Vector<BaiduParamApp>();

	public Vector<BaiduParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(BaiduParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		BaiduParamApp app = new BaiduParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (BaiduParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

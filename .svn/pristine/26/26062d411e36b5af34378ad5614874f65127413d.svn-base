package noumena.payment.webomg;

import java.util.Vector;

public class WebOMGParams
{
	public static String VERIFY_URL_RELEASE		= "http://member.app-master.com.tw/gamebarV2/api/TradeNoRecheck.ashx";
	public static String KEY_RELEASE     = "d95u6vfxveqvarta";
	public static String KEY_TEST		 = "r5kjjbvj38mmb8nn";
	public static String IV_RELEASE 	 = "pufvnmrjce3scq8b";
	public static String IV_TEST         = "3kwqxtqg6bbh9d7w";

	private Vector<WebOMGParamApp> apps = new Vector<WebOMGParamApp>();

	public Vector<WebOMGParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(WebOMGParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey, String appiv)
	{
		WebOMGParamApp app = new WebOMGParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		app.setAppiv(appiv);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (WebOMGParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
	
	public String getAppIVById(String appid)
	{
		for (WebOMGParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppiv();
			}
		}
		return null;
	}
}

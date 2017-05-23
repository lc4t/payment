package noumena.payment.suole;

import java.util.Vector;

public class SuoleParams
{
	public static String SUBMIT_ORDER_STATUS_URL_TEST		= "http://117.135.139.77:8080/gameserver/order";
	public static String SUBMIT_ORDER_STATUS_URL_RELEASE	= "http://playpluspay.socogame.com/gameserver/order";
	public static String CHECK_ORDER_STATUS_URL_TEST		= "http://117.135.139.77:8080/gameserver/confirmOrder";
	public static String CHECK_ORDER_STATUS_URL_RELEASE		= "http://playpluspay.socogame.com/gameserver/confirmOrder";
	private Vector<SuoleParamApp> apps = new Vector<SuoleParamApp>();
	
	public Vector<SuoleParamApp> getApps()
	{
		return apps;
	}

	public void addApp(SuoleParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		SuoleParamApp app = new SuoleParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (SuoleParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
	
	//AppId：3873
	//AppKey：4825bf27-371e-1358-2cad-50a08df44c4c
}

package noumena.payment.innova;

import java.util.Vector;

public class InnovaParams
{
	public static String GAME_ID		= "43000001";
	public static String SERVER_ID		= "1";
	public static String MERCHANT_DES_KEY	= "12345678";

	private Vector<InnovaParamApp> apps = new Vector<InnovaParamApp>();

	public Vector<InnovaParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(InnovaParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		InnovaParamApp app = new InnovaParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (InnovaParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

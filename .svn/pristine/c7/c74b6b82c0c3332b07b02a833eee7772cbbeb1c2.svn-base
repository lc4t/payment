package noumena.payment.kongmp;

import java.util.Vector;

public class KongMPParams
{
	public static final String KEYPRE		= "[af12";
	public static final String KEYEND		= "39hk]";
	
	public static final String SIGN_KEY		= "k1o9n2g8m3p7";
	
	public static final String GET_USEABLE_ITEM_URL		= "http://gb.rsbwl.com/CBS/trinet/1p0.jsp?";
	
	public static final String SUCCESS		= "SUCCESS";
	public static final String ERR_SIGN		= "ERROR_SIGN";
	public static final String ERR_REPEAT	= "ERROR_REPEAT";
	public static final String ERR_NOORDER	= "ERROR_USER";
	public static final String FAILURE		= "ERROR_FAIL";

	private Vector<KongMPParamApp> apps = new Vector<KongMPParamApp>();

	public Vector<KongMPParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(KongMPParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		KongMPParamApp app = new KongMPParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (KongMPParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

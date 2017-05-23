package noumena.payment.olleh;

import java.util.Vector;

public class OllehParams
{
	public static String OLLEH_GETKEY_URL_TEST		= "https://221.148.247.203:7777";
	public static String OLLEH_GETKEY_URL_RELEASE	= "https://inapppurchase.ollehmarket.com:443";
	public static String OLLEH_GETKEY_SERVICE		= "/INAP_GW/inap_gw/getSymKeyGen";
	public static String OLLEH_VERIFY_URL_TEST		= "http://221.148.247.203:8080";
	public static String OLLEH_VERIFY_URL_RELEASE	= "http://inapppurchase.ollehmarket.com:80";
	public static String OLLEH_VERIFY_TRANSID		= "checkBuyDiItem/tr_id/%s";
	public static String OLLEH_VERIFY_SERVICE		= "/INAP_GW/inap_gw/crypto_param/%s/seq_key/%s";

	private Vector<OllehParamApp> apps = new Vector<OllehParamApp>();

	public Vector<OllehParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(OllehParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		OllehParamApp app = new OllehParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (OllehParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

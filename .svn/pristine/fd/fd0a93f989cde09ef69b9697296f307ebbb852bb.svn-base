package noumena.payment.omg;

import java.util.Vector;

public class OMGParams
{
	public static String VERIFY_URL_RELEASE		= "http://member.app-master.com.tw/gamebarV2/api/recheck.ashx";
	public static String VERIFY_URL_TEST		= "http://vendortest.app-master.com.tw/gamebarV2/api/recheck.ashx";
	public static String KEY_RELEASE = "d95u6vfxveqvarta";
	public static String KEY_TEST = "r5kjjbvj38mmb8nn";
	public static String IV_RELEASE = "pufvnmrjce3scq8b";
	public static String IV_TEST = "3kwqxtqg6bbh9d7w";
	
	private Vector<OMGParamApp> apps = new Vector<OMGParamApp>();

	public Vector<OMGParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(OMGParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey,String appiv)
	{
		OMGParamApp app = new OMGParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		app.setAppiv(appiv);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (OMGParamApp app : this.getApps())
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
		for (OMGParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppiv();
			}
		}
		return null;
	}
}

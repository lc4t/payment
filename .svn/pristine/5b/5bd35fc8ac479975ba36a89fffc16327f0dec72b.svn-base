package noumena.payment.vnsoha;

import java.util.Vector;

public class VNSohaParams
{
	public static String VERIFY_URL	= "https://soap.soha.vn/api/a/GET/order/mobileconfirm?app_id=%s&app_secret=%s&order_id=%s";
	
	private Vector<VNSohaParamApp> apps = new Vector<VNSohaParamApp>();

	public Vector<VNSohaParamApp> getApps()
	{
		return apps;
	}
	
	public void addVNSohaApp(VNSohaParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addVNSohaApp(String appname, String appid, String appkey)
	{
		VNSohaParamApp app = new VNSohaParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (VNSohaParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

package noumena.payment.unionpay;

import java.util.Vector;

import noumena.payment.util.ChannelParams;

public class UnionpayParams extends ChannelParams
{
	public static String MOBOJOY_KEY	= "537fa1a83c58e003bf252f83478cb828";

	public static String CHANNEL_ID = "union";
	private Vector<UnionpayParamApp> apps = new Vector<UnionpayParamApp>();

	public Vector<UnionpayParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(UnionpayParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		UnionpayParamApp app = new UnionpayParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (UnionpayParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

package noumena.payment.igs;

import java.util.Vector;

public class IGSParams
{
	public static String IGS_VERIFY_URL_DESIGN	= "http://paycenter.app.design.towergame.com/Order.svc/DrawEntrepotData";
	public static String IGS_VERIFY_URL_TEST	= "http://paycenter.app.towergame.com/Order.svc/DrawEntrepotData";
	public static String IGS_VERIFY_URL_RELEASE	= "http://paycenter.app.gametower.com.tw/Order.svc/DrawEntrepotData";

	private Vector<IGSParamApp> apps = new Vector<IGSParamApp>();

	public Vector<IGSParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(IGSParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		IGSParamApp app = new IGSParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (IGSParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

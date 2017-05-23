package noumena.payment.weixin;

import java.util.Vector;

public class WeixinParams
{
	//public static final String PARTNER_ID	= "1220017501";
//	public static final String PARTNER_ID	= "1900000109";
	//public static final String PARTNER_KEY	= "f3110f64d3802670c2b0dd974fbf4d6b";
//	public static final String PARTNER_KEY	= "8934e7d15453e97507ef794cf7b0519d";
	public static final String NOTIFY_URL	= "http://p.ko.cn/pay/weixincb";
//	public static final String NOTIFY_URL	= "http://paystage.ko.cn:6001/paymentsystem/weixincb";
//	public static final String NOTIFY_URL	= "http://weixin.qq.com";
	
	private Vector<WeixinParamApp> apps = new Vector<WeixinParamApp>();

	public Vector<WeixinParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(WeixinParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey, String shanghukey)
	{
		WeixinParamApp app = new WeixinParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppSecretkey(appkey);
		app.setAppShuanghukey(shanghukey);
		this.getApps().add(app);
	}
	
	public String getAppSecretKeyById(String appid)
	{
		for (WeixinParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppSecretkey();
			}
		}
		return null;
	}
	
	public String getAppShanghuKeyById(String appid)
	{
		for (WeixinParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppShuanghukey();
			}
		}
		return null;
	}
}

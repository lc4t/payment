package noumena.payment.lguplus;

import java.util.Vector;

public class LGUplusParams
{
	public static String LGUPLUS_GETKEY_URL_TEST		= "https://iabsdev.uplus.co.kr:5002/if/encrypt.jsp?str=";
	public static String LGUPLUS_VERIFY_URL_TEST		= "https://iabsdev.uplus.co.kr:5002/if/buyVerify.app";
	public static String LGUPLUS_GETKEY_URL_RELEASE		= "https://iabs.lguplus.co.kr:5002/if/encrypt.jsp?str=";
	public static String LGUPLUS_VERIFY_URL_RELEASE		= "https://iabs.lguplus.co.kr:5002/if/buyVerify.app";
//	public static String LGUPLUS_VERIFY_URL_RELEASE		= "https://iabs.lguplus.co.kr:5002/if/buyVerify.app";
	public static String LGUPLUS_REQUEST_IP_TEST		= "218.247.140.208";
	public static String LGUPLUS_REQUEST_IP_RELEASE		= "42.62.7.205";

	private Vector<LGUplusParamApp> apps = new Vector<LGUplusParamApp>();

	public Vector<LGUplusParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(LGUplusParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		LGUplusParamApp app = new LGUplusParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (LGUplusParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return "TLSENT";
	}
}

package noumena.payment.tstore;

import java.util.Vector;

public class TStoreParams
{
	public static String TSTORE_CHECK_URL_TEST		= "http://211.234.231.208:8082/billIntf/billinglog/billlogconfirm.action?TID=%s&APPID=%s";
	public static String TSTORE_CHECK_URL_RELEASE	= "http://211.234.231.209:8090/billIntf/billinglog/billlogconfirm.action?TID=%s&APPID=%s";

	public static String TSTORE_E_CHECK_URL_TEST	= "https://iap.tstore.co.kr/digitalsignconfirm.iap";
	//public static String TSTORE_E_CHECK_URL_RELEASE	= "https://iapdev.tstore.co.kr/digitalsignconfirm.iap";
	public static String TSTORE_E_CHECK_URL_RELEASE	= "https://iap.tstore.co.kr/digitalsignconfirm.iap";
	
	private Vector<TStoreParamApp> apps = new Vector<TStoreParamApp>();
	
	private Vector<TStoreParamApp> getApps()
	{
		return apps;
	}

	public String getCheckUrl(String tid, String appid, Boolean istest)
	{
		try
		{
			String appkey = getAppKeyById(appid);
			if (appkey == null)
			{
				//no appid
				return null;
			}
			tid = CryptoManager.encrypt(appkey, tid);
		}
		catch (Exception e1)
		{
			e1.printStackTrace();
		}
		
		if (istest == true)
		{
			return String.format(TSTORE_CHECK_URL_TEST, tid, appid);
		}
		else
		{
			return String.format(TSTORE_CHECK_URL_RELEASE, tid, appid);
		}
	}
	
	public void addTStoreApp(TStoreParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addTStoreApp(String appname, String appid, String appkey)
	{
		TStoreParamApp app = new TStoreParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (TStoreParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}
}

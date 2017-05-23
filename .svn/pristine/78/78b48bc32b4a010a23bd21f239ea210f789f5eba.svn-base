package noumena.payment.gionee;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class GioneeParams extends ChannelParams
{
	public static String CHANNEL_ID		= "gionee";
	
	public GioneeParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new GioneeParamsVO();
		}
		else
		{
			return (GioneeParamsVO) vo;
		}
	}
	
	
////	public static final String CALL_BACK_URL = "http://paystage.ko.cn:6001/paymentsystem/gioneecb";
//	public static final String CALL_BACK_URL = "http://p.ko.cn/pay/gioneecb";
//	public static final String CREATE_ORDER_URL = "https://pay.gionee.com/order/create";
//	
////	public static final String PUBKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKh5YX3EraSQFe5B5K1BZ3IEkLCgyrwH2xL3lmIrWsn2CT2qYvKKUg4cX3m4A+2yeA2DNgqD9SGfBBk4aXBO4hyB1xycaQVLCr1XYFBorHNKl4uYuPm4ylme5h6DqiwONEZXknE/3K27FwtW0aYvUhz4KrThOtkBjrO29clUJq3wIDAQAB";
//	public static final String DELIVER_TYPE = "1";
//	
//	private Vector<GioneeParamApp> apps = new Vector<GioneeParamApp>();
//
//	public Vector<GioneeParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(GioneeParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey, String pubkey)
//	{
//		GioneeParamApp app = new GioneeParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		app.setPubkey(pubkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (GioneeParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
//	
//	public String getpubKeyById(String appid)
//	{
//		for (GioneeParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getPubkey();
//			}
//		}
//		return null;
//	}
}

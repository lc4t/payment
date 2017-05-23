package noumena.payment.ndpay;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class NdpayParams extends ChannelParams
{
	public static String CHECK_ORDER_STATUS_URL_TEST		= "http://172.20.3.159/WTXM/api/getIapTransStatus";
	public static String CHECK_ORDER_STATUS_URL_RELEASE		= "https://wms.catch.net.tw/WTXM/api/getIapTransStatus";
	public static String CHANNEL_ID		= "ndpay";
	
	public NdpayParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new NdpayParamsVO();
		}
		else
		{
			return (NdpayParamsVO) vo;
		}
	}
	
//	private Vector<NdpayParamApp> apps = new Vector<NdpayParamApp>();
//	
//	private Vector<NdpayParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(NdpayParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		NdpayParamApp app = new NdpayParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (NdpayParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

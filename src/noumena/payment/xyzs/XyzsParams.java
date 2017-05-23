package noumena.payment.xyzs;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class XyzsParams extends ChannelParams
{
	public static String CHANNEL_ID		= "xyzs";
	
	public XyzsParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new XyzsParamsVO();
		}
		else
		{
			return (XyzsParamsVO) vo;
		}
	}
//	private Vector<XyzsParamApp> apps = new Vector<XyzsParamApp>();
//
//	public Vector<XyzsParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(XyzsParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey, String paykey)
//	{
//		XyzsParamApp app = new XyzsParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		app.setPaykey(paykey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (XyzsParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
//	
//	public String getPayKeyById(String appid)
//	{
//		for (XyzsParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getPaykey();
//			}
//		}
//		return null;
//	}
}

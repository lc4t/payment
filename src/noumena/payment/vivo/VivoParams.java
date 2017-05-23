package noumena.payment.vivo;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class VivoParams extends ChannelParams
{
	public static String CHANNEL_ID		= "vivo";
	
	public VivoParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new VivoParamsVO();
		}
		else
		{
			return (VivoParamsVO) vo;
		}
	}
	
//	public String getCPID(String appid)
//	{
//		VivoParamsVO vo = (VivoParamsVO) super.getParamsVO(appid);
//		return vo.getCp_id();
//	}
//	
//	public String getCPKey(String appid)
//	{
//		VivoParamsVO vo = (VivoParamsVO) super.getParamsVO(appid);
//		return vo.getCp_key();
//	}
//	
//	public String getVivoVer(String appid)
//	{
//		VivoParamsVO vo = (VivoParamsVO) super.getParamsVO(appid);
//		return vo.getVivo_ver();
//	}
//	
//	public String getVivoSign(String appid)
//	{
//		VivoParamsVO vo = (VivoParamsVO) super.getParamsVO(appid);
//		return vo.getVivo_sign();
//	}
//	
//	public String getCBURL(String appid)
//	{
//		VivoParamsVO vo = (VivoParamsVO) super.getParamsVO(appid);
//		return vo.getCallback_url();
//	}
//	
//	public String getKey(String appid)
//	{
//		VivoParamsVO vo = (VivoParamsVO) super.getParamsVO(appid);
//		return vo.getCallback_url();
//	}

//	public static String CP_ID			= "20141014114351157593";
//	public static String CP_KEY			= "6a2b21a047cc72a26f405a8cd04c46f7";
//	public static String VIVO_VER		= "1.0.0";
//	public static String VIVO_SIGN		= "MD5";
//	public static String CALLBACK_URL	= "http://paystage.ko.cn:6001/paymentsystem/vivocb";
//	public static String CALLBACK_URL	= "http://p.ko.cn/pay/vivocb";
	
//	private Vector<VivoParamApp> apps = new Vector<VivoParamApp>();
//
//	public Vector<VivoParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(VivoParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		VivoParamApp app = new VivoParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (VivoParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

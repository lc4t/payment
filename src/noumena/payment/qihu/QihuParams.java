package noumena.payment.qihu;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class QihuParams extends ChannelParams
{
	public static String GET_TOKEN_URL		= "https://openapi.360.cn/oauth2/access_token?grant_type=authorization_code&code=%s&client_id=%s&client_secret=%s&redirect_uri=oob";
	public static String GET_USER_INFO_URL	= "https://openapi.360.cn/user/me.json?access_token=%s&fields=id,name,avatar,sex,area";
	public static String VERIFY_URL			= "https://openapi.360.cn/pay/verify_mobile_notification.json?app_key=%s&product_id=%s&amount=%s&app_uid=%s&order_id=%s&app_order_id=%s&sign_type=%s&sign_return=%s&client_id=%s&client_secret=%s";
	public static String CHANNEL_ID		= "qihu360";
	
	public QihuParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new QihuParamsVO();
		}
		else
		{
			return (QihuParamsVO) vo;
		}
	}
	
//	private Vector<QihuParamApp> apps = new Vector<QihuParamApp>();
//
//	public Vector<QihuParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addQihuApp(QihuParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addQihuApp(String appname, String appid, String appkey)
//	{
//		QihuParamApp app = new QihuParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (QihuParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

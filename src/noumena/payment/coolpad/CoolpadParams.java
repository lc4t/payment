package noumena.payment.coolpad;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class CoolpadParams extends ChannelParams
{
	public static String CHANNEL_ID		= "coolpad";
	
	public CoolpadParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new CoolpadParamsVO();
		}
		else
		{
			return (CoolpadParamsVO) vo;
		}
	}
	
	
////	public static final String PUBKEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCjsRxSVcSl3FkKPk1kmLdeUVZwvDpEa6AaZ+YtnZEZw6FypwSMwsVX7nVUIVGkY6x5s3m5U/tsM8Vaf/IV6c3M2FLXQldkq5BFbXkg6y5pOluMYwsj6OUrVPgO/LYTsdMV4mXJQrH/gRr9H+vxaXm4TMwBzetXbLVphRaeQ94M2wIDAQAB";
//	public static final String CREAT_ORDER_URL = "http://pay.coolyun.com:6988/payapi/order";
////	public static final String CALL_BACK_URL = "http://paystage.ko.cn:6001/paymentsystem/coolpadcb";
//	public static final String CALL_BACK_URL = "http://p.ko.cn/pay/coolpadcb";
//	
	public static final String SUCCESS	= "SUCCESS";
	public static final String FAILURE	= "FAILURE";
//	public static final String GET_TOKEN_URL = "https://openapi.coolyun.com/oauth2/token?grant_type=authorization_code&client_id=%s&client_secret=%s&code=%s&redirect_uri=http://paystage.ko.cn:6001/paymentsystem/coolpadcb";
//	
//	private Vector<CoolpadParamApp> apps = new Vector<CoolpadParamApp>();
//
//	public Vector<CoolpadParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(CoolpadParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey, String pubkey)
//	{
//		CoolpadParamApp app = new CoolpadParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		app.setPubkey(pubkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (CoolpadParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
//	
//	public String getPubKeyById(String appid)
//	{
//		for (CoolpadParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getPubkey();
//			}
//		}
//		return null;
//	}
}

package noumena.payment.huawei;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class HuaweiParams extends ChannelParams
{
	public static String CHANNEL_ID		= "huawei";
	
	public HuaweiParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new HuaweiParamsVO();
		}
		else
		{
			return (HuaweiParamsVO) vo;
		}
	}
//	public static final String devPubKey = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBALKm0RgdgzryAZFVOZitLX9cryrd5rdpVJuqSyXpAsbCoKPVIHdMY51e2TodUS1tqqdHSgTmS7sUXV/C885wz2cCAwEAAQ==";
//	public static final String devPriKey = "MIIBVQIBADANBgkqhkiG9w0BAQEFAASCAT8wggE7AgEAAkEAsqbRGB2DOvIBkVU5mK0tf1yvKt3mt2lUm6pLJekCxsKgo9Ugd0xjnV7ZOh1RLW2qp0dKBOZLuxRdX8LzznDPZwIDAQABAkEAmqFi+CGLVbjkfHus16aFa+i4QT1CFlyX/Aig9NZDLe8RtHCN74Ce16X2B+UK+mZc2OUlJDA0PuezfIEkPXzbiQIhAO3uChivW2xQ3d1z3rXGSzhKzNK2WYdo/PPHTbhbKMa7AiEAwDhCHibWkWKRnF/s6vOYff/FuF1gxtV7tAFGKau3TUUCIQCDV853BgZ/6rwvTZF54vcPIiujfko59/j7UeBTju8ZlQIgMozY/nryRKuqZXhoouVK/h/MjI6qC8BK1uza2cyZ2Q0CIGM4Ewd675yY0rMERShVxIn2szocRI0KebIYgpqG9xL6";
//	
//	private Vector<HuaweiParamApp> apps = new Vector<HuaweiParamApp>();
//
//	public Vector<HuaweiParamApp> getApps()
//	{
//		return apps;
//	}
//	
//	public void addApp(HuaweiParamApp app)
//	{
//		this.getApps().add(app);
//	}
//	
//	public void addApp(String appname, String appid, String appkey)
//	{
//		HuaweiParamApp app = new HuaweiParamApp();
//		app.setAppname(appname);
//		app.setAppid(appid);
//		app.setAppkey(appkey);
//		this.getApps().add(app);
//	}
//	
//	public String getAppKeyById(String appid)
//	{
//		for (HuaweiParamApp app : this.getApps())
//		{
//			if (app.getAppid().equals(appid))
//			{
//				return app.getAppkey();
//			}
//		}
//		return null;
//	}
}

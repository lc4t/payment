package noumena.payment.truemeizu;


import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class TrueMeizuParams extends ChannelParams
{
	public static final String SIGN_TYPE = "md5";
	public static String CHANNEL_ID		= "meizu";
	
	public TrueMeizuParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new TrueMeizuParamsVO();
		}
		else
		{
			return (TrueMeizuParamsVO) vo;
		}
	}
	
	/*private Vector<TrueMeizuParamApp> apps = new Vector<TrueMeizuParamApp>();

	public Vector<TrueMeizuParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(TrueMeizuParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		TrueMeizuParamApp app = new TrueMeizuParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (TrueMeizuParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}*/
}

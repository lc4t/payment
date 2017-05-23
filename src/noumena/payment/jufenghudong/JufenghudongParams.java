package noumena.payment.jufenghudong;


import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class JufenghudongParams extends ChannelParams
{
	public static String CHANNEL_ID		= "jufenghudong";

	public JufenghudongParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new JufenghudongParamsVO();
		}
		else
		{
			return (JufenghudongParamsVO) vo;
		}
	}
	/*private Vector<JufenghudongParamApp> apps = new Vector<JufenghudongParamApp>();

	public Vector<JufenghudongParamApp> getApps()
	{
		return apps;
	}
	
	public void addApp(JufenghudongParamApp app)
	{
		this.getApps().add(app);
	}
	
	public void addApp(String appname, String appid, String appkey)
	{
		JufenghudongParamApp app = new JufenghudongParamApp();
		app.setAppname(appname);
		app.setAppid(appid);
		app.setAppkey(appkey);
		this.getApps().add(app);
	}
	
	public String getAppKeyById(String appid)
	{
		for (JufenghudongParamApp app : this.getApps())
		{
			if (app.getAppid().equals(appid))
			{
				return app.getAppkey();
			}
		}
		return null;
	}*/
}

package noumena.payment.mol;

import noumena.payment.util.ChannelParams;
import noumena.payment.util.ChannelParamsVO;

public class MOLParams extends ChannelParams
{
//	public static String PURCHASE_URL_TEST		= "http://molv3.molsolutions.com/api/login/u_module/purchase.aspx";
//	public static String PURCHASE_URL_RELEASE	= "https://global.mol.com/api/login/u_module/purchase.aspx";
	public static String HEARTBEAT_URL_TEST		= "http://molv3.molsolutions.com/api/login/s_module/heartbeat.asmx";
	public static String HEARTBEAT_URL_RELEASE	= "https://global.mol.com/api/login/s_module/heartbeat.asmx";
//
//	public static String MERCHANT_ID_TEST		= "201303050116";
//	public static String MERCHANT_ID_RELEASE	= "201305160165";
//	public static String SECRET_PIN_TEST		= "ezhq2456ezhq";
//	public static String SECRET_PIN_RELEASE		= "ryjo6935ryjo";
	
	public static String CHANNEL_ID		= "mol";

	public MOLParamsVO getParams(String appid)
	{
		ChannelParamsVO vo = super.getParamsVO(appid);
		if (vo == null)
		{
			return new MOLParamsVO();
		}
		else
		{
			return (MOLParamsVO) vo;
		}
	}
	
}

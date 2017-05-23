package noumena.payment.gash;

import java.util.Vector;

public class GASHParams
{
	public static String GASH_MSG_SUCCESS			= "交易成功";
	public static String GASH_MSG_ERROR_CODE		= "壓碼不正確";
	public static String GASH_MSG_FAILD_ORDER		= "交易失敗";
	public static String GASH_MSG_ORDER_TIMEOUT		= "交易超時";
	public static String GASH_MSG_ORDER_RELAY		= "交易稍後處理";
	public static String GASH_MSG_ORDER_INVALID		= "交易非法";
	
	public static String GASH_TRADE_URL_TEST		= "http://stage-api.eg.gashplus.com/CP_Module/order.aspx";
	public static String GASH_TRADE_URL_RELEASE		= "https://api.eg.gashplus.com/CP_Module/order.aspx";
	public static String GASH_SETTLE_URL_TEST		= "http://stage-api.eg.gashplus.com/CP_Module/settle.asmx";
	public static String GASH_SETTLE_URL_RELEASE	= "https://api.eg.gashplus.com/CP_Module/settle.asmx";
	public static String GASH_CHECK_URL_TEST		= "http://stage-api.eg.gashplus.com/CP_Module/checkorder.asmx";
	public static String GASH_CHECK_URL_RELEASE		= "https://api.eg.gashplus.com/CP_Module/checkorder.asmx";
	public static String GASH_CB_URL_TEST			= "http://211.151.64.145:6001/paymentsystem/gashcb";
	public static String GASH_CB_URL_RELEASE		= "http://asiapay.ko.cn/pay/gashcb";
	
	public static Vector<GASHPaytypeParams> paytypes	= new Vector<GASHPaytypeParams>();

	public static Vector<GASHPaytypeParams> getPaytypes()
	{
		return paytypes;
	}
}

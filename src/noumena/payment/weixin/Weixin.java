package noumena.payment.weixin;

public class Weixin {
	public static class Fields {
		public static final String APP_ID = "appid";
		public static final String MCH_ID = "mch_id";
		public static final String NONCE_STR = "nonce_str";
		public static final String SIGN = "sign";
		public static final String BODY = "body";
		public static final String OUT_TRAE_NO = "out_trade_no";
		public static final String TOTAL_FREE = "total_fee";
		public static final String NOTIFY_URL = "notify_url";
		public static final String IP = "spbill_create_ip";
		public static final String TRADE_TYPE = "trade_type";
		public static final String KEY = "key";
		public static final String XML = "xml";
		public static final String PARTNER_ID = "partnerid";
		public static final String PREPAY_ID = "prepayid";
		public static final String PACKAGE = "package";
		public static final String TIMESTAMP = "timestamp";
		public static final String RETURN_CODE = "return_code";
		public static final String RETURN_MSG = "return_msg";
		public static final String TIME_EXPIRE = "time_expire";
		public static final String DETAIL = "detail";
	}

	public static class Defaults {
		public static final String APP_ID = "";
		public static final String MCH_ID = "";
		public static final String TRADE_TYPE = "APP";
		public static final String NOTIFY_URL = "";
		public static final String API_KEY = "";
		public static final String PACKAGE = "Sign=WXPay";
		public static final int PAY_EXPIRE = 1440;
	}

	public static class UNIFIED_ORDER {
		public static final String APPID = "appid";
		public static final String NONCESTR = "noncestr";
		public static final String PARTNERID = "partnerid";
		public static final String PREPAYID = "prepayid";
		public static final String PACKAGE = "package";
		public static final String TIMESTAMP = "timestamp";
		public static final String SIGN = "sign";
		public static final String KEY = "key";
	}

	public static class CALLBACK_CODE {
		public static final String SUCCESS = "SUCCESS";
		public static final String FAIL = "FAIL";
	}

	public static class CALLBACK_MSG {
		public static final String OK = "OK";
		public static final String FAILED = "failed";
	}

	public static final class TRADE_CODE {
		public static final String SUCCESS = "SUCCESS";
		public static final String FAIL = "FAIL";
	}

	public static final class RefundStatus {
		public static final String SUCCESS = "SUCCESS";
		public static final String FAIL = "FAIL";
		public static final String PROCESSING = "PROCESSING";
		public static final String NOTSURE = "NOTSURE";
		public static final String CHANGE = "CHANGE";
	}
}

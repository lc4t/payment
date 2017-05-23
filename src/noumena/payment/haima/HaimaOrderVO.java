package noumena.payment.haima;

public class HaimaOrderVO
{
	private String notify_time;
	private String appid;
	private String out_trade_no;
	private String total_fee;
	private String subject;
	private String body;
	private String trade_status;
	private String sign;
	private String user_param;
	
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notifyTime) {
		notify_time = notifyTime;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String outTradeNo) {
		out_trade_no = outTradeNo;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String totalFee) {
		total_fee = totalFee;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getBody() {
		return body;
	}
	public void setBody(String body) {
		this.body = body;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public void setTrade_status(String tradeStatus) {
		trade_status = tradeStatus;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getUser_param() {
		return user_param;
	}
	public void setUser_param(String userParam) {
		user_param = userParam;
	}
}

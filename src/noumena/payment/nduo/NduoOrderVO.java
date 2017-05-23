package noumena.payment.nduo;

public class NduoOrderVO
{
	private String nduoTradeNo;
	private String appTradeNo;
	private String appKey;
	private String userToken;
	private String amount;
	private String server;
	private String serverName;
	private String subject;
	private String body;
	private String sign;
	
	public String getNduoTradeNo() {
		return nduoTradeNo;
	}
	public void setNduoTradeNo(String nduoTradeNo) {
		this.nduoTradeNo = nduoTradeNo;
	}
	public String getAppTradeNo() {
		return appTradeNo;
	}
	public void setAppTradeNo(String appTradeNo) {
		this.appTradeNo = appTradeNo;
	}
	public String getAppKey() {
		return appKey;
	}
	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}
	public String getUserToken() {
		return userToken;
	}
	public void setUserToken(String userToken) {
		this.userToken = userToken;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getServer() {
		return server;
	}
	public void setServer(String server) {
		this.server = server;
	}
	public String getServerName() {
		return serverName;
	}
	public void setServerName(String serverName) {
		this.serverName = serverName;
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
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
}

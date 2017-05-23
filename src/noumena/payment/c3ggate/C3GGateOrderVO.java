package noumena.payment.c3ggate;

public class C3GGateOrderVO
{
	private String orderid;
	private String gameid;
	private String token;
	private String cpid;
	private String access;
	private String paytotalfee;
	private String paytypeid;
	private String cporderid;
	private String stime;
	
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getGameid() {
		return gameid;
	}
	public void setGameid(String gameid) {
		this.gameid = gameid;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getCpid() {
		return cpid;
	}
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}
	public String getAccess() {
		return access;
	}
	public void setAccess(String access) {
		this.access = access;
	}
	public String getPaytotalfee() {
		return paytotalfee;
	}
	public void setPaytotalfee(String paytotalfee) {
		this.paytotalfee = paytotalfee;
	}
	public String getPaytypeid() {
		return paytypeid;
	}
	public void setPaytypeid(String paytypeid) {
		this.paytypeid = paytypeid;
	}
	public String getCporderid() {
		return cporderid;
	}
	public void setCporderid(String cporderid) {
		this.cporderid = cporderid;
	}
	public String getStime() {
		return stime;
	}
	public void setStime(String stime) {
		this.stime = stime;
	}
}

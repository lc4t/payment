package noumena.payment.coolpad;

public class CoolpadOrderVO
{
	/**
	 * {"appid":"5000003085","appuserid":"20010781","cporderid":"2016031805443911804Q",
	 * "cpprivate":"","currency":"RMB","feetype":2,"money":6.00,"paytype":401,"result":0,
	 * "transid":"32031603180544396056","transtime":"2016-03-18 05:44:56","transtype":0,"waresid":1}
	 */
	
	
	
	

	private String transtype;
	private String exorderno;
	private String transid;
	private String count;
	private String appid;
	private String waresid;
	private String feetype;
	private String money;
	private String currency;
	private String result;
	private String transtime;
	private String cpprivate;
	private String paytype;
	public String getTranstype() {
		return transtype;
	}
	public void setTranstype(String transtype) {
		this.transtype = transtype;
	}

	public String getExorderno() {
		return exorderno;
	}
	public void setExorderno(String exorderno) {
		this.exorderno = exorderno;
	}
	public String getTransid() {
		return transid;
	}
	public void setTransid(String transid) {
		this.transid = transid;
	}

	public String getCount() {
		return count;
	}
	public void setCount(String count) {
		this.count = count;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getWaresid() {
		return waresid;
	}
	public void setWaresid(String waresid) {
		this.waresid = waresid;
	}
	public String getFeetype() {
		return feetype;
	}
	public void setFeetype(String feetype) {
		this.feetype = feetype;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public String getResult() {
		return result;
	}
	public void setResult(String result) {
		this.result = result;
	}
	public String getTranstime() {
		return transtime;
	}
	public void setTranstime(String transtime) {
		this.transtime = transtime;
	}
	public String getCpprivate() {
		return cpprivate;
	}
	public void setCpprivate(String cpprivate) {
		this.cpprivate = cpprivate;
	}
	public String getPaytype() {
		return paytype;
	}
	public void setPaytype(String paytype) {
		this.paytype = paytype;
	}
	
	
}

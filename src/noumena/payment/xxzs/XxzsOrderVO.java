package noumena.payment.xxzs;

public class XxzsOrderVO
{
	private String trade_no;
	private String serialNumber;
	private String money;
	private String status;
	private String t;
	private String sign;
	private String appid;
	private String item_id;
	private String item_price;
	private String item_count;
	private String reserved;
	
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String tradeNo) {
		trade_no = tradeNo;
	}
	public String getSerialNumber() {
		return serialNumber;
	}
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}
	public String getMoney() {
		return money;
	}
	public void setMoney(String money) {
		this.money = money;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getT() {
		return t;
	}
	public void setT(String t) {
		this.t = t;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getItem_id() {
		return item_id;
	}
	public void setItem_id(String itemId) {
		item_id = itemId;
	}
	public String getItem_price() {
		return item_price;
	}
	public void setItem_price(String itemPrice) {
		item_price = itemPrice;
	}
	public String getItem_count() {
		return item_count;
	}
	public void setItem_count(String itemCount) {
		item_count = itemCount;
	}
	public String getReserved() {
		return reserved;
	}
	public void setReserved(String reserved) {
		this.reserved = reserved;
	}
}

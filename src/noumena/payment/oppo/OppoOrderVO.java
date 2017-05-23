package noumena.payment.oppo;

public class OppoOrderVO
{
	String UID;
	String MerchandiseName;
	String OrderMoney;
	String StartDateTime;
	String BankDateTime;
	String OrderStatus;
	String StatusMsg;
	String ExtInfo;
	
	public String getUID() {
		return UID;
	}
	public void setUID(String uID) {
		UID = uID;
	}
	public String getMerchandiseName() {
		return MerchandiseName;
	}
	public void setMerchandiseName(String merchandiseName) {
		MerchandiseName = merchandiseName;
	}
	public String getOrderMoney() {
		return OrderMoney;
	}
	public void setOrderMoney(String orderMoney) {
		OrderMoney = orderMoney;
	}
	public String getStartDateTime() {
		return StartDateTime;
	}
	public void setStartDateTime(String startDateTime) {
		StartDateTime = startDateTime;
	}
	public String getBankDateTime() {
		return BankDateTime;
	}
	public void setBankDateTime(String bankDateTime) {
		BankDateTime = bankDateTime;
	}
	public String getOrderStatus() {
		return OrderStatus;
	}
	public void setOrderStatus(String orderStatus) {
		OrderStatus = orderStatus;
	}
	public String getStatusMsg() {
		return StatusMsg;
	}
	public void setStatusMsg(String statusMsg) {
		StatusMsg = statusMsg;
	}
	public String getExtInfo() {
		return ExtInfo;
	}
	public void setExtInfo(String extInfo) {
		ExtInfo = extInfo;
	}
}

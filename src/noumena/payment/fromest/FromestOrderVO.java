package noumena.payment.fromest;

public class FromestOrderVO
{
	private String fmorderid;
	private String orderid;
	private String productid;
	private String cardno;
	private String amount;
	private String amountunit;
	private String ret;
	private String cardStatus;
	private String merPriv;
	private String verifystring;
	
	public String getFmorderid() {
		return fmorderid;
	}
	public void setFmorderid(String fmorderid) {
		this.fmorderid = fmorderid;
	}
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getProductid() {
		return productid;
	}
	public void setProductid(String productid) {
		this.productid = productid;
	}
	public String getCardno() {
		return cardno;
	}
	public void setCardno(String cardno) {
		this.cardno = cardno;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getAmountunit() {
		return amountunit;
	}
	public void setAmountunit(String amountunit) {
		this.amountunit = amountunit;
	}
	public String getRet() {
		return ret;
	}
	public void setRet(String ret) {
		this.ret = ret;
	}
	public String getCardStatus() {
		return cardStatus;
	}
	public void setCardStatus(String cardStatus) {
		this.cardStatus = cardStatus;
	}
	public String getMerPriv() {
		return merPriv;
	}
	public void setMerPriv(String merPriv) {
		this.merPriv = merPriv;
	}
	public String getVerifystring() {
		return verifystring;
	}
	public void setVerifystring(String verifystring) {
		this.verifystring = verifystring;
	}
}

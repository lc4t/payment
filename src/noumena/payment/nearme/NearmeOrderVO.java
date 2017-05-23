package noumena.payment.nearme;

public class NearmeOrderVO
{
	private String notify_id;
	private String partner_code;
	private String partner_order;
	private String orders;
	private String pay_result;
	private String sign;
	
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notify_id) {
		this.notify_id = notify_id;
	}
	public String getPartner_code() {
		return partner_code;
	}
	public void setPartner_code(String partner_code) {
		this.partner_code = partner_code;
	}
	public String getPartner_order() {
		return partner_order;
	}
	public void setPartner_order(String partner_order) {
		this.partner_order = partner_order;
	}
	public String getOrders() {
		return orders;
	}
	public void setOrders(String orders) {
		this.orders = orders;
	}
	public String getPay_result() {
		return pay_result;
	}
	public void setPay_result(String pay_result) {
		this.pay_result = pay_result;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
}

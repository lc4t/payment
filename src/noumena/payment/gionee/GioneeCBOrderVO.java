package noumena.payment.gionee;

public class GioneeCBOrderVO {
	private String player_id;
	private String api_key;
	private String deal_price;
	private String deliver_type;
	private String notify_url;
	private String out_order_no;
	private String subject;
	private String submit_time;
	private String total_fee;
	private String sign;
	public String getPlayer_id() {
		return player_id;
	}
	public void setPlayer_id(String playerId) {
		player_id = playerId;
	}
	public String getApi_key() {
		return api_key;
	}
	public void setApi_key(String apiKey) {
		api_key = apiKey;
	}
	public String getDeal_price() {
		return deal_price;
	}
	public void setDeal_price(String dealPrice) {
		deal_price = dealPrice;
	}
	public String getDeliver_type() {
		return deliver_type;
	}
	public void setDeliver_type(String deliverType) {
		deliver_type = deliverType;
	}
	public String getNotify_url() {
		return notify_url;
	}
	public void setNotify_url(String notifyUrl) {
		notify_url = notifyUrl;
	}
	public String getOut_order_no() {
		return out_order_no;
	}
	public void setOut_order_no(String outOrderNo) {
		out_order_no = outOrderNo;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getSubmit_time() {
		return submit_time;
	}
	public void setSubmit_time(String submitTime) {
		submit_time = submitTime;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String totalFee) {
		total_fee = totalFee;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	
	
	
}

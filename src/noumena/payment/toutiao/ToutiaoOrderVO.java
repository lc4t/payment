package noumena.payment.toutiao;

public class ToutiaoOrderVO
{
	private String notify_id;
	private String notify_type;
	private String notify_time;
	private String trade_status;
	private String way;
	private String client_id;
	private String out_trade_no;
	private String trade_no;
	private String pay_time;
	private String total_fee;
	private String buyer_id;
	private String tt_sign;
	private String tt_sign_type;
	
	
	public String getNotify_id() {
		return notify_id;
	}
	public void setNotify_id(String notifyId) {
		notify_id = notifyId;
	}
	public String getNotify_type() {
		return notify_type;
	}
	public void setNotify_type(String notifyType) {
		notify_type = notifyType;
	}
	public String getNotify_time() {
		return notify_time;
	}
	public void setNotify_time(String notifyTime) {
		notify_time = notifyTime;
	}
	public String getTrade_status() {
		return trade_status;
	}
	public void setTrade_status(String tradeStatus) {
		trade_status = tradeStatus;
	}
	public String getWay() {
		return way;
	}
	public void setWay(String way) {
		this.way = way;
	}
	public String getClient_id() {
		return client_id;
	}
	public void setClient_id(String clientId) {
		client_id = clientId;
	}
	public String getOut_trade_no() {
		return out_trade_no;
	}
	public void setOut_trade_no(String outTradeNo) {
		out_trade_no = outTradeNo;
	}
	public String getTrade_no() {
		return trade_no;
	}
	public void setTrade_no(String tradeNo) {
		trade_no = tradeNo;
	}
	public String getPay_time() {
		return pay_time;
	}
	public void setPay_time(String payTime) {
		pay_time = payTime;
	}
	public String getTotal_fee() {
		return total_fee;
	}
	public void setTotal_fee(String totalFee) {
		total_fee = totalFee;
	}
	public String getBuyer_id() {
		return buyer_id;
	}
	public void setBuyer_id(String buyerId) {
		buyer_id = buyerId;
	}
	public String getTt_sign() {
		return tt_sign;
	}
	public void setTt_sign(String ttSign) {
		tt_sign = ttSign;
	}
	public String getTt_sign_type() {
		return tt_sign_type;
	}
	public void setTt_sign_type(String ttSignType) {
		tt_sign_type = ttSignType;
	}
}

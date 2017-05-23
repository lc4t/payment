package noumena.payment.kugou.ribao;

public class RibaoOrderVO {
	private String orderid;
	private String outorderid;
	private String amount;
	private String username;
	private String status;
	private String time;
	private String ext1;
	private String ext2;
	private String sign;
	
	public String getOrderid() {
		return orderid;
	}
	public void setOrderid(String orderid) {
		this.orderid = orderid;
	}
	public String getOutorderid() {
		return outorderid;
	}
	public void setOutorderid(String outorderid) {
		this.outorderid = outorderid;
	}
	public String getAmount() {
		return amount;
	}
	public void setAmount(String amount) {
		this.amount = amount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		this.time = time;
	}
	public String getExt1() {
		return ext1;
	}
	public void setExt1(String ext1) {
		this.ext1 = ext1;
	}
	public String getExt2() {
		return ext2;
	}
	public void setExt2(String ext2) {
		this.ext2 = ext2;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	@Override
	public String toString() {
		return "RibaoOrderVO [orderid=" + orderid + ", outorderid=" + outorderid + ", amount=" + amount + ", username=" + username + ", status="
				+ status + ", time=" + time + ", ext1=" + ext1 + ", ext2=" + ext2 + ", sign=" + sign + "]";
	}
	public String buildContent() {
		return orderid + outorderid + amount + username + status + time + ext1 + ext2;
	}
}

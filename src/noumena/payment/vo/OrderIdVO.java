package noumena.payment.vo;

public class OrderIdVO {
	private String payId;
	private String time;
	private String msg;
	private String token;

	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public String getMsg()
	{
		return msg;
	}
	public void setMsg(String msg)
	{
		this.msg = msg;
	}

	public OrderIdVO() {
	}

	public OrderIdVO(String payId, String time) {
		this.payId = payId;
		this.time = time;
	}

	public String getPayId() {
		return payId;
	}

	public void setPayId(String payId) {
		this.payId = payId;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}

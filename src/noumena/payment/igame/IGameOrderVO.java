package noumena.payment.igame;

public class IGameOrderVO
{
	private String cp_order_id;
	private String correlator;
	private String result_code;
	private String fee;
	private String pay_type;
	private String method;
	private String sign;
	private String version;
	public String getCp_order_id() {
		return cp_order_id;
	}
	public void setCp_order_id(String cpOrderId) {
		cp_order_id = cpOrderId;
	}
	public String getCorrelator() {
		return correlator;
	}
	public void setCorrelator(String correlator) {
		this.correlator = correlator;
	}
	public String getResult_code() {
		return result_code;
	}
	public void setResult_code(String resultCode) {
		result_code = resultCode;
	}
	public String getFee() {
		return fee;
	}
	public void setFee(String fee) {
		this.fee = fee;
	}
	public String getPay_type() {
		return pay_type;
	}
	public void setPay_type(String payType) {
		pay_type = payType;
	}
	public String getMethod() {
		return method;
	}
	public void setMethod(String method) {
		this.method = method;
	}
	public String getSign() {
		return sign;
	}
	public void setSign(String sign) {
		this.sign = sign;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
}

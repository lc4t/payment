package noumena.payment.olleh;

public class OllehOrderVO
{
	private String symmetric_key;
	private String seq_key;
	private String code;
	private String app_id;
	private String di_id;
	private String price_vat;
	
	public String getSymmetric_key() {
		return symmetric_key;
	}
	public void setSymmetric_key(String symmetric_key) {
		this.symmetric_key = symmetric_key;
	}
	public String getSeq_key() {
		return seq_key;
	}
	public void setSeq_key(String seq_key) {
		this.seq_key = seq_key;
	}
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getApp_id() {
		return app_id;
	}
	public void setApp_id(String app_id) {
		this.app_id = app_id;
	}
	public String getDi_id() {
		return di_id;
	}
	public void setDi_id(String di_id) {
		this.di_id = di_id;
	}
	public String getPrice_vat() {
		return price_vat;
	}
	public void setPrice_vat(String price_vat) {
		this.price_vat = price_vat;
	}
}

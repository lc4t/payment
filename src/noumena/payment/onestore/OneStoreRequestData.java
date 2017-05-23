package noumena.payment.onestore;

public class OneStoreRequestData {
	private String txid;
	private String appid;
	private String signdata;

	public String getTxid() {
		return txid;
	}

	public void setTxid(String txid) {
		this.txid = txid;
	}

	public String getAppid() {
		return appid;
	}

	public void setAppid(String appid) {
		this.appid = appid;
	}

	public String getSigndata() {
		return signdata;
	}

	public void setSigndata(String signdata) {
		this.signdata = signdata;
	}

	public OneStoreRequestData(String txid, String appid, String signdata) {
		super();
		this.txid = txid;
		this.appid = appid;
		this.signdata = signdata;
	}

}

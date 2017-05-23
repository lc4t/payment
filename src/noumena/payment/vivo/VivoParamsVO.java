package noumena.payment.vivo;

import noumena.payment.util.ChannelParamsVO;


public class VivoParamsVO extends ChannelParamsVO
{
	private String cp_id;
	private String cp_key;
	private String vivo_ver;
	private String vivo_sign;
	private String callback_url;
	
	public String getCp_id() {
		return cp_id;
	}
	public void setCp_id(String cp_id) {
		this.cp_id = cp_id;
	}
	public String getCp_key() {
		return cp_key;
	}
	public void setCp_key(String cp_key) {
		this.cp_key = cp_key;
	}
	public String getVivo_ver() {
		return vivo_ver;
	}
	public void setVivo_ver(String vivo_ver) {
		this.vivo_ver = vivo_ver;
	}
	public String getVivo_sign() {
		return vivo_sign;
	}
	public void setVivo_sign(String vivo_sign) {
		this.vivo_sign = vivo_sign;
	}
	public String getCallback_url() {
		return callback_url;
	}
	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}
}

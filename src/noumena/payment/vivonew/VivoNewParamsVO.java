package noumena.payment.vivonew;

import noumena.payment.util.ChannelParamsVO;


public class VivoNewParamsVO extends ChannelParamsVO
{
	private String cp_id;
	private String cp_key;
	
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
}

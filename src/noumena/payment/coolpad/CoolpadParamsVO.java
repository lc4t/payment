package noumena.payment.coolpad;

import noumena.payment.util.ChannelParamsVO;


public class CoolpadParamsVO extends ChannelParamsVO
{
	private String callback_url;
	private String create_order_url;
	private String get_token_url;
	private String public_key;
	
	public String getPublic_key() {
		return public_key;
	}
	public void setPublic_key(String public_key) {
		this.public_key = public_key;
	}
	public String getCallback_url() {
		return callback_url;
	}
	public void setCallback_url(String callback_url) {
		this.callback_url = callback_url;
	}
	public String getCreate_order_url() {
		return create_order_url;
	}
	public void setCreate_order_url(String create_order_url) {
		this.create_order_url = create_order_url;
	}
	public String getGet_token_url() {
		return get_token_url;
	}
	public void setGet_token_url(String get_token_url) {
		this.get_token_url = get_token_url;
	}
}

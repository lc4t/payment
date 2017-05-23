package noumena.payment.naver;

import noumena.payment.util.ChannelParamsVO;


public class NaverParamsVO extends ChannelParamsVO
{
	private String cpid;
	private String privatekey;
	
	public String getCpid() {
		return cpid;
	}
	public void setCpid(String cpid) {
		this.cpid = cpid;
	}
	public String getPrivatekey() {
		return privatekey;
	}
	public void setPrivatekey(String privatekey) {
		this.privatekey = privatekey;
	}
}

package noumena.payment.model;

/***
 * 渠道屏蔽的实体
 * @author kz
 *
 */
public class Shielding implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String channel;

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public Shielding(String channel) {
		super();
		this.channel = channel;
	}

	public Shielding() {
		super();
	}
	
	

}

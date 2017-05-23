package noumena.payment.model;

/**
 * PayServer entity. @author MyEclipse Persistence Tools
 */

public class GashPayServer implements java.io.Serializable {

	// Fields

	private String serverId;
	private String gameId;
	private String serverName;
	private String callbackUrl;
	private String createTime;
	private String checkUrl;
	private String payNotify;

	// Constructors

	/** default constructor */
	public GashPayServer() {
	}

	/** minimal constructor */
	public GashPayServer(String serverId, String payNotify) {
		this.serverId = serverId;
		this.payNotify = payNotify;
	}

	/** full constructor */
	public GashPayServer(String serverId, String gameId, String serverName,
			String callbackUrl, String createTime, String checkUrl,
			String payNotify) {
		this.serverId = serverId;
		this.gameId = gameId;
		this.serverName = serverName;
		this.callbackUrl = callbackUrl;
		this.createTime = createTime;
		this.checkUrl = checkUrl;
		this.payNotify = payNotify;
	}

	// Property accessors

	public String getServerId() {
		return this.serverId;
	}

	public void setServerId(String serverId) {
		this.serverId = serverId;
	}

	public String getGameId() {
		return this.gameId;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
	}

	public String getServerName() {
		return this.serverName;
	}

	public void setServerName(String serverName) {
		this.serverName = serverName;
	}

	public String getCallbackUrl() {
		return this.callbackUrl;
	}

	public void setCallbackUrl(String callbackUrl) {
		this.callbackUrl = callbackUrl;
	}

	public String getCreateTime() {
		return this.createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getCheckUrl() {
		return this.checkUrl;
	}

	public void setCheckUrl(String checkUrl) {
		this.checkUrl = checkUrl;
	}

	public String getPayNotify() {
		return this.payNotify;
	}

	public void setPayNotify(String payNotify) {
		this.payNotify = payNotify;
	}

}
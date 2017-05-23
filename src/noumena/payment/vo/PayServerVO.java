package noumena.payment.vo;

public class PayServerVO
{
	private String serverId;
	private String gameId;
	private String serverName;
	private String callbackUrl;
	private String createTime;
	private String checkUrl;
	private String payNotify;
	
	public String getServerId()
	{
		return serverId;
	}
	public void setServerId(String serverId)
	{
		this.serverId = serverId;
	}
	public String getGameId()
	{
		return gameId;
	}
	public void setGameId(String gameId)
	{
		this.gameId = gameId;
	}
	public String getServerName()
	{
		return serverName;
	}
	public void setServerName(String serverName)
	{
		this.serverName = serverName;
	}
	public String getCallbackUrl()
	{
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl)
	{
		this.callbackUrl = callbackUrl;
	}
	public String getCreateTime()
	{
		return createTime;
	}
	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}
	public String getCheckUrl()
	{
		return checkUrl;
	}
	public void setCheckUrl(String checkUrl)
	{
		this.checkUrl = checkUrl;
	}
	public String getPayNotify()
	{
		return payNotify;
	}
	public void setPayNotify(String payNotify)
	{
		this.payNotify = payNotify;
	}
}

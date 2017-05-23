package noumena.payment.wo;

public class WoOrderVO
{
	private String orderid;
	private String ordertime;
	private String cpid;
	private String appid;
	private String fid;
	private String consumeCode;
	private String payfee;
	private String payType;
	private String hRet;
	private String status;
	private String signMsg;
	
	public String getOrderid()
	{
		return orderid;
	}
	public void setOrderid(String orderid)
	{
		this.orderid = orderid;
	}
	public String getOrdertime()
	{
		return ordertime;
	}
	public void setOrdertime(String ordertime)
	{
		this.ordertime = ordertime;
	}
	public String getCpid()
	{
		return cpid;
	}
	public void setCpid(String cpid)
	{
		this.cpid = cpid;
	}
	public String getAppid()
	{
		return appid;
	}
	public void setAppid(String appid)
	{
		this.appid = appid;
	}
	public String getFid()
	{
		return fid;
	}
	public void setFid(String fid)
	{
		this.fid = fid;
	}
	public String getConsumeCode()
	{
		return consumeCode;
	}
	public void setConsumeCode(String consumeCode)
	{
		this.consumeCode = consumeCode;
	}
	public String getPayfee()
	{
		return payfee;
	}
	public void setPayfee(String payfee)
	{
		this.payfee = payfee;
	}
	public String getPayType()
	{
		return payType;
	}
	public void setPayType(String payType)
	{
		this.payType = payType;
	}
	public String gethRet()
	{
		return hRet;
	}
	public void sethRet(String hRet)
	{
		this.hRet = hRet;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getSignMsg()
	{
		return signMsg;
	}
	public void setSignMsg(String signMsg)
	{
		this.signMsg = signMsg;
	}
}

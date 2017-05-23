package noumena.payment.cctc;

public class CCTCOrderDataVO
{
	private String orderid;
	private String phoneid;
	private String messageid;
	private String status;
	private String rettime;
	
	public String getOrderid()
	{
		return orderid;
	}
	public void setOrderid(String orderid)
	{
		this.orderid = orderid;
	}
	public String getPhoneid()
	{
		return phoneid;
	}
	public void setPhoneid(String phoneid)
	{
		this.phoneid = phoneid;
	}
	public String getMessageid()
	{
		return messageid;
	}
	public void setMessageid(String messageid)
	{
		this.messageid = messageid;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getRettime()
	{
		return rettime;
	}
	public void setRettime(String rettime)
	{
		this.rettime = rettime;
	}
}

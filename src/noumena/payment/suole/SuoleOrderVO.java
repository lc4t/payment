package noumena.payment.suole;

public class SuoleOrderVO
{
	private String tradnum;
	private String status;
	private String realamount;
	private String result;
	private String checking;
	
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result = result;
	}
	public String getTradnum()
	{
		return tradnum;
	}
	public void setTradnum(String tradnum)
	{
		this.tradnum = tradnum;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getRealamount()
	{
		return realamount;
	}
	public void setRealamount(String realamount)
	{
		this.realamount = realamount;
	}
	public String getChecking()
	{
		return checking;
	}
	public void setChecking(String checking)
	{
		this.checking = checking;
	}
}

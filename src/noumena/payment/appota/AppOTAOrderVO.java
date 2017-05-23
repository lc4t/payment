package noumena.payment.appota;

public class AppOTAOrderVO
{
	private String status;
	private String sandbox;
	private String transaction_id;
	private String transaction_type;
	private String amount;
	private String currency;
	private String country_code;
	private String state;
	private String target;
	private String hash;
	
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getSandbox()
	{
		return sandbox;
	}
	public void setSandbox(String sandbox)
	{
		this.sandbox = sandbox;
	}
	public String getTransaction_id()
	{
		return transaction_id;
	}
	public void setTransaction_id(String transaction_id)
	{
		this.transaction_id = transaction_id;
	}
	public String getTransaction_type()
	{
		return transaction_type;
	}
	public void setTransaction_type(String transaction_type)
	{
		this.transaction_type = transaction_type;
	}
	public String getAmount()
	{
		return amount;
	}
	public void setAmount(String amount)
	{
		this.amount = amount;
	}
	public String getCurrency()
	{
		return currency;
	}
	public void setCurrency(String currency)
	{
		this.currency = currency;
	}
	public String getCountry_code()
	{
		return country_code;
	}
	public void setCountry_code(String country_code)
	{
		this.country_code = country_code;
	}
	public String getState()
	{
		return state;
	}
	public void setState(String state)
	{
		this.state = state;
	}
	public String getTarget()
	{
		return target;
	}
	public void setTarget(String target)
	{
		this.target = target;
	}
	public String getHash()
	{
		return hash;
	}
	public void setHash(String hash)
	{
		this.hash = hash;
	}
}

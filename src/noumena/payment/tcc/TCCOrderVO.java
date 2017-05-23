package noumena.payment.tcc;

public class TCCOrderVO
{
	private String return_code;
	private String description;
	private String token;
	private String service_ID;
	private String serviceDescription;
	private String ratingCode;
	private String payerSubID;
	private String status;
	private String aaTime;
	
	public String getReturn_code()
	{
		return return_code;
	}
	public void setReturn_code(String return_code)
	{
		this.return_code = return_code;
	}
	public String getDescription()
	{
		return description;
	}
	public void setDescription(String description)
	{
		this.description = description;
	}
	public String getToken()
	{
		return token;
	}
	public void setToken(String token)
	{
		this.token = token;
	}
	public String getService_ID()
	{
		return service_ID;
	}
	public void setService_ID(String service_ID)
	{
		this.service_ID = service_ID;
	}
	public String getServiceDescription()
	{
		return serviceDescription;
	}
	public void setServiceDescription(String serviceDescription)
	{
		this.serviceDescription = serviceDescription;
	}
	public String getRatingCode()
	{
		return ratingCode;
	}
	public void setRatingCode(String ratingCode)
	{
		this.ratingCode = ratingCode;
	}
	public String getPayerSubID()
	{
		return payerSubID;
	}
	public void setPayerSubID(String payerSubID)
	{
		this.payerSubID = payerSubID;
	}
	public String getStatus()
	{
		return status;
	}
	public void setStatus(String status)
	{
		this.status = status;
	}
	public String getAaTime()
	{
		return aaTime;
	}
	public void setAaTime(String aaTime)
	{
		this.aaTime = aaTime;
	}
}

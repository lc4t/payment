package noumena.payment.ndpay;

public class NdpaycbRetVO
{
	private String ErrorCode;
	private String ErrorDesc;
	
	public String getErrorCode()
	{
		return ErrorCode;
	}
	public void setErrorCode(String errorCode)
	{
		ErrorCode = errorCode;
	}
	public String getErrorDesc()
	{
		return ErrorDesc;
	}
	public void setErrorDesc(String errorDesc)
	{
		ErrorDesc = errorDesc;
	}
}

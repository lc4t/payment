package noumena.payment.ios;

public class IOSOrderVO
{
	private String ResCode;			//100-successfully
	private String MerchantID;
	private String MRef_ID;
	private String Amount;
	private String Currency;
	private String MOLOrderID;
	private String MOLUsername;
	private String TransDateTime;
	private String Signature;
	
	public String getResCode()
	{
		return ResCode;
	}
	public void setResCode(String resCode)
	{
		ResCode = resCode;
	}
	public String getMerchantID()
	{
		return MerchantID;
	}
	public void setMerchantID(String merchantID)
	{
		MerchantID = merchantID;
	}
	public String getMRef_ID()
	{
		return MRef_ID;
	}
	public void setMRef_ID(String mRef_ID)
	{
		MRef_ID = mRef_ID;
	}
	public String getAmount()
	{
		return Amount;
	}
	public void setAmount(String amount)
	{
		Amount = amount;
	}
	public String getCurrency()
	{
		return Currency;
	}
	public void setCurrency(String currency)
	{
		Currency = currency;
	}
	public String getMOLOrderID()
	{
		return MOLOrderID;
	}
	public void setMOLOrderID(String mOLOrderID)
	{
		MOLOrderID = mOLOrderID;
	}
	public String getMOLUsername()
	{
		return MOLUsername;
	}
	public void setMOLUsername(String mOLUsername)
	{
		MOLUsername = mOLUsername;
	}
	public String getTransDateTime()
	{
		return TransDateTime;
	}
	public void setTransDateTime(String transDateTime)
	{
		TransDateTime = transDateTime;
	}
	public String getSignature()
	{
		return Signature;
	}
	public void setSignature(String signature)
	{
		Signature = signature;
	}
}

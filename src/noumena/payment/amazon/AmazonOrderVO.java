package noumena.payment.amazon;

public class AmazonOrderVO
{
	private String itemType;			//100-successfully
	private String startDate;
	private String endDate;
	private String sku;
	private String purchaseToken;
	
	public String getItemType()
	{
		return itemType;
	}
	public void setItemType(String itemType)
	{
		this.itemType = itemType;
	}
	public String getStartDate()
	{
		return startDate;
	}
	public void setStartDate(String startDate)
	{
		this.startDate = startDate;
	}
	public String getEndDate()
	{
		return endDate;
	}
	public void setEndDate(String endDate)
	{
		this.endDate = endDate;
	}
	public String getSku()
	{
		return sku;
	}
	public void setSku(String sku)
	{
		this.sku = sku;
	}
	public String getPurchaseToken()
	{
		return purchaseToken;
	}
	public void setPurchaseToken(String purchaseToken)
	{
		this.purchaseToken = purchaseToken;
	}
}

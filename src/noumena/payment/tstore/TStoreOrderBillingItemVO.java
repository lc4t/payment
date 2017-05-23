package noumena.payment.tstore;

public class TStoreOrderBillingItemVO
{
	private String tid;
	private String productId;
	private String logTime;
	private String chargeAmount;
	private String detailPname;
	private String bpInfo;
	private String tcashFlag;
	
	public String getTid()
	{
		return tid;
	}
	public void setTid(String tid)
	{
		this.tid = tid;
	}
	public String getProductId()
	{
		return productId;
	}
	public void setProductId(String productId)
	{
		this.productId = productId;
	}
	public String getLogTime()
	{
		return logTime;
	}
	public void setLogTime(String logTime)
	{
		this.logTime = logTime;
	}
	public String getChargeAmount()
	{
		return chargeAmount;
	}
	public void setChargeAmount(String chargeAmount)
	{
		this.chargeAmount = chargeAmount;
	}
	public String getDetailPname()
	{
		return detailPname;
	}
	public void setDetailPname(String detailPname)
	{
		this.detailPname = detailPname;
	}
	public String getBpInfo()
	{
		return bpInfo;
	}
	public void setBpInfo(String bpInfo)
	{
		this.bpInfo = bpInfo;
	}
	public String getTcashFlag()
	{
		return tcashFlag;
	}
	public void setTcashFlag(String tcashFlag)
	{
		this.tcashFlag = tcashFlag;
	}
}

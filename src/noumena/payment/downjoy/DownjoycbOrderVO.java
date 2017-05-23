package noumena.payment.downjoy;

public class DownjoycbOrderVO
{
	private String result;
	private String money;
	private String order;
	private String mid;
	private String time;
	private String signature;
	private String ext;
	private String appid;
	
	public String getResult()
	{
		return result;
	}
	public void setResult(String result)
	{
		this.result = result;
	}
	public String getAppid()
	{
		return appid;
	}
	public void setAppid(String appid)
	{
		this.appid = appid;
	}
	public String getMoney()
	{
		return money;
	}
	public void setMoney(String money)
	{
		this.money = money;
	}
	public String getOrder()
	{
		return order;
	}
	public void setOrder(String order)
	{
		this.order = order;
	}
	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public String getTime()
	{
		return time;
	}
	public void setTime(String time)
	{
		this.time = time;
	}
	public String getSignature()
	{
		return signature;
	}
	public void setSignature(String signature)
	{
		this.signature = signature;
	}
	public String getExt()
	{
		return ext;
	}
	public void setExt(String ext)
	{
		this.ext = ext;
	}
}

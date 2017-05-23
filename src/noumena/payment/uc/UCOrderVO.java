package noumena.payment.uc;

public class UCOrderVO
{
	private UCOrderDataVO data = new UCOrderDataVO();
	private String sign;
	private String ver;
	
	public UCOrderDataVO getData()
	{
		return data;
	}
	public void setData(UCOrderDataVO data)
	{
		this.data = data;
	}
	public String getSign()
	{
		return sign;
	}
	public void setSign(String sign)
	{
		this.sign = sign;
	}
	public String getVer() 
	{
		return ver;
	}
	public void setVer(String ver) 
	{
		this.ver = ver;
	}
}

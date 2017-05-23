package noumena.payment.wandoujia;


public class WanDoujiaNewOrderVo {
	private WndoujiaDataOrderVO data = new WndoujiaDataOrderVO();
	private String sign;
	private String ver;
	
	public WndoujiaDataOrderVO getData()
	{
		return data;
	}
	public void setData(WndoujiaDataOrderVO data)
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

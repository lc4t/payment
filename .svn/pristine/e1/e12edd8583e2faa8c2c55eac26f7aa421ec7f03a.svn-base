package noumena.payment.gash;

import java.util.Vector;

public class GASHPaytypeParams
{
	private String type;	//类型，自定义，1-点卡类；2-电信类；3-银行类
	private String mid;		//商家代碼
	private String cid;		//商家服務代碼
	private String key1;	//交易密鑰1
	private String key2;	//交易密鑰2
	private String pwd;		//交易密碼
	private Vector<GASHPaidParams> paids = new Vector<GASHPaidParams>();	//代收业者代码
	
	public String getType()
	{
		return type;
	}
	public void setType(String type)
	{
		this.type = type;
	}
	public GASHPaidParams getPaidParams(String paid)
	{
		for (int i = 0 ; i < paids.size() ; i++)
		{
			if (paids.get(i).getPaid().equals(paid))
			{
				return paids.get(i);
			}
		}
		return null;
	}
	public Vector<GASHPaidParams> getPaids()
	{
		return paids;
	}
	public String getMid()
	{
		return mid;
	}
	public void setMid(String mid)
	{
		this.mid = mid;
	}
	public String getCid()
	{
		return cid;
	}
	public void setCid(String cid)
	{
		this.cid = cid;
	}
	public String getKey1()
	{
		return key1;
	}
	public void setKey1(String key1)
	{
		this.key1 = key1;
	}
	public String getKey2()
	{
		return key2;
	}
	public void setKey2(String key2)
	{
		this.key2 = key2;
	}
	public String getPwd()
	{
		return pwd;
	}
	public void setPwd(String pwd)
	{
		this.pwd = pwd;
	}
}

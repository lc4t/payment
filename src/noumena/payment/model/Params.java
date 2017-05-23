package noumena.payment.model;

/**
 * Payinfo entity. @author MyEclipse Persistence Tools
 */

public class Params implements java.io.Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int id;
	private String appid;
	private String packagename;
	private String channel;
	private int payid;
	private String params;
	private String cburl;
	private String createtime;
	
	public Params()
	{
	}
	
	public Params(Params p)
	{
		this.setAppid(p.getAppid());
		this.setCburl(p.getCburl());
		this.setChannel(p.getChannel());
		this.setCreatetime(p.getCreatetime());
		this.setId(p.getId());
		this.setPackagename(p.getPackagename());
		this.setParams(p.getParams());
		this.setPayid(p.getPayid());
	}
	
	public String getCreatetime() {
		return createtime;
	}
	public void setCreatetime(String createtime) {
		this.createtime = createtime;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getAppid() {
		return appid;
	}
	public void setAppid(String appid) {
		this.appid = appid;
	}
	public String getPackagename() {
		return packagename;
	}
	public void setPackagename(String packagename) {
		this.packagename = packagename;
	}
	public String getChannel() {
		return channel;
	}
	public void setChannel(String channel) {
		this.channel = channel;
	}
	public int getPayid() {
		return payid;
	}
	public void setPayid(int payid) {
		this.payid = payid;
	}
	public String getParams() {
		return params;
	}
	public void setParams(String params) {
		this.params = params;
	}
	public String getCburl() {
		return cburl;
	}
	public void setCburl(String cburl) {
		this.cburl = cburl;
	}
}
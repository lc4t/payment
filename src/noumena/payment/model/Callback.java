package noumena.payment.model;

import noumena.payment.util.Constants;



/**
 * Callback generated by MyEclipse - Hibernate Tools
 */

public class Callback  implements java.io.Serializable {


    // Fields    

     /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String orderId;
     private String callbackUrl;
     private String callbackContent;
     private Integer callbackStatus = Constants.CALLBACK_STSTUS_DEFAULT;
     private Integer serverStatus = Constants.CALLBACK_SERVER_STSTUS_DEFAULT;
     private Integer KStatus;
     private String createTime;
     private String callbackTime;
     private String payRealPrice;


    // Constructors

    /** default constructor */
    public Callback() {
    }

	/** minimal constructor */
    public Callback(String orderId) {
        this.orderId = orderId;
    }
    
    /** full constructor */
    public Callback(String orderId, String callbackUrl, String callbackContent, Integer callbackStatus, Integer serverStatus, Integer KStatus, String createTime, String callbackTime) {
        this.orderId = orderId;
        this.callbackUrl = callbackUrl;
        this.callbackContent = callbackContent;
        this.callbackStatus = callbackStatus;
        this.serverStatus = serverStatus;
        this.KStatus = KStatus;
        this.createTime = createTime;
        this.callbackTime = callbackTime;
    }

   
    // Property accessors

    public String getCallbackContent()
	{
		return callbackContent;
	}

	public void setCallbackContent(String callbackContent)
	{
		this.callbackContent = callbackContent;
	}

    public String getOrderId() {
        return this.orderId;
    }
    
	public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getCallbackUrl() {
        return this.callbackUrl;
    }
    
    public void setCallbackUrl(String callbackUrl) {
        this.callbackUrl = callbackUrl;
    }

    public Integer getCallbackStatus() {
        return this.callbackStatus;
    }
    
    public void setCallbackStatus(Integer callbackStatus) {
        this.callbackStatus = callbackStatus;
    }

    public Integer getServerStatus() {
        return this.serverStatus;
    }
    
    public void setServerStatus(Integer serverStatus) {
        this.serverStatus = serverStatus;
    }

    public Integer getKStatus() {
        return this.KStatus;
    }
    
    public void setKStatus(Integer KStatus) {
        this.KStatus = KStatus;
    }

    public String getCreateTime()
	{
		return createTime;
	}

	public void setCreateTime(String createTime)
	{
		this.createTime = createTime;
	}

	public String getCallbackTime() {
        return this.callbackTime;
    }
    
    public void setCallbackTime(String callbackTime) {
        this.callbackTime = callbackTime;
    }

	public String getPayRealPrice() {
		return payRealPrice;
	}

	public void setPayRealPrice(String payRealPrice) {
		this.payRealPrice = payRealPrice;
	}
   








}
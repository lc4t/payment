package noumena.payment.googleplay;

import noumena.mgsplus.logs.util.DateUtil;

public class GooglePlayOrderVO
{
	private Integer id;
	private String txnId;
	private Integer userId;
	private String app;
	private String callbackUrl;
	private String logTime;
	private String paymentDate;
	private Float mcGross;
	private String itemId;
	private Integer quantity;
	private String mcCurrency;
	private String business;
	private String addressCountryCode;
	private String custom;
	private String cmd;
	private String memo;
	private String paymentStatus;
	private Integer status;
	private String imei;
	private String channel;
	private String ip;
	
	public Integer getId()
	{
		return id;
	}
	public void setId(Integer id)
	{
		this.id = id;
	}
	public String getTxnId()
	{
		return txnId;
	}
	public void setTxnId(String txnId)
	{
		this.txnId = txnId;
	}
	public Integer getUserId()
	{
		return userId;
	}
	public void setUserId(Integer userId)
	{
		this.userId = userId;
	}
	public String getApp()
	{
		return app;
	}
	public void setApp(String app)
	{
		this.app = app;
	}
	public String getCallbackUrl()
	{
		return callbackUrl;
	}
	public void setCallbackUrl(String callbackUrl)
	{
		this.callbackUrl = callbackUrl;
	}
	public String getLogTime()
	{
		return logTime;
	}
	public void setLogTime(String logTime)
	{
		this.logTime = logTime;
	}
	public String getPaymentDate()
	{
		return paymentDate;
	}
	public void setPaymentDate(String paymentDate)
	{
		this.paymentDate = paymentDate;
	}
	public Float getMcGross()
	{
		return mcGross;
	}
	public void setMcGross(Float mcGross)
	{
		this.mcGross = mcGross;
	}
	public String getItemId()
	{
		return itemId;
	}
	public void setItemId(String itemId)
	{
		this.itemId = itemId;
	}
	public Integer getQuantity()
	{
		return quantity;
	}
	public void setQuantity(Integer quantity)
	{
		this.quantity = quantity;
	}
	public String getMcCurrency()
	{
		return mcCurrency;
	}
	public void setMcCurrency(String mcCurrency)
	{
		this.mcCurrency = mcCurrency;
	}
	public String getBusiness()
	{
		return business;
	}
	public void setBusiness(String business)
	{
		this.business = business;
	}
	public String getAddressCountryCode()
	{
		return addressCountryCode;
	}
	public void setAddressCountryCode(String addressCountryCode)
	{
		this.addressCountryCode = addressCountryCode;
	}
	public String getCustom()
	{
		return custom;
	}
	public void setCustom(String custom)
	{
		this.custom = custom;
	}
	public String getCmd()
	{
		return cmd;
	}
	public void setCmd(String cmd)
	{
		this.cmd = cmd;
	}
	public String getMemo()
	{
		return memo;
	}
	public void setMemo(String memo)
	{
		this.memo = memo;
	}
	public String getPaymentStatus()
	{
		return paymentStatus;
	}
	public void setPaymentStatus(String paymentStatus)
	{
		this.paymentStatus = paymentStatus;
	}
	public Integer getStatus()
	{
		return status;
	}
	public void setStatus(Integer status)
	{
		this.status = status;
	}
	public String getImei()
	{
		return imei;
	}
	public void setImei(String imei)
	{
		this.imei = imei;
	}
	public String getChannel()
	{
		return channel;
	}
	public void setChannel(String channel)
	{
		this.channel = channel;
	}
	public String getIp()
	{
		return ip;
	}
	public void setIp(String ip)
	{
		this.ip = ip;
	}
	
	

	/** default constructor */
	public GooglePlayOrderVO() {
	}

	/** minimal constructor */
	public GooglePlayOrderVO(Integer status) {
		this.status = status;
	}

	/** full constructor */
	public GooglePlayOrderVO(String txnId, Integer userId, String app, String callbackUrl, String logTime, String paymentDate, Float mcGross, String itemId, Integer quantity, String mcCurrency,
			String business, String addressCountryCode, String custom, String cmd, String paymentStatus, String memo, Integer status) {
		this.txnId = txnId;
		this.userId = userId;
		this.app = app;
		this.callbackUrl = callbackUrl;
		this.logTime = logTime;
		this.paymentDate = paymentDate;
		this.mcGross = mcGross;
		this.itemId = itemId;
		this.quantity = quantity;
		this.mcCurrency = mcCurrency;
		this.business = business;
		this.addressCountryCode = addressCountryCode;
		this.custom = custom;
		this.cmd = cmd;
		this.memo = memo;
		this.status = status;
		this.paymentStatus = paymentStatus;
	}

	public GooglePlayOrderVO(int purchaseState,String notifyId,String productId,String orderId, String purchaseTime, String developerPayload){
		this.status = purchaseState;
		this.txnId = orderId;
		this.paymentDate = purchaseTime;
		this.custom = developerPayload;
		this.itemId = productId;
		this.logTime = DateUtil.getCurrentTime();
		this.business = GooglePlayParams.GOOGLEPLAY;
		
		//i={_imei}#c={_channel}#uid={_uid}#app={_app}#cb={_callbackUrl}
		String[] customs = developerPayload.split("#");
	 	for(int i=0; i<customs.length; i++){
	 		String[] subCm = customs[i].split("=");
	 		if(subCm[0].equals("i")){
	 			this.imei = subCm[1];
		 	}else if(subCm[0].equals("c")){
		 		this.channel = subCm[1];
		 	}else if(subCm[0].equals("uid")){
		 		try{this.userId = Integer.parseInt(subCm[1]);}catch(Exception e){e.printStackTrace();}
		 	}else if(subCm[0].equals("app")){
		 		this.app = subCm[1];
		 	}else if(subCm[0].equals("cb")){
			 	int pos = customs[i].indexOf("=");
			 	if(pos > 0) {
		 			String cbValue = customs[i].substring(pos+1,customs[i].length());
		 			String callbackUrl = cbValue;
		 			this.callbackUrl = callbackUrl;
			 	}
		 	}
	 	}
	}
}

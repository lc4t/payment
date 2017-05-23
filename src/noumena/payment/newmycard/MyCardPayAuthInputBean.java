package noumena.payment.newmycard;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/***
 * mycard 獲取交易授權碼的實體
 * 
 * @author kz
 * 
 */
public class MyCardPayAuthInputBean {
	// ※廠商服務代碼由 MyCard 編列
	private String FacServiceId;
	// ※廠商交易序號廠商自訂，每筆訂單編號不得重覆，為訂單資料 key 值只能用英數、底線(_)及連字號(-)
	private String FacTradeSeq;
	// ※交易模式1:Android SDK (手遊適用) 2:WEB
	private String TradeType;
	// ※伺服器代號用戶在廠商端的伺服器編號不可輸入中文僅允許 0-9 a-z A-Z . _ -
	private String ServerId;
	// 會員代號 用戶在廠商端的會員唯一識別編號僅允許 0-9 a-z A-Z . _ -
	private String CustomerId;
	// ※付費方式 / 付費方式群組代 碼參閱 4.1 付費方式和品項代碼查詢
	private String PaymentType;
	// 品項代碼
	private String ItemCode;
	// 產品名稱
	private String ProductName;
	// 交易金額
	private String Amount;
	// 幣別
	private String Currency;
	// 是否為測試環境
	private String SandBoxMode;
	// 驗證碼
	private String Hash;

	public String getFacServiceId() {
		return FacServiceId;
	}

	public void setFacServiceId(String facServiceId) {
		FacServiceId = facServiceId;
	}

	public String getFacTradeSeq() {
		return FacTradeSeq;
	}

	public void setFacTradeSeq(String facTradeSeq) {
		FacTradeSeq = facTradeSeq;
	}

	public String getTradeType() {
		return TradeType;
	}

	public void setTradeType(String tradeType) {
		TradeType = tradeType;
	}

	public String getServerId() {
		return ServerId;
	}

	public void setServerId(String serverId) {
		ServerId = serverId;
	}

	public String getCustomerId() {
		return CustomerId;
	}

	public void setCustomerId(String customerId) {
		CustomerId = customerId;
	}

	public String getPaymentType() {
		return PaymentType;
	}

	public void setPaymentType(String paymentType) {
		PaymentType = paymentType;
	}

	public String getItemCode() {
		return ItemCode;
	}

	public void setItemCode(String itemCode) {
		ItemCode = itemCode;
	}

	public String getProductName() {
		return ProductName;
	}

	public void setProductName(String productName) {
		ProductName = productName;
	}

	public String getAmount() {
		return Amount;
	}

	public void setAmount(String amount) {
		Amount = amount;
	}

	public String getCurrency() {
		return Currency;
	}

	public void setCurrency(String currency) {
		Currency = currency;
	}

	public String getSandBoxMode() {
		return SandBoxMode;
	}

	public void setSandBoxMode(String sandBoxMode) {
		SandBoxMode = sandBoxMode;
	}

	public String getHash() {
		return Hash;
	}

	public void setHash(String hash) {
		Hash = hash;
	}

	public MyCardPayAuthInputBean(String facServiceId, String facTradeSeq,
			String tradeType, String serverId, String customerId,
			String paymentType, String itemCode, String productName,
			String amount, String currency, String sandBoxMode, String hash) {
		super();
		FacServiceId = facServiceId;
		FacTradeSeq = facTradeSeq;
		TradeType = tradeType;
		ServerId = serverId;
		CustomerId = customerId;
		PaymentType = paymentType;
		ItemCode = itemCode;
		ProductName = productName;
		Amount = amount;
		Currency = currency;
		SandBoxMode = sandBoxMode;
		Hash = hash;
	}

	public MyCardPayAuthInputBean() {
		super();
	}

	public String params(String key) throws UnsupportedEncodingException {
		return "" + this.FacServiceId + this.FacTradeSeq  + this.TradeType 
				+ this.ServerId  + this.CustomerId + this.PaymentType 
				+ ItemCode + mycardEncode(this.ProductName)+ this.Amount
				+ Currency + SandBoxMode  + key;
	}
	
	public static String mycardEncode(String name) throws UnsupportedEncodingException{
		 String result="";
  	   String regEx = "[\u4e00-\u9fa5]";
			for(int i=0;i<name.length();i++){
				String tempString=name.substring(i, i+1);
				System.out.println(tempString);
				String endString=tempString;
			if(!Character.isDigit(tempString.charAt(0))){
				endString=URLEncoder.encode(tempString,"UTF-8");
				if(!(tempString.charAt(0) <= 'Z' && tempString.charAt(0) >= 'A')
                || (tempString.charAt(0) <= 'z' && tempString.charAt(0) >= 'a')){
					endString=URLEncoder.encode(tempString,"UTF-8").toLowerCase();
				}
			}else{
				Pattern pat = Pattern.compile(regEx);
				Matcher matcher = pat.matcher(tempString);
				if (matcher.find()){
					endString=URLEncoder.encode(tempString,"UTF-8").toLowerCase();
				}
			}
			result=result+endString;
			}
			return result;
	}
	
	
	
	public static void main(String arg[]) throws UnsupportedEncodingException{
		System.out.println(mycardEncode(")"));
	}

}

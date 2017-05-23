package noumena.payment.weixin;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;



public class WeixinParamPrepared
{
	private String appId;
	private String mchId;
	private String nonceStr;
	private String sign;
	private String body;
	private String detail;
	private String outTradeNo;
	private String totalFee;
	private String notifyUrl;
	private String ip;
	private String tradeType;
	private String timeExpire;
	
	private String key;
	
	
	public WeixinParamPrepared(String appId, String mchId, String nonceStr,
			 String body, String detail, String outTradeNo,
			String totalFee, String notifyUrl, String ip, String tradeType,String key
			) {
		super();
		this.appId = appId;
		this.mchId = mchId;
		this.nonceStr = nonceStr;
		this.body = body;
		this.detail = detail;
		this.outTradeNo = outTradeNo;
		this.totalFee = totalFee;
		this.notifyUrl = notifyUrl;
		this.ip = ip;
		this.tradeType = tradeType;
		this.key = key;
		this.formatPayExpire();
	}

	public String getXmlInfo() {
		StringBuilder builder = new StringBuilder();
		builder.append(appendXmlElement(Weixin.Fields.APP_ID, getAppId()));
		builder.append(appendXmlElement(Weixin.Fields.BODY, getBody()));
		builder.append(appendXmlElement(Weixin.Fields.DETAIL, getDetail()));
		builder.append(appendXmlElement(Weixin.Fields.MCH_ID, getMchId()));
		builder.append(appendXmlElement(Weixin.Fields.NONCE_STR, getNonceStr()));
		builder.append(appendXmlElement(Weixin.Fields.NOTIFY_URL, getNotifyUrl()));
		//builder.append(appendXmlElement(Weixin.Fields.SIGN, getSign()));
		builder.append(appendXmlElement(Weixin.Fields.OUT_TRAE_NO, getOutTradeNo()));
		builder.append(appendXmlElement(Weixin.Fields.IP, getIp()));
		builder.append(appendXmlElement(Weixin.Fields.TIME_EXPIRE, getTimeExpire()));
		builder.append(appendXmlElement(Weixin.Fields.TOTAL_FREE, getTotalFee()));
		builder.append(appendXmlElement(Weixin.Fields.TRADE_TYPE, getTradeType()));
		builder.append(appendXmlElement(Weixin.Fields.SIGN, sign()));
		
		
		System.out.println(builder.toString());
		String xml = appendXmlElement(Weixin.Fields.XML, builder.toString());
		try {
			return new String(xml.getBytes("UTF-8"), "ISO-8859-1");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
	
	private void formatPayExpire() {
		final int MAX_MINUTES = 1440;
		final int MIN_MINUTES = 5;
		int minutes = Weixin.Defaults.PAY_EXPIRE;
		try {
			minutes = Integer.valueOf(timeExpire);
			minutes = Math.min(MAX_MINUTES, minutes);
			minutes = Math.max(MIN_MINUTES, minutes);
		} catch (Exception e) {
		}
		this.timeExpire = wXPayFormat(minutes);
	}
	
	public static String wXPayFormat(int minutes) {
		long ms = System.currentTimeMillis();
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
		return df.format(new Date(ms + minutes * 60 * 1000));
	}
	
	public String sign() {
		StringBuilder builder = new StringBuilder();
		appendSign(builder, Weixin.Fields.APP_ID, getAppId());
		appendSign(builder, Weixin.Fields.BODY, getBody());
		appendSign(builder, Weixin.Fields.DETAIL, getDetail());
		appendSign(builder, Weixin.Fields.MCH_ID, getMchId());
		appendSign(builder, Weixin.Fields.NONCE_STR, getNonceStr());
		appendSign(builder, Weixin.Fields.NOTIFY_URL, getNotifyUrl());
		appendSign(builder, Weixin.Fields.OUT_TRAE_NO, getOutTradeNo());
		appendSign(builder, Weixin.Fields.IP, getIp());
		appendSign(builder, Weixin.Fields.TIME_EXPIRE, getTimeExpire());
		appendSign(builder, Weixin.Fields.TOTAL_FREE, getTotalFee());
		appendSign(builder, Weixin.Fields.TRADE_TYPE, getTradeType());
		
		appendSign(builder, Weixin.Fields.KEY, getKey());
		builder.deleteCharAt(builder.length() - 1);
		System.out.println(builder.toString());
		setSign(MD5.getMessageDigest(builder.toString().getBytes()).toUpperCase());
		return getSign();
	}
	
	public static String appendXmlElement(String key, String value) {
		return String.format("<%s>%s</%s>", key, value, key);
	}
	
	public static void appendSign(StringBuilder builder, String key, String value) {
			builder.append(key);
			builder.append("=");
			builder.append(value);
			builder.append("&");
	}

	public String getAppId() {
		return appId;
	}

	public void setAppId(String appId) {
		this.appId = appId;
	}

	public String getMchId() {
		return mchId;
	}

	public void setMchId(String mchId) {
		this.mchId = mchId;
	}

	public String getNonceStr() {
		return nonceStr;
	}

	public void setNonceStr(String monceStr) {
		this.nonceStr = monceStr;
	}

	public String getSign() {
		return sign;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getOutTradeNo() {
		return outTradeNo;
	}

	public void setOutTradeNo(String outTradeNo) {
		this.outTradeNo = outTradeNo;
	}

	public String getTotalFee() {
		return totalFee;
	}

	public void setTotalFee(String totalFee) {
		this.totalFee = totalFee;
	}

	public String getNotifyUrl() {
		return notifyUrl;
	}

	public void setNotifyUrl(String notifyUrl) {
		this.notifyUrl = notifyUrl;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getTradeType() {
		return tradeType;
	}

	public void setTradeType(String tradeType) {
		this.tradeType = tradeType;
	}

	public String getTimeExpire() {
		return timeExpire;
	}

	public void setTimeExpire(String timeExpire) {
		this.timeExpire = timeExpire;
	}

	public String getDetail() {
		return detail;
	}

	public void setDetail(String detail) {
		this.detail = detail;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}

package noumena.payment.lenovo.tools;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class CpTransSyncSignValidNew {

	public static final String CHARSET = "utf-8";
	
//	/**
//	 * 公钥验签
//	 * @param transdata	待加密数据
//	 * @param sign		同步签名
//	 * @param publicKey	CP公钥
//	 * @return
//	 */
//	public static Boolean validSign(String transdata,String sign,String publicKey){
//		boolean ret = Tools.signVeryfy(transdata, sign, publicKey,CHARSET);
//		return ret;
//	}
	
	/**
	 * 私钥加密
	 * @param transdata	加密数据
	 * @param privateKey	CP私钥
	 * @return
	 */
	public static String sign(String transdata,String privateKey){
		String sign = Tools.sign(transdata, privateKey,CHARSET);
		return sign;
	}
	
	/**
	 * 
	 * @param privateKey
	 * @param sign
	 * @param transdata
	 * @return
	 */
	public static Boolean verifySign(String privateKey, String sign, String transdata) {
		String tmp = Tools.sign(transdata, privateKey, CHARSET);
		if (sign.equals(tmp)) {
			return true;
		}
		return false;
	}
	
	/**
	 * 公钥验签
	 * @param transdata	待加密数据
	 * @param sign		同步签名
	 * @param privateKey	CP私钥
	 * @return
	 */
	public static Boolean validSign(String transdata,String sign,String privateKey){
		String tmp = Tools.sign(transdata, privateKey, CHARSET);
		if (sign.equals(tmp)) {
			return true;
		}
		try {
			if (tmp.equals(URLDecoder.decode(sign, CHARSET))) {
				return true;
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return false;
	}
	
}

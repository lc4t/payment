package noumena.payment.tencent;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * @author wuzhuang
 */
public class TencentSignUtils 
{
	private static final String MAC_NAME = "HmacSHA1";
	private static final String ENCODING = "UTF-8";
	
	
	/**
	 * 使用 HMAC-SHA1 签名方法对对 encryptText 进行签名
	 * 
	 * @param encryptText
	 *            被签名的字符串
	 * @param encryptKey
	 *            密钥
	 * @return 返回被加密后的字符串
	 * @throws Exception
	 */
	public static byte[] HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception
	{
		byte[] data = encryptKey.getBytes(ENCODING);
		SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
		Mac mac = Mac.getInstance(MAC_NAME);
		mac.init(secretKey);
		byte[] text = encryptText.getBytes(ENCODING);
		return mac.doFinal(text);
	}
	
	public static String sortByKey(Map<String, String> params)
	{
		List<String> keys = new ArrayList<String>(params.keySet());
		keys.remove("sig");
		Collections.sort(keys);
		
		String prestr = "";
		
		for (int i = 0; i < keys.size(); i++) {
			String key = keys.get(i);
			String value = params.get(key);
			if (i == keys.size()-1) {
				prestr = prestr + key + "=" + value;
			}else {
				prestr = prestr + key + "=" + value + "&";
			}
		}
		
		return prestr;
	}
}

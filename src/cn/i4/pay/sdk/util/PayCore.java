package cn.i4.pay.sdk.util;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;
import cn.i4.pay.sdk.conf.PayConfig;

/**
 * 
 * @author Jonny
 *
 */
public class PayCore {
	
	/** = */
	public static final String QSTRING_EQUAL = "=";

	/** & */
	public static final String QSTRING_SPLIT = "&";
	
    /** 
     * 除去请求要素中的空值和签名参数
     * @param para 请求要素
     * @return 去掉空值与签名参数后的请求要素
     */
    public static Map<String, String> paraFilter(Map<String, String> para) {
        Map<String, String> result = new HashMap<String, String>();
        if (para == null || para.size() <= 0) {
            return result;
        }
        for (String key : para.keySet()) {
            String value = para.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase(PayConfig.SIGNATURE)) {
                continue;
            }
            try {
				value = URLDecoder.decode(value, PayConfig.CHARSET);
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
            result.put(key, value);
        }

        return result;
    }


    /**
     * 把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param para 请求要素
     * @param sort 是否需要根据key值作升序排列
     * @param encode 是否需要URL编码
     * @return 拼接成的字符串
     */
    public static String createLinkString(Map<String, String> para, boolean sort, boolean encode) {
        List<String> keys = new ArrayList<String>(para.keySet());
        if (sort)
        	Collections.sort(keys);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            String key = keys.get(i);
            String value = para.get(key);
            
            if (encode) {
				try {
					value = URLEncoder.encode(value, PayConfig.CHARSET);
				} catch (UnsupportedEncodingException e) {
				}
            }
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                sb.append(key).append(QSTRING_EQUAL).append(value);
            } else {
                sb.append(key).append(QSTRING_EQUAL).append(value).append(QSTRING_SPLIT);
            }
        }
        return sb.toString();
    }

	/**
	 * 解析应答字符串，生成应答要素
	 * 
	 * @param str 需要解析的字符串
	 * @return 解析的结果map
	 * @throws UnsupportedEncodingException
	 */
	public static Map<String, String> parseQString2(String str) throws UnsupportedEncodingException {
		Map<String, String> map = new HashMap<String, String>();
		int len = str.length();
		StringBuilder temp = new StringBuilder();
		char curChar;
		String key = null;
		boolean isKey = true;
		for (int i = 0; i < len; i++) {// 遍历整个带解析的字符串
			curChar = str.charAt(i);// 取当前字符
			if (curChar == '&') {// 如果读取到&分割符
				putKeyValueToMap(temp, isKey, key, map);
				temp.setLength(0);
				isKey = true;
			} else {
				if (isKey) {// 如果当前生成的是key
					if (curChar == '=') {// 如果读取到=分隔符
						key = temp.toString();
						temp.setLength(0);
						isKey = false;
					} else {
						temp.append(curChar);
					}
				} else {// 如果当前生成的是value
					temp.append(curChar);
				}
			}
		}
		putKeyValueToMap(temp, isKey, key, map);
		return map;
	}
	
	/**
     * 生成签名
     * @param req 需要解析签名的要素
     * @return 解析签名的结果map
	 * @throws Exception 
     */
    public static Map<String, String> parseSignature(String sign, String key) throws Exception {
		BASE64Decoder base64Decoder = new BASE64Decoder();
        byte[] dcDataStr = base64Decoder.decodeBuffer(sign);
        byte[] plainData = RSADecrypt.decryptByPublicKey(dcDataStr, key);
        String parseString = new String(plainData);
        System.out.println("parseString=" + parseString);
		return parseQString2(parseString);
    }
	
	private static void putKeyValueToMap(StringBuilder temp, boolean isKey,
			String key, Map<String, String> map) throws UnsupportedEncodingException {
		if (isKey) {
			key = temp.toString();
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, "");
		} else {
			if (key.length() == 0) {
				throw new RuntimeException("QString format illegal");
			}
			map.put(key, URLDecoder.decode(temp.toString(), PayConfig.CHARSET));
		}
	}
    
}

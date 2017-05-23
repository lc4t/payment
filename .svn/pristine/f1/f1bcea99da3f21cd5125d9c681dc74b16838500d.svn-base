package noumena.payment.lenovo.tools;

import java.util.Map;


public class Tools {
	
    /** 
     * 把数组所有元素排序，并按照“参数=参数值”的模式用“&”字符拼接成字符串
     * @param params 需要排序并参与字符拼接的参数组
     * @return 拼接后字符串
     */
	public static String createLinkString(Map<String, String> params){
		return SignCore.createLinkString(params);
	}
	
	/**
	* RSA签名
	* @param content 待签名数据
	* @param privateKey 商户私钥
	* @param input_charset 编码格式
	* @return 签名值
	*/
	public static String sign(String content, String privateKey, String input_charset) {
		return Rsa.sign(content, privateKey, input_charset);
	}
	
	/**
	* RSA验签名检查
	* @param content 待签名数据
	* @param sign 签名值
	* @param ali_public_key 支付宝公钥
	* @param input_charset 编码格式
	* @return 布尔值
	*/
	public static boolean verify(String content, String sign, String publicKey, String charset){
		return Rsa.verify(content, sign, publicKey, charset);
	}
	
	 /**
     * 根据反馈回来的信息，生成签名结果
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	public static boolean signVeryfy(Map<String, String> params, String sign,String publicKey,String charset) {
    	//过滤空值、sign与sign_type参数
    	Map<String, String> sParaNew = SignCore.paraFilter(params);
        //获取待签名字符串
        String preSignStr = createLinkString(sParaNew);
        //获得签名验证结果
        boolean isSign = false;
        isSign = verify(preSignStr, sign, publicKey, charset);
//		System.out.println("preSignStr:" + preSignStr + " sign:" + sign + " publicKey:" + publicKey);
        return isSign;
    }
	
    /**
     * 签名json串，并校验
     * @param Params 通知返回来的参数数组
     * @param sign 比对的签名结果
     * @return 生成的签名结果
     */
	public static boolean signVeryfy(String json, String sign,String publicKey,String charset) {
        //获得签名验证结果
        boolean isSign = false;
        isSign = verify(json, sign, publicKey, charset);
//		System.out.println("preSignStr:" + preSignStr + " sign:" + sign + " publicKey:" + publicKey);
        return isSign;
    }
	
//	/**
//	 * 将对象转换为JSON字符串
//	 * @param object
//	 * @return
//	 */
//	public static String toJson(Object object) {
//		ObjectMapper mapper = new ObjectMapper();
//		try {
//			return mapper.writeValueAsString(object);
//		} catch (Exception e) {
//			e.printStackTrace();
//			return null;
//		}
//	}
}

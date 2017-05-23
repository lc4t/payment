package noumena.payment.coolpad;

import java.math.BigInteger;

public class SignHelper
{
	/**
	 * Desc:cp交易同步签名验证
	 * 
	 * @param transdata
	 *            同步过来的transdata数据
	 * @param sign
	 *            同步过来的sign数据
	 * @param key
	 *            应用的密钥(商户可从商户自服务系统获取)
	 * @return 验证签名结果 true:验证通过 false:验证失败
	 */
	public static boolean validSign(String transdata, String sign, String key) {
		try {
			String md5Str = MD5.md5Digest(transdata);

			String decodeBaseStr = Base64.decode(key);

			String[] decodeBaseVec = decodeBaseStr.replace('+', '#').split("#");

			String privateKey = decodeBaseVec[0];
			String modkey = decodeBaseVec[1];

			String reqMd5 = RSAUtil.decrypt(sign, new BigInteger(privateKey),
					new BigInteger(modkey));

			if (md5Str.equals(reqMd5)) {
				return true;
			} else {
				return false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return false;

	}
	
	public static String genSign(String transdata, String key) {
		String sign = "";
		try {
			// 获取privatekey和modkey
			String decodeBaseStr = Base64.decode(key);

			String[] decodeBaseVec = decodeBaseStr.replace('+', '#').split("#");

			String privateKey = decodeBaseVec[0];
			String modkey = decodeBaseVec[1];

			// 生成sign的规则是先md5,再rsa
			String md5Str = MD5.md5Digest(transdata);

			sign = RSAUtil.encrypt(md5Str, new BigInteger(privateKey),
					new BigInteger(modkey));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return sign;

	}
}

package noumena.payment.xiaoqi;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;

import org.apache.commons.codec.binary.Base64;

public class CXiaoQiCharge {
	private static CXiaoQiParams params = new CXiaoQiParams();
	// RSA最大解密密文大小
	private static int MAX_DECRYPT_BLOCK = 128;
    public static final String SIGN_ALGORITHMS = "SHA1WithRSA";  
	public static String getCallbackFromXiaoQi(Map<String, String> xiaoqiparams) {
		System.out.println("XiaoQiCharge====================param "
				+ params.toString());
		String encrypData = xiaoqiparams.get("encryp_data");
		String gameOrderid = xiaoqiparams.get("game_orderid");
		String guid = xiaoqiparams.get("guid");
		String subject = xiaoqiparams.get("subject");
		String xiaoQiGoid = xiaoqiparams.get("xiao7_goid");
		String signData = xiaoqiparams.get("sign_data");
		try {
			
			// 取出订单信息
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(gameOrderid);
			// 订单不存在
			if (order == null) {
				System.out
						.println(gameOrderid
								+ "XiaoQiCharge====================order is not exist ");
				return "ERROR";
			}
			// 取出对应的appid
			String appid = order.getAppId();
			String appKey = params.getParams(appid).getAppkey();
			
			//验证签名
			Map<String, String> map = new TreeMap<>();
	        map.put("encryp_data", encrypData);
	        map.put("game_orderid", gameOrderid);
	        map.put("guid", guid);
	        map.put("subject", subject);
	        map.put("xiao7_goid", xiaoQiGoid);
	        String sourceStr = buildHttpQuery(map);
	        if ( !doCheck(sourceStr, signData, loadPublicKeyByStr(appKey)) )
            {
	        	System.out
				.println(gameOrderid
						+ "XiaoQiCharge====================sign data is error");
		    	return "failed"; 
            }
			PublicKey publicKey = loadPublicKeyByStr(appKey);
			byte[] encrypDataByte = decrypt(publicKey, decode(encrypData));
			String encrypDataResult = new String(encrypDataByte);
		    Map<String, String> decryptMap = decodeHttpQuery(encrypDataResult);
		    //验证解密信息里的订单号
		    if (!decryptMap.containsKey("game_orderid") || !decryptMap.get("game_orderid").equals(gameOrderid)){
		    	System.out
				.println(gameOrderid
						+ "XiaoQiCharge====================order is difference"+decryptMap.containsKey("game_orderid"));
		    	return "failed"; 
            }
		    if(!decryptMap.containsKey("payflag")|| !decryptMap.get("payflag").equals("1")){
		    	System.out
				.println(gameOrderid
						+ "XiaoQiCharge====================order is fail"+decryptMap.get("payflag"));
		    	return "failed"; 
		    }
		     Float payPrice= Float.parseFloat(decryptMap.get("pay"));
		    if(!decryptMap.containsKey("pay")||!payPrice.equals(order.getAmount())){
		    	System.out
				.println(gameOrderid
						+ "XiaoQiCharge====================order amount is difference"+decryptMap.get("pay")+" "+order.getAmount());
		    	return "failed"; 	
		    }
			
			if (order.getKStatus().equals(Constants.K_STSTUS_SUCCESS)) {
				System.out
						.println(gameOrderid
								+ "XiaoQiCharge====================order is already success");
				return "success";
			}
			if (order.getKStatus() != Constants.K_STSTUS_DEFAULT) {
				System.out
						.println(gameOrderid
								+ "XiaoQiCharge====================order is repeated ");
				return "failed";
			}
			bean.updateOrderAmountPayIdExinfo(decryptMap.get("game_orderid"), gameOrderid, decryptMap.get("pay"),
					subject);
			bean.updateOrderKStatus(decryptMap.get("game_orderid"), Constants.K_STSTUS_SUCCESS);
			return "success";
		} catch (Exception e) {
			System.out.println(gameOrderid
					+ "XiaoQiCharge====================system error ");
			e.printStackTrace();
			return "failed";
		}

	}

	// 公钥解密
	public static byte[] decrypt(PublicKey publicKey, byte[] cipherData)
			throws Exception {
		if (publicKey == null) {
			throw new Exception("解密公钥为空, 请设置");
		}
		Cipher cipher;
		try {
			cipher = Cipher.getInstance("RSA");
			cipher.init(Cipher.DECRYPT_MODE, publicKey);

			int inputLen = cipherData.length;
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			int offSet = 0;
			byte[] cache;
			int i = 0;
			// 对数据分段解密
			while (inputLen - offSet > 0) {
				if (inputLen - offSet > MAX_DECRYPT_BLOCK) {
					cache = cipher.doFinal(cipherData, offSet,
							MAX_DECRYPT_BLOCK);
				} else {
					cache = cipher.doFinal(cipherData, offSet, inputLen
							- offSet);
				}
				out.write(cache, 0, cache.length);
				i++;
				offSet = i * MAX_DECRYPT_BLOCK;
			}
			byte[] decryptedData = out.toByteArray();
			out.close();
			return decryptedData;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此解密算法");
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
			return null;
		} catch (InvalidKeyException e) {
			throw new Exception("解密公钥非法,请检查");
		} catch (IllegalBlockSizeException e) {
			throw new Exception("密文长度非法");
		} catch (BadPaddingException e) {
			throw new Exception("密文数据已损坏");
		}
	}

	// 从字符串加载公钥
	public static PublicKey loadPublicKeyByStr(String publicKeyString)
			throws Exception {
		try {
			String publicKeyStr = "";

			int count = 0;
			for (int i = 0; i < publicKeyString.length(); ++i) {
				if (count < 64) {
					publicKeyStr += publicKeyString.charAt(i);
					count++;
				} else {
					publicKeyStr += publicKeyString.charAt(i) + "\r\n";
					count = 0;
				}
			}
			byte[] buffer = decode(publicKeyStr);
			KeyFactory keyFactory = KeyFactory.getInstance("RSA");
			X509EncodedKeySpec keySpec = new X509EncodedKeySpec(buffer);
			PublicKey publicKey = keyFactory.generatePublic(keySpec);
			// System.out.println(publicKey);
			return publicKey;
		} catch (NoSuchAlgorithmException e) {
			throw new Exception("无此算法");
		} catch (InvalidKeySpecException e) {
			throw new Exception("公钥非法");
		} catch (NullPointerException e) {
			throw new Exception("公钥数据为空");
		}
	}

	// Base64编码
	public String encode(final byte[] bytes) {
		return new String(Base64.encodeBase64(bytes));
	}

	// Base64解码
	public static byte[] decode(String str) {
		return Base64.decodeBase64(str.getBytes());
	}

	// 解码http查询字符串
	private static Map<String, String> decodeHttpQuery(String httpQuery)
			throws UnsupportedEncodingException {
		Map<String, String> map = new TreeMap<>();

		for (String s : httpQuery.split("&")) {
			String pair[] = s.split("=");
			map.put(URLDecoder.decode(pair[0], "utf-8"),
					URLDecoder.decode(pair[1], "utf-8"));
		}

		return map;
	}
	 //创建http查询字符串
    private static String buildHttpQuery(Map<String, String> data) throws UnsupportedEncodingException {
        String builder = new String();
        for (Entry<String, String> pair : data.entrySet()) {
            builder += URLEncoder.encode(pair.getKey(), "utf-8") + "=" + URLEncoder.encode(pair.getValue(), "utf-8") + "&";
        }
        return builder.substring(0, builder.length() - 1);
    }
    
    //RSA验签名检查   
    public static boolean doCheck(String content, String sign, PublicKey publicKey)  
    {  
        try   
        {  
            java.security.Signature signature = java.security.Signature.getInstance(SIGN_ALGORITHMS);  
          
            signature.initVerify(publicKey);
            //System.out.println(content.getBytes());
            signature.update(content.getBytes());
          
            boolean bverify = signature.verify(decode(sign));  
            return bverify;  
              
        }   
        catch (Exception e)   
        {  
            e.printStackTrace();
            return false;  
        }         
    }  
    

	public static void main(String arg[]) {

	}

	public static void init() {
		params.initParams(CXiaoQiParams.CHANNEL_ID, new CXiaoQinVO());
	}

}

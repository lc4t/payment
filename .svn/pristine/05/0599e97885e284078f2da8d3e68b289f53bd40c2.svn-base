package noumena.payment.itools;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.Cipher;

import noumena.payment.lenovo.util.Base64;



public class RSASignature
{
	public static final String RSA_PUBLIC ="MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQC2kcrRvxURhFijDoPpqZ/IgPlAgppkKrek6wSrua1zBiGTwHI2f+YCa5vC1JEiIi9uw4srS0OSCB6kY3bP2DGJagBoEgj/rYAGjtYJxJrEiTxVs5/GfPuQBYmU0XAtPXFzciZy446VPJLHMPnmTALmIOR5Dddd1Zklod9IQBMjjwIDAQAB";
	public static final String  SIGN_ALGORITHMS = "SHA1WithRSA";

	/**
	* 得到公钥
	*/
	public static PublicKey getPublicKey() throws Exception 
	{
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		byte[] tmp = RSA_PUBLIC.getBytes("UTF-8");
		byte[] encodedKey = Base64.decodeBase64(tmp);
		PublicKey pubKey = keyFactory.generatePublic(new X509EncodedKeySpec(encodedKey));

		return pubKey;
	}

	/**
	* RSA 公钥解密
	*/
	public static String decrypt(String content) throws Exception 
	{
        PublicKey pubKey = getPublicKey();

        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, pubKey);

        byte[] tmp = content.getBytes("UTF-8");
        InputStream ins = new ByteArrayInputStream(Base64.decodeBase64(tmp));
        ByteArrayOutputStream writer = new ByteArrayOutputStream();
        //rsa解密的字节大小最多是128，将需要解密的内容，按128位拆开解密
        byte[] buf = new byte[128];
        int bufl;

        while ((bufl = ins.read(buf)) != -1) {
            byte[] block = null;

            if (buf.length == bufl) {
                block = buf;
            } else {
                block = new byte[bufl];
                for (int i = 0; i < bufl; i++) {
                    block[i] = buf[i];
                }
            }

            writer.write(cipher.doFinal(block));
        }

        return new String(writer.toByteArray(), "utf-8");
    }

    /**
    * RSA 签名检查
    */
    public static boolean verify(String content, String sign) throws Exception
    {
    	PublicKey pubKey = getPublicKey();

    	try {
    		java.security.Signature signature = java.security.Signature
			.getInstance(SIGN_ALGORITHMS);

			signature.initVerify(pubKey);
			signature.update(content.getBytes("utf-8"));

			byte[] tmp = sign.getBytes("UTF-8");
			boolean result = signature.verify(Base64.decodeBase64(tmp));

			return result;
    	} catch (Exception e) {
    		e.printStackTrace();
    	}

    	return false;
    }

}

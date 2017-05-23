package noumena.payment.userverify.util;

import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.security.InvalidKeyException;
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Date;

import javax.crypto.Cipher;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.binary.StringUtils;



/**
 * 证书组件
 * 
 * @version 1.0
 * @since 1.0
 */
public class CertificateUtil {

    /**
     * Java密钥库(Java Key Store，JKS)KEY_STORE
     */
    public static final String KEY_STORE = "JKS";

    public static final String X509      = "X.509";

    /**
     * 由KeyStore获得私钥
     * 
     * @param keyStorePath 证书库路径
     * @param alias 证书别名
     * @param keyStorePassword 证书库密码
     * @param aliasPassword 证书密码
     * @return PrivateKey
     * @throws Exception
     */
    private static PrivateKey getPrivateKey(String keyStorePath, String alias,
            String keyStorePassword, String aliasPassword) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
        PrivateKey key = (PrivateKey) ks.getKey(alias, aliasPassword.toCharArray());
        return key;
    }

    /**
     * 由Certificate获得公钥
     * 
     * @param certificatePath 证书路径
     * @return PublicKey
     * @throws Exception
     */
    private static PublicKey getPublicKey(String certificatePath) throws Exception {
        Certificate certificate = getCertificate(certificatePath);
        PublicKey key = certificate.getPublicKey();
        return key;
    }

    /**
     * 获得Certificate
     * 
     * @param certificatePath 证书路径
     * @return Certificate
     * @throws Exception
     */
    private static Certificate getCertificate(String certificatePath) throws Exception {
        CertificateFactory certificateFactory = CertificateFactory.getInstance(X509);
        FileInputStream in = new FileInputStream(certificatePath);

        Certificate certificate = certificateFactory.generateCertificate(in);
        in.close();

        return certificate;
    }

    /**
     * 获得Certificate
     * 
     * @param keyStorePath 证书库路径
     * @param alias 证书别名
     * @param keyStorePassword 证书库密码
     * @return Certificate
     * @throws Exception
     */
    private static Certificate getCertificate(String keyStorePath, String alias,
            String keyStorePassword) throws Exception {
        KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
        Certificate certificate = ks.getCertificate(alias);

        return certificate;
    }

    /**
     * 获得KeyStore
     * 
     * @param keyStorePath 证书库路径
     * @param keyStorePassword 证书库密码
     * @return KeyStore
     * @throws Exception
     */
    private static KeyStore getKeyStore(String keyStorePath, String keyStorePassword)
            throws Exception {
        FileInputStream is = new FileInputStream(keyStorePath);
        KeyStore ks = KeyStore.getInstance(KEY_STORE);
        ks.load(is, keyStorePassword.toCharArray());
        is.close();
        return ks;
    }

    /**
     * 私钥加密
     * 
     * @param data 要加密的数据
     * @param keyStorePath 证书库路径
     * @param alias 证书别名
     * @param keyStorePassword 证书库密码
     * @param aliasPassword 证书密码
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encryptByPrivateKey(byte[] data, String keyStorePath, String alias,
            String keyStorePassword, String aliasPassword) throws Exception {
        // 取得私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, keyStorePassword, aliasPassword);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);

        return cipher.doFinal(data);

    }

    /**
     * 私钥解密
     * 
     * @param data 加密后的数据
     * @param keyStorePath 证书库路径
     * @param alias 证书别名
     * @param keyStorePassword 证书库密码
     * @param aliasPassword 证书密码
     * @return byte[]
     * @throws Exception
     */
    public static byte[] decryptByPrivateKey(byte[] data, String keyStorePath, String alias,
            String keyStorePassword, String aliasPassword) throws Exception {
        // 取得私钥
        PrivateKey privateKey = getPrivateKey(keyStorePath, alias, keyStorePassword, aliasPassword);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(privateKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, privateKey);

        return cipher.doFinal(data);

    }

    /**
     * 公钥加密
     * 
     * @param data 要加密的数据
     * @param certificatePath 证书路径
     * @return byte[]
     * @throws Exception
     */
    public static byte[] encryptByPublicKey(byte[] data, String certificatePath) throws Exception {

        // 取得公钥
        PublicKey publicKey = getPublicKey(certificatePath);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);

        return cipher.doFinal(data);

    }

    /**
     * 公钥解密
     * 
     * @param data 加密后的数据
     * @param certificatePath 证书路径
     * @return
     * @throws Exception
     */
    public static byte[] decryptByPublicKey(byte[] data, String certificatePath) throws Exception {
        // 取得公钥
        PublicKey publicKey = getPublicKey(certificatePath);

        // 对数据加密
        Cipher cipher = Cipher.getInstance(publicKey.getAlgorithm());
        cipher.init(Cipher.DECRYPT_MODE, publicKey);

        return cipher.doFinal(data);

    }

    /**
     * 验证Certificate
     * 
     * @param certificatePath 证书路径
     * @return boolean
     */
    public static boolean verifyCertificate(String certificatePath) {
        return verifyCertificate(new Date(), certificatePath);
    }

    /**
     * 验证Certificate是否过期或无效
     * 
     * @param date 日期
     * @param certificatePath 证书路径
     * @return boolean
     */
    public static boolean verifyCertificate(Date date, String certificatePath) {
        boolean status = true;
        try {
            // 取得证书
            Certificate certificate = getCertificate(certificatePath);

            // 验证证书是否过期或无效
            status = verifyCertificate(date, certificate);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * 验证证书是否过期或无效
     * 
     * @param date 日期
     * @param certificatePath 证书路径
     * @return
     */
    private static boolean verifyCertificate(Date date, Certificate certificate) {
        boolean status = true;
        try {
            X509Certificate x509Certificate = (X509Certificate) certificate;
            x509Certificate.checkValidity(date);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * 签名
     * 
     * @param data 加密后的数据
     * @param keyStorePath 证书库路径
     * @param alias 证书别名
     * @param keyStorePassword 证书库密码
     * @param aliasPassword 证书密码
     * @return
     * @throws Exception
     */
    public static String sign(byte[] data, String keyStorePath, String alias,
            String keyStorePassword, String aliasPassword) throws Exception {
        // 获得证书
        X509Certificate x509Certificate = (X509Certificate) getCertificate(keyStorePath, alias,
                keyStorePassword);
        // 由keyStore获取私钥
        KeyStore ks = getKeyStore(keyStorePath, keyStorePassword);
        PrivateKey privateKey = (PrivateKey) ks.getKey(alias, aliasPassword.toCharArray());

        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initSign(privateKey);
        signature.update(data);
        return StringUtils.newStringUtf8(Base64.encodeBase64(signature.sign()));
    }

    /**
     * 验证签名
     * 
     * @param data 加密后的数据
     * @param sign 签名
     * @param certificatePath 证书路径
     * @return boolean
     * @throws Exception
     */
    public static boolean verify(String publicKeyUrl, String signedData, String signature) throws Exception {
       /* // 获得证书
        X509Certificate x509Certificate = (X509Certificate) getCertificate(certificatePath);
        // 获得公钥
        PublicKey publicKey = x509Certificate.getPublicKey();
        // 构建签名
        Signature signature = Signature.getInstance(x509Certificate.getSigAlgName());
        signature.initVerify(publicKey);
        signature.update(data);

        return signature.verify(Base64.decodeBase64(sign));*/
    	try{
    		FileInputStream in = new FileInputStream(publicKeyUrl); 
            CertificateFactory cf =   CertificateFactory.getInstance("X509");
            X509Certificate c = (X509Certificate)cf.generateCertificate(in);

            PublicKey key22 = c.getPublicKey();

            byte[] result = Base64.decodeBase64(signedData);//;
            byte[] decodedSignature = Base64.decodeBase64(signature);

            Signature sig;
            try {
                sig = Signature.getInstance("SHA256withRSA");
                sig.initVerify(key22);
                sig.update(result);
                if (!sig.verify(decodedSignature)) {
                    return false;
                }else{
                    return true;
                }
            } catch (NoSuchAlgorithmException e) {
               e.printStackTrace();
            } catch (InvalidKeyException e) {
               e.printStackTrace();
            } catch (SignatureException e) {
               e.printStackTrace();
            }
        }catch(Exception e){
            e.printStackTrace();
        }

        return false;
    }

    public static String makeEncodedSignedDataForAppleVerify(String player_id, String bundle_id, String timestamp, String salt){
        return new String(Base64.encodeBase64(concat(player_id.getBytes(),
                bundle_id.getBytes(),
                ByteBuffer.allocate(8).putLong(Long.parseLong(timestamp)).array(), 
                Base64.decodeBase64(salt))));
    }
    
    static byte[] concat(byte[]...arrays)
    {
        // Determine the length of the result array
        int totalLength = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            totalLength += arrays[i].length;
        }

        // create the result array
        byte[] result = new byte[totalLength];

        // copy the source arrays into the result array
        int currentIndex = 0;
        for (int i = 0; i < arrays.length; i++)
        {
            System.arraycopy(arrays[i], 0, result, currentIndex, arrays[i].length);
            currentIndex += arrays[i].length;
        }

        return result;
    }
    /**
     * 验证Certificate
     * 
     * @param date 日期
     * @param keyStorePath 证书库路径
     * @param alias 证书别名
     * @param keyStorePassword 证书库密码
     * @return
     */
    public static boolean verifyCertificate(Date date, String keyStorePath, String alias,
            String keyStorePassword) {
        boolean status = true;
        try {
            Certificate certificate = getCertificate(keyStorePath, alias, keyStorePassword);
            status = verifyCertificate(date, certificate);
        } catch (Exception e) {
            status = false;
        }
        return status;
    }

    /**
     * 验证Certificate
     * 
     * @param keyStorePath 证书库路径
     * @param alias 证书别名
     * @param keyStorePassword 证书库密码
     * @return
     */
    public static boolean verifyCertificate(String keyStorePath, String alias,
            String keyStorePassword) {
        return verifyCertificate(new Date(), keyStorePath, alias, keyStorePassword);
    }
}

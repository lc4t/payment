package noumena.payment.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.AlgorithmParameters;
import java.security.MessageDigest;
import java.security.Security;
import java.util.List;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import noumena.payment.naver.NaverCharge;

import org.jdom.Document;
import org.jdom.Element;
import org.jdom.input.SAXBuilder;
import org.xml.sax.InputSource;

public class StringEncrypt
{
	public static String Encrypt(String strSrc, String encName)
	{
		MessageDigest md = null;
		String strDes = null;

		try
		{
			byte[] bt = strSrc.getBytes("utf-8");
			md = MessageDigest.getInstance(encName);
			md.update(bt);
			strDes = bytes2Hex(md.digest()); // to HexString
		}
		catch (Exception e)
		{
			return null;
		}
		return strDes;
	}

	public static String Encrypt(String strSrc)
	{
		return Encrypt(strSrc, "MD5");
	}

	public static String EncryptSHA256(String strSrc)
	{
		return Encrypt(strSrc, "SHA-256");
	}

	public static String bytes2Hex(byte[] bts)
	{
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++)
		{
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1)
			{
				des += "0";
			}
			des += tmp;
		}
		return des;
	}
	
	public static byte[] hex2Bytes(String hexStr) {  
        if (hexStr.length() < 1)  
                return null;  
        byte[] result = new byte[hexStr.length()/2];  
        for (int i = 0;i< hexStr.length()/2; i++) {  
                int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);  
                int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);  
                result[i] = (byte) (high * 16 + low);  
        }  
        return result;  
	}  

	public static String md5(String strSrc)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
			return baseEncoder.encode(md.digest(strSrc.getBytes()));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}

	}
	
	public static String dmd5(String strSrc)
	{
		try
		{
			MessageDigest md = MessageDigest.getInstance("MD5");
			sun.misc.BASE64Encoder baseEncoder = new sun.misc.BASE64Encoder();
			return baseEncoder.encode(md.digest(md.digest(strSrc.getBytes())));
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return "";
		}

	}

	public static String doPost(String url, String content)
	{
		String res = "";
		try
		{
			URL u = new URL(url);
			HttpURLConnection uc = (HttpURLConnection) u.openConnection();
			uc.setRequestMethod("POST");
			uc.setReadTimeout(60000);
			uc.setRequestProperty("Content-type", "text/xml");
			uc.setDoInput(true);
			uc.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(uc.getOutputStream(), "UTF-8");
			outs.write(content);
			outs.flush();
			outs.close();
			BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
			String line = null;
			while ((line = in.readLine()) != null)
			{
				res += line;
			}
			in.close();
			uc.disconnect();
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return res;
	}
	
	public static String encryptAESIV(String content, String key, String iv) {
		try {
			byte[] bText=null;
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			AlgorithmParameters mAlgo =AlgorithmParameters.getInstance("AES");
			mAlgo.init(new IvParameterSpec(iv.getBytes()));
			SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher mCipher =Cipher.getInstance("AES/CBC/PKCS5Padding");
			mCipher.init(Cipher.ENCRYPT_MODE, secretKey, mAlgo);
			bText = mCipher.doFinal(content.getBytes());
			String str = bytes2Hex(bText);
			return URLEncoder.encode(str,"utf-8");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static String decryptAESIV(String content,String key,String iv) {
		try {
			byte[] sText=null;
			String str = URLDecoder.decode(content, "utf-8");
			sText = hex2Bytes(str);
			
			Security.addProvider(new org.bouncycastle.jce.provider.BouncyCastleProvider());
			AlgorithmParameters mAlgo =AlgorithmParameters.getInstance("AES");
			mAlgo.init(new IvParameterSpec(iv.getBytes()));
			SecretKey secretKey = new SecretKeySpec(key.getBytes(), "AES");
			Cipher mCipher =Cipher.getInstance("AES/CBC/PKCS5Padding");
			mCipher.init(Cipher.DECRYPT_MODE, secretKey, mAlgo);
			byte[] bText = mCipher.doFinal(sText);
			String ret = new String(bText);
			return ret.trim();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
    }


	public static void main(String[] args)
	{
//		NaverCharge.init();
//		NaverCharge.setTestmode(true);
//		NaverCharge.checkOrderFromNaver("1013820", "1003746898", "BQYU332281398306467989");
//		NaverCharge.checkOrderFromNaver("1013820", "1003228993", "RENW317221395899076045");

		// System.out.println(md5("175123456"));
		//System.out.println(Encrypt("112991800000359618040792570648.002012-03-13 09:29:15{\"itemId\": 5, \"itemNum\": 1}jla(*(HJ"));
		//System.out.println(EncryptSHA256("a"));

//		MyCardTWOrderDataVO vo = new MyCardTWOrderDataVO();
//		String res = "{\"AuthCode\":null,\"TradeType\":0,\"ExtensionData\":{},\"ReturnMsg\":\"參數有誤\",\"ReturnMsgNo\":-901}";
//		JSONObject json = JSONObject.fromObject(res);
//		vo.setExtensionData(json.getJSONObject("ExtensionData"));
//		vo.setAuthCode(json.getString("AuthCode"));
//		vo.setTradeType(json.getString("TradeType"));
//		vo.setReturnMsg(json.getString("ReturnMsg"));
//		vo.setReturnMsgNo(json.getString("ReturnMsgNo"));
		
//		String t = "{\"itemId\":\"Service_gift_token1_1\",\"itemNum\":\"1\"}#";
//		//String t = "";
//		String[] s = t.split("#");
//		System.out.println("s len->" + s.length);
//		System.out.println("s[0]" + s[0]);
//		String sign = StringEncrypt.Encrypt("4751042300000501115930081.002013-01-15 15:19:36" + s[0] + "jla(*(HJ");
//		sign = StringEncrypt.Encrypt("20130305011623961.0myrezhq2456ezhqac473dc0-0da9-4718-9302-20cf3fb92390", "sha");
//		System.out.println(sign);
//		sign = StringEncrypt.md5("6162300000100296160.0120121229155918{\"itemId\":\"service_token1_1\",\"itemNum\":\"1\"}jl&kk").toUpperCase();
//		System.out.println(sign);
		

//		try
//		{
//			String cbdata = "<BillingApplyRq><FatoryId>kz</FatoryId><TotalNum>1</TotalNum><Records><Record><ReturnMsgNo>1</ReturnMsgNo><ReturnMsg></ReturnMsg><TradeSeq>1070</TradeSeq></Record></Records></BillingApplyRq>";
//			StringReader read = new StringReader(cbdata);
//			InputSource source = new InputSource(read);
//			SAXBuilder builder = new SAXBuilder();
//			Document doc = builder.build(source);
//			Element root = doc.getRootElement();
//			List<?> childrens = root.getChildren("Records");
//			if (childrens.size() > 0)
//			{
//				Element e_records = (Element) childrens.get(0);
//				List<?> l_records = e_records.getChildren("Record");
//				for (int i = 0 ; i < l_records.size() ; i++)
//				{
//					Element e_recorddata = (Element) l_records.get(i);
//					List<?> l_recorddata = e_recorddata.getChildren("ReturnMsgNo");
//					if (l_recorddata.size() > 0)
//					{
//						Element data = (Element) l_recorddata.get(0);
//						String str = data.getText();
//						System.out.println("ReturnMsgNo->" + str);
//					}
//					l_recorddata = e_recorddata.getChildren("ReturnMsg");
//					if (l_recorddata.size() > 0)
//					{
//						Element data = (Element) l_recorddata.get(0);
//						String str = data.getText();
//						System.out.println("ReturnMsg->" + str);
//					}
//					l_recorddata = e_recorddata.getChildren("TradeSeq");
//					if (l_recorddata.size() > 0)
//					{
//						Element data = (Element) l_recorddata.get(0);
//						String str = data.getText();
//						System.out.println("TradeSeq->" + str);
//					}
//				}
//			}
//
//			System.out.println("xml->" + OSUtil.XML2String(doc));
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
//		
//		String c = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><request><userId>12426901</userId><cpServiceId>620610064908</cpServiceId><consumeCode>000064907001</consumeCode><cpParam>1000000000000002</cpParam><hRet>0</hRet><status>1101</status><transIDO>2144049PONE301111</transIDO><versionId>100<yersionId></request>";
////		doPost("http://xxxxxxx", c);
	}
}

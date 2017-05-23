package noumena.payment.userverify;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.CertificateUtil;

public class CGamecenterVerify {
	public static String verify(int model, ChannelInfoVO vo)
	{
		return getIdFrom(vo);
	}
	
/*public static void main(String[] args) {
	String s = "{\"playerID\":\"G:1996467529\",\"type\":\"gamecenter\",\"bundleID\":\"com.noumena.ios.m5en.all\",\"salt\":\"zvHgEg==\",\"public_key_url\":\"https://static.gc.apple.com/public-key/gc-prod-2.cer\",\"signature\":\"k/1gE9tkX8MTX7qFKEicQMRcNif9fJ0z565i4WpD88DVwVdJk/kBgTEGpmqGQ1wCQxpIK9TP5G2tkRSraOWxAQSV1RPaqLNRu57ZWjt9mdoOob8Zy9dX50Dmh39UsByA01bfmlsS8mEj1FxVA8jYHqt62Bw/MCRi8SZEke1bX65La/W0AKUdqsHV/OZifNa5oQtE6ePVQF2+n6kDD9rsqFMEsU8VdUe9TNoTizhOeyy3XohsdeA3r82yibM/jRb609Kt15CE3qgS8AslZOU/y5QxUdlmiy5qWonVQswaPZRQsONzLQuAXXH1azzZ7oLl+lqiy2LquG+DQyyKKCZcPA==\",\"timestamp\":\"1447918984930\"}";
	ChannelInfoVO vo = new ChannelInfoVO();
	vo.setExinfo(s);
	getIdFrom(vo);
}*/
	private static String getIdFrom(ChannelInfoVO vo)
	{
		//json内容： {"type":"gamecenter","public_key_url":"","signature":"","timestamp":"","salt":"","playerID":"","bundleID":""}
		String info = vo.getExinfo();
		JSONObject json = JSONObject.fromObject(info);
		String playerID = json.getString("playerID");
		String bundleID = json.getString("bundleID");
		String public_key_url = json.getString("public_key_url");
		String signature = json.getString("signature");
		String timestamp = json.getString("timestamp");
		String salt = json.getString("salt");
		
		String newSignature = CertificateUtil.makeEncodedSignedDataForAppleVerify(playerID, bundleID, timestamp, salt);
		ChannelVerify.GenerateLog("gamecenter get user info parameters ->" + info);
		try
		{
			if(null != public_key_url && !public_key_url.equals("")){
				String certificatePath = System.getProperty("user.home") + "/payment";
				String certName = public_key_url.substring(public_key_url.lastIndexOf("/"));
				File certPath= new File(certificatePath);
				if(!certPath.exists()){
					certPath.mkdirs();
				}
				File certFile = new File(certificatePath + certName);
				if(!certFile.exists()){
					URL url = new URL(public_key_url);
					HttpURLConnection connection = (HttpURLConnection) url.openConnection();
					DataOutputStream dos = new DataOutputStream(new FileOutputStream(certFile)); 
					
					DataInputStream dis = new DataInputStream(connection.getInputStream());
					int a;
			        while ((a = dis.read()) != -1) {
			        	dos.write(a);
			        }  
			        dos.flush();
			        connection.disconnect();
				}
		        boolean res = CertificateUtil.verify(certificatePath + certName,newSignature, signature);
		        //System.out.println("gamecenter get user info ret ->" + res);
		        if(res){
					return playerID;
				}
		        ChannelVerify.GenerateLog("gamecenter get user info ret ->" + res);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return "";
	}
	
}

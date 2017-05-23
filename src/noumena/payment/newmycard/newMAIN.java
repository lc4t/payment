package noumena.payment.newmycard;

import java.net.URLEncoder;
import java.security.MessageDigest;
import java.util.Scanner;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.getorders.OrderUtil;

public class newMAIN {
	@SuppressWarnings("resource")
	public static void main(String args[]) throws Exception {

		Scanner input = new Scanner(System.in);
		String ordrid = input.next();// 输入一个正整数
		System.out.println(ordrid);
		JSONObject jsonString = new JSONObject();
		jsonString.put("FacServiceId", "THOL");
		jsonString.put("FacTradeSeq", ordrid);
		jsonString.put("StartDateTime", "");
		jsonString.put("EndDateTime", "");
		jsonString.put("CancelStatus", "0");
		String hash = getHash(jsonString);
		jsonString.put("Hash", hash);
		System.out.println(jsonString.toString());
		String urlString="https://b2b.mycard520.com.tw/MyBillingPay/api/SDKTradeQuery?FacServiceId=THOL&FacTradeSeq="+ordrid+"&StartDateTime=&EndDateTime=&CancelStatus=0&Hash="+hash;
		System.out.println(urlString);
		String result = OrderUtil.getHttpsInterenceStream(
				urlString,
				"get", "");
		System.out.println(result);
		JSONObject jsonObjectResultOut = JSONObject.fromObject(result);
		if (jsonObjectResultOut.get("ReturnCode").equals("1")) {
			JSONArray jsonObjectEnd = JSONArray.fromObject(jsonObjectResultOut
					.get("ListSDKTradeQuery"));
			String paymentType = JSONObject.fromObject(jsonObjectEnd.get(0))
					.getString("PaymentType");
			System.out.println(paymentType);
		}
		System.exit(0);

	}

	private static String getHash(JSONObject jsonObject) throws Exception {
		String preHashValue = jsonObject.getString("FacServiceId")
				+ jsonObject.getString("FacTradeSeq")
				+ jsonObject.getString("StartDateTime")
				+ jsonObject.getString("EndDateTime")
				+ jsonObject.getString("CancelStatus")
				+ "eD8mkUBBm5GXLaP6Fk8lhKKBQXAgNAeA";
		preHashValue= URLEncoder.encode(preHashValue, "UTF-8");
		MessageDigest md = MessageDigest.getInstance("SHA-256");
		byte[] bt = preHashValue.getBytes("UTF-8");
		md.update(bt);
		String afsign = bytes2Hex(md.digest()); // to HexString
		afsign = afsign.replace("-", "").toLowerCase();
		return afsign;
	}

	/**
	 * sha256加密
	 * 
	 * @param bts
	 * @return
	 */
	private static String bytes2Hex(byte[] bts) {
		String des = "";
		String tmp = null;
		for (int i = 0; i < bts.length; i++) {
			tmp = (Integer.toHexString(bts[i] & 0xFF));
			if (tmp.length() == 1) {
				des += "0";
			}
			des += tmp;
		}
		return des;
	}

}

package noumena.payment.googleplay;

import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class GooglePlayYuanCharge
{
	private static GooglePlayParams params = new GooglePlayParams();
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}
	public static void setTestmode(boolean testmode)
	{
		GooglePlayYuanCharge.testmode = testmode;
	}
	
	public static String getTransactionId(Orders order)
	{
		OrdersBean bean = new OrdersBean();
		String cburl = order.getCallbackUrl();
		String payId;
		if (cburl == null || cburl.equals(""))
		{
			payId = bean.CreateOrder(order);
		}
		else
		{
			if (cburl.indexOf("?") == -1)
			{
				cburl += "?pt=" + Constants.PAY_TYPE_BAIDU;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_BAIDU;
			}
			payId = bean.CreateOrder(order, cburl);
		}
		order.setCallbackUrl(cburl);
		String date = DateUtil.formatDate(order.getCreateTime());
		OrderIdVO orderIdVO = new OrderIdVO(payId, date);
		
		JSONObject json = JSONObject.fromObject(orderIdVO);
		return json.toString();
	}
	
	public static String checkOrdersStatus(String payIds)
	{
		String[] orderIds = payIds.split(",");

		OrdersBean bean = new OrdersBean();
		List<Orders> orders = bean.qureyOrders(orderIds);
		List<OrderStatusVO> statusret = new ArrayList<OrderStatusVO>();
		for (int i = 0 ; i < orders.size() ; i++)
		{
			Orders order = orders.get(i);
			int status = order.getKStatus();
			OrderStatusVO st = new OrderStatusVO();
			st.setPayId(order.getOrderId());
			if (status == Constants.K_STSTUS_DEFAULT || status == Constants.K_CON_ERROR)
			{
				//如果订单状态是初始订单或者是网络连接有问题状态，返回不知道
				st.setStatus(3);
			}
			else if (status == Constants.K_STSTUS_SUCCESS)
			{
				//如果订单已经成功，直接返回订单状态
				st.setStatus(1);
			}
			else
			{
				//订单已经失败，直接返回订单状态
				st.setStatus(2);
			}
			statusret.add(st);
		}
		JSONArray arr = JSONArray.fromObject(statusret);
		
		return arr.toString();
	}

	public static void purchase(String appId, String signature, String signedData)
	{
		System.out.println("Google play purchase appId:" + appId);
		System.out.println("Google play purchase signature:" + signature);
		System.out.println("Google play purchase signedData:" + signedData);

		List<GooglePlayOrderVO> ars = verifyPurchaseToList(signedData, signature, appId);
		if (ars == null || ars.size() == 0)
		{
			return;
		}
	}

	public static List<GooglePlayOrderVO> verifyPurchaseToList(String signedData, String signature, String appId)
	{
		List<GooglePlayOrderVO> ars = new ArrayList<GooglePlayOrderVO>();
		if (signedData == null)
		{
			System.out.println("-----------------signedData is null");
			return null;
		}
		boolean verified = false;

		//1、验证签名是否正确
		try
		{
			if (!signature.isEmpty())
			{
				String base64EncodedPublicKey = params.getAppKeyById(appId);
				if (base64EncodedPublicKey == null)
				{
					base64EncodedPublicKey = params.getAppKeyById("default");
				}
				PublicKey key = generatePublicKey(base64EncodedPublicKey);
				if (key == null)
				{
					return null;
				}
				verified = verify(key, signedData, signature);
				if (!verified)
				{
					base64EncodedPublicKey = params.getAppKeyById("GP.base64EncodedPublicKey.com.noumena.android.k6tw");
					key = generatePublicKey(base64EncodedPublicKey);
					verified = verify(key, signedData, signature);
					if (!verified)
					{
						System.out.println("-----------------verified is false");
						return null;
					}
				}
			}
			else
			{
				return null;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

		//2、解析传输数据并且创建订单
		JSONObject jObject;
		JSONArray jTransactionsArray = null;
		int numTransactions = 0;
		try
		{
			jObject = JSONObject.fromObject(signedData);
			jTransactionsArray = jObject.optJSONArray("orders");
			if (jTransactionsArray != null)
			{
				numTransactions = jTransactionsArray.size();
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}

		try
		{
			for (int i = 0; i < numTransactions; i++)
			{
				JSONObject jElement = jTransactionsArray.getJSONObject(i);
				int purchaseState = jElement.getInt("purchaseState");
				String productId = jElement.getString("productId");
				String packageName = jElement.getString("packageName");
				long purchaseTime = jElement.getLong("purchaseTime");
				String orderId = jElement.optString("orderId", "");
				String notifyId = null;
				if (jElement.has("notificationId"))
				{
					notifyId = jElement.getString("notificationId");
				}
				String developerPayload = jElement.optString("developerPayload", null);

				if (purchaseState != 0)
				{
					continue;
				}

				OrdersBean bean = new OrdersBean();
				List<Orders> orders = bean.qureyOrdersByPayId(orderId);
				if (orders != null && orders.size() > 0)
				{
					continue;
				}
				
				//i={_imei}#c={_channel}#uid={_uid}#app={_app}#cb={_callbackUrl}
				String imei="", channel="", userid="", appid="", cburl="", amount="0";
				String[] customs = developerPayload.split("#");
			 	for(int j=0; j<customs.length; j++)
			 	{
			 		String[] subCm = customs[j].split("=");
			 		if(subCm[0].equals("i"))
			 		{
			 			imei = subCm[1];
				 	}
			 		else if(subCm[0].equals("c"))
			 		{
				 		channel = subCm[1];
				 	}
			 		else if(subCm[0].equals("uid"))
			 		{
				 		userid = subCm[1];
				 	}
			 		else if(subCm[0].equals("app"))
			 		{
				 		appid = subCm[1];
				 	}
			 		else if(subCm[0].equals("m"))
			 		{
				 		amount = subCm[1];
				 	}
			 		else if(subCm[0].equals("cb"))
			 		{
					 	int pos = customs[j].indexOf("=");
					 	if(pos > 0)
					 	{
				 			cburl = customs[j].substring(pos+1,customs[j].length());
				 			cburl += "&pt=" + Constants.PAY_TYPE_GOOGLEPLAY;
					 	}
				 	}
			 	}

				Orders order = new Orders();
				Date date = new Date();
				SimpleDateFormat df1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
				order.setUId(userid);
				order.setAppId(packageName);
				if (amount == null || amount.equals(""))
				{
					amount = "0.00";
				}
				else
				{
					amount = new DecimalFormat("0.00").format(new Float(amount));
				}
				order.setAmount(Float.valueOf(amount));
				order.setCreateTime(df1.format(date));
				order.setPayType(Constants.PAY_TYPE_GOOGLEPLAY);
				{
					try
					{
						int pos = productId.lastIndexOf(".");
						if (pos >= 0)
						{
							productId = productId.substring(pos+1);
						}
					}
					catch (Exception e)
					{
						e.printStackTrace();
					}
				}
				order.setItemId(productId);
				order.setItemNum(1);
				order.setPayId(orderId);
				order.setMoney(amount);
				order.setChannel(channel);
				order.setCallbackUrl(cburl);
				order.setImei(imei);
				order.setExInfo(signedData);
				
				String payid = bean.CreateOrder(order, cburl);
				
				bean.updateOrderKStatus(payid + "", Constants.K_STSTUS_SUCCESS);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
		return ars;
	}

	/**
	 * Generates a PublicKey instance from a string containing the
	 * Base64-encoded public key.
	 * 
	 * @param encodedPublicKey
	 *            Base64-encoded public key
	 * @throws IllegalArgumentException
	 *             if encodedPublicKey is invalid
	 */
	public static PublicKey generatePublicKey(String encodedPublicKey) throws Exception
	{
		byte[] decodedKey = Base64.decode(encodedPublicKey);
		KeyFactory keyFactory = KeyFactory.getInstance("RSA");
		return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
	}

	/**
	 * Verifies that the signature from the server matches the computed
	 * signature on the data. Returns true if the data is correctly signed.
	 * 
	 * @param publicKey
	 *            public key associated with the developer account
	 * @param signedData
	 *            signed data from server
	 * @param signature
	 *            server signature
	 * @return true if the data and signature match
	 */
	public static boolean verify(PublicKey publicKey, String signedData, String signature)
	{
		Signature sig;
		try
		{
			sig = Signature.getInstance(GooglePlayParams.SIGNATURE_ALGORITHM);
			sig.initVerify(publicKey);
			sig.update(signedData.getBytes());
			if (!sig.verify(Base64.decode(signature)))
			{
				System.out.println("------------------Signature verification failed.");
				return false;
			}
			return true;
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		return false;
	}

	public static void init()
	{
		params.addApp("default", "default", 									"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiOt/PdvN8Qd1hVmho2t5AgSissoWLF8SP3cw5E5pIs51cQW54cjW7wfj/YySVXch6urA1G27K+OvUU6zFg4Kx84763Yxe0RtcUroEUumratBdEiyzWOpfks2cURaBLEhU/OcBZtG9TyzSXFkFwIhemmqwOgF6tgQNB/lxprGiuSwZaA0lqTtJF2NL96/Rk3MSGeVCWyTkvwbnFPtmit3HdYZKjUy/ka+rlGqA6c1GgJYqxyKxPVBdVcF/sneH219MudNhFqyPQpMfyr9haQUXYJKtRmz9e1pDLNCe9lfLKLxjrhRIpr3USI0/x+0OFPR8rmZDXI0vp9bpqH2M32yPwIDAQAB");
		params.addApp("galaxy2tw", "com.noumena.android.galaxy2tw", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApCnm86ofTd1D7mLJx2r/ZhNEskhPhHyhtxfHATcVn7QbsZyUjKqpwsAwXKoXhreonTKAPCN8/xJ1lg9DY7vBROGgz5F50ITpEFmUAQRopUfBZFY2CJmTockupIbrFM8jdGdl4OJdyZQSGkMZsARM0Z+7yGTy7ee/g0t89Uvp3neulwTUQsI/spyaslMUOM7tbpfNDOR+d8MoxBGoU93QMzv/woD+HsYXjVpgB9aO+C6mBqljWWcTGQQooGE2Yd5YPT+4PV8RPRaGtz5zrcq69IAnDnpLSwTXJTKUPzenxFrzISsca16g4IL4ElS2rHpxj7wjvAWbvvN62KyHFNep4QIDAQAB");
		params.addApp("k6tw", "com.noumena.android.k6tw", 						"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAzvs1kMV5Iuhxkt40dV7NaudbkglCWW1yDlCyef57YbScEZwgvXMTP0NP/fzPJzOK6OIgJkomK6rBGgt9FOwswo1ZOdXHcj/lNJZYJkA5lxvknpeRuzZzqq5Nq4pENPTKUlGkn21fJmXJ+O46w8zqDZ0VL4s5waIn/7c56VJlyniFGubGoZE2v/5Hy0wbvIj2yjMRaQZNWjIZ/lTwRktnwqMDTfYnIgq8TGY7gO5COtuPiQC5V4KAtZgAIw4Gr/dQbU93P5b5A1eWnZSoaWtoR86Xw/fQowb5uVoWL0gicClVIuG3VKMteLmEGTnmSP2aGqxymrG2pOPCkCdWvw6nQwIDAQAB");
		params.addApp("tinywartwgp", "com.nounema.android.tinywartwgp", 		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAwkvqBuZsZm7iqNvEwaxcdm8RH8kYvzN8Kx2jpaUXKnYJM9a4xnaOvRd6J+S51+k+xPLz0P2FbrG1GLJoqUPubG80SANon9ret15PEg7IUkdd2R7aEzZ4wGLNTQxEb2Zsg9SwuYaiDOEpB4Nv1AhshYiYmgMffxwuHFWbxgnabOpbs169UUKhRIV+i/RvcNS2LDNegGHFyYRYvHE4yI93/9Wj4qXZ5uzPb/mJHTyM/biJ+WePYeMWcIl8WHqqVjuP3SH4lc2vhWtpGD525/hI5vJ6Q8htiIniCVLfu8M5xtaYRpuWK1t17rlKXsRZHu+lyx85AYwg8FZG0MsP2aNAYQIDAQAB");
		params.addApp("zqsdengp", "com.kongzhong.tjmammoth.android.zqsdengp", 	"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjgcnqFsKDhjHnVtwl0E6l6Jx/mXRViPWMGhjloOJPZE5Nkfe+rzYaE0lb5jeTumqXh4Ht0+xTm1dpqUbheDHGw3+il+ZxEWp5aWnHb0AaLxuu3QzSM5z5xmkUH+HJK6ED5gN7Uaog+XQkgEEQ1APxymBZRN3FpJWin+Ky4HezNjZL8SpwtMQCkdHOgamqz/hRRTER+l1BQfTJiWkSDKdZUBiQHxoICDH2ZA3VnWZIBuVZEIjPO15cQ2xxatzzxOewHl0PN7H8mFHYfaB0pqkNr2XyaTMRI1oQ2kKOeTEoPJwkPAbn47ck6x2/AVjBipdz3962nqeySDvI9tzUYNHvQIDAQAB");
		params.addApp("zqsdtwgp", "com.kongzhong.tjmammoth.android.zqsdtwgp", 	"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA0+zI0mIPkZDw9MZplzo1f/BvwNPyWyTost4dmsj8kaXiotmOKaErQV63rrbU+7pCa9jiaoxrVvqYOe/vxoqVmyibPmNjoO+VacljjOqk/v9catpyO6vGOsUjvP55upA3L5RbLl62vcueN+bSTL9k6CL0LCRhQTW/UdN9dtBcpxSLxqmJ1uxT4hXhUDY8qDJIZlAN08DDSfZ2GVcfoWs7Kygwf11t6xH6OEU3KJkeaQg7K3b+RICxicyN1aDkbMt9ARm9Ojd3Gl+fLSlWq87jM0ADLlp33cDpaV7k7MqfujI4Fuv2y86Beh6wfb3OK7bj2/A93sfrrWVNDW92W62kSwIDAQAB");
		params.addApp("galaxy2korgp", "com.noumena.android.galaxy2korgp", 		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnTO64SyAqGCsz3JlUVesvO5iz8arsE+q2jxTXOqg30vaIW/1pLKsmkxQC+c/H8OoBqhjzgSWb8oGE9K8ZCNYfZw1cGSo4GocQlBNkpW9wIkXJOTQ8a61ALTML1L+5crHnGqhF3rddJPXp2tfi0gxkh+VU1Mx55bM2Cxn0OLW7r3lUGeI8SPA8VLah6C+XLl0G7Z7CGaWMRuABVxGIry0f99juBUjge5kmiq5cDgFuczyPJqybc9JD+6wi0MEFNpPO8Gz8PDMO6idz+CAquVvTg/tp+iGWfHSJmERJaR40gwwnkJv4V+vZbK0C/99Hr2TZUy5QebZEXZp1Ye/of7wswIDAQAB");
		params.addApp("tinyfortus", "com.noumena.android.tinyfortus", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAqeXIKh2rWaKlS31VOoZrnZJZ6V7Q2+45UX3tQgzXhWbG+9EAh2TuEisFFO/WB8o4jEm2nCZTfc9L+slYZLg/GjsFcgKihrK90Vt4r/IWIDZxwWLQVz/6NFUXWK8jRlqKEIiEoGx21Y4UpRUEjGaKHHqej+sxA+s07Q3kKQFm09U1xe0EUN9v+iHvDd9FoL1C8CNtUg7+bbDWC9JJYOJ+UjbTgu3qho0WBb5QaajzwlQAJf886bB8g/rThaTgB5fa11p5QOYzU9H0R0KI9lzkzDVh7sMm5MyWspZvc5ltcl01U+VABs326aVCqSJ3WF/O8siYorbGzheYpreyEDTN1QIDAQAB");
		params.addApp("cqqsl", "com.kongzhong.tjmammoth.android.cqqsl", 		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAiOt/PdvN8Qd1hVmho2t5AgSissoWLF8SP3cw5E5pIs51cQW54cjW7wfj/YySVXch6urA1G27K+OvUU6zFg4Kx84763Yxe0RtcUroEUumratBdEiyzWOpfks2cURaBLEhU/OcBZtG9TyzSXFkFwIhemmqwOgF6tgQNB/lxprGiuSwZaA0lqTtJF2NL96/Rk3MSGeVCWyTkvwbnFPtmit3HdYZKjUy/ka+rlGqA6c1GgJYqxyKxPVBdVcF/sneH219MudNhFqyPQpMfyr9haQUXYJKtRmz9e1pDLNCe9lfLKLxjrhRIpr3USI0/x+0OFPR8rmZDXI0vp9bpqH2M32yPwIDAQAB");
		params.addApp("cqqslengp", "com.kongzhong.tjmammoth.android.cqqslengp", "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApKOffmYCHQ6NXVdeQtXMXbId3xjQzwywVAdc8fN/qPh8jtLFKV5KGFlVcvJ8FiwA8cfAlCm8RBs45RWqZyo8GIACTUiqrP2WIZpol5GszVPMy5Zzeq6Tmnwr+q+optV4ZJ5Jz0BjWXAT4b6Z+h6l+tl4CK8gfTuA9oZhwflxX3ErC/EpObS05LKonHFhknBlk4Ufrh1mrnRfpy0fUOe+HC2g+faVfifaQ0BwR0AzEVwcjrJnmC6g1OrMUwmpfumVlIZN6EWlIjOKUR4SwSvBM7Ig/holKmXpsGe5Lb/FhAH9rOq8oxbhmYJkBRzJES4/aMZ+v+6fcl25grKbDJCLUQIDAQAB");
		params.addApp("tinywarmol", "com.noumena.android.tinywarmol", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAmpnyc3yvIMiIf1wBcXKrdknRyX91C86Kggtxey78tVguqYXPo2sDJ0yZdxaWxfjKdj0MPxLGxWHgYm15+gqrriZCP7+RQQCNnZnohYOd5zi4vWBauSTM0EjQC8FaQirKJdRbJwdUYbx3DscjNIPZ0TtaIRPjQ+o9s3ejmqXYKimYUVyyb+zaVZF16rIfzCITSWlwPk1kzPLvyb8XAHd24ksm7DI4+TfkgZG0b7kQyzIHDOd8NJXthlQiyPRfB5cYUo99cF0bikrMxwbW59OwIakLFIWjOJCLykfXJaH/dsG5FM6IvgrOy/OII50ijBzEMckUiS8av6zcmAzHCice8wIDAQAB");
		params.addApp("galaxy2twmol", "com.noumena.android.galaxy2twmol", 		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEApUYgparTZhMheuw25JZw7Dre2wDSifMKzFIAuJ3h4nQ30JZbj9EdxpZgp+MTbSQD8mhV/28at7IHBa0prOgdtWAEntsr1cxSlkoUbcUNc7XHVDk4eAZZg3Rkf/JtHl1JdVeWT87k8MzBlE5TpZKzvNaFOeSiv3cqgN9Xa2Oxm2VCh+Tqxkyods6pxjoqxhTiMtFZ8WyAQZIQa+bDBasItNu6vC6sEehJ2KxJmHPV11x95PKXaBrCvhtKiNJWKPHPxZCPpeSklKgwgihrZju1tQH5IPxd5miYCWBf8ZJ4XbR9Dxq42LWkF0YUduckMQqiYUo1BTJXiQuzLWRFqRWt/wIDAQAB");
		params.addApp("m1en", "com.noumena.andorid.pocketfort.m1en", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnSXRrMlnWLL4fofPHkSalM6bQRhYyJ2gCC5SiWXE0C5NKZIgVe62QW83aRwX0L7C5MVgb6EVAIK9qTf2vz5WHrMNAF8kPaUP1q0LgZ64/qC0MvOckfLcXsVxX6YOpQ/4nekOkK7InKQIL1mQn26C8TyWtrZgB4vwI5XYtFRADzd7RDbos2OpfZfy5IUMl98CQye/if8dad6j5Z15h9NPJsDZNzpgjPdW/A9QNy2OB9xePFlbh8IOs88JITtjEo78nultNQEB4I8L+503/ke/YwfFt3ZffpUMUYENHjRYLFlgoxx8o49hqew5BXRKV50TxRWh2C5LAd/WZ8E9i8hhdQIDAQAB");
//		params.addApp("m1en.pro", "com.noumena.andorid.pocketfort.m1en.pro", 	"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAn/VDvoA+cWJsVYxP3EU24RMYWILTQw57WwXXGM6i73ioEiDRAoDCA06ZOxzQlAwcZbLBYIDv7RiftPaMMQIFJsYF0HQMLfoC5Tj4E1rPqD5U5/DbSiqfdii9QHXDwMaqQz47P7AFZGOBuVypITjdDsFB2Cicxn1GGlo2cTYfpPcrXEmoW+Z339u6PYeP8D7eoAX6nbtwNeIw8aKwDzBmegeAMaBzQHO1iIR8QHeGW6gdMlhQmEG0SiRDQTBv50rtaC1ePeBbWolRTdIqBYuqRPQ1Op4NzR68rFFwGmot/mlHVTa3LkWge2Qi+PCt6XOFfmAQ2WijXCaEBCcF4lEtsQIDAQAB");
		params.addApp("m1en.pro", "com.noumena.andorid.pocketfort.m1en.pro", 	"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAsuJFcLsiT+szugmIKT6Xb7NQ7tOBDUuB8K+G/mEaJRnKxbjnCmKk2urFh4y7XNR+2Kn5CvR2XegZRPiVgC3VHuuLRvAmNjyPblpyyef/o1NCD7HV5v2tE9hSnJiKlHWod2peWkmqDa/ywwqvuEm1NUY/zgadoKW5eqxVYP2z6miofG7sLdluHZuQL9YPezESwzcqBU3unxe73+a4ZNCpmUHzuonvpH+YPpUXyUttWJHbpKDnDMbxtq+If/ujrMfSTvHxXv94XQ3IkRQ0BfXnAmpb/hMvqityBkENpguE7PVNwXwSvk0WrdPwRWSVW5ZyUvG5wjt7SvqhnRo7JrTDCQIDAQAB");
		params.addApp("m1en.na", "com.noumena.andorid.pocketfort.m1en.na", 		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAw51V5RQrxQY+kb9/wTO8D7lLznDfVcuXT+hwW501EStiPcdmSK5QwnrklvgPDjIcMPsimXVn5WaA0lfT2AUR49qg/ptnxWllX3WoGq9jr+A9BMz38jMIvjoeAMYDYr0CynfmGVUT/hKSSMJVdzqQqgHTgMyhQZcu2ZVVhy5PIRbIv6o1G31rUPRDNczVZZM1396JIEv/vrajdJwlAXFQOADYt/RyA+oPPwz9cSN2Inj4iyjsguPBjWmZuyksMb7wePXoGnUMHGWo4Ne7r0mgWL+4ER/WtkI4+gU3hTUw1pZ/t89RrX+pKqJiBavfpuDfSUEuKvUFLFSAjWXcNnFo0wIDAQAB");
		params.addApp("t6tw", "com.noumena.tjmammoth.android.t6tw", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAjLyqATmXsefVj8mT1OnM1x8RS69L1COQ+/kYuevfhqzAVgQFV3Ti0rQd2rQ9/KNDdEcSJguoYk3nIAbhC5Q7fJ2Es/3y0BvQi688+p6/moXzVAYNmjCWZKyBXvZrUt46AfhqFnJqbYvHn+jzoje0RNIzI+GISEtNPhD6BCSW79eoejUskEF8T6GaydbRaKiuTiKK56ccrID5HSxUK+V+P1OOa40PhMZcuDKPtATS1mQso+QFOcdf2liCl2e7SPDpgXDUmcCKVwe8p9djVrmrmG2IJlJRz2fweWPlzyXNkZ+bsI4pwtvNFyoCgnB5azVdvgKoJ724eqcxfvuiuU2qfwIDAQAB");
		params.addApp("btw", "com.neocyon.btw", 								"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAuO09SBPCw9jyTvgjUyBFLbG9UFFAGEUaHftBxmumrM8Jh+BpdTya9fjv+6X7x9raVm2Yq5KgKa4MwjKcOrlqJzm6fvAiOVrt/Tqz/myalIMNBDopV1zthm0J1U/GiBBvgC4p7znK56bAUvoaroEqIoQFgh6VsIoA4hd2QH58XfB3LC2C5vheSDqV8xNH7Kc16mzVqhQuYs178U9HqgSIgqclDSBUBBVi8tJZy/G8XYhrKSGh7hsARkzinFx8ZdrCzOiybFn0EKqOdAFXmpOZ8eNiKz1//qs03mOFdGAP5fPfHT3f4OTTVENVhcTTp6VPwl7tTLtBDiwpriuwIgfBtwIDAQAB");
		params.addApp("t6kr", "com.noumena.tjmammoth.android.t6kr", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAnDqXWNJyGwxNBgq0CQbSajHZtUBmSXFQKbXRUdRziE6yXn/og3zxqzYNoy9fG/yvNQMPyMDAFAkE12IrlU9fHn+vWXT6iV29gsfOhBLyEcLdSrX5ydamyhYAa12/o0B/XP43wkYUtKiYuOeoULtTK9vqqMpIL/0Yvo89J0m/OIVewXKOcPLHqrjGAvDr3X7T0B0yOt5LF7prMBJoKw2nFWNhfsZ5InPlDzLb9E3ZSRrY7xCcSIuCf/CT1Q1ZN6W72qHSgFcRMgb0emN8hiU2qnzVw/9lCEpWhDEocf66H+K24bJ4yBnGPln3yc6kh7ASFYLmUXEsxuZReNRIYQYpQQIDAQAB");
		params.addApp("gaoguaikakao", "com.kongzhong.simlife.sanguokr", 		"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAoXuqNrmNev6Dv6H6/7r6/Iaqm7vlje3xHZjDV0qjgIvujKptJPU1oFD16uwUX4EPJJgHqwxpqC+1ts3VNagF6nbcdz21sx/uKb+JTvHMc9EAX7OzPZJX3ZxvsuuvgENElAo5k8ScDXbxrmzsB2K4OsTdLcMno/xCBASn3ulSSIpKELfkMRzHguV31eGvEQom/Ru4f5rD32277+a1bfgtTDiqge56r2WUXzcHg0+uBOwku3nzE5kpTma1lxM+phRK8exOpy+eX2DtupEnUEsFV5k6dDgV9+LfTBzB08ceXVMMXvNEqUPtAIf3L2HoYDBtg1XM3BBDlxjJtsGkxgd+ZQIDAQAB");
		params.addApp("t6us", "com.noumena.tjmammoth.android.t6us", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhxqTYIu1ajWlFhyFXBX+DpCs5wfcOSRRuZhgNaLpdYQArn9/h13hqpRcBRvIMQIDbAA/XhgR8giS2RGJorG92HQ2/guMte2GMDFRsuGKm7i3qs/Gsx4i1EhZVucg1wsFN0STZhRibJttQzr7PjRUq9OckmlWcYN2x9udriNUFLR4etJlY7XGlR/zEujsuZZKQkSnUL0Vxa35V2ewI9KSJCxo+PfxBfCBGyDJtrE1CNgu4Fmp30VQsiBxkyTGcSmfN2E/EmQLHLHXnVyCVlT4JS3kp/W6mZfcnv2UvEFDujh5lVLCIi55CX1Fi1x2/MxXOt0vSFOvoDu8wL4ZONerxwIDAQAB");
		params.addApp("m1Russia", "eu.inn.pocketfort", 							"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAhC0bZ36dawOkp36NH8r+p2LJjv8EZEPiabNYaqLPpqiZVzPeSWWmKAIE9U0t1qDt0/xxSiNvN4QivAOsanrt0XLitMVJavD+3Xt1TvuRjtVRKdQO7by8pW6eU/iWT3YutIOJjkVF+meOMy1HRLhExXw9GLFn9shCWzdxdZdMSx9GypnDoHFLJrAv65LewbiGnsOgYkmUd7xz+7GW9ZLvu+kH0DVtGe+VWHQcWcTjEd7lB7ZEtwxh/k1j4NtK6EDWJMhRWKxOQ1Sbijv8ZsG7cisgJxu3BhRJORDQRzQ2Q0QZQWk+AacURbFnyfGZSqwgBcsYK2Gy4Y/i6XcwdxdkOwIDAQAB");
		params.addApp("m1Russia", "eu.inn.pocketfort.live", 					"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAscj+zpxvO7+OLN0IFQHsunGVjRMTxnzdcDF+vDLIm5lZA1EbuAzLph8LnytWy9y4GX7grjiGtO8QifI7Wg+CGZ4ZF/Q4fybbj8gU3GiX55NhrvH2RT2j4ePA4S5NqIGfvtX1ghX4AoZmx/5+wJl8YF4OvuKiQbsLi4coM+y8A+rwU/OCxfYMLBQ1T5NK6RuYP0lNToaWxFFkj1IEi3McPEspzRYEpxjnotpPDRm8ZLKEWzdiXRetGToz/PyOncuQb8kS1Jpz4QXRbH91SXG92Ar4G6dr8WIbA4muMVczmdDYEGcKXEvqjDb0i+QQCF7ahSrxeBeIg1xlJJM5cZ6X5QIDAQAB");
		params.addApp("t6kr", "com.noumena.tjmammoth2.android.t6kr", 			"MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA6VnSVrYyMBXLpcr/DK4YU4jR5103p4wxywE9uzpT9fXS2BzDES1sqdgpNGrT4hpzoNWIzSjl+KjfkG9VNZKqnDUAU/h9iaEOyKHMGdFQ2ro8HOQH63RA+aC/HSBLnwwOgBIydB3IO1jvyqkrp89TCN6j4MTLQFmZw4/QOXCVwfypLzseaL6n/5+rA+BpVfYxjRA4jQDdL4SOjxKmz5jlkBeRgjI2nfQYcQQV3rVmTeExAyuZIx7b4ndhWLt+YUa2eu4Rs0GTrUMY2MFCR3JmS2UhnlcqfUyi4zNkuK1moBJcyEiAoI4U+BgFccYLNuUOyLHR2NjsekFojYsoCMoXgQIDAQAB");
	}
}

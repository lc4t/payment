package noumena.payment.lezhuo;

import java.util.Map;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.now.MD5;
import noumena.payment.util.Constants;

public class LeZhuoCharge {

	private static String SERVER_KEY = "2ff07f06632747a2994bbb47";

	/***
	 * 回调
	 * 
	 * @param vo
	 * @return
	 */
	public static String getCallbackFromLeZhuo(Map<String, String> guopanparams) {

		System.out.println("getCallbackFromLeZhuo  cb ================>"
				+ guopanparams.toString());
		String amount = guopanparams.get("amount");
		String area = guopanparams.get("area");
		String ext1 = guopanparams.get("ext1");
		String ext2 = guopanparams.get("ext2");
		String lzwOrderid = guopanparams.get("lzw_orderid");
		String orderid = guopanparams.get("orderid");
		String productdesc = guopanparams.get("productdesc");
		String state = guopanparams.get("state");
		String sign = guopanparams.get("sign");
		String time = guopanparams.get("time");
		String totalfee = guopanparams.get("totalfee");
	     if(ext1==null){
	    	 ext1="";
	     }
	     if(ext2==null){
	    	 ext2="";
	     }
		String presign = amount + area + ext1 + ext2 +lzwOrderid+orderid+productdesc+state+time+totalfee+ SERVER_KEY;
		try {
			if ("failure".equals(state)) {
				System.out
				.println(orderid+" getCallbackFromLeZhuo  cb ================> state failure");
				return "failure";
			}
			System.out
					.println(orderid+" getCallbackFromLeZhuo  cb ================> persign"
							+ presign);
			String endSign = MD5.md5(presign, "UTF-8").toLowerCase();
			System.out
					.println(orderid+" getCallbackFromLeZhuo  cb ================> endSign"
							+ endSign);
			if (!endSign.equals(sign)) {
				System.out
				.println(orderid+" getCallbackFromLeZhuo  cb ================> endSign"
						+ sign+" "+endSign);
				return "sign is error";
			}
			OrdersBean bean = new OrdersBean();
			Orders orders = bean.qureyOrder(orderid);
			if (orders == null) {
				System.out
				.println(orderid+" getCallbackFromLeZhuo  cb ================> order is not exist"
						);
				return "order is not exist";
			}
			if (!orders.getKStatus().equals(Constants.K_STSTUS_DEFAULT)) {
				System.out
				.println(orderid+" getCallbackFromLeZhuo  cb ================> order is repeating"
						);
				return "order is repeating ";
			}
			if (!orders.getAmount().equals(Float.parseFloat(totalfee))) {
				System.out.println(orderid+"money is differen =========order "
						+ orders.getAmount() + "input"
						+ Float.parseFloat(totalfee));
				return "money is different ";
			}
			bean.updateOrderAmountPayIdExinfo(orderid, lzwOrderid, totalfee,
					productdesc+"/"+amount+"/"+area+"/"+ext1+"/"+ext2);
			bean.updateOrderKStatus(orderid, Constants.K_STSTUS_SUCCESS);
			System.out
			.println(orderid+" getCallbackFromLeZhuo  cb ================> order is success"
					);
			return "success";
		} catch (Exception e) {
			e.printStackTrace();

			return "system error ";
		}
	}
}

package noumena.payment.qiangwan;

import java.util.Map;

import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.StringEncrypt;

public class QiangWanCharge {
	private static String key = "";
	public static String getCallbackQiangWan(Map<String, String> QiangWanparams) {
		String gameId = QiangWanparams.get("game_id");
		String outTradeNo = QiangWanparams.get("out_trade_no");
		String price = QiangWanparams.get("price");
		String extend = QiangWanparams.get("extend");
		String sign = QiangWanparams.get("sign");
		String preKey = gameId + outTradeNo + price + extend + key;
		System.out
				.println("getCallbackQiangWan============================preKey"
						+ preKey);
		String afterKey = StringEncrypt.Encrypt(preKey, "MD5");
		System.out
				.println("getCallbackQiangWan============================afterKey"
						+ afterKey);
		if(!sign.equals(afterKey)){
			System.out.println(outTradeNo+"getCallbackQiangWan==========================>sign is error");
			return "{\"result\":\"sign is error\"}";
		}
		OrdersBean bean = new OrdersBean();
		if (outTradeNo == null || "".equals(outTradeNo)) {
			System.out.println(outTradeNo+"getCallbackQiangWan==========================>outTradeNo is empty");
			return "{\"result\":\"outTradeNo is empty\"}";
		}
		Orders orders = bean.qureyOrder(outTradeNo);
		if (orders == null) {
			System.out.println(outTradeNo+"getCallbackQiangWan==========================>order is not exit");
			return "{\"result\":\"order is not exit\"}";
		}
		if (orders.getKStatus() == Constants.K_STSTUS_SUCCESS) {
			System.out.println(outTradeNo+"getCallbackQiangWan==========================>order is already success");
			return "{\"result\":\"order is already success\"}";
		}
		if (orders.getKStatus() == Constants.K_STSTUS_DEFAULT) {
			bean.updateOrderAmountPayIdExinfo(outTradeNo, outTradeNo, price, extend);
			bean.updateOrderKStatus(outTradeNo, Constants.K_STSTUS_SUCCESS);
			System.out.println(outTradeNo+"getCallbackQiangWan==========================>order  success");
			return "{\"result\":\"success\"}";
		}

		System.out.println(outTradeNo+"getCallbackQiangWan==========================>error");
		return "{\"result\":\"error\"}";
	}

	public static void main(String args[]) {
	}

	public static void init() {

		// params.addQihuApp("TinyWar", "586d0609d40b1e014248a9558c29763c",
		// "c16e06f895ed0109f5811f758781e93f");
		// params.addQihuApp("Galaxy2", "716a2ac2d5062da3f364a4c845984f4e",
		// "277496fece2ae04518ad1d7aeec5b6e6");
		// params.addQihuApp("BattleLand", "15448dce69c76e5fcd657c4f7a141631",
		// "5259f12fda7d0cc990ba04b9b0b6f504"); //Appid: 200266196
		// params.addQihuApp("M1", "73ea650556a7bd5a05633388c1de15a7",
		// "5bf6722d5022a24f161d800d3584f30a"); //Appid: 200542601
		// params.addQihuApp("T6", "1391ed3caaf74f60efe420d6b10e50fd",
		// "cf6306985cde4537cef13f5af4d1eb1d"); //Appid: 200626241
		// params.addQihuApp("Mingjiang", "60e3f1b5a6ee08559b84669b6c8e5f10",
		// "ec7a7dc906d1e4e49c1de2a34da7d80f"); //Appid: 201023021
		// params.addQihuApp("Gaoguai", "c1262e00fe079a8a8bbed448a26c36df",
		// "ff1453dd0ebc2b30e032acb1d8de20d1"); //Appid: 201078481
		// params.addQihuApp("T6", "f3a11f58c3f68bef3239e907accebb4c",
		// "c0ca2518950be07f16ec1a3f53b3a2a1"); //Appid: 201117096
		// params.addQihuApp("T6", "a6f904a4f06fe76212f7e761dc06f7b7",
		// "0dc2a35ed72c26a853ac5fd49195b4d3"); //Appid: 201168356
		// params.addQihuApp("xiexuegui", "10804801513f20868b16e1b1731f52c1",
		// "402aeb73b9a3425b1172993bd92ed0c4"); //Appid: 201774721
		// params.addQihuApp("M5", "97e9cf4c13c691f0d1e1e24fe5b5feab",
		// "091cd3d50cc39e24354ecb93fd1d9efb"); //Appid: 201828271
		// params.addQihuApp("qingwa", "9c11195f64315044e8409ac73ba6a5f2",
		// "a316dbadf43d819ac9dd4ed1a363af78"); //Appid: 201875901
		// params.addQihuApp("Qzhuan", "280dd7b40f9576b81b175a1bd0bee989",
		// "a6b84b41c10572fe6c8c74e6147927b3"); //Appid: 202083501
		// params.addQihuApp("M3", "f739cfbb9f9510ef039a3c22e2756e01",
		// "b6c5ec4a0ae5da926170f7c95b447282"); //Appid: 202121406
	}
}

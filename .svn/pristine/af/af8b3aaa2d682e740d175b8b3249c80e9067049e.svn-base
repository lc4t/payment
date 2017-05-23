package cn.i4.pay.sdk.service;

import java.util.Map;

import cn.i4.pay.sdk.conf.PayConfig;
import cn.i4.pay.sdk.util.PayCore;



/**
 * 
 * @author Jonny
 *
 */
public class PayService{
	
	
	
    /**
     * 异步通知消息验证
     * @param para 异步通知消息
     * @return 验证结果
     */
    public static boolean verifySignature(Map<String, String> para, String publickey) {
        try {
			String respSignature = para.get(PayConfig.SIGNATURE);
			// 除去数组中的空值和签名参数
			Map<String, String> filteredReq = PayCore.paraFilter(para);
			System.out.println("filteredStr=" + PayCore.createLinkString(filteredReq, true, true));
			Map<String, String> signature = PayCore.parseSignature(respSignature, publickey);
			for (String key : filteredReq.keySet()) {
			    String value = filteredReq.get(key);
			    String signValue = signature.get(key);
			    if (!value.equals(signValue)) {
			    	return false;
			    }
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
        return true;
    }
	
}
	
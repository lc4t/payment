package noumena.pay.util;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * MD5Facade  ---  汇集多类型MD5签名逻辑
 * User: 韩彦伟
 * Date: 14-8-6
 * Time: 下午7:52
 * To change this template use File | Settings | File Templates.
 */
public class MD5Facade {
    /**
     * 针对NowPay目前统一的MD5签名方式：key1=value1&key2=value2....keyn=valuen&securityKeySignature  进行MD5
     * @param dataMap  --需要参与MD5签名的数据
     * @param securityKey    --密钥
     * @return
     */
    public static boolean validateFormDataParamMD5(Map<String,String> dataMap,String securityKey,String currentSignature){
        if(dataMap == null) return false;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(value != null && value.length()>0){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = MD5.md5(securityKey,securityKey);
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();

            String actualMD5Value = MD5.md5(toMD5String,"");

            return actualMD5Value.equals(currentSignature);
        }catch (Exception ex){
            return false;
        }
    }

    /**
     * 针对NowPay目前统一的MD5签名方式：key1=value1&key2=value2....keyn=valuen&securityKeySignature  进行MD5
     * <p>要先对Key进行按字典升序排序
     * @param dataMap  -- 数据
     * @param securityKey    --密钥
     * @param charset
     * @return
     */
    public static String getFormDataParamMD5(Map<String,String> dataMap,String securityKey,String charset){
    	System.out.println(securityKey);
        if(dataMap == null) return null;

        Set<String> keySet = dataMap.keySet();
        List<String> keyList = new ArrayList<String>(keySet);
        Collections.sort(keyList);

        StringBuilder toMD5StringBuilder = new StringBuilder();
        for(String key : keyList){
            String value = dataMap.get(key);

            if(value != null && value.length()>0){
                toMD5StringBuilder.append(key+"="+ value+"&");
            }
        }

        try{
            String securityKeyMD5 = MD5.md5(securityKey,charset);
            toMD5StringBuilder.append(securityKeyMD5);

            String toMD5String = toMD5StringBuilder.toString();
            System.out.println("待MD5签名字符串：" + toMD5String);

            String lastMD5Result = MD5.md5(toMD5String,charset);
            System.out.println("MD5签名后字符串:" + lastMD5Result);

            return lastMD5Result;
        }catch (Exception ex){
            //ignore
            return "";
        }
    }
    public static void main(String arg[]) throws Exception{
    	String aString="appId=1410853543946442&frontNotifyUrl=http://paystage.ko.cn:6001/paymentsystem/nowhtmlcb&mhtCharset=UTF-8&mhtCurrencyType=156&mhtOrderAmt=1&mhtOrderDetail=sfvvcxcv&mhtOrderName=ewwe&mhtOrderNo=20170104173314462P7U&mhtOrderStartTime=20170104173314&mhtOrderType=01&notifyUrl=http://localhost:8080/PaymentKong/nowcb&payChannelType=1301&cdd1f161a169087c66d4e5ab61dc8e78";
    	System.out.println(MD5.md5(aString, "UTF-8"));
    }
    
    
}
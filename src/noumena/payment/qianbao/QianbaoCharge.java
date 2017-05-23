package noumena.payment.qianbao;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import sun.misc.BASE64Decoder;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.toutiao.Base64;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;

public class QianbaoCharge
{
	private static QianbaoParams params = new QianbaoParams();
	
	public static String getTransactionId(Orders order)
	{
		order.setCurrency(Constants.CURRENCY_RMB);
		order.setUnit(Constants.CURRENCY_UNIT_YUAN);
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
				cburl += "?pt=" + Constants.PAY_TYPE_QIANBAO;
			}
			else
			{
				cburl += "&pt=" + Constants.PAY_TYPE_QIANBAO;
			}
			cburl += "&currency=" + Constants.CURRENCY_RMB;
			cburl += "&unit=" + Constants.CURRENCY_UNIT_YUAN;
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
				Calendar cal1 = DateUtil.getCalendar(order.getCreateTime());
				Calendar cal2 = Calendar.getInstance();
				if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT)
				{
					st.setStatus(4);
				}
				else
				{
					st.setStatus(3);
				}
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
	
	public static String getCallbackFromQianbao(Map<String,String> qianbaoparams)
	{
		String ret = "{\"isSuccess\":false}";
		System.out.println("qianbao cb ->" + qianbaoparams.toString());
		
		QianbaoOrderVO covo = new QianbaoOrderVO();
		covo.setResponseCode(qianbaoparams.get("responseCode"));
		covo.setErrorCode(qianbaoparams.get("errorCode"));
		covo.setErrorMsg(qianbaoparams.get("errorMsg"));
		covo.setData(qianbaoparams.get("data"));
		JSONObject json = JSONObject.fromObject(covo.getData());
		String orderid = json.getString("orderNo");
		String flowid = json.getString("sdkflowId");
		String miwen = "";
		miwen += "responseCode=" + covo.getResponseCode() + ",";
		miwen += "errorCode=\"\",";
		miwen += "sdkflowId=" + flowid + ",";
		miwen += "orderNo=" + orderid;
		
		if("1000".equals(covo.getResponseCode())){
			OrdersBean bean = new OrdersBean();
			Orders order = bean.qureyOrder(orderid);
			System.out.println("qianbaomiwen:"+miwen);
			System.out.println("qianbaosign:"+qianbaoparams.get("signCode"));
			System.out.println("qianbaoappkey:"+params.getParams(order.getSign()).getAppkey());
			try {
				if(verify(miwen.getBytes(),params.getParams(order.getSign()).getAppkey(),qianbaoparams.get("signCode"))){
					if (order != null) {
							if (order.getKStatus() != Constants.K_STSTUS_SUCCESS) {
								bean.updateOrderAmountPayIdExinfo(orderid, flowid, order.getAmount().toString(), "");
								bean.updateOrderKStatus(orderid,Constants.K_STSTUS_SUCCESS);
							} else {
								System.out.println("qianbao order ("+ order.getOrderId() + ") had been succeed");
							}
							ret = "{\"isSuccess\":true}";
					}
				}else{
					System.out.println("qianbao order ("+ orderid + ") sign is error");
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
			System.out.println("qianbao cb ret->" + ret);

			String path = OSUtil.getRootPath() + "../../logs/qianbaocb/"
					+ DateUtil.getCurTimeStr().substring(0, 8);
			OSUtil.makeDirs(path);
			String filename = path + "/" + orderid;

			OSUtil.saveFile(filename, qianbaoparams.toString());
		return ret;
	}
	
	/**
     * 校验数字签名
     * @param data	加密数据
     * @param publicKey	公钥
     * @param sign	数字签名
     * @return
     * @throws Exception
     */
    public static boolean verify(byte[] data,String publicKey,String sign)throws Exception{
        //解密公钥
        byte[] keyBytes = decryptBASE64(publicKey);
        //构造X509EncodedKeySpec对象
        X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(keyBytes);
        //指定加密算法
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        //取公钥匙对象
        PublicKey publicKey2 = keyFactory.generatePublic(x509EncodedKeySpec);
        
        Signature signature = Signature.getInstance("MD5withRSA");
        signature.initVerify(publicKey2);
        signature.update(data);
        //验证签名是否正常
        return signature.verify(decryptBASE64(sign));
        
    }
    /**
     * BASE64解密
     * @param key
     * @return
     * @throws Exception
     */
    public static byte[] decryptBASE64(String key) throws Exception{
        return (new BASE64Decoder()).decodeBuffer(key);
    }
    
	
	public static void init()
	{
		params.initParams(QianbaoParams.CHANNEL_ID, new QianbaoParamsVO());
	}
	
	public static void main(String args[]){//0c8865930fc94d8d61b1bdcc8312a65d
		String date = "responseCode=1000,errorCode=\"\",sdkflowId=55,orderNo=20160816145835941247";
		String sign = "Bf9foNnY7QbxkS7YtRfgkZWDzL5fJ12rjHt9ev3eW8/u0oGUAQMeiH3nPCVU2ISP1bud5SUj+ITvMgUsTW6bVVuSPSHi0sSi4bmTSXmFyoKQol4zdU6TxFH6wudWpyc1LipSG5oRCqh9jVLK/6/YWF4Na1cQ0gepW9us4fXnVGk=";
		String key = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDIAt5u+adjWIi7hVA9pKGGjNQKNFeyaDgG59rwUAANREkzkYJtRrj5TtiqUKy+695QAfcowRv0u02QTTw1FBTubVxkEb2aOmHaYWV+KG73ZfcA3PdcgcuoD/ToW57JmtNC5f+dNOTJWzpaK8WZzoUshVIi5dOUe1uFnZiXx3e9dQIDAQAB";
		try {
			boolean	a = verify(date.getBytes(),key,sign);
			System.out.println(a);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}

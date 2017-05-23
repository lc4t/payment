package noumena.payment.gash;

import java.net.URLEncoder;
import java.text.DecimalFormat;

import noumena.payment.bean.OrdersBean;
import noumena.payment.gash.ws.Checkorder;
import noumena.payment.gash.ws.CheckorderSoap;
import noumena.payment.gash.ws.Settle;
import noumena.payment.gash.ws.SettleSoap;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;

import com.gashplus.gps.transaction.Trans;


public class GASHCharge
{
	private static boolean testmode = false;
	
	public static boolean isTestmode()
	{
		return testmode;
	}

	public static void setTestmode(boolean testmode)
	{
		GASHCharge.testmode = testmode;
	}
	
	public static void init()
	{
		GASHPaytypeParams paytype = null;
		GASHPaidParams paid = null;
		/*
		paytype = new GASHPaytypeParams();
		paytype.setType(Constants.PAY_TYPE_GASH_TEL);
		paytype.setMid("M1000081");
		paytype.setCid("C000810000164");
		paytype.setKey1("w5vey9OvmzbHyiInEmqW0hFxIFh+Fugm");
		paytype.setKey2("7iBQXg/2YAg=");
		paytype.setPwd("1QASW23EDZXC");
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELCHT01");
		paid.setName("中华电信 市话");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELCHT02");
		paid.setName("中华电信 Hinet");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELCHT03");
		paid.setName("中华电信 手机");
		paytype.getPaids().add(paid);
		GASHParams.getPaytypes().add(paytype);
		
		paytype = new GASHPaytypeParams();
		paytype.setType(Constants.PAY_TYPE_GASH_POINT);
		paytype.setMid("M1000080");
		paytype.setCid("C000800000165");
		paytype.setKey1("/FVSrzJVqzaoUvHR3K4NifZgxErIzHHG");
		paytype.setKey2("1/e8sMeKEK8=");
		paytype.setPwd("FQFBNLUA");
		paid = new GASHPaidParams();
		paid.setCuid("PIN");
		paid.setPaid("COPGAM02");
		paid.setName("GASH点数卡");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("PIN");
		paid.setPaid("COPGAM05");
		paid.setName("行动平台 GASH点数卡");
		paytype.getPaids().add(paid);
		GASHParams.getPaytypes().add(paytype);*/
		
		paytype = new GASHPaytypeParams();
		paytype.setType(Constants.PAY_TYPE_GASH_TEL);
		paytype.setMid("M1000111");
		paytype.setCid("C001110000204");
		paytype.setKey1("1DXawfnUh9KFpQQ7xImQjBE2h8QsXEUG");
		paytype.setKey2("4l+ksqRnabY=");
		paytype.setPwd("ksi3zh7a");
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELCHT07 ");
		paid.setName("中華電信市話三合一型");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELCHT06");
		paid.setName("中華電信數據一般型(Hinet)");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELCHT05");
		paid.setName("中華電信839一般型");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		/*paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELVIBO");
		paid.setName("威寶電信");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);*/
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELTCC01");
		paid.setName("台灣大哥大一般型");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		/*paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELSON04");
		paid.setName("亞太電信");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);*/
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("TELFET01");
		paid.setName("遠傳電信一般型");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		GASHParams.getPaytypes().add(paytype);
		
		paytype = new GASHPaytypeParams();
		paytype.setType(Constants.PAY_TYPE_GASH_BANK);
		paytype.setMid("M1000111");
		paytype.setCid("C001110000204");
		paytype.setKey1("1DXawfnUh9KFpQQ7xImQjBE2h8QsXEUG");
		paytype.setKey2("4l+ksqRnabY=");
		paytype.setPwd("ksi3zh7a");
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("COPPEZ01");
		paid.setName("Pay Easy");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("COPPAL01");
		paid.setName("PayPal");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("BNK82204");
		paid.setName("銀聯卡");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("BNK82201");
		paid.setName("中國信託一般信用卡");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("BNK80803");
		paid.setName("玉山銀行(大額交易)");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("BNK80802");
		paid.setName("玉山銀行消費性扣款");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("TWD");
		paid.setPaid("BNK80801");
		paid.setName("玉山銀行WEB ATM");
		paid.setErpid("J990001");
		paytype.getPaids().add(paid);
		GASHParams.getPaytypes().add(paytype);
		
		paytype = new GASHPaytypeParams();
		paytype.setType(Constants.PAY_TYPE_GASH_POINT);
		paytype.setMid("M1000112");
		paytype.setCid("C001120000205");
		paytype.setKey1("E3JK8ebSVKGHSDTb3f0EaX6TtlJWz+l8");
		paytype.setKey2("JD/ixPOy4iw=");
		paytype.setPwd("83dh2wkp");
		paid = new GASHPaidParams();
		paid.setCuid("PIN");
		paid.setPaid("COPGAM02");
		paid.setName("PINHALL");
		paid.setErpid("PINHALL");
		paytype.getPaids().add(paid);
		paid = new GASHPaidParams();
		paid.setCuid("PIN");
		paid.setPaid("COPGAM05");
		paid.setName("PINHALL_Mobile");
		paid.setErpid("PINHALL");
		paytype.getPaids().add(paid);
		GASHParams.getPaytypes().add(paytype);
	}
	
	public static GASHPaytypeParams getGashPaytypeParamsByPaid(String paid)
	{
		for (int i = 0 ; i < GASHParams.getPaytypes().size() ; i++)
		{
			GASHPaytypeParams paytype = GASHParams.getPaytypes().get(i);
			if (paytype.getPaidParams(paid) != null)
			{
				return paytype;
			}
		}
		return null;
	}
	
	public static GASHPaytypeParams getGashPaytypeParams(String cid)
	{
		for (int i = 0 ; i < GASHParams.getPaytypes().size() ; i++)
		{
			if (GASHParams.getPaytypes().get(i).getCid().equals(cid))
			{
				return GASHParams.getPaytypes().get(i);
			}
		}
		return null;
	}
	
	public static String getSendData(String orderid, String paid, String amount, String prodname, String prodcodename, String uid)
	{
		GASHPaytypeParams paytype = getGashPaytypeParamsByPaid(paid);
		GASHPaidParams paidp = paytype.getPaidParams(paid);
		String cburl = "";
		
		if (testmode == true)
		{
			cburl = GASHParams.GASH_CB_URL_TEST;
		}
		else
		{
			cburl = GASHParams.GASH_CB_URL_RELEASE;
		}

		Trans trans = new Trans();
		trans.setKey(paytype.getKey1());
		trans.setIv(paytype.getKey2());
		trans.setPwd(paytype.getPwd());

		// 交易訊息代碼
		trans.putNode("MSG_TYPE", "0100");
		// 交易處理代碼
		trans.putNode("PCODE", "300000"); // 一般交易請使用 300000, 月租交易請使用 303000, 月租退租請使用 330000
		// 商家遊戲代碼
		trans.putNode("CID", paytype.getCid());
		// 商家訂單編號
		trans.putNode("COID", orderid);
		// 幣別 ISO Alpha Code
		trans.putNode("CUID", paidp.getCuid());
		// 付款代收業者代碼
		trans.putNode("PAID", paidp.getPaid()); // 此範例為台灣大哥大一般型, 月租交易請使用 TELTCC02
		// 交易金額
		String f = new DecimalFormat("0.00").format(new Float(amount));
		if (paytype.getType().equals(Constants.PAY_TYPE_GASH_POINT))
		{
			//点卡类不传金额
			trans.putNode("AMOUNT", "0");
		}
		else
		{
			//其他类需要传入给定金额
			trans.putNode("AMOUNT", f);
		}
		// 商家接收交易結果網址
		trans.putNode("RETURN_URL", cburl);
		// 是否指定付款代收業者
		trans.putNode("ORDER_TYPE", "M"); // 請固定填 M
		// 交易備註 ( 此為選填 )
		trans.putNode("MEMO", "測試交易"); // 請填寫此筆交易之備註內容
		// 商家商品名稱 ( 此為選填 )
		trans.putNode("PRODUCT_NAME", prodname);
		// 商家商品代碼 ( 此為選填 )
		trans.putNode("PRODUCT_ID", prodcodename);
		// 玩家帳號 ( 此為選填 )
		trans.putNode("USER_ACCTID", uid);
		// ERP ID
		trans.putNode("ERP_ID", paidp.getErpid());

		// 以商家密碼、商家密鑰 I , II ( 已於 Common.php 內設定 ) 取得 ERQC
		String erqc = trans.getErqc(trans.getPwd(), trans.getKey(), trans.getIv());

		// 商家交易驗證壓碼
		trans.putNode("ERQC", erqc);

		// 取得送出之交易資料
		String sendData = "";
		try
		{
			sendData = URLEncoder.encode(trans.getSendData(), "utf-8");
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
		
		return sendData;
	}
	
	public static String getSettleData(String recvPaid, String orderid, String recvAmount)
	{
		//生成请款压码
		Trans trans = new Trans();
		
		GASHPaytypeParams paytype = GASHCharge.getGashPaytypeParamsByPaid(recvPaid);
		trans.setKey(paytype.getKey1());
		trans.setIv(paytype.getKey2());
		trans.setPwd(paytype.getPwd());

        // 交易訊息代碼
        trans.putNode("MSG_TYPE", "0500");
        // 交易處理代碼
        trans.putNode("PCODE", "300000"); // 一般交易請使用 300000, 月租交易請使用 303000, 月租退租請使用 330000
        // 商家遊戲代碼
        trans.putNode("CID", paytype.getCid());
        // 商家訂單編號
        trans.putNode("COID", orderid);
        // 幣別 ISO Alpha Code
        trans.putNode("CUID", "TWD");
        // 幣別 ISO Alpha Code
        trans.putNode("PAID", recvPaid);
		String f = new DecimalFormat("0.00").format(new Float(recvAmount));
        trans.putNode("AMOUNT", f);
        
        // 以商家密碼、商家密鑰 I , II ( 已於 Common.php 內設定 ) 取得 ERQC
        String erqc = trans.getErqc(trans.getPwd(), trans.getKey(), trans.getIv());

        // 商家交易驗證壓碼
        trans.putNode("ERQC", erqc);

        // 取得送出之交易資料
        String sendData = trans.getSendData();
        
        return sendData;
	}

	public static String settle(String sendData)
	{
		//请款SOAP请求
        Settle ws = new Settle();
        SettleSoap port = ws.getSettleSoap();
        String recvData = port.getResponse(sendData);
        
        return recvData;
	}
	
	public static String handleSettle(String recvPaid, String recvData)
	{
		//处理请款请求
		String retstr = GASHParams.GASH_MSG_SUCCESS;
		Trans trans = new Trans(recvData);

		GASHPaytypeParams paytype = GASHCharge.getGashPaytypeParamsByPaid(recvPaid);
		trans.setKey(paytype.getKey1());
		trans.setIv(paytype.getKey2());
		trans.setPwd(paytype.getPwd());
		
		String recvRCode = trans.getNodes().get("RCODE");
		String recvPayStatus = trans.getNodes().get("PAY_STATUS");
		String recvERPC = trans.getNodes().get("ERPC");
        String recvCoid = trans.getNodes().get("COID");
    	String amount = trans.getNodes().get("AMOUNT");
    	
		org.dom4j.Document doc = trans.generateXmlDoc();
		String cbcontent = doc.asXML();
		String path = OSUtil.getRootPath() + "../../logs/gashsettle/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + recvCoid;
		OSUtil.saveFile(filename, cbcontent);

		String erpc = trans.getErpc(trans.getKey(), trans.getIv());

		OrdersBean bean = new OrdersBean();
        if (erpc != null && !erpc.equals("") && erpc.equals(recvERPC))
        {
        	//验证通过，合法回调
        	if (recvRCode.equals("0000"))
        	{
		        if (recvPayStatus.equals("S"))
		        {
		        	//成功
		        	retstr = orderDone(bean, recvCoid, amount);
		        }
		        else if (recvPayStatus.equals("F"))
		        {
		        	//失败
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
	        		retstr = GASHParams.GASH_MSG_FAILD_ORDER;
		        }
		        else if (recvPayStatus.equals("T"))
		        {
		        	//交易超时
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
	        		retstr = GASHParams.GASH_MSG_ORDER_TIMEOUT;
		        }
		        else if (recvPayStatus.equals("0"))
		        {
		        	//交易未完成
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_TIMEOUT);
	        		retstr = GASHParams.GASH_MSG_ORDER_RELAY;
		        }
		        else if (recvPayStatus.equals("W"))
		        {
		        	//交易待确认
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_TIMEOUT);
	        		retstr = GASHParams.GASH_MSG_ORDER_RELAY;
		        }
        	}
        	else
        	{
        		retstr = GASHParams.GASH_MSG_FAILD_ORDER;
				bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
        	}
        }
        else
        {
        	//压码不正确
        	retstr = GASHParams.GASH_MSG_ERROR_CODE;
			bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
        }
        return retstr;
	}
	
	private static String orderDone(OrdersBean bean, String recvCoid, String amount)
	{
		String ret = GASHParams.GASH_MSG_SUCCESS;
		int kstatus = Constants.K_STSTUS_SUCCESS;
    	double amountd = Double.parseDouble(amount);
    	Orders order = bean.qureyOrder(recvCoid);
    	if (order.getAmount() == amountd)
    	{
    		//选择和支付的金额是一致的，成功
			kstatus = Constants.K_STSTUS_SUCCESS;
    	}
    	else
    	{
    		//选择和支付的金额不一致的需要修改订单数据库
    		if (order.getAmount() > amountd)
        	{
        		//选择的金额大于支付的金额，玩家黑钻，不成功
    			kstatus = Constants.ORDER_ERROR;
    			ret = GASHParams.GASH_MSG_ORDER_INVALID;
        	}
        	else
        	{
        		//选择的金额小于支付的金额，玩家亏了，成功
        		kstatus = Constants.K_STSTUS_SUCCESS;
        	}
    		order.setItemPrice(amount);
    		bean.updateOrder(recvCoid, order);
    	}
		bean.updateOrderKStatus(recvCoid, kstatus);
		return ret;
	}

	public static void toCheckOrder(Orders order)
	{
		String sendData = getCheckOrderData(order.getExInfo(), order.getOrderId(), "" + order.getAmount());
		String recvData = checkOrder(sendData);
		handleCheckOrder(order.getExInfo(), recvData);
	}
	
	public static String getCheckOrderData(String paid, String orderid, String recvAmount)
	{
		//生成验证订单压码
		Trans trans = new Trans();
		
		GASHPaytypeParams paytype = GASHCharge.getGashPaytypeParamsByPaid(paid);
		trans.setKey(paytype.getKey1());
		trans.setIv(paytype.getKey2());
		trans.setPwd(paytype.getPwd());

        // 交易訊息代碼
        trans.putNode("MSG_TYPE", "0500");
        // 交易處理代碼
        trans.putNode("PCODE", "300000"); // 一般交易請使用 300000, 月租交易請使用 303000, 月租退租請使用 330000
        // 商家遊戲代碼
        trans.putNode("CID", paytype.getCid());
        // 商家訂單編號
        trans.putNode("COID", orderid);
        // 幣別 ISO Alpha Code
        trans.putNode("CUID", "TWD");
        // 幣別 ISO Alpha Code
//        trans.putNode("PAID", paid);
        trans.putNode("AMOUNT", recvAmount);

        // 以商家密碼、商家密鑰 I , II ( 已於 Common.php 內設定 ) 取得 ERQC
        String erqc = trans.getErqc(trans.getPwd(), trans.getKey(), trans.getIv());

        // 商家交易驗證壓碼
        trans.putNode("ERQC", erqc);

        // 取得送出之交易資料
        String sendData = trans.getSendData();
        
        return sendData;
	}

	public static String checkOrder(String sendData)
	{
		//验证订单SOAP请求
        Checkorder ws = new Checkorder();
        CheckorderSoap port = ws.getCheckorderSoap();
        String recvData = port.getResponse(sendData);
        
        return recvData;
	}
	
	public static String handleCheckOrder(String paid, String recvData)
	{
		//处理验证订单请求
		String retstr = GASHParams.GASH_MSG_SUCCESS;
		Trans trans = new Trans();
		
		GASHPaytypeParams paytype = GASHCharge.getGashPaytypeParamsByPaid(paid);
		trans.setKey(paytype.getKey1());
		trans.setIv(paytype.getKey2());
		trans.setPwd(paytype.getPwd());

        trans = new Trans(recvData);
		trans.setKey(paytype.getKey1());
		trans.setIv(paytype.getKey2());
		trans.setPwd(paytype.getPwd());
		
		String recvRCode = trans.getNodes().get("RCODE");
		String recvPayStatus = trans.getNodes().get("PAY_STATUS");
		String recvERPC = trans.getNodes().get("ERPC");
        String recvCoid = trans.getNodes().get("COID");
		String erpc = trans.getErpc(trans.getKey(), trans.getIv());

		org.dom4j.Document doc = trans.generateXmlDoc();
		String cbcontent = doc.asXML();
		String path = OSUtil.getRootPath() + "../../logs/gashcheckorder/" + DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + recvCoid;
		OSUtil.saveFile(filename, cbcontent);

		OrdersBean bean = new OrdersBean();
        if (erpc != null && !erpc.equals("") && erpc.equals(recvERPC))
        {
        	//验证通过，合法回调
        	if (recvRCode.equals("0000"))
        	{
		        if (recvPayStatus.equals("S"))
		        {
		        	//成功
		        	String amount = trans.getNodes().get("AMOUNT");
		        	orderDone(bean, recvCoid, amount);
		        }
		        else if (recvPayStatus.equals("F"))
		        {
		        	//失败
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
		        }
		        else if (recvPayStatus.equals("T"))
		        {
		        	//交易超时
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
		        }
		        else if (recvPayStatus.equals("0"))
		        {
		        	//交易未完成
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_TIMEOUT);
		        }
		        else if (recvPayStatus.equals("W"))
		        {
		        	//交易待确认
					bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_TIMEOUT);
		        }
        	}
        	else
        	{
        		retstr = GASHParams.GASH_MSG_FAILD_ORDER;
				bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
        	}
        }
        else
        {
        	//压码不正确
        	retstr = GASHParams.GASH_MSG_ERROR_CODE;
			bean.updateOrderKStatus(recvCoid, Constants.K_STSTUS_ERROR);
        }
        return retstr;
	}
}

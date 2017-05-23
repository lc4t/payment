package noumena.payment.wandoujia;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import noumena.pay.util.MD5;
import noumena.payment.bean.OrdersBean;
import noumena.payment.model.Orders;
import noumena.payment.userverify.ChannelVerify;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.OSUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderIdVO;
import noumena.payment.vo.OrderStatusVO;
public class WandoujiaCharge
{
  private static WandoujiaParams params = new WandoujiaParams();
  private static boolean testmode = false;
  
  public static boolean isTestmode()
  {
    return testmode;
  }
  
  public static void setTestmode(boolean testmode)
  {
    testmode = testmode;
  }
  
  public static String getTransactionId(Orders order)
  {
    order.setCurrency(Constants.CURRENCY_RMB);
    order.setUnit(Constants.CURRENCY_UNIT_FEN);
    
    OrdersBean bean = new OrdersBean();
    String cburl = order.getCallbackUrl();
    String payId;
    if ((cburl == null) || (cburl.equals("")))
    {
      payId = bean.CreateOrder(order);
    }
    else
    {
      if (cburl.indexOf("?") == -1) {
        cburl = cburl + "?pt=5045";
      } else {
        cburl = cburl + "&pt=5045";
      }
      cburl = cburl + "&currency=" + Constants.CURRENCY_RMB;
      cburl = cburl + "&unit=" + Constants.CURRENCY_UNIT_FEN;
      
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
    for (int i = 0; i < orders.size(); i++)
    {
      Orders order = (Orders)orders.get(i);
      int status = order.getKStatus().intValue();
      OrderStatusVO st = new OrderStatusVO();
      st.setPayId(order.getOrderId());
      if ((status == 0) || (status == -3)) {
        st.setStatus(3);
      } else if (status == 1) {
        st.setStatus(1);
      } else {
        st.setStatus(2);
      }
      statusret.add(st);
    }
    JSONArray arr = JSONArray.fromObject(statusret);
    
    return arr.toString();
  }
  
  public static String getCallbackFromWandoujia(String transdata, String sign)
  {
    String ret = "SUCCESS";
    try
    {
      if ((transdata == null) || ("".equalsIgnoreCase(transdata))) {
        return "FAILURE";
      }
      if ((sign == null) || ("".equalsIgnoreCase(sign))) {
        return "FAILURE";
      }
      OrdersBean bean = new OrdersBean();
      JSONObject json = JSONObject.fromObject(transdata);
      WandoujiaOrderVO ordervo = (WandoujiaOrderVO)JSONObject.toBean(json, WandoujiaOrderVO.class);
      Orders order = bean.qureyOrder(ordervo.getOut_trade_no());
      if (order != null)
      {
        boolean isvlid = validMessage(transdata, sign, ordervo.getAppKeyId());
        if (!isvlid) {
          return "FAILURE";
        }
        if (order.getKStatus().intValue() != 1)
        {
          bean.updateOrderAmountPayId(ordervo.getOut_trade_no(), ordervo.getOrderId(), ordervo.getMoney());
          

          bean.updateOrderKStatus(ordervo.getOut_trade_no(), 1);
        }
        else
        {
          System.out.println("wandoujia order (" + order.getOrderId() + ")(" + order.getPayId() + ") had been succeed");
        }
        String path = OSUtil.getRootPath() + "../../logs/wandoujiacb/" + DateUtil.getCurTimeStr().substring(0, 8);
        OSUtil.makeDirs(path);
        String filename = path + "/" + ordervo.getOut_trade_no() + "_" + ordervo.getOrderId();
        
        String res = "";
        res = res + "transdata=" + transdata;
        res = res + "&sign=" + sign;
        OSUtil.saveFile(filename, res);
      }
      else
      {
        ret = "ILLEGAL ORDER";
      }
    }
    catch (Exception e)
    {
      e.printStackTrace();
      ret = "FAILURE";
    }
    return ret;
  }
  

	/***
	 * 生成 加密串
	 * 
	 * @param map
	 * @return
	 * @throws Exception
	 */
	public static String getSign(Map<String, String> map) throws Exception {
		//Map<String, Object> resp = new HashMap<String, Object>();
		map.remove("model");
		map.remove("signType");
		String appid = map.remove("appid");
		JSONObject jsonObject = JSONObject.fromObject(map);
		OrderStatusVO orderIdVO = new OrderStatusVO();
		System.out.println("wandoujia getkey================ jsonObject"
				+ jsonObject.toString());
		Map<String, Object> data = jsonToObject(jsonObject);
		System.out.println("wandoujia getkey================ preSign"
				+ data.toString());

		String sign = createMD5Sign(data, "",params.getParams(appid)
				.getAppkey());
		orderIdVO.setTid(sign);
		orderIdVO.setStatus(1);
		orderIdVO.setPayId(map.get("cpOrderId"));
		System.out.println("uc getkey================ sign" + sign);
		return JSONObject.fromObject(orderIdVO).toString();
	}
	
	
	
	@SuppressWarnings("unchecked")
	public static Map<String ,Object> jsonToObject(JSONObject jsonObject ) throws Exception {
		Iterator<String> nameItr = jsonObject.keys();
		String name;
		Map<String, Object> outMap = new HashMap<String, Object>();
		while (nameItr.hasNext()) {
			name = nameItr.next();
			outMap.put(name, jsonObject.get(name));
		}
		return outMap;
	}
	
	
	/***
	 * 验证订单
	 * 
	 * @param map
	 * @throws Exception
	 */
	public static String updateOrderStatus(String res) throws Exception {
		JSONObject json = JSONObject.fromObject(res);
		WanDoujiaNewOrderVo ordervo = (WanDoujiaNewOrderVo) JSONObject
				.toBean(json, WanDoujiaNewOrderVo.class);
		String exinfo = ordervo.getData().getPayWay() + "#"
				+ ordervo.getData().getAccountId()
				+ ordervo.getData().getFailedDesc();
		String respSign = (String) json.getString("sign");
		JSONObject jsondataJsonObject=JSONObject.fromObject(json.getString("data"));
		Map<String, Object> data = jsonToObject(jsondataJsonObject);
		OrdersBean bean = new OrdersBean();
		Orders orders = bean.qureyOrder(ordervo.getData().getCpOrderId());
		if (orders == null) {
			System.out.println(ordervo.getData().getCpOrderId()
					+ "updateOrderStatus=================>order is not exist");
			return "FAILURE";
		}
		System.out.println(ordervo.getData().getCpOrderId()
				+ "updateOrderStatus=================>" + data.toString());
		System.out.println(ordervo.getData().getCpOrderId()
				+ "updateOrderStatus=================>" + orders.toString()
				+ " " + orders.getSign());
		String  key=params.getParams(ordervo.getData().getGameId()).getAppkey();
		System.out.println(ordervo.getData().getCpOrderId()+"key"+key);
		String sign = createMD5Sign(data, "", key);
		System.out.println(ordervo.getData().getCpOrderId()
				+ "updateOrderStatus sign =================>" + sign + " "
				+ respSign);
		if (!respSign.equals(sign)) {
			System.out.println(ordervo.getData().getCpOrderId()
					+ "updateOrderStatus=================>sign is serror"
					+ sign);
			return "FAILURE";
		}

		Float amountYuanFloat=orders.getAmount()/100f;
		if (!(amountYuanFloat).equals(
				Float.valueOf(ordervo.getData().getAmount()))) {
			System.out.println(ordervo.getData().getCpOrderId()
					+ "updateOrderStatus=================>amount is different"
					+ orders.getAmount() + " "
					+ Float.valueOf(ordervo.getData().getAmount()));
			return "FAILURE";

		}
		System.out.println(ordervo.getData().getCpOrderId()
				+ "updateOrderStatus=================>order"
				+ orders.toString());
		if (!ordervo.getData().getOrderStatus().toLowerCase().equals("s")) {
			System.out.println(ordervo.getData().getCpOrderId()
					+ "updateOrderStatus=================>order is fail"
					+ ordervo.getData().getFailedDesc());
			return "FAILURE";
		}
		if(orders.getKStatus().equals(Constants.K_STSTUS_SUCCESS)){
			System.out.println(ordervo.getData().getCpOrderId()+"updateOrderStatus=================>order is already succeed");
			return "SUCCESS";
		}
		if(!orders.getKStatus().equals(Constants.K_STSTUS_DEFAULT)){
			System.out.println(ordervo.getData().getCpOrderId()+"updateOrderStatus=================>order is used");
			return "FAILURE";
		}
		bean.updateOrderAmountPayIdExinfo(ordervo.getData().getCpOrderId(),
				ordervo.getData().getOrderId(), (Float.parseFloat(ordervo.getData().getAmount())*100f)+"",
				exinfo);
		bean.updateOrderKStatus(ordervo.getData().getCpOrderId(),
				Constants.K_STSTUS_SUCCESS);
		String path = OSUtil.getRootPath() + "../../logs/uccb/"
				+ DateUtil.getCurTimeStr().substring(0, 8);
		OSUtil.makeDirs(path);
		String filename = path + "/" + ordervo.getData().getCpOrderId();
		OSUtil.saveFile(filename, json.toString());
		System.out.println(ordervo.getData().getCpOrderId()
				+ "updateOrderStatus=================>order is success");
		return "SUCCESS";
	}
	
	/****
	 * 获取accountId
	 * 
	 * @return
	 */
	public static String getAccountId(String token, String appid) {
		OrderStatusVO orderIdVO = new OrderStatusVO();
		String uid = "";

		// true - 新游戏调新接口， false - 旧游戏调旧接口
		try {
			String urlstr = "";
			String id = System.currentTimeMillis() + "";
			String data = "{\"sid\":\"" + token + "\"}";
			String game = "";
			String minwen = "";
			urlstr = "http://sdk.9game.cn/cp/account.verifySession";
			game = "{\"gameId\":\"" + appid + "\"}";
			minwen = "sid=" + token + params.getParams(appid).getAppkey();
			System.out.println("getAccountId" + minwen);
			String miwen = StringEncrypt.Encrypt(minwen);
			String body = "{";
			body += "\"id\":" + id + ",";
			body += "\"data\":" + data + ",";
			body += "\"game\":" + game + ",";
			body += "\"sign\":\"" + miwen + "\"";
			body += "}";

			ChannelVerify.GenerateLog("wandoujia get user info urlstr ->" + urlstr);
			ChannelVerify.GenerateLog("wandoujia get user info body ->" + body);
			URL url = new URL(urlstr);
			HttpURLConnection connection = (HttpURLConnection) url
					.openConnection();
			connection.setDoOutput(true);
			OutputStreamWriter outs = new OutputStreamWriter(
					connection.getOutputStream());

			outs.flush();
			outs.write(body);
			outs.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(
					connection.getInputStream()));
			String res = "", line = null;
			while ((line = in.readLine()) != null) {
				res += line;
			}

			connection.disconnect();

			ChannelVerify.GenerateLog("wandoujia get user info ret ->" + res);

			JSONObject json = JSONObject.fromObject(res);
			String retstate = json.getString("state");
			String retdata = json.getString("data");
			json = JSONObject.fromObject(retstate);
			String retcode = json.getString("code");
			if (retcode.equals("1")) {
				json = JSONObject.fromObject(retdata);

				uid = json.getString("accountId");
				orderIdVO.setStatus(1);
				orderIdVO.setTid(uid);
			}
		} catch (Exception e) {
			e.printStackTrace();
			orderIdVO.setStatus(2);
		}

		return JSONObject.fromObject(orderIdVO).toString();
	}

	/**
	 * 按照接口规范生成请求数据的MD5签名
	 * 
	 * @param params
	 *            业务数据
	 * @param caller
	 *            客户端平台
	 * @param secKey
	 *            MD5签名用的密钥
	 * @return MD5签名生成的字符串。如果传入的参数有一个为null，将返回null
	 * @throws Exception 
	 */
	public static String createMD5Sign(Map<String, Object> params,
			String caller, String secKey) throws Exception {
		if (null == params || null == caller || null == secKey) {
			return null;
		}

		String temp = caller + createSignData(params, null) + secKey;
		temp=temp.replace("&", "");
		System.out.println("uc persign==" + temp);

		return MD5.md5(temp,"UTF-8");
	}
	
	/**
	 * 将Map数据组装成待签名字符串
	 * 
	 * @param params
	 *            待签名的参数列表
	 * @param notIn
	 *            不参与签名的参数名列表
	 * @return 待签名字符串。如果参数params为null，将返回null
	 */
	public static String createSignData(Map<String, Object> params,
			String[] notIn) {
		if (null == params) {
			return null;
		}

		StringBuilder content = new StringBuilder(200);

		// 按照key排序
		List<String> notInList = null;
		if (null != notIn) {
			notInList = Arrays.asList(notIn);
		}
		List<String> keys = new ArrayList<String>(params.keySet());
		Collections.sort(keys);
		for (int i = 0; i < keys.size(); i++) {
			String key = (String) keys.get(i);

			if (notIn != null && notInList.contains(key))
				continue;

			String value = params.get(key) == null ? "" : params.get(key)
					.toString();
			content.append(key).append("=").append(value);
		}

		String result = content.toString();
		System.out.println("uc getkey  persignResult result" + result);

		return result;
	}
	
  
  private static boolean validMessage(String transdata, String sign, String appid)
    throws Exception
  {
    return WandouRsa.doCheck(transdata, sign);
  }
  
  public static void init()
  {
    params.initParams(WandoujiaParams.CHANNEL_ID, new WandoujiaParamsVO());
  }
  
  
}

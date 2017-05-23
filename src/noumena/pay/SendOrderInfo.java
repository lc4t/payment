package noumena.pay;

import java.text.SimpleDateFormat;

import net.sf.json.JSONObject;
import noumena.payment.bean.SendGame;
import noumena.payment.dao.SendGameDao;
import noumena.payment.getorders.OrderUtil;
import noumena.payment.model.Orders;
import noumena.payment.model.SendGameBean;
import noumena.payment.util.SpringContextUtil;


/***
 * 推送订单信息
 * @author kz
 *
 */
public class SendOrderInfo {
	
	private static String urlString="http://logs.ko.cn/KZService/pay.do";
	
	
	public static void sendOrderInfo(Orders ordersBean){
		SendGame sendGame=new SendGame();
		try {
			String gameId=ordersBean.getChannel();
			SendGameBean sendGameBean=sendGame.getChannel(gameId);
			if(sendGameBean!=null){
				JSONObject jsonObject=new JSONObject();
				jsonObject.put("gameid", gameId.substring(0,3));
				jsonObject.put("userid", ordersBean.getUId());
				jsonObject.put("channel", ordersBean.getChannel());
				jsonObject.put("language", ordersBean.getChannel().substring(4, 5));
				jsonObject.put("os", ordersBean.getDeviceType());
				jsonObject.put("osversion", ordersBean.getOsversion());
				jsonObject.put("androidid", "");
				jsonObject.put("imei", ordersBean.getImei());
				jsonObject.put("idfa", "");
				jsonObject.put("appudid", "");
				jsonObject.put("device", ordersBean.getDeviceId());
				jsonObject.put("mac", "");
				jsonObject.put("currency", ordersBean.getCurrency());
				jsonObject.put("payrealprice", Double.parseDouble(ordersBean.getMoney())*Double.parseDouble(ordersBean.getUnit())+"");
				jsonObject.put("pt", ordersBean.getPayType());
				jsonObject.put("unit", "1");
				jsonObject.put("payid", ordersBean.getOrderId());
				jsonObject.put("flowid", ordersBean.getPayId());
				jsonObject.put("status", ordersBean.getKStatus()+"");
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd"); 
				SimpleDateFormat myFmt1=new SimpleDateFormat("yy/MM/dd"); 
				String date1=myFmt1.format(sdf1.parse(ordersBean.getCreateTime()));
				jsonObject.put("paydate",date1);
				
				
				
				
			
				
			
				String result=OrderUtil.getHttpInterenceWink(urlString, "get", jsonObject);
				System.out.println(ordersBean.getOrderId()+"send info result "+result);	
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(ordersBean.getOrderId()+"send info result errors ");
		}
		
	}
	
	

}

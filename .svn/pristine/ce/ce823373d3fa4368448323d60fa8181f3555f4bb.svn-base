package noumena.payment.bean;

import noumena.mgsplus.logs.bean.GameLogsBean;
import noumena.mgsplus.logs.model.GameLogs;
import noumena.payment.model.Orders;

public class SaveFileBean {
	public static void saveLog(Orders nowvo){
		try{
			if (nowvo.getKStatus() != noumena.payment.util.Constants.K_STSTUS_SUCCESS) {
				System.out.println(nowvo.getKStatus());
				return;
			}
			GameLogs log = new GameLogs();
			log.setImei(nowvo.getImei());
			log.setuId(nowvo.getUId());
			log.setAppId(nowvo.getAppId());
			if (nowvo.getAmount() == null)
			{
				log.setCharge(0);
			}
			else
			{
				log.setCharge(nowvo.getAmount().doubleValue());
			}
			log.setScreen(nowvo.getScreenSize());
			log.setModel(nowvo.getDeviceId());
			if (nowvo.getExInfo() != null)
			{
				String[] tmp = nowvo.getExInfo().split("#");
				if (tmp.length > 1) {
					log.setIp(tmp[1]);
				}
			}
			log.setPlatform(nowvo.getDeviceType());
			log.setOsversion(nowvo.getOsversion());
			log.setGversion(nowvo.getGversion());
			log.setChannel(nowvo.getChannel());
			log.setItemId(nowvo.getItemId());
			log.setItemInfo(nowvo.getItemPrice());
			if (nowvo.getItemNum() == null){
				log.setItemNum(0);
			} else {
				log.setItemNum(nowvo.getItemNum());
			}
			String payType;
			if (nowvo.getPayType() ==null) {
				payType = noumena.mgsplus.logs.util.Constants.SERVER_DC;
			} else if (nowvo.getPayType().equals(noumena.payment.util.Constants.PAY_TYPE_ZFB)) {
				payType = noumena.mgsplus.logs.util.Constants.SERVER_DC;
			} else if (nowvo.getPayType().equals(noumena.payment.util.Constants.PAY_TYPE_DACHENG_WEB)) {
				payType = noumena.mgsplus.logs.util.Constants.SERVER_WEB;
			} else if (nowvo.getPayType().equals(noumena.payment.util.Constants.PAY_TYPE_TSTORE)) {
				payType = "T";
			} else {
				payType = noumena.mgsplus.logs.util.Constants.SERVER_SZX;
			}
			GameLogsBean.purchase(log, noumena.mgsplus.logs.util.Constants.CN, payType);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

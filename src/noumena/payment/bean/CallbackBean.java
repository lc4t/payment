package noumena.payment.bean;

import java.util.List;

import noumena.payment.util.DateUtil;
import noumena.payment.dao.CallbackDAO;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.util.Constants;
import noumena.payment.util.SpringContextUtil;

public class CallbackBean {
	public void createCallback(Callback vo) {
		CallbackDAO dao = (CallbackDAO) SpringContextUtil
				.getBean("CallbackDAO");
		dao.Create(vo);
	}

	public Callback qureyCallback(String orderid) {
		CallbackDAO dao = (CallbackDAO) SpringContextUtil
				.getBean("CallbackDAO");
		return dao.qureyCallback(orderid);
	}

	public List qureyCallbacks(int callbackStatus) {
		CallbackDAO dao = (CallbackDAO) SpringContextUtil
				.getBean("CallbackDAO");
		return dao.qureyCallbacks(callbackStatus);
	}

	public void updateCallback(Callback vo) {
		CallbackDAO dao = (CallbackDAO) SpringContextUtil
				.getBean("CallbackDAO");
		dao.update(vo);
	}

	public void toCallback(Orders vo) {
		Callback callbackvo = qureyCallback(vo.getOrderId());
		if (callbackvo.getCallbackStatus() == Constants.CALLBACK_STSTUS_COMPLETE) {
			return;
		}
		CallBackGameServBean callBackGameServBean = new CallBackGameServBean();
		String url = callbackvo.getCallbackUrl();
		if (url.indexOf("?") == -1) {
			url += "?status=" + vo.getKStatus();
		} else {
			url += "&status=" + vo.getKStatus();
		}
		String serverMessage = callBackGameServBean.doGet(url);
		if (serverMessage == null) {
			callbackvo.setCallbackStatus(Constants.CALLBACK_STSTUS_ERROR);
		} else {
			callbackvo.setCallbackStatus(Constants.CALLBACK_STSTUS_COMPLETE);
			if (serverMessage == "ok") {
				callbackvo
						.setServerStatus(Constants.CALLBACK_SERVER_STSTUS_DEFAULT);
			} else {
				callbackvo.setServerStatus(Integer.parseInt(serverMessage));
			}
		}
		callbackvo.setCallbackTime(DateUtil.getCurrentTime());
		CallbackDAO dao = (CallbackDAO) SpringContextUtil
				.getBean("CallbackDAO");
		dao.update(callbackvo);
	}
}

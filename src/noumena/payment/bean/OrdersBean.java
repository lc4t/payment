package noumena.payment.bean;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import noumena.pay.SendOrderInfo;
import noumena.payment.dao.OrdersDAO;
import noumena.payment.dao.ShieldingDao;
import noumena.payment.dao.impl.OrdersDAOImpl;
import noumena.payment.gash.GASHCharge;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.model.Shielding;
import noumena.payment.util.Constants;
import noumena.payment.util.DateUtil;
import noumena.payment.util.LogVO;
import noumena.payment.util.OSUtil;
import noumena.payment.util.SpringContextUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.OrderStatusVO;

public class OrdersBean {

	public String CreateOrder(Orders vo) {
		// 加入屏蔽
		ShieldingDao shieldingDao = (ShieldingDao) SpringContextUtil
				.getBean("ShieldingDao");
		Shielding shielding = shieldingDao.getChannel(vo.getChannel());
		if (shielding != null) {
			System.out.println("================>channel=====>"
					+ vo.getChannel() + "===> is shielding");
			return "\"}".replaceAll("\\", "");
		}
		if (null != vo.getAppId() && !"".equals(vo.getAppId())
				&& vo.getAppId().contains("d22")) {
			CreateOrderIdBean bean = new CreateOrderIdBean();
			int orderId = bean.createOrderId(vo.getCreateTime());
			// String strOrderid = Constants.ORDERID_PRE + orderId;
			vo.setOrderId(orderId + "");
		} else {
			String orderId = this.getOrderID();
			vo.setOrderId(orderId);
		}

		/*
		 * vo.setKStatus(Constants.K_STSTUS_DEFAULT);
		 * vo.setCStatus(Constants.C_STSTUS_DEFAULT);
		 * vo.setEStatus(Constants.E_STSTUS_DEFAULT);
		 * vo.setIscallback(Constants.CALLBACK_OFF);
		 */
		String tmp = "";
		tmp += vo.getOrderId();
		tmp += vo.getAppId();
		tmp += vo.getImei();
		tmp += vo.getAmount();
		tmp += vo.getCreateTime();
		tmp += vo.getExInfo();
		if (vo.getSign() == null || vo.getSign().equals("")) {
			String sign = StringEncrypt.Encrypt(tmp + Constants.C_KEY);
			vo.setSign(sign);
		}
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		dao.CreateOrder(vo);

		try {
			LogVO logvo = new LogVO();
			logvo.setItem1(DateUtil.date2Str(System.currentTimeMillis(), 4));
			logvo.setItem2("");
			logvo.setItem3(Constants.ORDERID_PRE + vo.getOrderId());
			logvo.setItem4(vo.getDeviceId());
			logvo.setItem5(vo.getImei());
			logvo.setItem6("");
			logvo.setItem7("");
			logvo.setItem8(vo.getDeviceType());
			logvo.setItem9(vo.getOsversion());
			logvo.setItem10((vo.getChannel() != null && vo.getChannel()
					.length() >= 3) ? vo.getChannel().substring(0, 3) : "");
			logvo.setItem11(vo.getGversion());
			logvo.setItem12(vo.getChannel());
			logvo.setItem13(vo.getUId());
			logvo.setItem14(vo.getPayType());
			// ID#订单项单价#订单项数量@ID#订单项单价#订单项数量
			logvo.setItem15(vo.getItemId() + "#" + vo.getAmount() + "#" + "1");
			OSUtil.genLog(logvo);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return vo.getOrderId();
	}

	public String CreateOrder(Orders vo, String url) {
		// 创建Order
		// CreateOrderIdBean bean = new CreateOrderIdBean();
		// int orderId = bean.createOrderId(vo.getCreateTime());
		// vo.setOrderId(String.valueOf(orderId));
		// vo.setKStatus(Constants.K_STSTUS_DEFAULT);
		// vo.setCStatus(Constants.C_STSTUS_DEFAULT);
		// vo.setEStatus(Constants.E_STSTUS_DEFAULT);
		// vo.setIscallback(Constants.CALLBACK_ON);
		// String tmp = "";
		// tmp += vo.getOrderId();
		// tmp += vo.getAppId();
		// tmp += vo.getImei();
		// tmp += vo.getAmount();
		// tmp += vo.getCreateTime();
		// tmp += vo.getExInfo();
		// String sign = StringEncrypt.Encrypt(tmp + Constants.C_KEY);
		// vo.setSign(sign);
		// OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		// dao.CreateOrder(vo);

		vo.setIscallback(Constants.CALLBACK_ON);
		String orderId = this.CreateOrder(vo);
		if (orderId == null || "\"}".equals(orderId)) {
			return orderId;
		}

		// 创建Callback
		CallbackBean callbackBean = new CallbackBean();
		Callback callbackvo = new Callback();
		callbackvo.setOrderId(String.valueOf(orderId));

		// if (url.indexOf("?") < 0)
		// {
		// url += "?payId=" + orderId;
		// }
		// else
		// {
		// url += "&payId=" + orderId;
		// }
		callbackvo.setCallbackUrl(url);
		callbackvo.setKStatus(vo.getKStatus());
		callbackvo.setCreateTime(DateUtil.getCurrentTime());
		callbackBean.createCallback(callbackvo);

		return orderId;
	}

	public List<Orders> qureyOrdersAmazon(String orderid, String amazonid,
			String receipt) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrdersAmazon(orderid, amazonid, receipt);
	}

	public Orders qureyOrder(String orderId) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrder(orderId);
	}

	public int qureyOrderStatus(String orderId) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders vo = dao.qureyOrder(orderId);
		if (vo == null) {
			return Constants.ORDER_ERROR;
		}
		int status = vo.getKStatus();
		if (status == Constants.K_STSTUS_DEFAULT) {
			Calendar cal1 = DateUtil.getCalendar(vo.getCreateTime());
			Calendar cal2 = Calendar.getInstance();
			if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT) {
				return Constants.K_STSTUS_TIMEOUT;
			} else {
				Post post = new Post();
				status = post.gatOrderKStatus(orderId, vo.getSign(),
						Constants.URL, vo.getPayType());
				if (status != Constants.K_STSTUS_DEFAULT) {
					updateOrderKStatus(orderId, status);
				}
			}
		}
		return status;
	}

	public List<Orders> qureyOrders2(String[] orderIds) throws Exception {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrders2(orderIds);
	}

	public List<Orders> qureyOrders(String[] orderIds) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrders(orderIds);
	}

	public List<Orders> qureyOrdersByTypeEx(String paytype, String exinfo) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrdersByTypeEx(paytype, exinfo);
	}

	public List<Orders> qureyOrdersByTypeDate(String paytype, String startdate,
			String enddate) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrdersByTypeDate(paytype, startdate, enddate);
	}

	public List<Orders> qureyOrdersByTypeDatenew(String paytype,
			String startdate, String enddate) {
		OrdersDAO dao = new OrdersDAOImpl();
		return dao.qureyOrdersByTypeDate(paytype, startdate, enddate);
	}

	public void qureyOrdersByGASHFailed() {
		List<Orders> orders = qureyOrdersByPayType(
				Constants.PAY_TYPE_GASH_POINT, Constants.K_STSTUS_TIMEOUT);
		for (int i = 0; i < orders.size(); i++) {
			Orders order = orders.get(i);
			GASHCharge.toCheckOrder(order);
		}
		orders = qureyOrdersByPayType(Constants.PAY_TYPE_GASH_TEL,
				Constants.K_STSTUS_TIMEOUT);
		for (int i = 0; i < orders.size(); i++) {
			Orders order = orders.get(i);
			GASHCharge.toCheckOrder(order);
		}
		orders = qureyOrdersByPayType(Constants.PAY_TYPE_GASH_BANK,
				Constants.K_STSTUS_TIMEOUT);
		for (int i = 0; i < orders.size(); i++) {
			Orders order = orders.get(i);
			GASHCharge.toCheckOrder(order);
		}
	}

	public List<Orders> qureyOrdersByPayId(String taobaoid) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrdersByPayId(taobaoid);
	}

	public List<Orders> qureyOrdersByPayType(String paytype, int status) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		return dao.qureyOrdersByPayType(paytype, status);
	}

	public List<OrderStatusVO> qureyOrdersStatus(String[] orderIds) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		List<Orders> ordersList = dao.qureyOrders(orderIds);
		List<OrderStatusVO> orderStatusVOList = new ArrayList<OrderStatusVO>();

		for (int i = 0; i < orderIds.length; i++) {
			OrderStatusVO orderStatusVO = new OrderStatusVO();
			orderStatusVO.setPayId(orderIds[i]);
			boolean flag = true;

			for (Orders vo : ordersList) {
				if (vo.getPayType() == null || "".equals(vo.getPayType())) {
					vo.setPayType(Constants.PAY_TYPE_ZFB);
				}
				String sign = StringEncrypt.Encrypt(orderIds[i]
						+ vo.getPayType() + Constants.S_KEY);
				if (orderIds[i].equals(vo.getOrderId())) {
					int status = vo.getKStatus();
					if (status == Constants.K_STSTUS_DEFAULT
							|| status == Constants.K_CON_ERROR) {
						Calendar cal1 = DateUtil
								.getCalendar(vo.getCreateTime());
						Calendar cal2 = Calendar.getInstance();
						if ((cal2.getTimeInMillis() - cal1.getTimeInMillis()) >= Constants.ORDER_TIMEOUT) {
							status = Constants.K_STSTUS_TIMEOUT;
						} else {
							status = Constants.K_STSTUS_DEFAULT;
						}
						// ====================暂时不去主动询问订单状态，减少大承压力
						// else {
						// Calendar cal3 =
						// DateUtil.getCalendar(vo.getUpdateTime());
						// if (cal2.getTimeInMillis() - cal3.getTimeInMillis() <
						// Constants.ORDER_SEPERATE)
						// {
						// //如果距离上次处理时间间隔太短就不去主动验证订单
						// }
						// else
						// {
						// Post post = new Post();
						// status = post.gatOrderKStatus(orderIds[i], sign,
						// Constants.URL, vo.getPayType());
						// if (status != Constants.K_CON_ERROR && status !=
						// Constants.K_STSTUS_DEFAULT) {
						// updateOrderKStatus(orderIds[i], status);
						// }
						// }
						// }
					} else if (vo.getKStatus() == Constants.K_STSTUS_SUCCESS) {
						status = Constants.K_STSTUS_SUCCESS;
					} else {
						status = Constants.K_STSTUS_ERROR;
					}
					orderStatusVO.setStatus(status);
					flag = false;
				}
			}
			if (flag) {
				orderStatusVO.setStatus(Constants.ORDER_ERROR);
			}
			orderStatusVOList.add(orderStatusVO);
		}
		return orderStatusVOList;
	}

	public int updateOrderKStatus(String orderid, int kStatus) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo == null) {
			return Constants.BACK_K_ERROR;
		}
		if (kStatus == nowvo.getKStatus()) {
			return Constants.BACK_K_DEFAULT;
		} else {
			int eStatus = getEStatusUpdateKStatus(kStatus, nowvo.getCStatus(),
					nowvo.getKStatus());
			nowvo.setKStatus(kStatus);
			nowvo.setEStatus(eStatus);
			nowvo.setUpdateTime(DateUtil.getCurrentTime());
			dao.update(nowvo);
			// 不是网络异常的情况 callback
			if (kStatus != Constants.K_CON_ERROR
					&& kStatus != Constants.K_STSTUS_DEFAULT) {
				try {
					LogVO logvo = new LogVO();
					logvo.setItem1(DateUtil.date2Str(
							System.currentTimeMillis(), 4));
					logvo.setItem2(Constants.ORDERID_PRE + nowvo.getOrderId());
					if (nowvo.getMoney() != null
							&& !nowvo.getMoney().equals("")
							&& nowvo.getMoney().matches("^(-?\\d+)(\\.\\d+)?$") == true) {
						logvo.setItem3(nowvo.getMoney());
					} else {
						logvo.setItem3(nowvo.getAmount() + "");
					}
					logvo.setItem4(kStatus + "");
					logvo.setItem5("");
					logvo.setItem6(nowvo.getPayId());
					logvo.setItem7(nowvo.getCurrency());
					if (nowvo.getUnit() != null && !nowvo.getUnit().equals("")) {
						if (nowvo.getUnit().equals("100")) {
							logvo.setItem8("元");
						} else if (nowvo.getUnit().equals("1")) {
							logvo.setItem8("分");
						} else {
							logvo.setItem8("角");
						}
					}
					OSUtil.genLog(logvo, 1);
				} catch (Exception e) {
					e.printStackTrace();
				}

				// 进行callback
				if (nowvo.getIscallback() == Constants.CALLBACK_ON) {
					CallbackBean callbackBean = new CallbackBean();
					Callback callbackvo = callbackBean.qureyCallback(nowvo
							.getOrderId());
					callbackvo.setKStatus(kStatus);
					callbackvo.setPayRealPrice(nowvo.getMoney());
					CallBackGameServBean callBackGameServBean = new CallBackGameServBean();
					callBackGameServBean.toCallback(callbackvo,
							Constants.CALLBACK_STSTUS_DEFAULT);
					SendOrderInfo.sendOrderInfo(nowvo);
					// System.out.println("callback");
					// callbackBean.toCallback(nowvo);
				}
			}
			// SaveFileBean.saveLog(nowvo);
		}

		return Constants.BACK_K_DEFAULT;
	}

	public int updateOrderKStatusNoCB(String orderid, int kStatus) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo == null) {
			return Constants.BACK_K_ERROR;
		}
		if (kStatus == nowvo.getKStatus()) {
			return Constants.BACK_K_DEFAULT;
		} else {
			int eStatus = getEStatusUpdateKStatus(kStatus, nowvo.getCStatus(),
					nowvo.getKStatus());
			nowvo.setKStatus(kStatus);
			nowvo.setEStatus(eStatus);
			nowvo.setUpdateTime(DateUtil.getCurrentTime());
			dao.update(nowvo);
		}
		return Constants.BACK_K_DEFAULT;
	}

	public void updateOrderAmountExinfo(String orderid, String exinfo,
			String amount) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo != null) {
			nowvo.setItemPrice("" + nowvo.getAmount());
			nowvo.setExInfo(exinfo);
			nowvo.setMoney(amount);
			dao.update(nowvo);
		}
	}

	public void updateOrderAmountPayId(String orderid, String payid,
			String amount) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo != null) {
			nowvo.setPayId(payid);
			nowvo.setMoney(amount);
			dao.update(nowvo);
		}
	}

	public void updateOrderAmountPayIdExinfo(String orderid, String payid,
			String amount, String exinfo) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo != null) {
			nowvo.setPayId(payid);
			nowvo.setMoney(amount);
			nowvo.setExInfo(nowvo.getExInfo() + "#" + exinfo);
			dao.update(nowvo);
		}
	}

	public void updateOrderExInfo(String orderid, String exinfo) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo != null) {
			nowvo.setExInfo(exinfo);
			dao.update(nowvo);
		}
	}

	public void updateOrderAmount(String orderid, float amount) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo != null) {
			nowvo.setAmount(amount);
			dao.update(nowvo);
		}
	}

	public void updateOrder(String orderid, Orders order) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		if (order != null) {
			dao.update(order);
		}
	}

	public void updateOrdernew(String orderid, Orders order) {
		OrdersDAO dao = new OrdersDAOImpl();
		if (order != null) {
			dao.update(order);
		}
	}

	public int updateOrderCStatus(String orderid) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo == null) {
			return Constants.ORDER_ERROR;
		}
		int eStatus;
		if (nowvo.getCStatus() == Constants.C_STSTUS_COMPLETE) {
			eStatus = Constants.E_STSTUS_C_COMPLETE_C_COMPLETE;
		} else {
			eStatus = getEStatusUpdateCStatus(nowvo.getKStatus());
			nowvo.setCStatus(Constants.C_STSTUS_COMPLETE);
		}
		nowvo.setEStatus(eStatus);
		nowvo.setCompleteTime(DateUtil.getCurrentTime());
		dao.update(nowvo);
		return eStatus;
	}

	public int updateOrderCStatus(String orderid, String stauts) {
		OrdersDAO dao = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		Orders nowvo = dao.qureyOrder(orderid);
		if (nowvo != null) {
			nowvo.setCStatus(Integer.parseInt(stauts));
			dao.update(nowvo);
		}
		return 0;
	}

	private int getEStatusUpdateKStatus(int kStatus, int cStatus, int nowKStatus) {
		if (cStatus == Constants.C_STSTUS_DEFAULT) {
			return Constants.E_STSTUS_DEFAULT;
		} else {
			if (kStatus == Constants.K_STSTUS_SUCCESS) {
				return Constants.E_STSTUS_C_COMPLETE_K_SUCCESS;
			} else if (kStatus == Constants.K_STSTUS_ERROR) {
				return Constants.E_STSTUS_C_COMPLETE_K_ERROR;
			} else {
				if (nowKStatus == Constants.K_STSTUS_SUCCESS) {
					return Constants.E_STSTUS_C_COMPLETE_K_SUCCESS_DEFAULT;
				} else {
					return Constants.E_STSTUS_C_COMPLETE_K_ERROR_DEFAULT;
				}
			}
		}
	}

	private int getEStatusUpdateCStatus(int kStatus) {
		if (kStatus == Constants.K_STSTUS_SUCCESS) {
			return Constants.E_STSTUS_DEFAULT;
		} else if (kStatus == Constants.K_STSTUS_ERROR) {
			return Constants.E_STSTUS_K_ERROR_C_COMPLETE;
		} else {
			return Constants.E_STSTUS_K_DEFAULT_C_COMPLETE;
		}
	}

	/**
	 * 生成订单号，规则为：年月日时分秒微秒+3位随机数
	 * 
	 * @return
	 */
	public String getOrderID() {
		String val = "";
		Random random = new Random();
		for (int i = 0; i < 3; i++) {
			// 输出字母还是数字
			String charOrNum = random.nextInt(2) % 2 == 0 ? "char" : "num";
			// 字符串
			if ("char".equalsIgnoreCase(charOrNum)) {
				// 取得大写字母还是小写字母
				int choice = random.nextInt(2) % 2 == 0 ? 65 : 97;
				val += (char) (choice + random.nextInt(26));
			} else if ("num".equalsIgnoreCase(charOrNum)) { // 数字
				val += String.valueOf(random.nextInt(10));
			}
		}
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return format.format(new Date()) + val;
	}
}

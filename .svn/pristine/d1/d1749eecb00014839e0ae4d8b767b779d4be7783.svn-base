package noumena.payment.bean;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import net.sf.json.JSONArray;
import noumena.payment.dao.CallbackDAO;
import noumena.payment.dao.OrdersDAO;
import noumena.payment.dao.PayinfoDao;
import noumena.payment.model.Callback;
import noumena.payment.model.Orders;
import noumena.payment.model.Payinfo;
import noumena.payment.util.Constants;
import noumena.payment.util.SpringContextUtil;
import noumena.payment.util.StringEncrypt;
import noumena.payment.vo.PayInfoVO;

public class PayinfoBean {
	public List<PayInfoVO> getDetails(String beginTime, String endTime,
			String payTypeId) {
		URL u;
		BufferedReader in = null;
		List<PayInfoVO> vo = new ArrayList<PayInfoVO>();
		try {
			u = new URL(Constants.DETAILS_URL);
			try {
				HttpURLConnection uc = (HttpURLConnection) u.openConnection();
				uc.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(uc
						.getOutputStream());
				String sign = StringEncrypt
						.Encrypt(payTypeId + beginTime + " 00:00:00" + endTime
								+ " 23:59:59" + Constants.Q_KEY);
				sign = sign.toUpperCase();

				beginTime = beginTime + " 00:00:00";
				beginTime = URLEncoder.encode(beginTime, "utf-8");
				endTime = endTime + " 23:59:59";
				endTime = URLEncoder.encode(endTime, "utf-8");
				writer.write("payId=&starttime=" + beginTime + "&endtime="
						+ endTime + "&sign=" + sign + "&payTypeId="
						+ payTypeId);
				writer.flush();
				writer.close();
				in = new BufferedReader(new InputStreamReader(uc
						.getInputStream()));
				String tempString = null;
				String s = "";
				while ((tempString = in.readLine()) != null) {
					s += tempString;
				}
				//System.out.println(s);
				if (s.length() > 11) {
					s = s.substring(11, s.length() - 1);
					//System.out.println(s);
					JSONArray json = JSONArray.fromObject(s);
					vo = (List<PayInfoVO>) JSONArray.toCollection(json, PayInfoVO.class);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vo;
	}
	public List<PayInfoVO> getDetails(String payId, String payTypeId) {
		URL u;
		BufferedReader in = null;
		List<PayInfoVO> vo = new ArrayList<PayInfoVO>();
		try {
			u = new URL(Constants.DETAILS_URL);
			try {
				HttpURLConnection uc = (HttpURLConnection) u.openConnection();
				uc.setDoOutput(true);
				OutputStreamWriter writer = new OutputStreamWriter(uc
						.getOutputStream());
				String sign = StringEncrypt
						.Encrypt(payTypeId + payId + Constants.Q_KEY);

				writer.write("payId=" + payId + "&sign=" + sign + "&payTypeId="
						+ payTypeId);
				writer.flush();
				writer.close();
				in = new BufferedReader(new InputStreamReader(uc
						.getInputStream()));
				String tempString = null;
				String s = "";
				while ((tempString = in.readLine()) != null) {
					s += tempString;
				}
				//System.out.println(s);
				if (s.length() > 11) {
					s = s.substring(11, s.length() - 1);
					JSONArray json = JSONArray.fromObject(s);
					vo = (List<PayInfoVO>) JSONArray.toCollection(json, PayInfoVO.class);
				}

			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return vo;
	}
	//自动生成
	public void savePayInfo() {
		System.out.println("begin savePayInfo");
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(cal.getTime());
		//String date = "2012-03-01";
		PayinfoDao dao = (PayinfoDao) SpringContextUtil.getBean("PayinfoDao");
		OrdersDAO dao1 = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		CallbackDAO dao2 = (CallbackDAO) SpringContextUtil.getBean("CallbackDAO");
		//System.out.println("get list:");
		for (String payTypeId : Constants.PAY_TYPES) {
			List<PayInfoVO> list = getDetails(date, date, payTypeId);
			if (list != null) {
				for (PayInfoVO vo : list) {
					Payinfo payinfo;
					String orderid = vo.getGamePayid();
					//System.out.println("get Payinfo:" + orderid);
					payinfo = dao.get(orderid);
					if (payinfo == null) {
						//System.out.println("get Payinfo null:" + orderid);
						Orders order = dao1.qureyOrder(orderid);
						payinfo = new Payinfo();
						payinfo.setOrderId(orderid);
						payinfo.setGameId(vo.getPayGameId());
						//System.out.println("get order:" + orderid);
						if (order != null) {
							payinfo.setImei(order.getImei());
							payinfo.setUId(order.getUId());
							payinfo.setChannel(order.getChannel());
							payinfo.setKStatus(order.getKStatus());
							int iscallback = order.getIscallback();
							if (iscallback == 1) {
								Callback callback = dao2.qureyCallback(orderid);
								if (callback != null) {
									payinfo.setCallbackStatus(callback.getCallbackStatus());
									payinfo.setCallbackKStatus(callback.getKStatus());
									payinfo.setCallbackTime(callback.getCallbackTime());
								}
							}
							payinfo.setIscallback(iscallback);
						} else {
							payinfo.setImei(vo.getImei());
						}
						payinfo.setAreaId(vo.getPayAreaId());
						payinfo.setPayAccount(vo.getPayAccount());
						payinfo.setIp(vo.getPayIP());
						payinfo.setPayPrice(vo.getPayPrice());
						payinfo.setPayTypeId(vo.getPaytypeId());
						payinfo.setPayStatus(vo.getTradeStatus().equals("TRADE_FINISHED") ? 1 : 0);
						payinfo.setPayReturnTime(vo.getPayReturntime());
						payinfo.setPayWapId(vo.getPaywapId());
						payinfo.setCashierCode(vo.getCashierCode());
						payinfo.setTradeNo(vo.getTradeNo());
						payinfo.setCardSn(vo.getBuyerEmail());
						payinfo.setCreateTime(date);
						payinfo.setPayTime(vo.getPayTime());
						
						dao.save(payinfo);
					}
				}
			}
		}
		System.out.println("end savePayInfo");
	}
	//手动生成
	public void savePayInfo(String date) {
		System.out.println("begin savePayInfo");
		PayinfoDao dao = (PayinfoDao) SpringContextUtil.getBean("PayinfoDao");
		OrdersDAO dao1 = (OrdersDAO) SpringContextUtil.getBean("OrdersDAO");
		CallbackDAO dao2 = (CallbackDAO) SpringContextUtil.getBean("CallbackDAO");
		System.out.println("get list:");
		for (String payTypeId : Constants.PAY_TYPES) {
			List<PayInfoVO> list = getDetails(date, date, payTypeId);
			if (list != null) {
				for (PayInfoVO vo : list) {
					Payinfo payinfo;
					String orderid = vo.getGamePayid();
					System.out.println("get Payinfo:" + orderid);
					payinfo = dao.get(orderid);
					if (payinfo == null) {
						System.out.println("get Payinfo null:" + orderid);
						Orders order = dao1.qureyOrder(orderid);
						payinfo = new Payinfo();
						payinfo.setOrderId(orderid);
						payinfo.setGameId(vo.getPayGameId());
						System.out.println("get order:" + orderid);
						if (order != null) {
							payinfo.setImei(order.getImei());
							payinfo.setUId(order.getUId());
							payinfo.setChannel(order.getChannel());
							payinfo.setKStatus(order.getKStatus());
							int iscallback = order.getIscallback();
							if (iscallback == 1) {
								Callback callback = dao2.qureyCallback(orderid);
								if (callback != null) {
									payinfo.setCallbackStatus(callback.getCallbackStatus());
									payinfo.setCallbackKStatus(callback.getKStatus());
									payinfo.setCallbackTime(callback.getCallbackTime());
								}
							}
							payinfo.setIscallback(iscallback);
						} else {
							payinfo.setImei(vo.getImei());
						}
						payinfo.setAreaId(vo.getPayAreaId());
						payinfo.setPayAccount(vo.getPayAccount());
						payinfo.setIp(vo.getPayIP());
						payinfo.setPayPrice(vo.getPayPrice());
						System.out.println(vo.getPayPrice());
						payinfo.setPayTypeId(vo.getPaytypeId());
						payinfo.setPayStatus(vo.getTradeStatus().equals("TRADE_FINISHED") ? 1 : 0);
						payinfo.setPayReturnTime(vo.getPayReturntime());
						payinfo.setPayWapId(vo.getPaywapId());
						payinfo.setCashierCode(vo.getCashierCode());
						payinfo.setTradeNo(vo.getTradeNo());
						payinfo.setCardSn(vo.getBuyerEmail());
						payinfo.setCreateTime(date);
						payinfo.setPayTime(vo.getPayTime());
						//dao.save(payinfo);
					}
				}
			}
		}
		System.out.println("end savePayInfo");
	}
	public List<Payinfo> getPayinfoByTime(String beginTime, String endTime, String payTypeId) {
		List<Payinfo> list = new ArrayList<Payinfo>();
		PayinfoDao dao = (PayinfoDao) SpringContextUtil.getBean("PayinfoDao");
		list = dao.getPayinfoByTime(beginTime, endTime, payTypeId);
		return list;
	}
}

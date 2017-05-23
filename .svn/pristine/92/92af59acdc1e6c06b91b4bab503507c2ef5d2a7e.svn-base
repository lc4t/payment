package noumena.payment.dao.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import noumena.payment.dao.OrdersDAO;
import noumena.payment.model.Orders;
import noumena.payment.util.DBUtil;
import noumena.payment.util.NoumenaHibernateDaoSupport;
import noumena.payment.util.OSUtil;

public class OrdersDAOImpl extends NoumenaHibernateDaoSupport implements
		OrdersDAO {

	public void CreateOrder(Orders vo) {
		getHibernateTemplate().save(vo);
	}

	public Orders qureyOrder(String orderid) {
		if (orderid == null || orderid.equals(""))
		{
			return null;
		}
		Orders vo = (Orders) getHibernateTemplate().get(Orders.class, orderid);
		return vo;
	}

	public void update(Orders vo) {
		getHibernateTemplate().update(vo);
	}

	public List<Orders> qureyOrders2(String[] orderIds) throws Exception
	{
		List<Orders> list = new ArrayList<Orders>();
		Orders order = null;
		if (orderIds == null || orderIds.length <= 0)
		{
			return list;
		}
		
		String ids = "'" + orderIds[0] + "'";
		if (orderIds.length > 1)
		{
			for (int i = 1 ; i < orderIds.length ; i++)
			{
				ids += ",'" + orderIds[i] + "'";
			}
		}
		String sql = "SELECT * FROM orders WHERE order_id IN (" + ids + ")";
		
		Connection conn=null;
		PreparedStatement st=null;
		ResultSet rs=null;
		
		try
		{
			conn = DBUtil.getOrderDBConn();
			st = conn.prepareStatement(sql);
			rs = st.executeQuery();
			while (rs.next())
			{
				order = new Orders();
				
				order.setAmount(rs.getFloat("amount"));
				order.setAppId(rs.getString("app_id"));
//				order.setCallbackUrl("");
				order.setChannel(rs.getString("channel"));
				order.setCompleteTime(rs.getString("complete_time"));
				order.setCreateTime(rs.getString("create_time"));
				order.setCStatus(rs.getInt("c_status"));
				order.setDeviceId(rs.getString("device_id"));
				order.setDeviceType(rs.getString("device_type"));
				order.setEStatus(rs.getInt("e_status"));
				order.setExInfo(rs.getString("ex_info"));
				order.setGversion(rs.getString("gversion"));
				order.setImei(rs.getString("imei"));
				order.setIscallback(rs.getInt("iscallback"));
				order.setItemId(rs.getString("item_id"));
				order.setItemNum(rs.getInt("item_num"));
				order.setItemPrice(rs.getString("item_price"));
				order.setKStatus(rs.getInt("k_status"));
				order.setMoney(rs.getString("money"));
				order.setOrderId(rs.getString("order_id"));
				order.setOsversion(rs.getString("osversion"));
				order.setPayId(rs.getString("pay_id"));
				order.setPayType(rs.getString("pay_type"));
//				order.setProductId("");
				order.setScreenSize(rs.getString("screen_size"));
				order.setSign(rs.getString("sign"));
//				order.setSubId("");
				order.setUId(rs.getString("u_id"));
				order.setUpdateTime(rs.getString("complete_time"));
				
				list.add(order);
			}
		}
		finally
		{
			rs.close();
			st.close();
			conn.close();
		}
		
		return list;
	}

	public List<Orders> qureyOrders(String[] orderIds) {
//		List<Orders> list = new ArrayList<Orders>();
//		String sql = "from Orders";
//		for (int i = 0; i < orderIds.length; i++) {
//			if (i == 0) {
//				sql += " where order_id=" + orderIds[i];
//			} else {
//				sql += " or order_id=" + orderIds[i];
//			}
//		}
//		Session session = getSession();
//		Query query = session.createQuery(sql);
//		list = query.list();
//		return list;
		
		
		String sql = "from Orders";
		for (int i = 0; i < orderIds.length; i++) {
			if (i == 0) {
				sql += " where order_id='" + OSUtil.rmvZero(orderIds[i]) + "'";
			} else {
				sql += " or order_id='" + OSUtil.rmvZero(orderIds[i]) + "'";
			}
		}
		List<Orders> list = getHibernateTemplate().find(sql);
		return list;
	}

	public List<Orders> qureyOrdersAmazon(String orderid, String amazonid, String receipt)
	{
		String sql = "from Orders where ";
		sql += "payId='" + amazonid;
		sql += "' and exInfo='" + receipt;
		sql += "' and orderId!='" + orderid;
		sql += "'";
		List<Orders> list = getHibernateTemplate().find(sql);
		return list;
	}

	public List<Orders> qureyOrdersByTypeEx(String paytype, String exinfo)
	{
		String sql = "from Orders where KStatus=1 and payType=";
		sql += paytype;
		sql += " and exInfo like '%" + exinfo + "%'";
		List<Orders> list = getHibernateTemplate().find(sql);
		return list;
	}

	public List<Orders> qureyOrdersByTypeDate(String paytype, String startdate, String enddate)
	{
		String sql = "from Orders where KStatus=1 and payType=";
		sql += paytype;
		sql += " and createTime>= '" + startdate + " 00:00:00' and createTime <= '" + enddate + " 23:59:59'";
		List<Orders> list = getHibernateTemplate().find(sql);
		return list;
	}

	public List<Orders> qureyOrdersByPayType(String paytype, int status)
	{
		String sql = "from Orders where payType=";
		sql += paytype;
		sql += " and KStatus=";
		sql += status;
		List<Orders> list = getHibernateTemplate().find(sql);
		return list;
	}
	
	public List<Orders> qureyOrdersByPayId(String taobaoid)
	{
		String sql = "from Orders where payId='";
		sql += taobaoid;
		sql += "'";
		List<Orders> list = getHibernateTemplate().find(sql);
		return list;
	}
}

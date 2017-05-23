package noumena.payment.dao;

import java.util.List;

import noumena.payment.model.Orders;

public interface OrdersDAO {
	public void CreateOrder(Orders vo);
	public Orders qureyOrder(String orderid);
	public List<Orders> qureyOrders2(String[] orderIds) throws Exception;
	public List<Orders> qureyOrders(String[] orderIds);
	public List<Orders> qureyOrdersByTypeEx(String paytype, String exinfo);
	public List<Orders> qureyOrdersByTypeDate(String paytype, String startdate, String enddate);
	public List<Orders> qureyOrdersByPayType(String paytype, int status);
	public List<Orders> qureyOrdersAmazon(String orderid, String amazonid, String receipt);
	public List<Orders> qureyOrdersByPayId(String taobaoid);
	public void update(Orders vo);
}

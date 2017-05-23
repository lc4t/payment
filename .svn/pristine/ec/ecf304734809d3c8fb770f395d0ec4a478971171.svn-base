package noumena.payment.bean;

import org.springframework.dao.DataAccessException;

import noumena.payment.dao.CreateOrderIdDao;
import noumena.payment.model.CreateOrderId;
import noumena.payment.util.SpringContextUtil;

public class CreateOrderIdBean {
	public int createOrderId(String time){
		CreateOrderIdDao dao = (CreateOrderIdDao)SpringContextUtil.getBean("CreateOrderIdDao");
		CreateOrderId vo = new CreateOrderId();
		vo.setCreateTime(time);
		try {
			return dao.save(vo);
		} catch (DataAccessException e) {
			e.printStackTrace();
			return 0;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		}
	}
}

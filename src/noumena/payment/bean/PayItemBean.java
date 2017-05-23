package noumena.payment.bean;

import java.util.List;

import noumena.payment.dao.PayItemsDAO;
import noumena.payment.model.PayItems;
import noumena.payment.util.SpringContextUtil;

public class PayItemBean {
	public void save(PayItems vo) {
		PayItemsDAO dao = (PayItemsDAO) SpringContextUtil.getBean("PayItemsDao");
		dao.save(vo);
	}
	public void update(PayItems vo){
		PayItemsDAO dao = (PayItemsDAO) SpringContextUtil.getBean("PayItemsDao");
		dao.update(vo);
	}
	public void delete(PayItems vo){
		PayItemsDAO dao = (PayItemsDAO) SpringContextUtil.getBean("PayItemsDao");
		dao.delete(vo);
	}
	public List<PayItems> select(){
		PayItemsDAO dao = (PayItemsDAO) SpringContextUtil.getBean("PayItemsDao");
		return dao.select();
	}
	public List<PayItems> selectByGame (String gameid){
		PayItemsDAO dao = (PayItemsDAO) SpringContextUtil.getBean("PayItemsDao");
		return dao.selectByGame(gameid);
	}
	public List<PayItems> selectByGameItem (String gameid, String serverid, String itemid){
		PayItemsDAO dao = (PayItemsDAO) SpringContextUtil.getBean("PayItemsDao");
		return dao.selectByGameItem(gameid, serverid, itemid);
	}
	public PayItems selectById (int id){
		PayItemsDAO dao = (PayItemsDAO) SpringContextUtil.getBean("PayItemsDao");
		return dao.get(id);
	}
}

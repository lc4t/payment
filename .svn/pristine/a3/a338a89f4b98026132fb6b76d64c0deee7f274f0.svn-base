package noumena.payment.bean;

import java.util.List;

import noumena.payment.dao.GashPayServerDao;
import noumena.payment.dao.PayServerDao;
import noumena.payment.model.GashPayServer;
import noumena.payment.model.PayServer;
import noumena.payment.util.SpringContextUtil;

public class PayServerBean {
	public void savs(PayServer vo) {
		PayServerDao dao = (PayServerDao) SpringContextUtil.getBean("PayServerDao");
		dao.savs(vo);
	}
	public void update(PayServer vo){
		PayServerDao dao = (PayServerDao) SpringContextUtil.getBean("PayServerDao");
		dao.update(vo);
	}
	public void delete(PayServer vo){
		PayServerDao dao = (PayServerDao) SpringContextUtil.getBean("PayServerDao");
		dao.delete(vo);
	}
	public List<PayServer> selectByGame(String gameId){
		PayServerDao dao = (PayServerDao) SpringContextUtil.getBean("PayServerDao");
		return dao.selectByGame(gameId);
	}
	public List<GashPayServer> selectGashByGame(String gameId){
		GashPayServerDao dao = (GashPayServerDao) SpringContextUtil.getBean("GashPayServerDao");
		return dao.selectByGame(gameId);
	}
	public List<PayServer> select(){
		PayServerDao dao = (PayServerDao) SpringContextUtil.getBean("PayServerDao");
		return dao.select();
	}
	public PayServer get (String serverId){
		PayServerDao dao = (PayServerDao) SpringContextUtil.getBean("PayServerDao");
		return dao.get(serverId);
	}
	public GashPayServer getgash (String serverId){
		GashPayServerDao dao = (GashPayServerDao) SpringContextUtil.getBean("GashPayServerDao");
		return dao.get(serverId);
	}
	public List<PayServer> selectByTaobao (String taobaoid){
		PayServerDao dao = (PayServerDao) SpringContextUtil.getBean("PayServerDao");
		return dao.selectByTaobao(taobaoid);
	}
}

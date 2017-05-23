package noumena.payment.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;

import noumena.payment.dao.CallbackDAO;
import noumena.payment.model.Callback;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class CallbackDAOImpl extends NoumenaHibernateDaoSupport implements
		CallbackDAO {

	public void Create(Callback vo) {
		getHibernateTemplate().save(vo);
	}

	public Callback qureyCallback(String orderid) {
		Callback vo = (Callback)getHibernateTemplate().get(Callback.class, orderid);
		return vo;
	}

	public void update(Callback vo) {
		getHibernateTemplate().update(vo);
	}
	public List qureyCallbacks(int callbackStatus){
//		Session session = getSession();
//		
//		String sql = "from Callback where callback_status=?";
//		Query query = session.createQuery(sql);
//		query.setInteger(0, callbackStatus);
//
//		return query.list();
		
		String sql = "from Callback where callback_status=?";
		List l = getHibernateTemplate().find(sql,callbackStatus);
		return l;
		
	}
}

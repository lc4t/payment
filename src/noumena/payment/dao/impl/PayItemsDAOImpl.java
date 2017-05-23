package noumena.payment.dao.impl;

import java.util.List;

import noumena.payment.dao.PayItemsDAO;
import noumena.payment.model.PayItems;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class PayItemsDAOImpl extends NoumenaHibernateDaoSupport implements
		PayItemsDAO {

	@Override
	public void Create(PayItems vo) {
		getHibernateTemplate().save(vo);
	}

	@Override
	public void update(PayItems vo) {
		getHibernateTemplate().update(vo);
	}

	@Override
	public void save(PayItems vo) {
		getHibernateTemplate().save(vo);
	}
	
	@Override
	public void delete(PayItems vo) {
		getHibernateTemplate().delete(vo);
	}

	@Override
	public List<PayItems> select() {
		String sql = "from PayItems order by gameid";
		List<PayItems> list = getHibernateTemplate().find(sql);
		return list;
	}

	@Override
	public List<PayItems> selectByGame(String gameid) {
		String sql = "from PayItems where gameid = '" + gameid + "'";
		List<PayItems> list = getHibernateTemplate().find(sql);
		return list;
	}

	@Override
	public List<PayItems> selectByGameItem(String gameid, String serverid, String itemid)
	{
		String sql = "from PayItems where gameid='" + gameid + "'";
		sql += " and serverid='" + serverid + "'";
		sql += " and itemid='" + itemid + "'";
		List<PayItems> list = getHibernateTemplate().find(sql);
		return list;
	}

	@Override
	public PayItems get(int id)
	{
		PayItems vo = (PayItems)getHibernateTemplate().get(PayItems.class, id);
		return vo;
	}
}

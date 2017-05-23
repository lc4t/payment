package noumena.payment.dao.impl;

import java.util.List;

import noumena.payment.dao.PayGameDao;
import noumena.payment.model.PayGame;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class PayGameDaoImpl extends NoumenaHibernateDaoSupport implements PayGameDao {

	@Override
	public void delete(PayGame vo) {
		getHibernateTemplate().delete(vo);
	}

	@Override
	public PayGame get(String gameId) {
		PayGame vo = (PayGame)getHibernateTemplate().get(PayGame.class, gameId);
		return vo;
	}

	@Override
	public void savs(PayGame vo) {
		getHibernateTemplate().save(vo);
	}

	@Override
	public List<PayGame> select() {
		String sql = "from PayGame ORDER BY create_time";
		List<PayGame> list = getHibernateTemplate().find(sql);
		return list;
	}

	@Override
	public void update(PayGame vo) {
		getHibernateTemplate().update(vo);
	}

}

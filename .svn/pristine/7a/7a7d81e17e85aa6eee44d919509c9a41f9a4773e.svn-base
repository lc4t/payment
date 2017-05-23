package noumena.payment.dao.impl;

import java.util.List;

import noumena.payment.dao.GashPayGameDao;
import noumena.payment.model.GashPayGame;
import noumena.payment.model.PayGame;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class GashPayGameDaoImpl extends NoumenaHibernateDaoSupport implements GashPayGameDao {

	@Override
	public void delete(PayGame vo) {
		getHibernateTemplate().delete(vo);
	}

	@Override
	public GashPayGame get(String gameId) {
		GashPayGame vo = (GashPayGame)getHibernateTemplate().get(GashPayGame.class, gameId);
		return vo;
	}

	@Override
	public void savs(PayGame vo) {
		getHibernateTemplate().save(vo);
	}

	@Override
	public List<GashPayGame> select() {
		String sql = "from GashPayGame ORDER BY create_time";
		List<GashPayGame> list = getHibernateTemplate().find(sql);
		return list;
	}

	@Override
	public void update(PayGame vo) {
		getHibernateTemplate().update(vo);
	}

}

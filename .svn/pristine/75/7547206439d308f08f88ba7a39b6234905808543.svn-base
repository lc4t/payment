package noumena.payment.dao.impl;

import java.util.List;

import noumena.payment.dao.GashPayServerDao;
import noumena.payment.model.GashPayServer;
import noumena.payment.model.PayServer;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class GashPayServerDaoImpl extends NoumenaHibernateDaoSupport implements
		GashPayServerDao {

	@Override
	public void delete(PayServer vo) {
		getHibernateTemplate().delete(vo);

	}

	@Override
	public GashPayServer get(String serverId) {
		GashPayServer vo = (GashPayServer)getHibernateTemplate().get(GashPayServer.class, serverId);
		return vo;
	}

	@Override
	public void savs(PayServer vo) {
		getHibernateTemplate().save(vo);
	}

	@Override
	public List<PayServer> select() {
		String sql = "from PayServer ORDER BY server_id";
		List<PayServer> list = getHibernateTemplate().find(sql);
		return list;
	}

	@Override
	public List<GashPayServer> selectByGame(String gameId) {
		String sql = "from GashPayServer where game_id=? ORDER BY server_id";
		List<GashPayServer> list = getHibernateTemplate().find(sql, gameId);
		return list;
	}

	@Override
	public void update(PayServer vo) {
		getHibernateTemplate().update(vo);
	}

}

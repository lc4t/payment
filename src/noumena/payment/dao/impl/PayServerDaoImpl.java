package noumena.payment.dao.impl;

import java.util.List;

import noumena.payment.dao.PayServerDao;
import noumena.payment.model.PayServer;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class PayServerDaoImpl extends NoumenaHibernateDaoSupport implements
		PayServerDao {

	@Override
	public void delete(PayServer vo) {
		getHibernateTemplate().delete(vo);

	}

	@Override
	public PayServer get(String serverId) {
		PayServer vo = (PayServer)getHibernateTemplate().get(PayServer.class, serverId);
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
	public List<PayServer> selectByGame(String gameId) {
		String sql = "from PayServer where game_id=? ORDER BY server_id";
		List<PayServer> list = getHibernateTemplate().find(sql, gameId);
		return list;
	}

	@Override
	public List<PayServer> selectByTaobao(String taobaoid) {
		String sql = "from PayServer where taobao_id like '%" + taobaoid + "%'";
		List<PayServer> list = getHibernateTemplate().find(sql);
		return list;
	}

	@Override
	public void update(PayServer vo) {
		getHibernateTemplate().update(vo);
	}

}

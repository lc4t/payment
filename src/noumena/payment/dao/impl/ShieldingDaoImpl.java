package noumena.payment.dao.impl;

import noumena.payment.dao.ShieldingDao;
import noumena.payment.model.Shielding;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class ShieldingDaoImpl extends NoumenaHibernateDaoSupport implements ShieldingDao {

	@Override
	public Shielding getChannel(String channel) {
		// TODO Auto-generated method stub
		Shielding vo = (Shielding) getHibernateTemplate().get(Shielding.class, channel);
		return vo;
	}

}

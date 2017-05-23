package noumena.payment.dao.impl;

import noumena.payment.dao.SendGameDao;
import noumena.payment.model.SendGameBean;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class SendGameDaoImpl extends NoumenaHibernateDaoSupport implements SendGameDao {
	@Override
	public SendGameBean getInfoByGame(String gameId) {
		// TODO Auto-generated method stub
		SendGameBean vo = (SendGameBean) getHibernateTemplate().get(SendGameBean.class, gameId);
		return vo;
	}
}

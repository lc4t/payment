package noumena.payment.dao.impl;

import org.springframework.dao.DataAccessException;

import noumena.payment.dao.CreateOrderIdDao;
import noumena.payment.model.CreateOrderId;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class CreateOrderIdDaoImpl extends NoumenaHibernateDaoSupport implements CreateOrderIdDao {

	public int save(CreateOrderId vo ) throws DataAccessException{
		getHibernateTemplate().save(vo);
		return vo.getId();
	}

}

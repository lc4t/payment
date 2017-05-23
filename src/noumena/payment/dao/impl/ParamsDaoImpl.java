package noumena.payment.dao.impl;

import java.util.List;

import noumena.payment.dao.ParamsDAO;
import noumena.payment.model.Params;
import noumena.payment.util.NoumenaHibernateDaoSupport;

public class ParamsDaoImpl extends NoumenaHibernateDaoSupport implements ParamsDAO
{
	@Override
	public List<Params> getParams()
	{
		String sql = "from Params order by appid";
		List<Params> list = getHibernateTemplate().find(sql);
		return list;
	}
	
	@Override
	public Params qureyParam(String appid, String channel)
	{
		String sql = "from params where appid='" + appid + "'";
		sql += " and channel='" + channel + "'";
		List<Params> list = getHibernateTemplate().find(sql);
		if (list != null)
		{
			return list.get(0);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public String qureyChannelParam(String appid, String channel)
	{
		String sql = "from params where appid='" + appid + "'";
		sql += " and channel='" + channel + "'";
		List<Params> list = getHibernateTemplate().find(sql);
		if (list != null)
		{
			return list.get(0).getParams();
		}
		else
		{
			return null;
		}
	}

	@Override
	public void CreateParam(Params vo)
	{
		getHibernateTemplate().save(vo);
	}

	@Override
	public void update(Params vo)
	{
		getHibernateTemplate().update(vo);
	}

}

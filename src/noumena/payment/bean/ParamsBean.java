package noumena.payment.bean;

import java.util.List;

import noumena.payment.dao.ParamsDAO;
import noumena.payment.model.Params;
import noumena.payment.util.SpringContextUtil;

public class ParamsBean
{
	public void CreateParam(Params vo)
	{
		ParamsDAO dao = (ParamsDAO) SpringContextUtil.getBean("ParamsDao");
		dao.CreateParam(vo);
	}

	public List<Params> getParams()
	{
		ParamsDAO dao = (ParamsDAO) SpringContextUtil.getBean("ParamsDao");
		return dao.getParams();
	}

	public Params qureyParam(String appid, String channel)
	{
		ParamsDAO dao = (ParamsDAO) SpringContextUtil.getBean("ParamsDao");
		return dao.qureyParam(appid, channel);
	}

	public String qureyChannelParam(String appid, String channel)
	{
		ParamsDAO dao = (ParamsDAO) SpringContextUtil.getBean("ParamsDao");
		return dao.qureyChannelParam(appid, channel);
	}

	public void updateParam(Params vo)
	{
		ParamsDAO dao = (ParamsDAO) SpringContextUtil.getBean("ParamsDao");
		dao.update(vo);
	}
}

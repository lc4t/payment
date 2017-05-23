package noumena.payment.dao;

import java.util.List;

import noumena.payment.model.Params;

public interface ParamsDAO {
	public void CreateParam(Params vo);
	public List<Params> getParams();
	public Params qureyParam(String appid, String channel);
	public void update(Params vo);
	public String qureyChannelParam(String appid, String channel);
}

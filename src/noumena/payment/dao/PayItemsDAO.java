package noumena.payment.dao;

import java.util.List;

import noumena.payment.model.PayItems;

public interface PayItemsDAO {
	public void save(PayItems vo);
	public void Create(PayItems vo);
	public void update(PayItems vo);
	public void delete(PayItems vo);
	public List<PayItems> select();
	public List<PayItems> selectByGame(String gameid);
	public List<PayItems> selectByGameItem(String gameid, String serverid, String itemid);
	public PayItems get(int id);
}

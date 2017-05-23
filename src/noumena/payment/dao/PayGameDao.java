package noumena.payment.dao;

import java.util.List;

import noumena.payment.model.PayGame;


public interface PayGameDao {
	public void savs(PayGame vo);
	public void update(PayGame vo);
	public void delete(PayGame vo);
	public List<PayGame> select();
	public PayGame get (String gameId);
}

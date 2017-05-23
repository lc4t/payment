package noumena.payment.dao;

import java.util.List;

import noumena.payment.model.GashPayGame;
import noumena.payment.model.PayGame;


public interface GashPayGameDao {
	public void savs(PayGame vo);
	public void update(PayGame vo);
	public void delete(PayGame vo);
	public List<GashPayGame> select();
	public GashPayGame get (String gameId);
}

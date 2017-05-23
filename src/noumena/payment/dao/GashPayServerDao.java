package noumena.payment.dao;

import java.util.List;

import noumena.payment.model.GashPayServer;
import noumena.payment.model.PayServer;

public interface GashPayServerDao {
	public void savs(PayServer vo);
	public void update(PayServer vo);
	public void delete(PayServer vo);
	public List<GashPayServer> selectByGame(String gameId);
	public List<PayServer> select();
	public GashPayServer get (String serverId);
}

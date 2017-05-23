package noumena.payment.dao;

import java.util.List;

import noumena.payment.model.Callback;

public interface CallbackDAO {
	public void Create(Callback vo);
	public Callback qureyCallback(String orderid);
	public List qureyCallbacks(int callbackStatus);
	public void update(Callback vo);
}

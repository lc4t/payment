package noumena.payment.bean;

import noumena.payment.dao.ShieldingDao;
import noumena.payment.model.Shielding;
import noumena.payment.util.SpringContextUtil;

/***
 * 渠道屏蔽的实体
 * @author kz
 *
 */
public class ShieldingBean {

	public Shielding getChannel(String channel) {
		ShieldingDao dao = (ShieldingDao) SpringContextUtil
				.getBean("ShieldingDao");
		return dao.getChannel(channel);
	}
	

}

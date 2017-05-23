package noumena.payment.util;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONObject;
import noumena.payment.model.Params;

/**
 * @author liangjun
 * 渠道参数，每个渠道一个参数类，在初始化的时候从数据库获取
 * dbParams是从数据库原始数据克隆出来的参数对象，参数是json串
 * voParams是根据dbParams解析成对象形式的参数
 *
 */
public class ChannelParams
{
	private String channelId;
	private List<Params> dbParams = null;
	private List<ChannelParamsVO> voParams = new ArrayList<ChannelParamsVO>();
	
	public void initParams(String channelid, ChannelParamsVO vo)
	{
		channelId = channelid;
		dbParams = Constants.getChannelParams(channelid);
		
		for (int i = 0 ; i < dbParams.size() ; i++)
		{
			Params p = dbParams.get(i);
			String paramjsn = p.getParams();
			
			JSONObject json = JSONObject.fromObject(paramjsn);
			ChannelParamsVO ordervo = (ChannelParamsVO) JSONObject.toBean(json, vo.getClass());
			
			voParams.add(ordervo);
		}
	}
	
	public ChannelParamsVO getParamsVO(String appid)
	{
		//System.out.println(voParams.size());
		for (int i = 0 ; i < voParams.size() ; i++)
		{
			ChannelParamsVO p = voParams.get(i);
			//System.out.println(p.toString());
			if (p.getAppid().equals(appid))
			{
				return p;
			}
		}
		
		System.out.println("$$$$$(" + DateUtil.getCurTimeStr() + ")$$$$$channel(" + channelId + ") can not get params by appid(" + appid + ")");
		return null;
	}
}

package noumena.payment.bean;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import noumena.payment.dao.GashPayGameDao;
import noumena.payment.dao.PayGameDao;
import noumena.payment.model.GashPayGame;
import noumena.payment.model.GashPayServer;
import noumena.payment.model.PayGame;
import noumena.payment.model.PayServer;
import noumena.payment.util.SpringContextUtil;
import noumena.payment.vo.PayGameVO;
import noumena.payment.vo.PayServerVO;
import noumena.payment.vo.WebConvertVO;

public class PayGameBean {

	public void addConvert(WebConvertVO vo, String gameId) {
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		PayGame payGame = dao.get(gameId);
		String info = payGame.getInfo();

		List<WebConvertVO> list = toConvertList(info);

		list.add(vo);
		JSONArray json2 = JSONArray.fromObject(list);
		payGame.setInfo(json2.toString());
		dao.update(payGame);
	}

	public void delConvert(String codename, String gameId) {
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		PayGame payGame = dao.get(gameId);
		String info = payGame.getInfo();

		List<WebConvertVO> list = toConvertList(info);

		for (WebConvertVO convertVO : list) {
			if (convertVO.getCodeName().equals(codename)) {
				list.remove(convertVO);
				break;
			}
		}

		info = toConvertString(list);
		payGame.setInfo(info);
		dao.update(payGame);
	}

	public void update(PayGame vo) {
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		dao.update(vo);
	}

	public void save(PayGame vo) {
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		dao.savs(vo);
	}

	public void delete(PayGame vo) {
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		dao.delete(vo);
	}

	public PayGameVO getGameVO(String gameId) {
		PayGameVO payGameVO = new PayGameVO();
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		PayGame payGame = dao.get(gameId);
		payGameVO.setGameId(gameId);
		payGameVO.setGameName(payGame.getGameName());
		payGameVO.setWebsite(payGame.getWebsite());
		payGameVO.setCreateTime(payGame.getCreateTime());
		payGameVO.setConverts(toConvertList(payGame.getInfo()));
		PayServerBean payServerBean = new PayServerBean();
		List<PayServer> servers = payServerBean.selectByGame(gameId);
		List<PayServerVO> servervos = new ArrayList<PayServerVO>();
		for (PayServer ps : servers)
		{
			PayServerVO vo = new PayServerVO();
			vo.setCallbackUrl(ps.getCallbackUrl());
			vo.setCheckUrl(ps.getCheckUrl());
			vo.setCreateTime(ps.getCreateTime());
			vo.setGameId(ps.getGameId());
			vo.setPayNotify(ps.getPayNotify());
			vo.setServerId(ps.getServerId());
			vo.setServerName(ps.getServerName());
			servervos.add(vo);
		}
		payGameVO.setServers(servervos);
		return payGameVO;
	}

	public PayGameVO getGashGameVO(String gameId) {
		PayGameVO payGameVO = new PayGameVO();
		GashPayGameDao dao = (GashPayGameDao) SpringContextUtil.getBean("GashPayGameDao");
		GashPayGame payGame = dao.get(gameId);
		payGameVO.setGameId(gameId);
		payGameVO.setGameName(payGame.getGameName());
		payGameVO.setWebsite(payGame.getWebsite());
		payGameVO.setCreateTime(payGame.getCreateTime());
		payGameVO.setConverts(toConvertList(payGame.getInfo()));
		PayServerBean payServerBean = new PayServerBean();
		List<GashPayServer> servers = payServerBean.selectGashByGame(gameId);
		List<PayServerVO> servervos = new ArrayList<PayServerVO>();
		for (GashPayServer ps : servers)
		{
			PayServerVO vo = new PayServerVO();
			vo.setCallbackUrl(ps.getCallbackUrl());
			vo.setCheckUrl(ps.getCheckUrl());
			vo.setCreateTime(ps.getCreateTime());
			vo.setGameId(ps.getGameId());
			vo.setPayNotify(ps.getPayNotify());
			vo.setServerId(ps.getServerId());
			vo.setServerName(ps.getServerName());
			servervos.add(vo);
		}
		payGameVO.setServers(servervos);
		return payGameVO;
	}
	public PayGame getGame(String gameId) {
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		return dao.get(gameId);
	}

	public List<PayGameVO> getPayGameVOs() {
		List<PayGameVO> payGameVOList = new ArrayList<PayGameVO>();
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		List<PayGame> list = dao.select();
		for (PayGame vo : list) {
			PayGameVO payGameVO = getGameVO(vo.getGameId());
			payGameVOList.add(payGameVO);
		}

		return payGameVOList;
	}

	public List<PayGameVO> getGashPayGameVOs() {
		List<PayGameVO> payGameVOList = new ArrayList<PayGameVO>();
		GashPayGameDao dao = (GashPayGameDao) SpringContextUtil.getBean("GashPayGameDao");
		List<GashPayGame> list = dao.select();
		for (GashPayGame vo : list) {
			PayGameVO payGameVO = getGashGameVO(vo.getGameId());
			payGameVOList.add(payGameVO);
		}

		return payGameVOList;
	}
	
	public List<PayGame> getPayGames() {
		PayGameDao dao = (PayGameDao) SpringContextUtil.getBean("PayGameDao");
		return dao.select();
	}

	public List<WebConvertVO> toConvertList(String info) {
		JSONArray json = JSONArray.fromObject(info);
		List<WebConvertVO> list = (List<WebConvertVO>) JSONArray.toCollection(
				json, WebConvertVO.class);
		return list;
	}

	public String toConvertString(List<WebConvertVO> list) {
		JSONArray json = JSONArray.fromObject(list);

		return json.toString();
	}

}

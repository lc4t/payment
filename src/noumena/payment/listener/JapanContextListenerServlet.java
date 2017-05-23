package noumena.payment.listener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import noumena.payment.alipay.AlipayCharge;
import noumena.payment.anqu.AnquCharge;
import noumena.payment.anzhi.AnzhiCharge;
import noumena.payment.app01.App01Charge;
import noumena.payment.appota.AppOTACharge;
import noumena.payment.apus.ApusCharge;
import noumena.payment.augame.AuGameCharge;
import noumena.payment.baidu.BaiduCharge;
import noumena.payment.baiduqianbao.BaiduqianbaoCharge;
import noumena.payment.baiduyun.BaiduyunCharge;
import noumena.payment.baifubao.BaifubaoCharge;
import noumena.payment.bilibili.BilibiliCharge;
import noumena.payment.bluepay.BluepayCharge;
import noumena.payment.c07073.C07073Charge;
import noumena.payment.c37wan.C37wanCharge;
import noumena.payment.c3ggate.C3GGateCharge;
import noumena.payment.c4399.C4399Charge;
import noumena.payment.c49you.C49youCharge;
import noumena.payment.caishen.CaishenCharge;
import noumena.payment.cht.CHTCharge;
import noumena.payment.coolpad.CoolpadCharge;
import noumena.payment.douyu.DouyuCharge;
import noumena.payment.downjoy.DownjoyCharge;
import noumena.payment.duoku.DuokuCharge;
import noumena.payment.duowan.DuowanCharge;
import noumena.payment.efun.EfunCharge;
import noumena.payment.fromest.FromestCharge;
import noumena.payment.game65.Game65Charge;
import noumena.payment.gash.GASHCharge;
import noumena.payment.gionee.GioneeCharge;
import noumena.payment.gmobi.GmobiCharge;
import noumena.payment.google.GoogleCharge;
import noumena.payment.googleplay.GooglePlayCharge;
import noumena.payment.haima.HaimaCharge;
import noumena.payment.heepay.HeepayCharge;
import noumena.payment.huawei.HuaweiCharge;
import noumena.payment.i4.I4Charge;
import noumena.payment.igame.IGameCharge;
import noumena.payment.igs.IGSCharge;
import noumena.payment.jufenghudong.JufenghudongCharge;
import noumena.payment.jusdk.JusdkCharge;
import noumena.payment.kingnet.KingnetCharge;
import noumena.payment.kuaifa.KuaifaCharge;
import noumena.payment.kuaiyong.KuaiyongCharge;
import noumena.payment.kudong.KudongCharge;
import noumena.payment.kugou.ribao.RibaoCharge;
import noumena.payment.lengjing.LengjingCharge;
import noumena.payment.lenovo.LenovoCharge;
import noumena.payment.lguplus.LGUplusCharge;
import noumena.payment.liebao.LiebaoCharge;
import noumena.payment.linyou.LinyouCharge;
import noumena.payment.meizu.MeizuCharge;
import noumena.payment.mo9.Mo9Charge;
import noumena.payment.mol.MOLCharge;
import noumena.payment.mycardtw.MyCardTWCharge;
import noumena.payment.naver.NaverCharge;
import noumena.payment.ndpay.NdpayCharge;
import noumena.payment.nduo.NduoCharge;
import noumena.payment.now.NowCharge;
import noumena.payment.olleh.OllehCharge;
import noumena.payment.omg.OMGCharge;
import noumena.payment.pp.PPCharge;
import noumena.payment.pps.PPSCharge;
import noumena.payment.pptv.PPTVCharge;
import noumena.payment.qianbao.QianbaoCharge;
import noumena.payment.qidian.QidianCharge;
import noumena.payment.qihu.QihuCharge;
import noumena.payment.quick.QuickCharge;
import noumena.payment.renren.RenrenCharge;
import noumena.payment.sharejoy.SharejoyCharge;
import noumena.payment.sina.SinaCharge;
import noumena.payment.snail.SnailCharge;
import noumena.payment.sougou.SougouCharge;
import noumena.payment.suole.SuoleCharge;
import noumena.payment.tag.TAGCharge;
import noumena.payment.tcc2.TCC2Charge;
import noumena.payment.tencent.TencentCharge;
import noumena.payment.tencentY.TencentYCharge;
import noumena.payment.tongbu.TongbuCharge;
import noumena.payment.toutiao.ToutiaoCharge;
import noumena.payment.truemeizu.TrueMeizuCharge;
import noumena.payment.tstore.TStoreCharge;
import noumena.payment.uc.UCCharge;
import noumena.payment.util.Constants;
import noumena.payment.util.NotifyPurchase;
import noumena.payment.vivo.VivoCharge;
import noumena.payment.vivonew.VivoNewCharge;
import noumena.payment.vnsoha.VNSohaCharge;
import noumena.payment.wandoujia.WandoujiaCharge;
import noumena.payment.webomg.WebOMGCharge;
import noumena.payment.weixin.WeixinCharge;
import noumena.payment.xiaomi.xiaomiCharge;
import noumena.payment.xingshang.XingshangCharge;
import noumena.payment.xunlei.XunleiCharge;
import noumena.payment.xxzs.XxzsCharge;
import noumena.payment.xyzs.XyzsCharge;
import noumena.payment.yijie.YijieCharge;
import noumena.payment.yingyonghui.YingyonghuiCharge;
import noumena.payment.yinni.YinniCharge;
import noumena.payment.youwan.YouwanCharge;
import noumena.payment.youxin.YouxinCharge;
import noumena.payment.youxiqun.YouxiqunCharge;
import org.apache.log4j.PropertyConfigurator;

public class JapanContextListenerServlet implements ServletContextListener {
	public void contextDestroyed(ServletContextEvent arg0) {
	}

	public void contextInitialized(ServletContextEvent arg0) {
		ServletContext application = arg0.getServletContext();

		String path = application.getRealPath("/");
		System.setProperty("Web_Root", path);

		String propfile = path
				+ application.getInitParameter("log4jConfigLocation");
		PropertyConfigurator.configure(propfile);

		String p = arg0.getServletContext().getInitParameter("tstoremode");
		if ((p == null) || (!p.equals("test"))) {
			TStoreCharge.setTestmode(false);
		} else {
			TStoreCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("suolemode");
		if ((p == null) || (!p.equals("test"))) {
			SuoleCharge.setTestmode(false);
		} else {
			SuoleCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("mycardtwmode");
		if ((p == null) || (!p.equals("test"))) {
			MyCardTWCharge.setTestmode(false);
		} else {
			MyCardTWCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("mo9mode");
		if ((p == null) || (!p.equals("test"))) {
			Mo9Charge.setTestmode(false);
		} else {
			Mo9Charge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("gashmode");
		if ((p == null) || (!p.equals("test"))) {
			GASHCharge.setTestmode(false);
		} else {
			GASHCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("tcc2mode");
		if ((p == null) || (!p.equals("test"))) {
			TCC2Charge.setTestmode(false);
		} else {
			TCC2Charge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("chtmode");
		if ((p == null) || (!p.equals("test"))) {
			CHTCharge.setTestmode(false);
		} else {
			CHTCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("app01mode");
		if ((p == null) || (!p.equals("test"))) {
			App01Charge.setTestmode(false);
		} else {
			App01Charge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("molmode");
		if ((p == null) || (!p.equals("test"))) {
			MOLCharge.setTestmode(false);
		} else {
			MOLCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("alipaymode");
		if ((p == null) || (!p.equals("test"))) {
			AlipayCharge.setTestmode(false);
		} else {
			AlipayCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("navermode");
		if ((p == null) || (!p.equals("test"))) {
			NaverCharge.setTestmode(false);
		} else {
			NaverCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("ollehmode");
		if ((p == null) || (!p.equals("test"))) {
			OllehCharge.setTestmode(false);
		} else {
			OllehCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("igsmode");
		if (p == null) {
			IGSCharge.setTestmode(0);
		} else if (p.equals("test")) {
			IGSCharge.setTestmode(1);
		} else {
			IGSCharge.setTestmode(2);
		}
		p = arg0.getServletContext().getInitParameter("lguplusmode");
		if ((p == null) || (!p.equals("test"))) {
			LGUplusCharge.setTestmode(false);
		} else {
			LGUplusCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("omgmode");
		if ((p == null) || (!p.equals("test"))) {
			OMGCharge.setTestmode(false);
		} else {
			OMGCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("webomgmode");
		if ((p == null) || (!p.equals("test"))) {
			WebOMGCharge.setTestmode(false);
		} else {
			WebOMGCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("vivomode");
		if ((p == null) || (!p.equals("test"))) {
			VivoCharge.setTestmode(false);
		} else {
			VivoCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("gioneemode");
		if ((p == null) || (!p.equals("test"))) {
			GioneeCharge.setTestmode(false);
		} else {
			GioneeCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("youxiqunmode");
		if ((p == null) || (!p.equals("test"))) {
			YouxiqunCharge.setTestmode(false);
		} else {
			YouxiqunCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("notifymode");
		if ((p == null) || (!p.equals("test"))) {
			NotifyPurchase.setNotifymode(false);
		} else {
			NotifyPurchase.setNotifymode(true);
		}
		p = arg0.getServletContext().getInitParameter("tagmode");
		if ((p == null) || (!p.equals("test"))) {
			TAGCharge.setTestmode(false);
		} else {
			TAGCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("vivonewmode");
		if ((p == null) || (!p.equals("test"))) {
			VivoNewCharge.setTestmode(false);
		} else {
			VivoNewCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("heepaymode");
		if ((p == null) || (!p.equals("test"))) {
			HeepayCharge.setTestmode(false);
		} else {
			HeepayCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("tencentmode");
		if ((p == null) || (!p.equals("test"))) {
			TencentCharge.setTestmode(false);
		} else {
			TencentCharge.setTestmode(true);
		}
		p = arg0.getServletContext().getInitParameter("orderarea");
		Constants.ORDERID_PRE = p;

		p = arg0.getServletContext().getInitParameter("callbackmode");
		Constants.CALLBACK_MODE = p;

		TStoreCharge.init();
		SuoleCharge.init();
		GASHCharge.init();
		PPCharge.init();
		BaiduCharge.init();
		VNSohaCharge.init();
		AppOTACharge.init();
		KingnetCharge.init();
		GooglePlayCharge.init();
		BaiduqianbaoCharge.init();
		C3GGateCharge.init();
		LGUplusCharge.init();
		I4Charge.init();
		DuowanCharge.init();

		XunleiCharge.init();
		QidianCharge.init();
		C37wanCharge.init();
		SnailCharge.init();
		BaiduyunCharge.init();
		BaifubaoCharge.init();
		YouxinCharge.init();
		KudongCharge.init();
		BilibiliCharge.init();
		JufenghudongCharge.init();
		SharejoyCharge.init();
		OMGCharge.init();
		WeixinCharge.init();
		WebOMGCharge.init();
		LinyouCharge.init();

		Constants.initParams(true);
		VivoCharge.init();
		GioneeCharge.init();
		CoolpadCharge.init();
		LenovoCharge.init();
		MeizuCharge.init();
		DuokuCharge.init();
		WandoujiaCharge.init();
		GoogleCharge.init();
		QihuCharge.init();
		NaverCharge.init();
		NdpayCharge.init();
		PPSCharge.init();
		PPTVCharge.init();
		LengjingCharge.init();
		YouxiqunCharge.init();
		SinaCharge.init();
		TongbuCharge.init();
		xiaomiCharge.init();
		XyzsCharge.init();
		DownjoyCharge.init();
		NduoCharge.init();
		XingshangCharge.init();
		HuaweiCharge.init();
		RenrenCharge.init();
		C4399Charge.init();
		IGameCharge.init();
		VivoNewCharge.init();
		YijieCharge.init();
		KuaiyongCharge.init();
		HeepayCharge.init();
		TencentCharge.init();
		TencentYCharge.init();
		YingyonghuiCharge.init();
		DouyuCharge.init();
		AnzhiCharge.init();
		MOLCharge.init();
		BluepayCharge.init();
		ToutiaoCharge.init();
		HaimaCharge.init();
		EfunCharge.init();
		KuaifaCharge.init();
		XxzsCharge.init();
		LiebaoCharge.init();
		YouwanCharge.init();
		RibaoCharge.init();
		CaishenCharge.init();
		GmobiCharge.init();
		SougouCharge.init();
		YinniCharge.init();
		TrueMeizuCharge.init();
		FromestCharge.init();
		AnquCharge.init();
		C49youCharge.init();
		QianbaoCharge.init();
		ApusCharge.init();
		C07073Charge.init();
		QuickCharge.init();
		Game65Charge.init();
		NowCharge.init();
		JusdkCharge.init();
		UCCharge.init();
		AuGameCharge.init();
	}
}

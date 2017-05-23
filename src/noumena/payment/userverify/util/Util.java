package noumena.payment.userverify.util;

import java.io.InputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Properties;

import noumena.payment.userverify.ChannelVerify;

public class Util
{
	private static Properties channels = null;
	private static Properties ndpay = null;
	private static Properties duokoo = null;
	private static Properties uc = null;
	private static Properties qihu = null;
	private static Properties xiaomi = null;
	private static Properties anzhi = null;
	private static Properties baiduqianbao = null;
	private static Properties kuaiyong = null;
	private static Properties igs = null;
	private static Properties downjoy = null;
	private static Properties pps = null;
	private static Properties c37wan = null;
	private static Properties snail = null;
	private static Properties baiduyun = null;
	private static Properties youxin = null;
	private static Properties oppo = null;
	private static Properties jufenghudong = null;
	private static Properties omg = null;
	private static Properties duoku = null;
	private static Properties gionee = null;
	private static Properties meizu = null;
	private static Properties linyou = null;
	private static Properties bilibili = null;
	private static Properties sharejoy = null;
	private static Properties xingshang = null;
	private static Properties c4399 = null;
	private static Properties igame = null;
	private static Properties tencent = null;
	private static Properties weixin = null;
	private static Properties yingyonghui = null;
	private static Properties douyu = null;
	private static Properties efun = null;
	private static Properties kuaifa = null;
	private static Properties xxzs = null;
	private static Properties liebao = null;
	private static Properties ribao = null;
	private static Properties google = null;
	String rootpath;

	private static String getRootPath()
	{
		Util util = new Util();
		util.rootpath = util.getClass().getResource("/").getPath();
		
//		try
//		{
//			InputStream is = util.getClass().getResourceAsStream("/config/payment/channels.xml");
//			Util.channels.loadFromXML(is);
//			is.close();
//		}
//		catch (Exception e)
//		{
//			e.printStackTrace();
//		}
		
		return util.rootpath;
	}

	public static String getXMLPath()
	{
		String path = Util.getRootPath();
		path += "config/payment";
		ChannelVerify.GenerateLog("xml path->" + path);
		return path;
	}
	
	private static Properties getChannels()
	{
		if (Util.channels == null)
		{
			Util.channels = new Properties();
			InputStream is;
			try
			{
//				is = new FileInputStream(Util.getXMLPath() + "/channels.xml");
				is = Util.class.getResourceAsStream("/config/payment/channels.xml");
				Util.channels.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.channels;
	}
	
	private static Properties getNdpay()
	{
		if (Util.ndpay == null)
		{
			Util.ndpay = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/ndpay.xml");
				Util.ndpay.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.ndpay;
	}
	
	private static Properties getGoogle()
	{
		if (Util.google == null)
		{
			Util.google = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/google.xml");
				Util.google.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.google;
	}
	
	private static Properties getDuokoo()
	{
		if (Util.duokoo == null)
		{
			Util.duokoo = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/duokoo.xml");
				Util.duokoo.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.duokoo;
	}
	
	private static Properties getUc()
	{
		if (Util.uc == null)
		{
			Util.uc = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/uc.xml");
				Util.uc.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.uc;
	}
	
	private static Properties getQihu()
	{
		if (Util.qihu == null)
		{
			Util.qihu = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/qihu.xml");
				Util.qihu.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.qihu;
	}
	
	private static Properties getXiaomi()
	{
		if (Util.xiaomi == null)
		{
			Util.xiaomi = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/xiaomi.xml");
				Util.xiaomi.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.xiaomi;
	}
	
	private static Properties getAnzhi()
	{
		if (Util.anzhi == null)
		{
			Util.anzhi = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/anzhi.xml");
				Util.anzhi.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.anzhi;
	}
	
	private static Properties getBaiduqianbao()
	{
		if (Util.baiduqianbao == null)
		{
			Util.baiduqianbao = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/baiduqianbao.xml");
				Util.baiduqianbao.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.baiduqianbao;
	}
	
	private static Properties getKuaiyong()
	{
		if (Util.kuaiyong == null)
		{
			Util.kuaiyong = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/kuaiyong.xml");
				Util.kuaiyong.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.kuaiyong;
	}
	
	private static Properties getDownjoy()
	{
		if (Util.downjoy == null)
		{
			Util.downjoy = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/downjoy.xml");
				Util.downjoy.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.downjoy;
	}
	
	private static Properties getPPS()
	{
		if (Util.pps == null)
		{
			Util.pps = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/pps.xml");
				Util.pps.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.pps;
	}
	
	private static Properties get37wan()
	{
		if (Util.c37wan == null)
		{
			Util.c37wan = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/37wan.xml");
				Util.c37wan.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.c37wan;
	}
	
	private static Properties getSnail()
	{
		if (Util.snail == null)
		{
			Util.snail = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/snail.xml");
				Util.snail.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.snail;
	}
	
	private static Properties getBaiduyun()
	{
		if (Util.baiduyun == null)
		{
			Util.baiduyun = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/baiduyun.xml");
				Util.baiduyun.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.baiduyun;
	}
	
	private static Properties getYouxin()
	{
		if (Util.youxin == null)
		{
			Util.youxin = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/youxin.xml");
				Util.youxin.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.youxin;
	}
	
	private static Properties getOppo()
	{
		if (Util.oppo == null)
		{
			Util.oppo = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/oppo.xml");
				Util.oppo.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.oppo;
	}
	private static Properties getJufenghudong()
	{
		if (Util.jufenghudong == null)
		{
			Util.jufenghudong = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/jufenghudong.xml");
				Util.jufenghudong.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.jufenghudong;
	}
	
	private static Properties getOMG()
	{
		if (Util.omg == null)
		{
			Util.omg = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/omg.xml");
				Util.omg.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.omg;
	}
	
	private static Properties getDuoku()
	{
		if (Util.duoku == null)
		{
			Util.duoku = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/duoku.xml");
				Util.duoku.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.duoku;
	}
	
	private static Properties getGionee()
	{
		if (Util.gionee == null)
		{
			Util.gionee = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/gionee.xml");
				Util.gionee.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.gionee;
	}
	
	private static Properties getMeizu()
	{
		if (Util.meizu == null)
		{
			Util.meizu = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/meizu.xml");
				Util.meizu.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.meizu;
	}
	
	private static Properties getLinyou()
	{
		if (Util.linyou == null)
		{
			Util.linyou = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/linyou.xml");
				Util.linyou.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.linyou;
	}
	
	private static Properties getBilibili()
	{
		if (Util.bilibili == null)
		{
			Util.bilibili = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/bilibili.xml");
				Util.bilibili.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.bilibili;
	}
	
	private static Properties getSharejoy()
	{
		if (Util.sharejoy == null)
		{
			Util.sharejoy = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/sharejoy.xml");
				Util.sharejoy.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.sharejoy;
	}
	
	private static Properties getXingshang()
	{
		if (Util.xingshang == null)
		{
			Util.xingshang = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/xingshang.xml");
				Util.xingshang.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.xingshang;
	}
	
	private static Properties get4399()
	{
		if (Util.c4399 == null)
		{
			Util.c4399 = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/4399.xml");
				Util.c4399.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.c4399;
	}
	
	private static Properties getIGame()
	{
		if (Util.igame == null)
		{
			Util.igame = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/igame.xml");
				Util.igame.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.igame;
	}
	
	private static Properties getTencent()
	{
		if (Util.tencent == null)
		{
			Util.tencent = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/tencent.xml");
				Util.tencent.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.tencent;
	}
	
	private static Properties getWeixin()
	{
		if (Util.weixin == null)
		{
			Util.weixin = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/weixin.xml");
				Util.weixin.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.weixin;
	}
	
	private static Properties getYingyonghui()
	{
		if (Util.yingyonghui == null)
		{
			Util.yingyonghui = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/yingyonghui.xml");
				Util.yingyonghui.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.yingyonghui;
	}
	
	private static Properties getDouyu()
	{
		if (Util.douyu == null)
		{
			Util.douyu = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/douyu.xml");
				Util.douyu.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.douyu;
	}
	
	private static Properties getEfun()
	{
		if (Util.efun == null)
		{
			Util.efun = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/efun.xml");
				Util.efun.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.efun;
	}
	
	private static Properties getKuaifa()
	{
		if (Util.kuaifa == null)
		{
			Util.kuaifa = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/kuaifa.xml");
				Util.kuaifa.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.kuaifa;
	}
	
	private static Properties getXxzs()
	{
		if (Util.xxzs == null)
		{
			Util.xxzs = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/xxzs.xml");
				Util.xxzs.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.xxzs;
	}
	
	private static Properties getLiebao()
	{
		if (Util.liebao == null)
		{
			Util.liebao = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/liebao.xml");
				Util.liebao.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.liebao;
	}
	
	private static Properties getIGS()
	{
		if (Util.igs == null)
		{
			Util.igs = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/igs.xml");
				Util.igs.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.igs;
	}
	
	private static Properties getRibao()
	{
		if (Util.ribao == null)
		{
			Util.ribao = new Properties();
			InputStream is;
			try
			{
				is = Util.class.getResourceAsStream("/config/payment/ribao.xml");
				Util.ribao.loadFromXML(is);
				is.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}
		}
		return Util.ribao;
	}
	
	public static String getChannelType(String channel)
	{
		String ret = Util.getChannels().getProperty(channel);
		if (ret == null)
		{
			ret = channel;
		}
		return ret;
	}
	
	public static String getNdpayKey(String appid)
	{
		String ret = Util.getNdpay().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getGoogleKey(String appid)
	{
		String ret = Util.getGoogle().getProperty(appid+"aud");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getDuokooKey(String appid)
	{
		String ret = Util.getDuokoo().getProperty(appid + "key");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getDuokooSecret(String appid)
	{
		String ret = Util.getDuokoo().getProperty(appid + "secret");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getUCSid(String appid)
	{
		String ret = Util.getUc().getProperty(appid + "serverid");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getUCCpid(String appid)
	{
		String ret = Util.getUc().getProperty(appid + "cpid");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getUCKey(String appid)
	{
		String ret = Util.getUc().getProperty(appid + "key");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getQhKey(String appid)
	{
		String ret = Util.getQihu().getProperty(appid + "key");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getQhSecretKey(String appid)
	{
		String ret = Util.getQihu().getProperty(appid + "secretkey");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getXiaomiKey(String appid)
	{
		String ret = Util.getXiaomi().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getAnzhiKey(String appid)
	{
		String ret = Util.getAnzhi().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getBaiduqianbaoKey(String appid)
	{
		String ret = Util.getBaiduqianbao().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getKuaiyongKey(String appid)
	{
		String ret = Util.getKuaiyong().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getDownjoyKey(String appid)
	{
		String ret = Util.getDownjoy().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getPPSKey(String appid)
	{
		String ret = Util.getPPS().getProperty(appid);
		if (ret == null)
		{
//			ret = appid;
			ret = Util.getPPS().getProperty("0");
		}
		return ret;
	}
	
	public static String getIGSKey(String appid)
	{
		String ret = Util.getIGS().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String get37wanKey(String appid)
	{
		String ret = Util.get37wan().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getSnailKey(String appid)
	{
		String ret = Util.getSnail().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getBaiduyunKey(String appid)
	{
		String ret = Util.getBaiduyun().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getYouxinKey(String appid)
	{
		String ret = Util.getYouxin().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getOppoKey(String appid)
	{
		String ret = Util.getOppo().getProperty(appid+"key");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getOppoSecretKey(String appid)
	{
		String ret = Util.getOppo().getProperty(appid+"secret");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getJufenghudongKey(String appid)
	{
		String ret = Util.getJufenghudong().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getOMGKey(String appid)
	{
		String ret = Util.getOMG().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getOMGIVKey(String appid)
	{
		String ret = Util.getOMG().getProperty(appid+"iv");
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getDuokuKey(String appid)
	{
		String ret = Util.getDuoku().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getGioneeKey(String appid)
	{
		String ret = Util.getGionee().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getMeizuKey(String appid)
	{
		String ret = Util.getMeizu().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getLinyouKey(String appid)
	{
		String ret = Util.getLinyou().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getBilibiliKey(String appid)
	{
		String ret = Util.getBilibili().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getSharejoyKey(String appid)
	{
		String ret = Util.getSharejoy().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getXingshangKey(String appid)
	{
		String ret = Util.getXingshang().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String get4399Key(String appid)
	{
		String ret = Util.get4399().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getIGameKey(String appid)
	{
		String ret = Util.getIGame().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getTencentKey(String appid)
	{
		String ret = Util.getTencent().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getWeixinKey(String appid)
	{
		String ret = Util.getWeixin().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getYingyonghuiKey(String appid)
	{
		String ret = Util.getYingyonghui().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getDouyuKey(String appid)
	{
		String ret = Util.getDouyu().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getEfunKey(String appid)
	{
		String ret = Util.getEfun().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getKuaifaKey(String appid)
	{
		String ret = Util.getKuaifa().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getXxzsKey(String appid)
	{
		String ret = Util.getXxzs().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	public static String getLiebaoKey(String appid)
	{
		String ret = Util.getLiebao().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getRibaoKey(String appid)
	{
		String ret = Util.getRibao().getProperty(appid);
		if (ret == null)
		{
			ret = appid;
		}
		return ret;
	}
	
	public static String getCurTimeStr() {

		GregorianCalendar g = new GregorianCalendar();

		String str = null;
		String year = String.valueOf(g.get(Calendar.YEAR));
		String month = String.valueOf(g.get(Calendar.MONTH) + 1);
		String day = String.valueOf(g.get(Calendar.DAY_OF_MONTH));
		String hours = String.valueOf(g.get(Calendar.HOUR_OF_DAY));
		String minutes = String.valueOf(g.get(Calendar.MINUTE));
		String seconds = String.valueOf(g.get(Calendar.SECOND));
		String mseconds = String.valueOf(g.get(Calendar.MILLISECOND));

		month = formatString(month, 2, "0", true);
		day = formatString(day, 2, "0", true);
		hours = formatString(hours, 2, "0", true);
		minutes = formatString(minutes, 2, "0", true);
		seconds = formatString(seconds, 2, "0", true);
		mseconds = formatString(mseconds, 3, "0", true);

		str = year + month + day + hours + minutes + seconds + mseconds;

		return str;
	}

	public static String formatString(String old, int num, String value,
			boolean tag)
	{

		String str = "";

		if (old.length() >= num)
		{
			return old;
		}
		else
		{
			if (tag)
			{
				for (int i = 1; i <= num - old.length(); i++)
				{
					str = str + value;
				}
				str = str + old;
			}
			else
			{
				str = old;
				for (int i = 1; i <= num - old.length(); i++)
				{
					str = str + value;
				}
			}

			return str;
		}
	}
}

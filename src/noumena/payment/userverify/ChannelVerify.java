package noumena.payment.userverify;

import net.sf.json.JSONObject;
import noumena.payment.userverify.util.Util;

public class ChannelVerify
{
	private static boolean isGenLog = false;
	
	private static String qihuChannel;
	private static String lenovoChannel;
	private static String ppChannel;
	private static String duokooChannel;
	private static String ndpayChannel;
	private static String wandoujiaChannel;
	private static String ucChannel;
	private static String kingnetChannel;
	private static String facebookChannel;
	private static String gfanChannel;
	private static String miChannel;
	private static String tongbuChannel;
	private static String anzhiChannel;
	private static String kunlunChannel;
	private static String baiduqianbaoChannel;
	private static String c3ggateChannel;
	private static String kuaiyongChannel;
	private static String igsChannel;
	private static String mobojoyChannel;
	private static String lineChannel;
	private static String downjoyChannel;
	private static String ppsChannel;
	private static String i4Channel;
	private static String duowanChannel;
	private static String xunleiChannel;
	private static String qidianChannel;
	private static String xyzsChannel;
	private static String c37wanChannel;
	private static String itoolsChannel;
	private static String snailChannel;
	private static String baiduyunChannel;
	private static String vivoChannel;
	private static String huaweiChannel;
	private static String pptvChannel;
	private static String youxinChannel;
	private static String kudongChannel;
	private static String oppoChannel;
	private static String jufenghudongChannel;
	private static String bilibiliChannel;
	private static String omgChannel;
	private static String cmccChannel;
	private static String duokuChannel;
	private static String coolpadChannel;
	private static String gioneeChannel;
	private static String meizuChannel;
	private static String linyouChannel;
	private static String lengjingChannel;
	private static String sharejoyChannel;
	private static String youxiqunChannel;
	private static String sinaChannel;
	private static String kakaoChannel;
	private static String kongzhongChannel;
	private static String xingshangChannel;
	private static String nduoChannel;
	private static String renrenChannel;
	private static String c4399Channel;
	private static String igameChannel;
	private static String yijieChannel;
	private static String tencentChannel;
	private static String weixinChannel;
	private static String yingyonghuiChannel;
	private static String douyuChannel;
	private static String toutiaoChannel;
	private static String haimaChannel;
	private static String efunChannel;
	private static String kuaifaChannel;
	private static String xxzsChannel;
	private static String liebaoChannel;
	private static String youwanChannel;
	private static String ribaoChannel;
	private static String gamecenterChannel;
	private static String googleChannel;
	
	/**
	 * 去第三方联运方请求验证用户是否合法
	 * 
	 *  * =================2.05====================
	 * 母包登录验证测试，匹配上exinfo值为{"type":"kongzhong","appid":"appidvalue","openid":"openidvalue","access_token":"access_tokenvalue"}时，默认验证通过，返回123
	 * 
	 *  * =================2.04====================
	 * 新增D22(啪啪、弹弹)的安智、金立验证参数
	 * 
	 * =================2.03====================
	 * 新增google渠道登录验证
	 * 		例：{"uid":"","type":"google","token":""} 
	 * 新增超凡iiapple渠道参数
	 * 
	 * =================2.02====================
	 * 新增gamecenter渠道登录验证、日爆登录验证
	 * 		例：gamecenter- {"type":"gamecenter","public_key_url":"","signature":"","timestamp":"","salt":"","playerID":"","bundleID":""}
	 * 		日爆-{"type":"ribao","uid":"","appid":"","token":""}
	 * 
	 * =================2.01====================
	 * 新增D22（啪啪）的360、百度、小米验证参数
	 *
	 * =================2.00====================
	 * 有玩（youwan）新接口验证方法：
	 * 		例：{"type":"youwan","uid":""}
	 *
	 * =================1.99====================
	 * 猎宝（liebao）新接口验证方法：
	 * 		例：{"type":"liebao","username":"","appid":"","sign":"","logintime":""}
	 *
	 * =================1.98====================
	 * 新增D11（超神）的叉叉助手验证参数
	 * 新增M5的叉叉助手验证参数
	 *
	 * =================1.97====================
	 * 叉叉助手（xxzs）新接口验证方法：
	 * 		例：{"type":"xxzs","game_uin":"","appid":"","token":""}
	 * 
	 * =================1.96====================
	 * 快发助手（kuaifa）新接口验证方法：
	 * 		例：{"type":"kuaifa","openid":"","gamekey":"","token":""}
	 * 
	 * =================1.95====================
	 * 新增S9的UC参数
	 * 
	 * =================1.94====================
	 * D12（超凡）的百度、快用验证参数
	 * 易幻网络（efun）新接口验证方法：
	 * 		例：{"type":"efun","appid":"","userid":"","timestamp":"","sign":""}
	 * 
	 * =================1.93====================
	 * D12（超凡）的OPPO、小米验证参数
	 * 
	 * =================1.92====================
	 * 易接验证区分自建服务器和易接服务器
	 * UC验证区分新旧SDK
	 * D12（超凡）的UC验证参数
	 * 海马（haima）新接口验证方法：
	 * 		例：{"type":"haima","uid":"","appid":"","validateToken":""}
	 * 
	 * =================1.91====================
	 * 头条（toutiao）新接口验证方法：
	 * 		例：{"type":"toutiao","client_id":"","uid":"","access_token":""}
	 * 
	 * =================1.90====================
	 * Facebook（facebook）改用新接口验证方法：
	 * 		例：{"type":"facebook","token":""}
	 * 空中登录验证改用正式环境地址
	 * 
	 * =================1.89====================
	 * M6（口袋战争2）的小米验证参数（金山云申请的）
	 * D31（狂斩）的小米验证参数（金山云申请的）
	 * 
	 * =================1.88====================
	 * D11（超神）的当乐验证参数
	 * 
	 * =================1.87====================
	 * 新增斗鱼（douyu）新接口验证方法：
	 * 		例：{"type":"douyu","sid":"","appid":""}
	 * 
	 * =================1.86====================
	 * D11（超神）的爱游戏验证参数
	 * 
	 * =================1.85====================
	 * D12（超凡）的快用验证参数
	 * D31（狂斩）的快用验证参数
	 * 新增应用汇（yingyonghui）新接口验证方法：
	 * 		例：{"type":"yingyonghui","app_id":"","ticket":""}
	 * 
	 * =================1.84====================
	 * D11（超神）的魅族验证参数
	 * 新增微信（weixin）新接口验证方法：
	 * 		例：{"type":"weixin","openid":"","appid":"","accessToken":""}
	 * 新增腾讯（tencent）新接口验证方法：
	 * 		例：{"type":"tencent","openid":"","appid":"","openkey":"","userip":""}
	 * 
	 * =================1.83====================
	 * 空中用户中心（KONGZHONG）的验证新增新版本协议
	 * 新增易接（yijie）新接口验证方法：
	 * 		例：{"type":"yijie","uin":"","app":"","sess":"","sdk":""}
	 * 
	 * =================1.82====================
	 * D11（超神）的快用验证参数
	 * 
	 * =================1.81====================
	 * D31（狂斩）的空中验证
	 * 
	 * =================1.80====================
	 * 新增D31（狂斩天下）的百度多酷参数（后台有问题，重新申请）
	 * 移动基地（CMCC）的验证调整，重试一次
	 * 空中用户中心（KONGZHONG）的验证新增新版本协议
	 * 
	 * =================1.79====================
	 * 新增D31（狂斩天下）的4399参数
	 * 
	 * =================1.78====================
	 * 新增爱游戏（igame）新接口验证方法：
	 * 		例：{"type":"igame","client_id":"","code":""}
	 * 新增D11（超神战队）的多酷、金立、OPPO、360、UC、小米参数
	 * 
	 * =================1.77====================
	 * 新增M1的IGS参数
	 * 
	 * =================1.76====================
	 * 新增人人（renren）新接口验证方法：
	 * 		例：{"type":"renren","userId":"","access_token":"","mac_key":""}
	 * 新增4399（4399）新接口验证方法：
	 * 		例：{"type":"4399","uid":"","key":"","state":""}
	 * 新增狂斩、超神的魅族参数
	 * 
	 * =================1.75====================
	 * 新增N多（nduo）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"nduo","uid":""}", true)
	 * UC验证协议升级，修改验证方法
	 * 
	 * =================1.74====================
	 * 新增星尚（xingshang）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"xingshang","username":"","appid":"","sign":"","logintime":""}", true)
	 * 新增M5的OMG参数
	 * 
	 * =================1.73====================
	 * 新增M1的IGS参数
	 * 
	 * =================1.72====================
	 * 新增狂战天下的UC、当乐参数
	 * 
	 * =================1.71====================
	 * 新增狂战天下的百度多酷参数
	 * 
	 * =================1.70====================
	 * 新增狂战天下的360、金立、OPPO、小米参数
	 * 
	 * =================1.69====================
	 * kongzhong新版本（kongzhong）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"kongzhong","token":"token"}", true);
	 * 
	 * =================1.68====================
	 * 新增一个接口ChannelVerify.verify2，和老接口不一样的地方是返回的内容时渠道名+渠道id
	 * 
	 * =================1.67====================
	 * kakao新版本（kakao）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"kakao","user_id":"","access_token":"","client_id":"","sdkver":""}", true);
	 * 
	 * =================1.66====================
	 * 新增小坦克的小米参数
	 * sina新版本（sina）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"sina","uid":"","token":"","machineid":"","ip":""}", true);
	 * 
	 * =================1.65====================
	 * bilibili新版本（bilibili）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"bilibili","uid":"new","game_id":"{game_id}","merchant_id":"{merchant_id}","server_id":"{server_id}","access_key":"{access_key}"}", true);
	 * 享游新版本（sharejoy）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"sharejoy","game_id":"{game_id}","merchant_id":"{merchant_id}","server_id":"{server_id}","access_key":"{access_key}"}", true);
	 * 游戏群新版本（youxiqun）新接口验证方法：
	 * 		例：ChannelVerify.verify("{"type":"youxiqun","appid":"{appid}","openid":"{openid}"}", true);
	 * 
	 * =================1.64====================
	 * 新增M3的金立、OPPO参数
	 * 棱镜新版本（lengjing）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"lengjing\",\"userId\":\"{userId}\",\"channel\":\"{channel}\",\"token\":\"{token}\",\"productCode\":\"{productCode}\"}", true);
	 * 麟游新版本（linyou）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"linyou\",\"uid\":\"{uid}\",\"gamekey\":\"{gamekey}\",\"token\":\"{token}\",\"cp\":\"{cp}\"}", true);
	 * 
	 * =================1.63====================
	 * 魅族新版本（meizu）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"meizu\",\"gid\":\"{gid}\",\"token\":\"{token}\",\"pid\":\"{pid}\"}", true);
	 * 
	 * =================1.62====================
	 * 金立新版本（gionee）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"gionee\",\"uid\":\"{uid}\",\"id\":\"{id}\",\"AmigoToken\":\"{AmigoToken}\"}", true);
	 * 
	 * =================1.61====================
	 * 酷派新版本（coolpad）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"coolpad\",\"openid\":\"{openid}\",\"appid\":\"{appid}\",\"access_token\":\"{access_token}\"}", true);
	 * 
	 * =================1.60====================
	 * 联想（cnlenovo）验证方法调整，使用新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"cnlenovo\",\"appid\":\"{appid}\",\"token\":\"{token}\"}", true);
	 * 
	 * =================1.59====================
	 * M5的OMG使用正式参数
	 * 
	 * =================1.58====================
	 * 新增苍蓝之剑的uc参数
	 * 
	 * =================1.57====================
	 * 华为新版本（huawei）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"huawei\",\"token\":\"{token}\"}", true);
	 * 
	 * =================1.56====================
	 * 新增M5的pps参数
	 * 新增群英Q传的pps参数
	 * 
	 * =================1.55====================
	 * 修改群英Q传的快用参数
	 * 
	 * =================1.54====================
	 * 把所有老版多酷下的游戏都移到新版多酷下
	 * 
	 * =================1.53====================
	 * 多酷新版本（duoku）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"duoku\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"accesstoken\":\"{accesstoken}\"}", true);
	 * 多酷新版本（duoku）老接口验证方法：
	 * 		例：ChannelVerify.verify("duoku", uid, appid, accesstoken, "", true);
	 * 
	 * =================1.52====================
	 * 新增M3的UC参数
	 * 新增M3的360参数
	 * 新增M3的百度多酷参数
	 * 新增M3的小米参数
	 * 新增群英Q传的当乐参数
	 * 新增群英Q传的快用参数
	 * 新增群英Q传的OPPO参数
	 * 新增群英Q传的起点参数
	 * 新增群英Q传的百度多酷参数
	 * 新增群英Q传的百度91参数
	 * 新增龙族少年的当乐参数
	 * 新增龙族少年的UC参数
	 * 新增龙族少年的小米参数
	 * 新增龙族少年的XY助手参数
	 * 新增龙族少年的同步推参数
	 * CMCC（cmcc）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"cmcc\",\"token\":\"{token}\"}", true);
	 * CMCC（cmcc）老接口验证方法：
	 * 		例：ChannelVerify.verify("cmcc", "", "", token, "", true);
	 * 		其中，token为客户端登陆之后获取的特征串
	 * 
	 * =================1.51====================
	 * 新增群英Q传的360参数
	 * OMG（omg）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"omg\",\"uid\":\"{uid}\"\",\"appid\":\"{appid}\"\",\"token\":\"{token}\"\"}", true);
	 * OMG（omg）老接口验证方法：
	 * 		例：ChannelVerify.verify("omg", uid, appid, token, "", true);
	 * 		其中，uid为客户端登陆之后获取的uid
	 * 			appid为OMG分配的appid
	 * 			token为客户端登陆之后获取的token
	 * 
	 * =================1.50====================
	 * 新增群英Q传的UC参数
	 * 新增M5的OPPO参数
	 * BILIBILI（bilibili）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"bilibili\",\"uid\":\"{uid}\"\"}", true);
	 * BILIBILI（bilibili）老接口验证方法：
	 * 		例：ChannelVerify.verify("bilibili", uid, "", "", "", true);
	 * 		其中，uid为客户端登陆之后获取的uid
	 * 
	 * =================1.49====================
	 * 飓风互动（jufenghudong）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"jufenghudong\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"sessionid\":\"{sessionid}\"}", true);
	 * 飓风互动（jufenghudong）老接口验证方法：
	 * 		例：ChannelVerify.verify("jufenghudong", uid, appid, sessionid, "", true);
	 * 		其中，uid为客户端登陆之后的飓风uid
	 * 			appid为飓风分配的appid
	 * 			sessionid为客户端登陆获取的sessionid
	 * 
	 * =================1.48====================
	 * 新增搞怪的安智参数
	 * 新增群英换皮Q传的小米参数
	 * 新增小青蛙（海外手游）的小米参数
	 * 新增小青蛙（海外手游）的UC参数
	 * 新增小青蛙（海外手游）的360参数
	 * 新增小青蛙（海外手游）的多酷参数
	 * 
	 * =================1.47====================
	 * 新增m5的UC参数
	 * 新增m5的当乐参数
	 * PPTV（pptv）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"pptv\",\"username\":\"{username}\",\"sessionid\":\"{sessionid}\"}", true);
	 * PPTV（pptv）老接口验证方法：
	 * 		例：ChannelVerify.verify("pptv", username, "", sessionid, "", true);
	 * 		其中，username为LoginListener.onLoginSuccess 函数传递给客户端的BindUserName
	 * 			sessionid为客户端登陆获取的sessionid
	 * 有信（youxin）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"youxin\",\"appid\":\"{appid}\",\"openid\":\"{openid}\"}", true);
	 * 有信（youxin）老接口验证方法：
	 * 		例：ChannelVerify.verify("youxin", openid, appid, "", "", true);
	 * 		其中，openid为客户端获得的openid
	 * 			appid为有信分配的appid
	 * 酷动（kudong）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"kudong\",\"uid\":\"{uid}\",\"token\":\"{token}\",\"env\":\"{test}\"}", true);
	 * 酷动（kudong）老接口验证方法：
	 * 		例：ChannelVerify.verify("kudong", uid, "", token, "test", true);
	 * 		其中，uid为客户端获得的uid
	 * 			token为客户端获得token
	 * 			test表示测试环境验证
	 * OPPO（oppo）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"oppo\",\"appid\":\"{appid}\",\"token\":\"{token}\"}", true);
	 * OPPO（oppo）老接口验证方法：
	 * 		例：ChannelVerify.verify("oppo", "", appid, token, "", true);
	 * 		其中，tokem为客户端获得的oauth_token
	 * 			appid为有信分配的appid
	 * 
	 * =================1.46====================
	 * 新增m5的百度多酷参数
	 * 新增m5的百度移动云平台参数
	 * 新增m5的小米参数
	 * 新增m5的安智参数
	 * 新增安智（anzhi）的新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"anzhi\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"token\":\"{token}\"}", true);
	 * 安智（anzhi）老接口验证方法：
	 * 		例：ChannelVerify.verify("anzhi", uid, appid, token, "", true);
	 * 
	 * =================1.45====================
	 * 新增机锋（gfan）的新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"gfan\",\"token\":\"{token}\"}", true);
	 * 机锋（gfan）老接口验证方法：
	 * 		例：ChannelVerify.verify("gfan", "", "", token, "", true);
	 * 华为（huawei）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"huawei\",\"token\":\"{token}\"}", true);
	 * 华为（huawei）老接口验证方法：
	 * 		例：ChannelVerify.verify("huawei", "", "", token, "", true);
	 * 		其中，token为客户端登陆获取的accessToken
	 * 
	 * =================1.44====================
	 * VIVO（vivo）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"vivo\",\"token\":\"{token}\"}", true);
	 * VIVO（vivo）老接口验证方法：
	 * 		例：ChannelVerify.verify("vivo", "", "", token, "", true);
	 * 		其中，token为客户端登陆获取的authtoken
	 * 
	 * =================1.43====================
	 * 新增三国群英的百度移动云平台参数
	 * 新增M5的360参数
	 * 百度移动云（baiduyun）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"baiduyun\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"token\":\"{token}\"}", true);
	 * 百度移动云（baiduyun）老接口验证方法：
	 * 		例：ChannelVerify.verify("baiduyun", uid, appid, token, "", true);
	 * 		其中，uid为客户端登陆获取的uin
	 * 			appid为商务获得的游戏ID
	 * 			token为客户端登陆获取的AccessToken
	 * 
	 * =================1.42====================
	 * 同步推验证bug
	 * 新增以下4种渠道的新接口验证方法
	 * 小米（mi）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"mi\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"session\":\"{session}\"}", true);
	 * 小米（mi）老接口验证方法：
	 * 		例：ChannelVerify.verify("mi", uid, appid, session, "", true);
	 * 百度多酷（cnduokoo）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"cnduokoo\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"sessionid\":\"{sessionid}\"}", true);
	 * 百度多酷（cnduokoo）老接口验证方法：
	 * 		例：ChannelVerify.verify("cnduokoo", uid, appid, sessionid, "", true);
	 * 豌豆荚（wandoujia）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"wandoujia\",\"uid\":\"{uid}\",\"appkey_id\":\"{appkey_id}\",\"token\":\"{token}\"}", true);
	 * 豌豆荚（wandoujia）老接口验证方法：
	 * 		例：ChannelVerify.verify("wandoujia", uid, appkey_id, token, "", true);
	 * 360（qh）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"qh\",\"token\":\"{token}\"}", true);
	 * 360（qh）老接口验证方法：
	 * 		例：ChannelVerify.verify("qh", uid, appkey_id, token, "", true);
	 * 
	 * =================1.41====================
	 * 新增吸血鬼的360参数
	 * 蜗牛免商店（snail）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"snail\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"sessionid\":\"{sessionid}\"}", true);
	 * 蜗牛免商店（snail）老接口验证方法：
	 * 		例：ChannelVerify.verify("snail", uid, appid, sessionid, "", true);
	 * 		其中，uid为客户端登陆获取的uin
	 * 			appid为商务获得的游戏ID
	 * 			sessionid为客户端登陆获取的sessionid
	 * 
	 * =================1.40====================
	 * 新增M5的91参数
	 * 新增以下4种渠道的新接口验证方法
	 * 同步推（tongbu）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"tongbu\",\"token\":\"{token}\"}", true);
	 * 同步推（tongbu）老接口验证方法：
	 * 		例：ChannelVerify.verify("tongbu", "", "", token, "", true);
	 * 快用（kuaiyong）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"kuaiyong\",\"appid\":\"{appid}\",\"token\":\"{token}\"}", true);
	 * 快用（kuaiyong）老接口验证方法：
	 * 		例：ChannelVerify.verify("kuaiyong", "", appid, token, "", true);
	 * PP助手（cnpp）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"cnpp\",\"token\":\"{token}\"}", true);
	 * PP助手（cnpp）老接口验证方法：
	 * 		例：ChannelVerify.verify("cnpp", "", "", token, "", true);
	 * 91（ndpay）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"ndpay\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"token\":\"{token}\"}", true);
	 * 91（ndpay）老接口验证方法：
	 * 		例：ChannelVerify.verify("cnpp", uid, appid, token, "", true);
	 * 
	 * =================1.39====================
	 * 修改XY助手的验证方法
	 * 新增M5的快用参数
	 * itools（itools）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"itools\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"sessionid\":\"{sessionid}\"}", true);
	 * itools（itools）老接口验证方法：
	 * 		例：ChannelVerify.verify("itools", uid, appid, sessionid, "", true);
	 * 		其中，uid为客户端登陆获取的uid
	 * 			appid为商务获得的游戏ID
	 * 			sessionid为客户端登陆获取的sessionid
	 * 
	 * =================1.38====================
	 * 37玩（37wan）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"37wan\",\"gameid\":\"{gameid}\",\"sessionid\":\"{sessionid}\"}", true);
	 * 37玩（37wan）老接口验证方法：
	 * 		例：ChannelVerify.verify("37wan", "", appid, token, "", true);
	 * 		其中，gameid为商务获得的游戏ID
	 * 			sessionid为客户端登陆获取的sessionid
	 * 
	 * =================1.37====================
	 * XY助手（xyzs）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"xyzs\",\"uid\":\"{uid}\",\"appid\":\"{appid}\",\"token\":\"{token}\"}", true);
	 * XY助手（xyzs）老接口验证方法：
	 * 		例：ChannelVerify.verify("xyzs", uid, appid, token, "", true);
	 * 		其中，uid为uid
	 * 			appid为商务获得的游戏ID
	 * 			token为客户端登陆获取的用户Token加密串
	 * 
	 * =================1.36====================
	 * 起点（qidian）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"qidian\",\"packagename\":\"{packagename}\",\"gameid\":\"{gameid}\",\"tkey\":\"{tkey}\"}", true);
	 * 起点（qidian）老接口验证方法：
	 * 		例：ChannelVerify.verify("qidian", packagename, gameid, tkey, "", true);
	 * 		其中，packagename为游戏应用包名
	 * 			gameid为商务获得的游戏ID
	 * 			tkey为客户端登陆获取的用户CmFuToken加密串
	 * 
	 * =================1.35====================
	 * 修改LINE登录验证bug
	 * 迅雷（xunlei）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"xunlei\",\"customerid\":\"{customerid}\",\"gameid\":\"{gameid}\",\"customerKey\":\"{customerKey}\"}", true);
	 * 迅雷（xunlei）老接口验证方法：
	 * 		例：ChannelVerify.verify("xunlei", customerid, gameid, customerKey, "", true);
	 * 		其中，customerid为客户端登陆获取的customerid值
	 * 			gameid为商务获得的gameid
	 * 			customerKey为客户端登陆获取的customerKey值
	 * 
	 * =================1.34====================
	 * 多玩（duowan）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"duowan\",\"open_id\":\"{open_id}\",\"open_token\":\"{open_token}\",\"g_appid\":\"{g_appid}\"}", true);
	 * 多玩（duowan）老接口验证方法：
	 * 		例：ChannelVerify.verify("duowan", open_id, g_appid, open_token, "", true);
	 * 		其中，open_id为客户端登陆获取的open_id值
	 * 			g_appid为商务获得的g_appid
	 * 			open_token为客户端登陆获取的open_token值
	 * 
	 * =================1.33====================
	 * 爱思（i4）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"i4\",\"token\":\"{token}\"}", true);
	 * 爱思（i4）老接口验证方法：
	 * 		例：ChannelVerify.verify("i4", "", "", token, "", true);
	 * 		其中，token为客户端登陆获取的token值
	 * 修改T8、吸血鬼的小米参数
	 * 
	 * =================1.32====================
	 * UC实现新接口方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"uc\",\"appid\":\"{appid}\",\"sessionid\":\"{sessionid}\"}", true);
	 * 新增T8的百度多酷参数
	 * 新增T8的91参数
	 * 新增T8的小米参数
	 * 
	 * =================1.31====================
	 * 新增吸血鬼日记的小米参数
	 * 
	 * =================1.30====================
	 * PPS（pps）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"pps\",\"app_id\":\"{app_id}\",\"uid\":\"{uid}\",\"timestamp\":\"{timestamp}\",\"sign\":\"{sign}\"}", true);
	 * PPS（pps）老接口验证方法：
	 * 		例：ChannelVerify.verify("pps", uid, app_id, sign, timestamp, true);
	 * 		其中，type为关键字pps
	 * 			app_id为商务获得的appid
	 * 			uid为客户端成功登入后,SDK返回的PPSUser对象uid成员
	 * 			timestamp为客户端成功登入后,SDK返回的PPSUser对象timestamp成员
	 * 			sign为客户端成功登入后,SDK返回的PPSUser对象sign成员
	 * 
	 * =================1.29====================
	 * 当乐（downjoy）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"downjoy\",\"app_id\":\"xxx\",\"mid\":\"xxx\",\"token\":\"xxx\"}", true);
	 * 当乐（downjoy）老接口验证方法：
	 * 		例：ChannelVerify.verify("downjoy", mid, app_id, token, "", true);
	 * 		其中，type为关键字downjoy
	 * 			mid为客户端登陆后获得的mid值
	 * 			app_id为商务获得的appid
	 * 			token为客户端登陆后获得的token值
	 * 
	 * =================1.28====================
	 * LINE（line）新接口验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"line\",\"appId\":\"xxx\",\"reqStr\":\"xxx\",\"env\":\"xxx\"}", true);
	 * LINE（line）老接口验证方法：
	 * 		例：ChannelVerify.verify("line", "", appId, reqStr, env, true);
	 * 		其中，type为关键字line
	 * 			appId为商务获得的appid
	 * 			reqStr为Credentials string retrieved from the client SDK
	 * 			env为此次登录的环境：sendbox（沙箱）、test（测试）、release（正式）
	 * 
	 * =================1.27====================
	 * 新增一个接口：ChannelVerify.verify(logininfo, islog);
	 * 		logininfo为客户端在渠道方登录之后需要交给服务器验证json串，由jar和底层定好的协议，游戏不用关心了
	 * 91海外（mobojoy）新验证方法：
	 * 		例：ChannelVerify.verify("{\"type\":\"mobojoy\",\"appid\":\"92\",\"uin\":\"1326\",\"sessionid\":\"abc\"}", true);
	 * 
	 * =================1.26====================
	 * 新增91海外（mobojoy）用户验证（mobojoy），参数有uid、appid、token
	 * 		例：ChannelVerify.verify("mobojoy", uid, appid, token, "", true);
	 * 		其中，uid为客户端获得的Uin值
	 * 			appid为商务分配的appid；
	 * 			token为客户端获得sessionid；
	 * 
	 * =================1.25====================
	 * 新增搞怪大富翁的百度钱包参数
	 * 新增吸血鬼日记的91参数
	 * 
	 * =================1.24====================
	 * 新增繁体IGS用户验证（igs），参数有appid、token
	 * 		例：ChannelVerify.verify("igs", "", appid, token, "", true);
	 * 		其中，appid为商务分配的gameid；
	 * 			token为客户端获得的用户信息JSON及加密密文合并之后的字符串；
	 * 
	 * =================1.23====================
	 * 新增快用用户验证（kuaiyong），参数有appid、token
	 * 		例：ChannelVerify.verify("kuaiyong", "", appid, token, "", true);
	 * 		其中，appid为商务分配的gameid；
	 * 			token为客户端通过SDK获得的token内容；
	 * 
	 * =================1.22====================
	 * 新增T8的UC参数
	 * 大领主的UC参数调整
	 * 新增3G门户用户验证（3ggate），参数有uid、appid、sid、exinfo
	 * 		例：ChannelVerify.verify("3ggate", uid, appid, sid, exinfo, true);
	 * 		其中，uid为客户端通过SDK获得的token内容（token是唯一用户标识）；
	 * 			appid为商务分配的gameid；
	 * 			sid为客户端通过SDK获得的sid内容；
	 * 			exinfo是json格式的字符串，内容为{"cpid":"xxxx"}（cpid为商务分配的cpid）
	 * 
	 * =================1.21====================
	 * 新增百度移动钱包用户验证（baiduqianbao），参数有appid、code
	 * 新增搞怪三国的百度钱包参数
	 * 新增吸血鬼日记的UC参数
	 * 
	 * =================1.20====================
	 * 新增昆仑（厦门搞怪）用户验证，参数有token
	 * 新增三国群英的安智参数
	 * 
	 * =================1.19====================
	 * 新增安智帐号验证，参数有appid、uid、token
	 * 新增搞怪三国的小米参数
	 * 
	 * =================1.18====================
	 * 新增同步推帐号验证，参数有token
	 * 
	 * =================1.17====================
	 * 新增搞怪三国的百度参数
	 * 新增女神物语的UC参数
	 * 
	 * =================1.16====================
	 * 新增后宫无双的360参数
	 * 
	 * =================1.15====================
	 * 新增小米帐号验证，参数有appid、uid、token
	 * 
	 * =================1.14====================
	 * 新增机锋帐号验证，参数只有token
	 * 
	 * =================1.13====================
	 * 360帐号验证返回值修改
	 * 
	 * =================1.12====================
	 * 360帐号验证增强错误控制
	 * 
	 * =================1.11====================
	 * 新增搞怪三国的91参数
	 * 
	 * =================1.10====================
	 * 新增一个接口，参数和老接口一样，但是返回内容和老接口只返回一个id不一样，新接口返回一个json串
	 * 360帐号验证支持新接口，返回{"ret":0;"uid":"xxxx";"token":"xxxxx"}
	 * 
	 * =================1.09====================
	 * Kingnet帐号验证debug
	 * 
	 * =================1.08====================
	 * Kingnet帐号验证(kingnet)更新，参数为uid和token和exinfo
	 * uid --> kingnet帐号
	 * token --> kingnet帐号密码/facebook token
	 * exinfo --> 验证类型（kingnet登录：kingnet；facebook登录：facebook；kingnet注册：register）
	 * 
	 * 支持facebook（facebook）帐号验证，参数为token
	 * 
	 * =================1.07====================
	 * Kingnet帐号验证(kingnet)实现，参数为uid和token和exinfo
	 * uid --> kingnet帐号/facebookid
	 * token --> kingnet帐号密码
	 * exinfo --> 验证类型（kingnet登录：kingnet；facebook登录：facebook；kingnet注册：register）
	 * 
	 * =================1.06====================
	 * UC帐号验证(uc)实现，参数为appid和sessionid
	 * 
	 * =================1.05====================
	 * 百度帐号验证(cnduokoo)改用新接口实现，uid和sessionid(需要urlencode)分开传
	 * 
	 * =================1.04====================
	 * 支持91帐号验证(ndpay)
	 * 支持豌豆荚帐号验证(wandoujia)
	 * 
	 * 支持XML配置文件
	 * 
	 * 新增接口，多传入一个参数uid和exinfo扩展用
	 * 
	 * =================1.03====================
	 * 360帐号验证BUG
	 * 
	 * =================1.02====================
	 * 支持百度帐号验证(cnduokoo)
	 * appid 必填
	 * token格式：uid&sessionid（uid和sessionid需要urlencode）
	 * 
	 * =================1.01====================
	 * 支持360帐号验证(qh)
	 * 支持联想帐号验证(cnlenovo)
	 * 支持PP助手帐号验证(cnpp)
	 * 
	 * =========================================
	 * 
	 * @param type		联运方的channeltype，和客户端定义的一致
	 * @param uid		客户端从第三方获取的uid
	 * @param appid		联运方提供的appid
	 * @param token		客户端从第三方获取的验证码
	 * @param exinfo	冗余参数，扩展用
	 * @param islog		是否输出日志到控制台
	 * @return		返回从第三方验证之后得到的用户id
	 */

	@Deprecated
	public static String verify(String type, String appid, String token, boolean islog)
	{
		return verify(type, "", appid, token, "", islog);
	}
	
	public static String verifyNew(String type, String uid, String appid, String token, String exinfo, boolean islog)
	{
		return doVerify(1, type, uid, appid, token, exinfo, islog);
	}
	
	public static String verify(String type, String uid, String appid, String token, String exinfo, boolean islog)
	{
		return doVerify(0, type, uid, appid, token, exinfo, islog);
	}
	
	public static String verify(String loginfo, boolean islog)
	{
		JSONObject json = JSONObject.fromObject(loginfo);
		return doVerify(2, json.getString("type"), "", "", "", loginfo, islog);
	}
	
	public static String verify2(String loginfo, boolean islog)
	{
		JSONObject json = JSONObject.fromObject(loginfo);
		String type = json.getString("type");
		String id = doVerify(2, type, "", "", "", loginfo, islog);
		if (id == null || id.equals(""))
		{
			id = "";
		}
		else
		{
			id = type + "_" + id;
		}
		return id;
	}
	
	//model:0表示用的老接口，只返回uid；model:1表示用的新街口，返回的是用户信息的json串
	private static String doVerify(int model, String type, String uid, String appid, String token, String exinfo, boolean islog)
	{
		//该判断仅为母包登录放行用
		if(exinfo.equals("{\"type\":\"kongzhong\",\"appid\":\"appidvalue\",\"openid\":\"openidvalue\",\"access_token\":\"access_tokenvalue\"}")){
			return "123";
		}
		
		String id = null;
		try
		{
			initChannels();
			
			ChannelInfoVO vo = new ChannelInfoVO();
			vo.setType(type);
			vo.setAppid(appid);
			vo.setToken(token);
			vo.setUid(uid);
			vo.setExinfo(exinfo);
			isGenLog = islog;
			
			ChannelVerify.GenerateLog("=======verify model(" + model + ") type(" + type + ") uid(" + uid + ") appid(" + appid + ") token(" + token + ") exinfo(" + exinfo + ")=======");
					
			if (vo.getType().toLowerCase().equals(qihuChannel))
			{
				id = C360Verify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(lenovoChannel))
			{
				id = CLenovoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(ppChannel))
			{
				id = CPPVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(duokooChannel))
			{
				id = CBaiduVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(ndpayChannel))
			{
				id = C91Verify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(wandoujiaChannel))
			{
				id = CWandoujiaVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(ucChannel))
			{
				id = CUCVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(kingnetChannel))
			{
				id = CKingnetVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(facebookChannel))
			{
				id = CFacebookVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(gfanChannel))
			{
				id = CGfanVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(miChannel))
			{
				id = CMiVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(tongbuChannel))
			{
				id = CTongbuVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(anzhiChannel))
			{
				id = CAnzhiVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(kunlunChannel))
			{
				id = CKunlunVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(baiduqianbaoChannel))
			{
				id = CBaiduqianbaoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(c3ggateChannel))
			{
				id = C3GGateVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(kuaiyongChannel))
			{
				id = CKuaiyongVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(igsChannel))
			{
				id = CIGSVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(mobojoyChannel))
			{
				id = CMobojoyVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(lineChannel))
			{
				id = CLineVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(downjoyChannel))
			{
				id = CDownjoyVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(ppsChannel))
			{
				id = CPPSVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(i4Channel))
			{
				id = CI4Verify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(duowanChannel))
			{
				id = CDuowanVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(xunleiChannel))
			{
				id = CXunleiVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(qidianChannel))
			{
				id = CQidianVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(xyzsChannel))
			{
				id = CXyzsVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(c37wanChannel))
			{
				id = C37wanVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(itoolsChannel))
			{
				id = CiToolsVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(snailChannel))
			{
				id = CSnailVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(baiduyunChannel))
			{
				id = CBaiduyunVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(vivoChannel))
			{
				id = CVivoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(huaweiChannel))
			{
				id = CHuaweiVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(pptvChannel))
			{
				id = CPPTVVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(youxinChannel))
			{
				id = CYouxinVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(kudongChannel))
			{
				id = CKudongVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(oppoChannel))
			{
				id = COppoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(jufenghudongChannel))
			{
				id = CJufenghudongVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(bilibiliChannel))
			{
				id = CBilibiliVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(omgChannel))
			{
				id = COMGVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(cmccChannel))
			{
				id = CCMCCVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(duokuChannel))
			{
				id = CDuokuVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(coolpadChannel))
			{
				id = CCoolpadVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(gioneeChannel))
			{
				id = CGioneeVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(meizuChannel))
			{
				id = CMeizuVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(linyouChannel))
			{
				id = CLinyouVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(lengjingChannel))
			{
				id = CLengjingVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(sharejoyChannel))
			{
				id = CSharejoyVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(youxiqunChannel))
			{
				id = CYouxiqunVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(sinaChannel))
			{
				id = CSinaVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(kakaoChannel))
			{
				id = CKakaoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(kongzhongChannel))
			{
				id = CKongzhongVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(xingshangChannel))
			{
				id = CXingshangVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(nduoChannel))
			{
				id = CNduoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(renrenChannel))
			{
				id = CRenrenVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(c4399Channel))
			{
				id = C4399Verify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(igameChannel))
			{
				id = CIGameVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(yijieChannel))
			{
				id = CYijieVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(tencentChannel))
			{
				id = CTencentVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(weixinChannel))
			{
				id = CWeixinVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(yingyonghuiChannel))
			{
				id = CYingyonghuiVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(douyuChannel))
			{
				id = CDouyuVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(toutiaoChannel))
			{
				id = CToutiaoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(haimaChannel))
			{
				id = CHaimaVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(efunChannel))
			{
				id = CEfunVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(kuaifaChannel))
			{
				id = CKuaifaVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(xxzsChannel))
			{
				id = CXxzsVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(liebaoChannel))
			{
				id = CLiebaoVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(youwanChannel))
			{
				id = CYouwanVerify.verify(model, vo);
			}
			else if (vo.getType().toLowerCase().equals(ribaoChannel))
			{
				id = CRibaoVerify.verify(model, vo);
			}else if(vo.getType().toLowerCase().equals(gamecenterChannel)){
				id = CGamecenterVerify.verify(model, vo);
			}else if(vo.getType().toLowerCase().equals(googleChannel)){
				id = CGoogleVerify.verify(model, vo);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
			id = "";
		}
		
		ChannelVerify.GenerateLog("=======verify type(" + type + ") result->" + id + "=======");

		return id;
	}

	public static void initChannels()
	{
		qihuChannel = Util.getChannelType("360");
		lenovoChannel = Util.getChannelType("lenovo");
		ppChannel = Util.getChannelType("pp");
		duokooChannel = Util.getChannelType("baidu");
		ndpayChannel = Util.getChannelType("91");
		wandoujiaChannel = Util.getChannelType("wandoujia");
		ucChannel = Util.getChannelType("uc");
		kingnetChannel = Util.getChannelType("kingnet");
		facebookChannel = Util.getChannelType("facebook");
		gfanChannel = Util.getChannelType("gfan");
		miChannel = Util.getChannelType("xiaomi");
		tongbuChannel = Util.getChannelType("tongbu");
		anzhiChannel = Util.getChannelType("anzhi");
		kunlunChannel = Util.getChannelType("kunlun");
		baiduqianbaoChannel = Util.getChannelType("baiduqianbao");
		c3ggateChannel = Util.getChannelType("3ggate");
		kuaiyongChannel = Util.getChannelType("kuaiyong");
		igsChannel = Util.getChannelType("igs");
		mobojoyChannel = Util.getChannelType("mobojoy");
		lineChannel = Util.getChannelType("line");
		downjoyChannel = Util.getChannelType("downjoy");
		ppsChannel = Util.getChannelType("pps");
		i4Channel = Util.getChannelType("i4");
		duowanChannel = Util.getChannelType("duowan");
		xunleiChannel = Util.getChannelType("xunlei");
		qidianChannel = Util.getChannelType("qidian");
		xyzsChannel = Util.getChannelType("xyzs");
		c37wanChannel = Util.getChannelType("37wan");
		itoolsChannel = Util.getChannelType("itools");
		snailChannel = Util.getChannelType("snail");
		baiduyunChannel = Util.getChannelType("baiduyun");
		vivoChannel = Util.getChannelType("vivo");
		huaweiChannel = Util.getChannelType("huawei");
		pptvChannel = Util.getChannelType("pptv");
		youxinChannel = Util.getChannelType("youxin");
		kudongChannel = Util.getChannelType("kudong");
		oppoChannel = Util.getChannelType("oppo");
		jufenghudongChannel = Util.getChannelType("jufenghudong");
		bilibiliChannel = Util.getChannelType("bilibili");
		omgChannel = Util.getChannelType("omg");
		cmccChannel = Util.getChannelType("cmcc");
		duokuChannel = Util.getChannelType("duoku");
		coolpadChannel = Util.getChannelType("coolpad");
		gioneeChannel = Util.getChannelType("gionee");
		meizuChannel = Util.getChannelType("meizu");
		linyouChannel = Util.getChannelType("linyou");
		lengjingChannel = Util.getChannelType("lengjing");
		sharejoyChannel = Util.getChannelType("sharejoy");
		youxiqunChannel = Util.getChannelType("youxiqun");
		sinaChannel = Util.getChannelType("sina");
		kakaoChannel = Util.getChannelType("kakao");
		kongzhongChannel = Util.getChannelType("kongzhong");
		xingshangChannel = Util.getChannelType("xingshang");
		nduoChannel = Util.getChannelType("nduo");
		renrenChannel = Util.getChannelType("renren");
		c4399Channel = Util.getChannelType("4399");
		igameChannel = Util.getChannelType("igame");
		yijieChannel = Util.getChannelType("yijie");
		tencentChannel = Util.getChannelType("tencent");
		weixinChannel = Util.getChannelType("weixin");
		yingyonghuiChannel = Util.getChannelType("yingyonghui");
		douyuChannel = Util.getChannelType("douyu");
		toutiaoChannel = Util.getChannelType("toutiao");
		haimaChannel = Util.getChannelType("haima");
		efunChannel = Util.getChannelType("efun");
		kuaifaChannel = Util.getChannelType("kuaifa");
		xxzsChannel = Util.getChannelType("xxzs");
		liebaoChannel = Util.getChannelType("liebao");
		youwanChannel = Util.getChannelType("youwan");
		ribaoChannel = Util.getChannelType("ribao");
		gamecenterChannel = Util.getChannelType("gamecenter");
		googleChannel = Util.getChannelType("google");
	}
	
	public static void GenerateLog(String log)
	{
		if (isGenLog == true)
		{
			System.out.println(log);
		}
	}

	public static String getQihuChannel()
	{
		return qihuChannel;
	}

	public static void setQihuChannel(String qihuChannel)
	{
		ChannelVerify.qihuChannel = qihuChannel;
	}

	public static String getLenovoChannel()
	{
		return lenovoChannel;
	}

	public static void setLenovoChannel(String lenovoChannel)
	{
		ChannelVerify.lenovoChannel = lenovoChannel;
	}

	public static String getPpChannel()
	{
		return ppChannel;
	}

	public static void setPpChannel(String ppChannel)
	{
		ChannelVerify.ppChannel = ppChannel;
	}

	public static String getDuokooChannel()
	{
		return duokooChannel;
	}

	public static void setDuokooChannel(String duokooChannel)
	{
		ChannelVerify.duokooChannel = duokooChannel;
	}

	public static String getNdpayChannel()
	{
		return ndpayChannel;
	}

	public static void setNdpayChannel(String ndpayChannel)
	{
		ChannelVerify.ndpayChannel = ndpayChannel;
	}

	public static String getWandoujiaChannel()
	{
		return wandoujiaChannel;
	}

	public static void setWandoujiaChannel(String wandoujiaChannel)
	{
		ChannelVerify.wandoujiaChannel = wandoujiaChannel;
	}

	public static String getUcChannel()
	{
		return ucChannel;
	}

	public static void setUcChannel(String ucChannel)
	{
		ChannelVerify.ucChannel = ucChannel;
	}

	public static String getKingnetChannel()
	{
		return kingnetChannel;
	}

	public static void setKingnetChannel(String kingnetChannel)
	{
		ChannelVerify.kingnetChannel = kingnetChannel;
	}

	public static String getFacebookChannel()
	{
		return facebookChannel;
	}

	public static void setFacebookChannel(String facebookChannel)
	{
		ChannelVerify.facebookChannel = facebookChannel;
	}

	public static String getGfanChannel()
	{
		return gfanChannel;
	}

	public static void setGfanChannel(String gfanChannel)
	{
		ChannelVerify.gfanChannel = gfanChannel;
	}

	public static String getMiChannel()
	{
		return miChannel;
	}

	public static void setMiChannel(String miChannel)
	{
		ChannelVerify.miChannel = miChannel;
	}

	public static String getTongbuChannel()
	{
		return tongbuChannel;
	}

	public static void setTongbuChannel(String tongbuChannel)
	{
		ChannelVerify.tongbuChannel = tongbuChannel;
	}

	public static String getAnzhiChannel()
	{
		return anzhiChannel;
	}

	public static void setAnzhiChannel(String anzhiChannel)
	{
		ChannelVerify.anzhiChannel = anzhiChannel;
	}

	public static String getKunlunChannel()
	{
		return kunlunChannel;
	}

	public static void setKunlunChannel(String kunlunChannel)
	{
		ChannelVerify.kunlunChannel = kunlunChannel;
	}

	public static String getBaiduqianbaoChannel()
	{
		return baiduqianbaoChannel;
	}

	public static void setBaiduqianbaoChannel(String baiduqianbaoChannel)
	{
		ChannelVerify.baiduqianbaoChannel = baiduqianbaoChannel;
	}

	public static String getC3ggateChannel()
	{
		return c3ggateChannel;
	}

	public static void setC3ggateChannel(String c3ggateChannel)
	{
		ChannelVerify.c3ggateChannel = c3ggateChannel;
	}

	public static String getKuaiyongChannel() {
		return kuaiyongChannel;
	}

	public static void setKuaiyongChannel(String kuaiyongChannel) {
		ChannelVerify.kuaiyongChannel = kuaiyongChannel;
	}

	public static String getIgsChannel() {
		return igsChannel;
	}

	public static void setIgsChannel(String igsChannel) {
		ChannelVerify.igsChannel = igsChannel;
	}

	public static String getMobojoyChannel() {
		return mobojoyChannel;
	}

	public static void setMobojoyChannel(String mobojoyChannel) {
		ChannelVerify.mobojoyChannel = mobojoyChannel;
	}

	public static String getLineChannel() {
		return lineChannel;
	}

	public static void setLineChannel(String lineChannel) {
		ChannelVerify.lineChannel = lineChannel;
	}

	public static String getDownjoyChannel() {
		return downjoyChannel;
	}

	public static void setDownjoyChannel(String downjoyChannel) {
		ChannelVerify.downjoyChannel = downjoyChannel;
	}

	public static String getPpsChannel() {
		return ppsChannel;
	}

	public static void setPpsChannel(String ppsChannel) {
		ChannelVerify.ppsChannel = ppsChannel;
	}

	public static String getI4Channel() {
		return i4Channel;
	}

	public static void setI4Channel(String i4Channel) {
		ChannelVerify.i4Channel = i4Channel;
	}

	public static String getDuowanChannel() {
		return duowanChannel;
	}

	public static void setDuowanChannel(String duowanChannel) {
		ChannelVerify.duowanChannel = duowanChannel;
	}

	public static String getXunleiChannel() {
		return xunleiChannel;
	}

	public static void setXunleiChannel(String xunleiChannel) {
		ChannelVerify.xunleiChannel = xunleiChannel;
	}

	public static String getQidianChannel() {
		return qidianChannel;
	}

	public static void setQidianChannel(String qidianChannel) {
		ChannelVerify.qidianChannel = qidianChannel;
	}

	public static String getXyzsChannel() {
		return xyzsChannel;
	}

	public static void setXyzsChannel(String xyzsChannel) {
		ChannelVerify.xyzsChannel = xyzsChannel;
	}

	public static String getC37wanChannel() {
		return c37wanChannel;
	}

	public static void setC37wanChannel(String c37wanChannel) {
		ChannelVerify.c37wanChannel = c37wanChannel;
	}

	public static String getItoolsChannel() {
		return itoolsChannel;
	}

	public static void setItoolsChannel(String itoolsChannel) {
		ChannelVerify.itoolsChannel = itoolsChannel;
	}

	public static String getSnailChannel() {
		return snailChannel;
	}

	public static void setSnailChannel(String snailChannel) {
		ChannelVerify.snailChannel = snailChannel;
	}

	public static String getBaiduyunChannel() {
		return baiduyunChannel;
	}

	public static void setBaiduyunChannel(String baiduyunChannel) {
		ChannelVerify.baiduyunChannel = baiduyunChannel;
	}

	public static String getVivoChannel() {
		return vivoChannel;
	}

	public static void setVivoChannel(String vivoChannel) {
		ChannelVerify.vivoChannel = vivoChannel;
	}

	public static String getHuaweiChannel() {
		return huaweiChannel;
	}

	public static void setHuaweiChannel(String huaweiChannel) {
		ChannelVerify.huaweiChannel = huaweiChannel;
	}

	public static String getPptvChannel() {
		return pptvChannel;
	}

	public static void setPptvChannel(String pptvChannel) {
		ChannelVerify.pptvChannel = pptvChannel;
	}

	public static String getYouxinChannel() {
		return youxinChannel;
	}

	public static void setYouxinChannel(String youxinChannel) {
		ChannelVerify.youxinChannel = youxinChannel;
	}

	public static String getKudongChannel() {
		return kudongChannel;
	}

	public static void setKudongChannel(String kudongChannel) {
		ChannelVerify.kudongChannel = kudongChannel;
	}

	public static String getOppoChannel() {
		return oppoChannel;
	}

	public static void setOppoChannel(String oppoChannel) {
		ChannelVerify.oppoChannel = oppoChannel;
	}

	public static String getJufenghudongChannel() {
		return jufenghudongChannel;
	}

	public static void setJufenghudongChannel(String jufenghudongChannel) {
		ChannelVerify.jufenghudongChannel = jufenghudongChannel;
	}

	public static String getBilibiliChannel() {
		return bilibiliChannel;
	}

	public static void setBilibiliChannel(String bilibiliChannel) {
		ChannelVerify.bilibiliChannel = bilibiliChannel;
	}

	public static String getOmgChannel() {
		return omgChannel;
	}

	public static void setOmgChannel(String omgChannel) {
		ChannelVerify.omgChannel = omgChannel;
	}

	public static String getCmccChannel() {
		return cmccChannel;
	}

	public static void setCmccChannel(String cmccChannel) {
		ChannelVerify.cmccChannel = cmccChannel;
	}

	public static String getDuokuChannel() {
		return duokuChannel;
	}

	public static void setDuokuChannel(String duokuChannel) {
		ChannelVerify.duokuChannel = duokuChannel;
	}

	public static String getCoolpadChannel() {
		return coolpadChannel;
	}

	public static void setCoolpadChannel(String coolpadChannel) {
		ChannelVerify.coolpadChannel = coolpadChannel;
	}

	public static String getGioneeChannel() {
		return gioneeChannel;
	}

	public static void setGioneeChannel(String gioneeChannel) {
		ChannelVerify.gioneeChannel = gioneeChannel;
	}

	public static String getMeizuChannel() {
		return meizuChannel;
	}

	public static void setMeizuChannel(String meizuChannel) {
		ChannelVerify.meizuChannel = meizuChannel;
	}

	public static String getLinyouChannel() {
		return linyouChannel;
	}

	public static void setLinyouChannel(String linyouChannel) {
		ChannelVerify.linyouChannel = linyouChannel;
	}

	public static String getLengjingChannel() {
		return lengjingChannel;
	}

	public static void setLengjingChannel(String lengjingChannel) {
		ChannelVerify.lengjingChannel = lengjingChannel;
	}

	public static String getSharejoyChannel() {
		return sharejoyChannel;
	}

	public static void setSharejoyChannel(String sharejoyChannel) {
		ChannelVerify.sharejoyChannel = sharejoyChannel;
	}

	public static String getYouxiqunChannel() {
		return youxiqunChannel;
	}

	public static void setYouxiqunChannel(String youxiqunChannel) {
		ChannelVerify.youxiqunChannel = youxiqunChannel;
	}

	public static String getSinaChannel() {
		return sinaChannel;
	}

	public static void setSinaChannel(String sinaChannel) {
		ChannelVerify.sinaChannel = sinaChannel;
	}

	public static String getKakaoChannel() {
		return kakaoChannel;
	}

	public static void setKakaoChannel(String kakaoChannel) {
		ChannelVerify.kakaoChannel = kakaoChannel;
	}

	public static String getKongzhongChannel() {
		return kongzhongChannel;
	}

	public static void setKongzhongChannel(String kongzhongChannel) {
		ChannelVerify.kongzhongChannel = kongzhongChannel;
	}

	public static String getXingshangChannel() {
		return xingshangChannel;
	}

	public static void setXingshangChannel(String xingshangChannel) {
		ChannelVerify.xingshangChannel = xingshangChannel;
	}

	public static String getNduoChannel() {
		return nduoChannel;
	}

	public static void setNduoChannel(String nduoChannel) {
		ChannelVerify.nduoChannel = nduoChannel;
	}

	public static String getRenrenChannel() {
		return renrenChannel;
	}

	public static void setRenrenChannel(String renrenChannel) {
		ChannelVerify.renrenChannel = renrenChannel;
	}

	public static String getC4399Channel() {
		return c4399Channel;
	}

	public static void setC4399Channel(String c4399Channel) {
		ChannelVerify.c4399Channel = c4399Channel;
	}

	public static String getIgameChannel() {
		return igameChannel;
	}

	public static void setIgameChannel(String igameChannel) {
		ChannelVerify.igameChannel = igameChannel;
	}

	public static String getYijieChannel() {
		return yijieChannel;
	}

	public static void setYijieChannel(String yijieChannel) {
		ChannelVerify.yijieChannel = yijieChannel;
	}

	public static String getTencentChannel() {
		return tencentChannel;
	}

	public static void setTencentChannel(String tencentChannel) {
		ChannelVerify.tencentChannel = tencentChannel;
	}

	public static String getWeixinChannel() {
		return weixinChannel;
	}

	public static void setWeixinChannel(String weixinChannel) {
		ChannelVerify.weixinChannel = weixinChannel;
	}

	public static String getYingyonghuiChannel() {
		return yingyonghuiChannel;
	}

	public static void setYingyonghuiChannel(String yingyonghuiChannel) {
		ChannelVerify.yingyonghuiChannel = yingyonghuiChannel;
	}

	public static String getDouyuChannel() {
		return douyuChannel;
	}

	public static void setDouyuChannel(String douyuChannel) {
		ChannelVerify.douyuChannel = douyuChannel;
	}

	public static String getToutiaoChannel() {
		return toutiaoChannel;
	}

	public static void setToutiaoChannel(String toutiaoChannel) {
		ChannelVerify.toutiaoChannel = toutiaoChannel;
	}

	public static String getHaimaChannel() {
		return haimaChannel;
	}

	public static void setHaimaChannel(String haimaChannel) {
		ChannelVerify.haimaChannel = haimaChannel;
	}

	public static String getEfunChannel() {
		return efunChannel;
	}

	public static void setEfunChannel(String efunChannel) {
		ChannelVerify.efunChannel = efunChannel;
	}

	public static String getKuaifaChannel() {
		return kuaifaChannel;
	}

	public static void setKuaifaChannel(String kuaifaChannel) {
		ChannelVerify.kuaifaChannel = kuaifaChannel;
	}

	public static String getXxzsChannel() {
		return xxzsChannel;
	}

	public static void setXxzsChannel(String xxzsChannel) {
		ChannelVerify.xxzsChannel = xxzsChannel;
	}

	public static String getLiebaoChannel() {
		return liebaoChannel;
	}

	public static void setLiebaoChannel(String liebaoChannel) {
		ChannelVerify.liebaoChannel = liebaoChannel;
	}

	public static String getYouwanChannel() {
		return youwanChannel;
	}

	public static void setYouwanChannel(String youwanChannel) {
		ChannelVerify.youwanChannel = youwanChannel;
	}

	public static String getRibaoChannel() {
		return ribaoChannel;
	}

	public static void setRibaoChannel(String ribaoChannel) {
		ChannelVerify.ribaoChannel = ribaoChannel;
	}

	public static String getGamecenterChannel() {
		return gamecenterChannel;
	}

	public static void setGamecenterChannel(String gamecenterChannel) {
		ChannelVerify.gamecenterChannel = gamecenterChannel;
	}

	public static String getGoogleChannel() {
		return googleChannel;
	}

	public static void setGoogleChannel(String googleChannel) {
		ChannelVerify.googleChannel = googleChannel;
	}

}

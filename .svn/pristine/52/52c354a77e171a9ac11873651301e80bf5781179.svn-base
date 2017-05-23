package noumena.payment.ios;

public class Guid {
	public static final long EXTRA_VALUE = 10000000;
	
	/**
	 * 生成游戏账户用户ID
	 * @param userId
	 * @param serverId
	 * @return
	 */
	public static long guid(long userId, String serverId){
		return userId*EXTRA_VALUE + Long.parseLong(serverId);
	}
	
	/**
	 * 还原原始用户ID（不区分服务器）
	 * @param userId
	 * @return
	 */
	public static long oUId(long userId){
		return userId/EXTRA_VALUE;
	}
	
	/**
	 * 还原原始用户ID（不区分服务器）
	 * @param userId
	 * @return
	 */
	public static long oUId(String userId){
		Long l = Long.parseLong(userId);
		return l/EXTRA_VALUE;
	}
	
	/**
	 * 根据用户ID获得所在服务器ID
	 * @param userId
	 * @return
	 */
	public static String serverId(long userId){
		String suid = userId+"";
		return String.valueOf(Integer.parseInt(suid.substring(suid.length()-4,suid.length())));
	}
}

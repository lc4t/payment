package noumena.payment.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Vector;

public class DateUtil {

	public static String getCurrentTime() {

		GregorianCalendar g = new GregorianCalendar();
		return dateToString(g);
	}

	public static String getTime(long ms) {

		GregorianCalendar g = new GregorianCalendar();
		g.setTimeInMillis(ms);

		return dateToString(g);
	}

	public static GregorianCalendar getCalendar(String date) {

		GregorianCalendar g = new GregorianCalendar();

		try {
			g.setTime(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(date));
		} catch (ParseException e) {
			g = null;
		}

		return g;
	}

	public static String calcDate(GregorianCalendar g, int seconds, boolean flag) {

		if (g == null)
			return null;

		if (flag) {
			g.add(Calendar.SECOND, seconds);
		} else {
			g.add(Calendar.SECOND, -seconds);
		}

		return dateToString(g);
	}

	public static String calcDate(String date, int seconds, boolean flag) {

		GregorianCalendar g = getCalendar(date);

		return calcDate(g, seconds, flag);
	}

	public static String getCookieExpire(long ms) {

		GregorianCalendar g = new GregorianCalendar();
		g.setTimeInMillis(g.getTimeInMillis() + ms);
		SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd-MMM-yyyy HH:mm:ss zzz", Locale.US);
		sdf.setTimeZone(TimeZone.getTimeZone("GMT"));

		return sdf.format(g.getTime());
	}

	public static String dateToString(GregorianCalendar g) {

		String str = null;
		String year = String.valueOf(g.get(Calendar.YEAR));
		String month = String.valueOf(g.get(Calendar.MONTH) + 1);
		String day = String.valueOf(g.get(Calendar.DAY_OF_MONTH));
		String hours = String.valueOf(g.get(Calendar.HOUR_OF_DAY));
		String minutes = String.valueOf(g.get(Calendar.MINUTE));
		String seconds = String.valueOf(g.get(Calendar.SECOND));

		month = OSUtil.formatString(month, 2, "0", true);
		day = OSUtil.formatString(day, 2, "0", true);
		hours = OSUtil.formatString(hours, 2, "0", true);
		minutes = OSUtil.formatString(minutes, 2, "0", true);
		seconds = OSUtil.formatString(seconds, 2, "0", true);

		str = year + "-" + month + "-" + day + " " + hours + ":" + minutes + ":" + seconds;

		return str;
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

		month = OSUtil.formatString(month, 2, "0", true);
		day = OSUtil.formatString(day, 2, "0", true);
		hours = OSUtil.formatString(hours, 2, "0", true);
		minutes = OSUtil.formatString(minutes, 2, "0", true);
		seconds = OSUtil.formatString(seconds, 2, "0", true);

		str = year + month + day + hours + minutes + seconds;

		return str;
	}

	public static String getCurDateStr() {

		GregorianCalendar g = new GregorianCalendar();

		String str = null;
		String year = String.valueOf(g.get(Calendar.YEAR));
		String month = String.valueOf(g.get(Calendar.MONTH) + 1);
		String day = String.valueOf(g.get(Calendar.DAY_OF_MONTH));

		month = OSUtil.formatString(month, 2, "0", true);
		day = OSUtil.formatString(day, 2, "0", true);

		str = year + "-" + month + "-" + day;

		return str;
	}

	@SuppressWarnings("unchecked")
	public static Vector getMonthList(String startTime, String endTime) {

		Vector vector = new Vector();
		String str = "";

		int startYear = getYear(startTime);
		int startMonth = getMonth(startTime);
		int endYear = getYear(endTime);
		int endMonth = getMonth(endTime);

		if (startTime.compareTo(endTime) > 0)
			return null;

		if (startYear == endYear) {
			for (; startMonth <= endMonth; startMonth++) {
				str = endYear + "-" + OSUtil.formatString(String.valueOf(startMonth), 2, "0", true);
				vector.add(str);
			}
		} else {
			for (; startMonth <= 12; startMonth++) {
				str = startYear + "-" + OSUtil.formatString(String.valueOf(startMonth), 2, "0", true);
				vector.add(str);
			}

			for (++startYear; startYear < endYear; startYear++) {
				for (int month = 1; month <= 12; month++) {
					str = startYear + "-" + OSUtil.formatString(String.valueOf(month), 2, "0", true);
					vector.add(str);
				}
			}

			for (int month = 1; month <= endMonth; month++) {
				str = endYear + "-" + OSUtil.formatString(String.valueOf(month), 2, "0", true);
				vector.add(str);
			}
		}

		return vector;
	}

	public static int getYear(String time) {
		return Integer.parseInt(time.substring(0, 4));
	}

	public static int getMonth(String time) {
		return Integer.parseInt(time.substring(5, 7));
	}

	public static int getDay(String time) {
		return Integer.parseInt(time.substring(8, 10));
	}

	public static int getHours(String time) {
		return Integer.parseInt(time.substring(11, 13));
	}

	public static int getMinutes(String time) {
		return Integer.parseInt(time.substring(14, 16));
	}

	public static int getSeconds(String time) {
		return Integer.parseInt(time.substring(17));
	}

	public static String getNullTime() {
		return "0000-00-00 00:00:00";
	}


	public static int getDaysBetween(String beginDate, String endDate) {
		try{
			SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
			Date bDate = format.parse(beginDate);
			Date eDate = format.parse(endDate);
			Calendar d1 = new GregorianCalendar();
			d1.setTime(bDate);
			Calendar d2 = new GregorianCalendar();
			d2.setTime(eDate);
			int days = d2.get(Calendar.DAY_OF_YEAR) - d1.get(Calendar.DAY_OF_YEAR);
			int y2 = d2.get(Calendar.YEAR);
			if (d1.get(Calendar.YEAR) != y2)
			{
				d1 = (Calendar) d1.clone();
				do {
					days += d1.getActualMaximum(Calendar.DAY_OF_YEAR);// 得到当年的实际天数
					d1.add(Calendar.YEAR, 1);
				} while (d1.get(Calendar.YEAR) != y2);
			}
			return days;
		}catch(Exception e){
			e.printStackTrace();
		}
		return 0;
	}
	
	public static int getBetweenSeconds(String date1, String date2){
		long _date1 = getCalendar(date1).getTimeInMillis();
		long _date2 = getCalendar(date2).getTimeInMillis();
		long betweenMillis = _date1 - _date2;
		return (int)(betweenMillis/1000);
		
	}
	
	public static int getConLoginCnt(int cnt, String curTime, String lastTime){
		if(curTime.substring(0, 10).equals(lastTime.substring(0, 10))){//同一天
			return 0;
		}
		String temp = DateUtil.calcDate(curTime, 24*60*60, false);
		if(temp.substring(0, 10).equals(lastTime.substring(0, 10))){
			return (cnt+1);
		}else{
			return 1;
		}
	}
	
	//计算时间,粗略计算
	/*例如：
	    2 years ago/1 year ago
		2 months ago/1 month ago
		2 weeks ago/1 week ago
		2 days ago/1 day ago
		2 hours ago/1 hour ago
		其他
	 */
	public static String calcDate(String sDate){
		String[] array = sDate.split(" ");
		if(array.length != 3) return null;
		
		int diffSec = 0;
		
		if(array[1].indexOf("year")!=-1){
			diffSec = 60*60*24*365; 
		}else if(array[1].indexOf("month")!=-1){
			diffSec = 60*60*24*30; 
		}else if(array[1].indexOf("week")!=-1){
			diffSec = 60*60*24*7; 
		}else if(array[1].indexOf("day")!=-1){
			diffSec = 60*60*24; 
		}else if(array[1].indexOf("hour")!=-1){
			diffSec = 60*60; 
		}
		
		try {
			int num = Integer.parseInt(array[0]);
			return calcDate(getCurrentTime(), num*diffSec, false);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	public static String formatDate(String date){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String s ="";
		try {
			s = format.format(format1.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
	
	public static String formatDate2(String date){
		
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
		SimpleDateFormat format1 = new SimpleDateFormat("yyyy/MM/dd");
		String s ="";
		try {
			s = format.format(format1.parse(date));
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}

	/**
	 * 把一个日期转换成按照指定格式显示的字符串
	 * @param time Date数据类型的时间
	 * @param type
	 * 0 - 显示完整时间，格式：年-月-日 时：分：秒
	 * 1 - 显示格式：年-月-日
	 * 2 - 显示格式：月-日
	 * 3 - 显示格式：月-日 时：分
	 * 4 - 显示完整时间（带有时区），格式：年-月-日 时：分：秒 +0800
	 * @return 返回按照要求格式的字符串
	 */
	public static String date2Str(long time, int type)
	{
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(time);
		int year = cd.get(Calendar.YEAR);
		int month = cd.get(Calendar.MONTH) + 1;
		int day = cd.get(Calendar.DAY_OF_MONTH);
		int hour = cd.get(Calendar.HOUR_OF_DAY);
		int min = cd.get(Calendar.MINUTE);
		int sec = cd.get(Calendar.SECOND);
        SimpleDateFormat dfOut = new SimpleDateFormat("Z");
        String timezone = dfOut.format(cd.getTime());
		String syear = Integer.toString(year);
		String smonth;
		if (month < 10)
		{
			smonth = String.format("0%d", month);
		}
		else
		{
			smonth = Integer.toString(month);
		}
		String sday;
		if (day < 10)
		{
			sday = String.format("0%d", day);
		}
		else
		{
			sday = Integer.toString(day);
		}
		String shour;
		if (hour < 10)
		{
			shour = String.format("0%d", hour);
		}
		else
		{
			shour = Integer.toString(hour);
		}
		String smin;
		if (min < 10)
		{
			smin = String.format("0%d", min);
		}
		else
		{
			smin = Integer.toString(min);
		}
		String ssec;
		if (sec < 10)
		{
			ssec = String.format("0%d", sec);
		}
		else
		{
			ssec = Integer.toString(sec);
		}
		
		switch (type)
		{
		case 0:
			//年-月-日 时：分：秒
			return syear + "-" + smonth + "-" + sday + " " + shour + ":" + smin + ":" + ssec;
		case 1:
			//年-月-日
			return syear + "-" + smonth + "-" + sday;
		case 2:
			//月-日
			return smonth + "-" + sday;
		case 3:
			//月-日 时：分
			return smonth + "-" + sday + " " + shour + ":" + smin;
		case 4:
			//年-月-日 时：分：秒 +0800
			return syear + "-" + smonth + "-" + sday + " " + shour + ":" + smin + ":" + ssec + " " + timezone;
		}
		return "";
	}

	public static long str2Time(String str)
	{
		if (str.length() != 19)
		{
			return 0;
		}
		String tmp;
		Calendar cd = Calendar.getInstance();
		cd.setTimeInMillis(System.currentTimeMillis());
		
		tmp = str.substring(0, 4);
		cd.set(Calendar.YEAR, Integer.parseInt(tmp));
		tmp = str.substring(5, 7);
		cd.set(Calendar.MONTH, Integer.parseInt(tmp)-1);
		tmp = str.substring(8, 10);
		cd.set(Calendar.DAY_OF_MONTH, Integer.parseInt(tmp));
		tmp = str.substring(11, 13);
		cd.set(Calendar.HOUR_OF_DAY, Integer.parseInt(tmp));
		tmp = str.substring(14, 16);
		cd.set(Calendar.MINUTE, Integer.parseInt(tmp));
		tmp = str.substring(17, 19);
		cd.set(Calendar.SECOND, Integer.parseInt(tmp));
		cd.set(Calendar.MILLISECOND, 0);
		return cd.getTimeInMillis();
	}
	
	public static void main(String[] args){
		long t = str2Time("2015-03-31 00:00:00");
		System.out.println(t);
		System.out.println(System.currentTimeMillis());
		System.out.println(date2Str(System.currentTimeMillis(), 4));
//		System.out.println(getCurTimeStr());
//		System.out.println(getCurDateStr());
//		System.out.println(getCurrentTime());
		System.out.println(formatDate2("2013/02/04"));
	} 
}

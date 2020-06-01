package org.ltsh.core.core.util;

import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 日期工具类
 * @author Ych
 * 2017年5月18日
 */
public class DateUtil {
	/**
	 * 格式类型
	 * @author Ych
	 * 2017年5月18日
	 */
	public static enum Pattern{
		/**
		 * yyyyMMddHHmmss
		 */
		ALL("yyyyMMddHHmmss", ""),
		/**
		 * yyyy-MM-dd HH:mm:ss
		 */
		DATE_TIME("yyyy-MM-dd HH:mm:ss", ""),
		/**
		 * yyyy-MM-dd HHmmss
		 */
		DATE_TIME_SIMPLE("yyyy-MM-dd HHmmss",""),

		/***
		 * yyyy-MM-dd HH:mm:ss.SSS
		 */
		DATE_TIME_COMPLEX("yyyy-MM-dd HH:mm:ss.SSS",""),
		
		/**
		 * yyyyMMddHHmmssSSS 字符串时间戳
		 */
		DATE_TIME_COMPLEX_SIMPLE("yyyyMMddHHmmssSSS",""),
		/**
		 * yyyy
		 */
		YEAR("yyyy", ""),
		/**
		 * yyyy-MM-dd
		 */
		DATE("yyyy-MM-dd", ""),
		/**
		 * yyyyMMdd
		 */
		DATE8("yyyyMMdd", ""),
		/**
		 * yyMMdd
		 */
		DATE6("yyMMdd", ""),
		/**
		 * yyyyMM
		 */
		MONTH("yyyyMM", ""),
		
		/**
		 * yyyy-MM
		 */
		MONTH7("yyyy-MM", ""),
		
		/**
		 * HHmmss
		 */
		TIME6("HHmmss", ""),
		/**
		 * HH:mm:ss
		 */
		TIME("HH:mm:ss", ""),
		/**
		 * HHmmssS
		 */
		TIME7("HHmmssS", ""),
		/**
		 * HHmmssSS
		 */
		TIME8("HHmmssSS", ""),
		/**
		 * HHmmssSSS
		 */
		TIME9("HHmmssSSS", ""),
		/**
		 * SSS
		 */
		SSS("SSS", ""),
		
		/**
		 * yyyy年MM月dd日
		 */
		CHINESE_DATE_SIMPLE("yyyy年MM月dd日",""),
		
		_DATE("yyyy/MM/dd", "");
		;
		
		private final String pattern;
		private final String desc;
		
		@Override
		public String toString(){
			return this.desc;
		}
		
		Pattern(String pattern, String desc){
			this.pattern = pattern;
			this.desc = desc;
		}
		
		public String getPattern(){
			return this.pattern;
		}
		
		public String getDesc(){
			return this.desc;
		}
	}
	private static SimpleDateFormat createSDF(String pattern){
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		sdf.setLenient(false);	//严格模式，设置 1996-13-3 不能转为1997-1-3
		return sdf;
	}
	
	private static Calendar createCld(){
		return Calendar.getInstance();
	}
	
	/**
	 * 格式化简单日期	20170807 -> 2017-08-07
	 * @author YeChao
	 * @param date
	 * @return
	 */
	public static String formatToShortDate(String date){
		String rs = null;
		if(date.length() == 8){
			rs = date.substring(0, 4)+"-"+date.substring(4, 6)+"-"+date.substring(6);
		}
		return rs;
	}
	
	/**
	 * 格式化日期对象
	 * @author Ych
	 * @param timestamp
	 * @param pattern
	 * @return
	 */
	public static String convertTimestampToString(long timestamp, Pattern pattern){
		Date date = new Date(timestamp);
		return convertDateToString(date, pattern);
	}
	
	/**
	 * 解析时间字符串
	 * @author Ych
	 * @param date	只支持三种通用格式：yyyy-MM-dd、HH:mm:ss、yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static Date convertStringToDate(String date){
		Date rsDate = null;
		int len = date.length();
		
		if(!(len == 10 || len == 8 || len == 19)){
			throw new IllegalArgumentException("日期参数" + date + "时间格式错误");
		}
		
		Calendar calendar = Calendar.getInstance();
		try {
			if (len == 10) { // yyyy-MM-dd格式
				rsDate = convertStringToDate(date, DateUtil.Pattern.DATE);
				calendar.setTime(rsDate);
				calendar.set(Calendar.HOUR_OF_DAY, 0);
				calendar.set(Calendar.MINUTE, 0);
				calendar.set(Calendar.SECOND, 0);
				rsDate = calendar.getTime();
				
			} else if (len == 8) { // HH:mm:ss
				rsDate = convertStringToDate(date, DateUtil.Pattern.TIME);
				calendar.setTime(rsDate);
				
				Calendar nowCalender = Calendar.getInstance();
				calendar.set(Calendar.YEAR, nowCalender.get(Calendar.YEAR));
				calendar.set(Calendar.MONTH, nowCalender.get(Calendar.MONTH));
				calendar.set(Calendar.DAY_OF_MONTH, nowCalender.get(Calendar.DAY_OF_MONTH));
				
				rsDate = calendar.getTime();
				
			} else { // yyyy-MM-dd HH:mm:ss
				rsDate = convertStringToDate(date, DateUtil.Pattern.DATE_TIME);
			}
		} catch (ParseException e) {
			throw new IllegalArgumentException("日期参数" + date + "时间格式错误");
		}
		return rsDate;
	}
	
	/**
	 * 日期转换：String日期转为Date日期
	 * @author Ych
	 * @param String date
	 * @param String pattern
	 * @return	Date
	 */
	public static Date convertStringToDate(String date, Pattern pattern) throws ParseException{
		Date dateStr = createSDF(pattern.getPattern()).parse(date);
		return dateStr;
	}
	
	/**
	 * 日期转换：Date日期转为String日期
	 * @author Ych
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertDateToString(Date date){
		return convertDateToString(date, Pattern.DATE);
	}
	
	/**
	 * 日期转换：Date日期转为String日期
	 * @author Ych
	 * @param date
	 * @param pattern
	 * @return
	 */
	public static String convertDateToString(Date date, Pattern pattern){
		return createSDF(pattern.getPattern()).format(date);
	}
	
	/**
	 * 日期类型转换
	 * @author Ych
	 * @param obj
	 * @return
	 */
	public static Date convertObjToDate(Object obj){
		long ts = convertObjToTimestamps(obj);
		return ts > 0 ? new Date(ts) : null;
	}
	
	/**
	 * 日期类型转为毫秒时间戳
	 * @author Ych
	 * @param obj
	 * @return
	 */
	public static long convertObjToTimestamps(Object obj){
		long rs = -1L;
		if(obj instanceof Date){
			rs = ((Date) obj).getTime();
		}else if(obj instanceof java.sql.Date){
			rs = ((java.sql.Date) obj).getTime();
		}else if(obj instanceof oracle.sql.DATE){
			rs = ((oracle.sql.DATE) obj).timestampValue().getTime();
		}else if(obj instanceof java.sql.Timestamp){
			rs = ((java.sql.Timestamp) obj).getTime();
		}else if(obj instanceof oracle.sql.TIMESTAMP){
			try {
				rs = ((oracle.sql.TIMESTAMP) obj).timestampValue().getTime();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return rs;
	}
	/**
	 * 返回在date基础上加/减secs秒后的日期
	 * @author Ych
	 * @param date
	 * @param secs
	 * @return
	 */
	public static Date addSecond(Date date, int secs){
		Calendar cl = createCld();
		cl.setTime(date);
		cl.add(Calendar.SECOND, secs);
		return cl.getTime();
	}
	
	/**
	 * 返回在date基础上加/减days天后的日期
	 * @author Ych
	 * @param date	指定的日期
	 * @param days	天数，正数为加，负数为减
	 * @return
	 */
	public static Date addDays(Date date, int days){
		Calendar cl = createCld();
		cl.setTime(date);
		cl.add(Calendar.DATE, days);
		return cl.getTime();
	}
	
	/**
	 * 返回在date基础上加/减months月后的日期
	 * @author Ych
	 * @param date	指定的日期
	 * @param months	月份数，正数为加，负数为减
	 * @return
	 */
	public static Date addMonths(Date date, int months){
		Calendar cl = createCld();
		cl.setTime(date);
		cl.add(Calendar.MONTH, months);
		return cl.getTime();
	}
	
	/**
	 * 计算两时间相差的时间（秒、分钟、小时、天数）
	 * @author Ych
	 * @param srcDate
	 * @param targetDate
	 * @return
	 */
	public static double calculateSpace(Date srcDate, Date targetDate, TimeUnit timeUnit){
		long timestamp = Math.abs(srcDate.getTime() - targetDate.getTime());
		double time = timestamp / 1000D;
		if(TimeUnit.SECONDS.equals(timeUnit)){
			return time;
		}else if(TimeUnit.MINUTES.equals(timeUnit)){
			return time / 60;
		}else if(TimeUnit.HOURS.equals(timeUnit)){
			return time / 60 / 60;
		}else if(TimeUnit.DAYS.equals(timeUnit)){
			return time / 60 / 60 / 24;
		}
		return time;
	}
	
}

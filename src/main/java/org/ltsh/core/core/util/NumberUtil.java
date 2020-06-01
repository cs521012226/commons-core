package org.ltsh.core.core.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

import org.ltsh.core.core.util.RegularUtil.RegExp;

/**
 * 数字工具类
 * @author 邓端宁
 * @date 2018年6月13日
 */
public class NumberUtil {
	
	public static enum Pattern{
		/**
		 * @description: 四舍五入固定保留两位小数
		 * 例1: (1234.12556) ==> (1234.13)
		 * 例2: (123464) ==> (123464.00)
		 */
		DECIMAL("0.00"),
		/**
		 * @description: 四舍五入根据是否有小数再保留两位小数
		 * 例1: (1234.12556) ==> (1234.13)
		 * 例2: (123464) ==> (123464)
		 */
		STANDART("#.##"),
		/**
		 * @description: 四舍五入根据是否有小数再保留四位小数
		 * 例1: (1234.12556) ==> (1234.1256)
		 * 例2: (123464) ==> (123464)
		 */
		HIGH_POSITION("#.####"),
		/**
		 * @description: 四舍五入转百分比
		 * 例1: (0.1234) ==> (12.34%)
		 */
		PERCENT("0.00%"),
		/**
		 * @description: 金钱格式 四舍五入根据是否有小数再保留两位小数
		 * 例1: (1234.12556) ==> (1,234.13)
		 * 例2: (123464) ==> (123,464.00)
		 * 例3: (0) ==> (0.00)
		 */
		MONEY("###,###,##0.00");
		
		private final String pattern;
		
		Pattern(String pattern){
			this.pattern = pattern;
		}
		
		public String getPattern(){
			return this.pattern;
		}
	}
	
	public static String format(Number value){
		return format(value, Pattern.STANDART);
	}
	
	public static String format(Number value, Pattern pattern){
		return format(value, pattern.getPattern());
	}
	
	public static String format(Number value, String pattern){
		DecimalFormat d = new DecimalFormat(pattern);
		d.setRoundingMode(RoundingMode.HALF_UP);
		return d.format(value);
	}
	public static String format(String value, Pattern pattern){
		return format(value, pattern.getPattern());
	}
	
	public static String format(String value, String pattern){
		try {
			return format(new BigDecimal(value), pattern);
		} catch (NumberFormatException e) {
			return format(-1, pattern);
		}
	}
	/**
	 * 是否是整数数字
	 * @param text
	 * @return
	 */
	public static boolean isNumber(String text){
		if(StringUtil.isBlank(text))  {
			return false;
		}
		return RegularUtil.matches(text, RegExp.INTEGER);
	}
	
	/**
	 * 是否是小数
	 * @param text
	 * @return
	 */
	public static boolean isDecimal(String text) {
		if(StringUtil.isBlank(text)) {
			return false;
		}
		return RegularUtil.matches(text, RegExp.DOUBLE);
	}
	
	/**
	 * 是否是数字，包含整数/小数
	 * @param text
	 * @return
	 */
	public static boolean isNumeric(String text) {
		if(StringUtil.isBlank(text)){
			return false;
		}
		return RegularUtil.matches(text, RegExp.NUMBER);
	}
	
	/**
	 * 判断该字符串是否为i位整数数字, 
	 * <p>例: isNumberX("23426",5) 返回: true</p>
	 * @param text
	 * @param i
	 * @return
	 */
	public static boolean isNumberX(String text, int i) {
		if(StringUtil.isBlank(text)){
			return false;
		}
		return text.matches("\\d{" + i + "}");
	}
	
	/**
	 *  判断该字符串是否为i位小数数字, 
	 * <p>例: isDecimalX("123426.67",2) 返回: true</p>
	 * @param text
	 * @param i 
	 * @return
	 */
	public static boolean isDecimalX(String text,int i) {
		if(!isDecimal(text)){
			return false;
		}
		text = text.split("\\.")[1];
		if(text.length() == i) {
			return true;
		}
		return false;
	}
	
	/**
	 * 字符串转BigDecimal
	 * @author Ych
	 * @param text
	 * @return
	 */
	public static BigDecimal toDecimal(String text) {
		text = StringUtil.isBlank(text) ? "0" : text;
		return new BigDecimal(text);
	}
}

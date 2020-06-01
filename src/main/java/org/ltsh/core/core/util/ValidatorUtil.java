package org.ltsh.core.core.util;

/**
 * 验证工具类
 * @author 邓端宁
 * @date 2018年6月13日
 */
public class ValidatorUtil {
	
	/**
	 * 字段不允许为空，判断字段是否为空
	 * @param chName
	 * @param text
	 * @return 为空则反回提示，否则返回空字符串
	 */
	public static String stringIsBlank(String chName,String text) {
		if(StringUtil.isBlank(text)) return "【" + chName + "】不允许为空.<br/>";
		return "";
	}
	
	/**
	 * 验证字符串长度
	 * @param chName 中文名称，允许为空
	 * @param text 需要验证的字符串
	 * @param i 字符串需要达到的长度
	 * @param agree 是否允许为空，true允许，false不允许
	 * @return 如果不满足条件，返回验证提示信息
	 */
	public static String stringX(String chName,String text,int i,boolean agree) {
		if(!agree && StringUtil.isBlank(text)) return "【" + chName + "】不允许为空.<br/>";
		if(!StringUtil.isStringX(text, i)) return "【" + chName + "】长度必须为" + i +"位.<br/>";
		return "";
	}
	
	/**
	 * 验证字符串长度
	 * @param chName 中文名称，允许为空
	 * @param text 需要验证的字符串
	 * @param i 字符串小于等于i个长度
	 * @param agree 是否允许为空，true允许，false不允许
	 * @return 如果不满足条件，返回验证提示信息
	 */
	public static String stringLQ(String chName,String text,int i,boolean agree) {
		if(!agree && StringUtil.isBlank(text)) return "【" + chName + "】不允许为空.<br/>";
		if(!StringUtil.isStringLQ(text, i)) return "【" + chName + "】长度不能大于" + i +"位.<br/>";
		return "";
	}
	
	/**
	 * 验证字符串长度
	 * @param chName 中文名称，允许为空
	 * @param text 需要验证的字符串
	 * @param i 字符串大于等于i个长度
	 * @param agree 是否允许为空，true允许，false不允许
	 * @return 如果不满足条件，返回验证提示信息
	 */
	public static String stringGQ(String chName,String text,int i,boolean agree) {
		if(!agree && StringUtil.isBlank(text)) return "【" + chName + "】不允许为空.<br/>";
		if(!StringUtil.isStringGQ(text, i)) return "【" + chName + "】长度不能少于" + i +"位.<br/>";
		return "";
	}
	
	/**
	 * 验证字符串是否存在特殊字符
	 * @param chName
	 * @param text
	 * @return
	 */
	public static String stringSpecial(String chName,String text) {
		String specialStr = StringUtil.stringSpecial(text);
		if(!StringUtil.isBlank(specialStr)) return "【" + chName + "】存在特殊字符：\"" + specialStr + "\"";
		return "";
	}
	
	
	/**
	 * 验证字符长度
	 * @param chName 中文名称，允许为空
	 * @param text 需要验证的字符串
	 * @param i 字符串需要达到的长度
	 * @param agree 是否允许为空，true允许，false不允许
	 * @return 如果不满足条件，返回验证提示信息
	 */
	public static String stringCharX(String chName,String text,int i,boolean agree) {
		if(!agree && StringUtil.isBlank(text)) return "【" + chName + "】不允许为空.<br/>";
		if(!StringUtil.isStringX(text, i)) return "【" + chName + "】长度必须为" + i +"个字符.<br/>";
		return "";
	}
	
	/**
	 * 验证字符长度
	 * @param chName 中文名称，允许为空
	 * @param text 需要验证的字符串
	 * @param i 字符串小于等于i个长度
	 * @param agree 是否允许为空，true允许，false不允许
	 * @return 如果不满足条件，返回验证提示信息
	 */
	public static String stringCharLQ(String chName,String text,int i,boolean agree) {
		if(!agree && StringUtil.isBlank(text)) return "【" + chName + "】不允许为空.<br/>";
		if(!StringUtil.isStringLQ(text, i)) return "【" + chName + "】长度不能大于" + i +"个字符.<br/>";
		return "";
	}
	
	/**
	 * 验证字符长度
	 * @param chName 中文名称，允许为空
	 * @param text 需要验证的字符串
	 * @param i 字符串大于等于i个长度
	 * @param agree 是否允许为空，true允许，false不允许
	 * @return 如果不满足条件，返回验证提示信息
	 */
	public static String stringCharGQ(String chName,String text,int i,boolean agree) {
		if(!agree && StringUtil.isBlank(text)) return "【" + chName + "】不允许为空.<br/>";
		if(!StringUtil.isStringGQ(text, i)) return "【" + chName + "】长度不能少于" + i +"个字符.<br/>";
		return "";
	}
	
}

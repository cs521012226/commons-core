package org.ltsh.core.core.util;

import java.io.UnsupportedEncodingException;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @date 2014年10月21日
 * @author Ych
 * @description 字符串工具类
 */
public class StringUtil {

	private static final char[] chars = buildChars(36);
	private static final char UNDER_LINE = '_';
	
	/**
	 * 构建[0-9][A-Z]的字符数组
	 * @author Ych
	 * @param len
	 * @return
	 */
	private static char[] buildChars(int len){
		char[] chars = new char[len];
		for (int i = 0; i < chars.length; i++) {
			if (i < 10) {
				chars[i] = (char) (i + 48); // 0-9数字
			} else {
				chars[i] = (char) (i + 55); // A-Z字母
			}
		}
		return chars;
	}
	
	/**
	 * 是否为空
	 * @author	Ych
	 * @param src
	 * @return
	 */
	public static boolean isBlank(Object o) {
		if(o == null){
			return true;
		}
        if(o instanceof String){
        	return ((String) o).trim().isEmpty();
        }
        if(o instanceof String[]){
        	return ((String[]) o) != null && ((String[]) o).length > 0 ? false : true;
        }
        if(o instanceof Collection){
        	return ((Collection<?>) o).isEmpty();
        }
        if(o instanceof Map){
        	return ((Map<?, ?>) o).isEmpty();
        }
        return false;
    }
	
	/**
	 * text为空返回default值
	 * @author Ych
	 * @param text
	 * @param defaultValue
	 * @return
	 */
	public static String blank(String text, String defaultValue) {
		if(!isBlank(text)){
			return text;
		}
		return defaultValue;
	}
	
	/**
	 * 匹配参数格式化
	 * <p>例：StringUtil.format("{0}-{1}-{2}","2014","12","21") 返回： "2014-12-21"</p>
	 * @author	Ych
	 * @param pattern
	 * @param args
	 * @return
	 */
	public static String format(String pattern, Object... args){
		return MessageFormat.format(pattern, args);
	}
	
	/**
	 * 去除左右空格
	 * @author	Ych
	 * @param o
	 * @return
	 */
	public static String trim(Object o){
		if(o == null){
			return "";
		}
		return o.toString().trim();
	}
	
	/**
	 * 首字母转小写
	 * @author Ych
	 * @param str
	 * @return
	 */
	public static String firstCharToLowerCase(String str){
		if(isBlank(str)){
			return str;
		}
		str = str.trim();
		int len = str.length();
		if(len == 1){
			return str.toLowerCase();
		}
		return str.substring(0, 1).toLowerCase() + str.substring(1);
	}
	
	/**
	 * 首字母转大写
	 */
	public static String firstCharToUpperCase(String str) {
		if(isBlank(str)){
			return str;
		}
		str = str.trim();
		int len = str.length();
		if(len == 1){
			return str.toUpperCase();
		}
		return str.substring(0, 1).toUpperCase() + str.substring(1);
	}
	
	/**
	 * 下划线转驼峰写法
	 * @author Ych
	 * @param str
	 * @return
	 */
	public static String underlineToCamelCase(String str) {
		if(isBlank(str)){
			return str;
		}
		
		char[] chrs = str.toCharArray();
		StringBuilder rs = new StringBuilder(chrs.length);
		boolean hasLine = false;
		for(char chr : chrs){
			if(chr == UNDER_LINE){	//下划线跳过
				hasLine = true;
				continue;
			}
			
			if(hasLine){	//下划线标识为true时把字符转大写
				chr = Character.toUpperCase(chr);
			}
			rs.append(chr);
			hasLine = false;
		}
		return rs.toString();
	}
	
	/**
	 * 驼峰写法转下划线
	 * @author Ych
	 * @param str
	 * @return
	 */
	public static String camelToUnderline(String str) {
		if(isBlank(str)){
			return str;
		}
		
		char[] chrs = str.toCharArray();
		StringBuilder rs = new StringBuilder(chrs.length);
		for(int i=0; i<chrs.length; i++){
			char chr = chrs[i];
			
			if(chr >= 'A' && chr <= 'Z'){	//大写字母后面加下划线
				if(i != 0 && chrs[i-1] != UNDER_LINE) {
					rs.append(UNDER_LINE);
				}
				chr = Character.toLowerCase(chr);
			}
			rs.append(chr);
		}
		return rs.toString();
	}
	
	/**
	 * 通过分隔符连接字符串数组
	 * @author Ych
	 * @param strArr		字符串数组
	 * @param separator		分隔符
	 * @return
	 */
	public static String join(String separator, Object... arr){
		StringBuilder sb = new StringBuilder();
		for(Object s : arr){
			sb.append(separator);
			sb.append(s);
		}
		if(sb.length() > 0){
			return sb.substring(separator.length());
		}
		return "";
	}
	
	/**
	 * 通过分隔符连接指定Len数目的字符串
	 * 例：join(",", "?", 3) => "?,?,?"
	 * @author Ych
	 * @param separator	连接字符
	 * @param str		字符串
	 * @param len	长度
	 * @return
	 */
	public static String join(String separator, String str, int len){
		StringBuilder sb = new StringBuilder();
		while(len-- > 0){
			sb.append(separator);
			sb.append(str);
		}
		if(sb.length() > 0){
			return sb.substring(separator.length());
		}
		return sb.toString();
	}
	
	
	/**
	 * 通过分隔符连接字符串列表
	 * @author Ych
	 * @param separator
	 * @param strArr
	 * @return
	 */
	public static String join(String separator, List<?> strArr){
		return join(separator, strArr.toArray());
	}
	
	/** 
     * 获取[0-9]|[A-Z]范围内随机产生的字符串
     * @param len 长度
     * @return 
     * @throws CoderException 
     */  
    public static String randomString(int len)  {  
    	return randomString("", len);
    }
    /** 
     * <h6>获取[0-9]|[A-Z]随机产生的字符串</h6>
     * @param prefix 前缀
     * @param len 长度
     * @return 
     * @throws CoderException 
     */  
    public static String randomString(String prefix, int len)  {  
		StringBuilder result = new StringBuilder(prefix);
		Random r = new Random();
		for (int i = 0; i < len; i++) {
			result.append(chars[r.nextInt(chars.length)]);
		}
		return result.toString();
    }
	
	/**
	 * <h6>左填充，多出len长度的不做处理，少于len长度的用padChar左填充直到等于len长度<h6>
	 * <ul><li>例1： leftPad("abcd", '0', 10) => "000000abcd"</li></ul>
	 * @author Ych
	 * @param text		文本
	 * @param padStr	填充字符
	 * @param len		填充长度
	 * @return
	 */
	public static String leftPad(String text, char padChar, int len){
		if(text.length() >= len){
			return text;
		}
		int remain = len - text.length();
		StringBuilder sb = new StringBuilder();
		while(remain-- > 0){
			sb.append(padChar);
		}
		sb.append(text);
		return sb.toString();
	}
	
	/**
	 * 获取32位UUID
	 * @return
	 */
	public static String UUID() {
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	/**
	 * 空值检查，返回不为空的第一个值
	 * @author Ych
	 * @param text
	 * @param defaults	
	 * @return
	 */
	public static String nvl(String text, String... defaults){
		if(!isBlank(text)){
			return text;
		}
		for(String t : defaults){
			if(!isBlank(t)){
				return t;
			}
		}
		return "";
	}
	
	/**
	 * 字符串简短模式，例：asdfsdf -> asdfs...
	 * @author Ych
	 * @param text	文本
	 * @param limit		限制的长度，如果超过该长度，使用简短模式
	 * @param suffix	简单模式的后缀
	 * @return
	 */
	public static String simple(String text, int limit, String suffix){
		if(isBlank(text)){
			return "";
		}
		return text.length() > limit ? text.substring(0, limit) + suffix : text;
	}
	
	/**
	 * 转码字符串
	 * @author YeChao
	 * @param text
	 * @param srcCharset
	 * @param targetCharset
	 * @return
	 */
	public static String encodeCharset(String text, String srcCharset, String targetCharset){
		try {
			return new String(text.getBytes(srcCharset), targetCharset);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return text;
		}
	}
	
	/**
	 * 是否是特殊字符
	 * @param text
	 * @return 返回存在的特殊字符，null则不存在特殊字符
	 */
	public static String stringSpecial(String text) {
		String matchArray[] = new String[] {"/","\\","<",">","?","!","@","#","$","%","^","&","*","'"};
		for (int i = 0; i < matchArray.length; i++) {
			if(text.contains(matchArray[i])) {
				return matchArray[i];
			}
		}
		return null;
	}
	/**
	 * 字符串字符长度
	 * @param text
	 * @return
	 */
	public static int stringCharLen(String text) {
		try {
			return text.getBytes("GBK").length;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return 0;
	}
	/**
	 * 判断字符串长度是否等于i位
	 * @param text
	 * @param i
	 * @return
	 */
	public static boolean isStringCharEQ(String text,int i) {
		if(stringCharLen(text) == i) {
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串【字符】长度是否小于等于i位
	 * @param text
	 * @param i
	 * @return
	 */
	public static boolean isStringCharLQ(String text,int i) {
		if(stringCharLen(text) <= i) {
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串长度是否大于等于i位
	 * @param text
	 * @param i
	 * @return
	 */
	public static boolean isStringCharGQ(String text,int i) {
		if(stringCharLen(text) >= i) {
			return true;
		}
		return false;
	}
	/**
	 * 字符串长度
	 * @param text
	 * @param i
	 * @return
	 */
	public static int stringLen(String text) {
		if(isBlank(text)) {
			return 0;
		}
		return text.length();
	}
	
	/**
	 * 判断字符串长度是否等于i位
	 * @param text
	 * @param i
	 * @return
	 */
	public static boolean isStringX(String text,int i) {
		if(stringLen(text) == i) {
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串长度是否小于等于i位
	 * @param text
	 * @param i
	 * @return
	 */
	public static boolean isStringLQ(String text,int i) {
		if(stringLen(text) <= i) {
			return true;
		}
		return false;
	}
	/**
	 * 判断字符串长度是否大于等于i位
	 * @param text
	 * @param i
	 * @return
	 */
	public static boolean isStringGQ(String text,int i) {
		if(stringLen(text) >= i) {
			return true;
		}
		return false;
	}
	/**
	 * 将字符串数组拼接为字符串，如：'1','2'
	 * @author 邓端宁
	 * @return
	 */
	public static String StringAppend(String [] text) {
		if(isBlank(text)){
			return "";
		}
		StringBuffer str = new StringBuffer("");
		for (int i = 0; i < text.length; i++) {
			str.append("'").append(text[i]).append("'").append(i < text.length - 1 ? "," : "");
		}
		return str.toString();
	}
	
	/**
	 * 字符串分割后转为list
	 * @author Ych
	 * @param text
	 * @param separator
	 * @return
	 */
	public static List<String> splitToList(String text, String separator) {
		if(isBlank(text)){
			return null;
		}
		return Arrays.asList(text.split(separator));
	}
	

	/**
	 * 字符串数组转为list
	 * @author 邓端宁
	 * @param text
	 * @param separator
	 * @return
	 */
	public static List<String> arrayToList(String text[]) {
		if(isBlank(text)){
			return null;
		}
		return Arrays.asList(text);
	}
}

package org.ltsh.core.core.util;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.ltsh.core.core.constants.Config;

/**
 * Properties工具类
 * @author Ych
 * 2017年6月19日
 */
public class PropertiesUtil {
	private static Map<String, String> PROPERTIES_MAP = new HashMap<String, String>();
	
	/**
	 * 读取文件内容
	 * @author Ych
	 * 2017年6月19日
	 * @param proFile
	 */
	private static void readData(File proFile){
		InputStreamReader in = null;
		try {
			if(!proFile.exists()){
				return ;
			}
			Properties p = new Properties();
			in = new InputStreamReader(new FileInputStream(proFile), Config.CHARSET);
			p.load(in);
			for(Object o : p.keySet()){
				String key = (String) o;
				if(key != null && p.getProperty(key) != null){
					PROPERTIES_MAP.put(key.trim(), p.getProperty(key).trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * 初始化配置信息
	 * @author Ych
	 * 2017年6月19日
	 * @param basePath		基础目录，从该目录开始搜索文件
	 * @param fileNamePattern	文件名称正则匹配表达式
	 */
	public static synchronized void initProperties(String basePath, final String fileNamePattern){
		PROPERTIES_MAP.clear();
		addProperties(basePath, fileNamePattern);
	}
	
	/**
	 * 添加配置信息
	 * @author Ych
	 * @param basePath
	 * @param fileNamePattern
	 */
	public static synchronized void addProperties(String basePath, final String fileNamePattern){
		File baseDir = new File(basePath);
		//获取匹配的文件
		List<File> matchFileLsit = FileUtil.getFiles(baseDir, new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().matches(fileNamePattern);
			}
		});
		
		for(File f : matchFileLsit){
			readData(f);
		}
	}
	
	/**
	 * 取得key值
	 * @author Ych
	 * 2017年6月19日
	 * @param key
	 * @return
	 */
	public static String get(String key){
		return PROPERTIES_MAP.get(key);
	}
	
	/**
	 * 取得key值
	 * @author Ych
	 * @param key
	 * @param defaultValue
	 * @return
	 */
	public static String get(String key, String defaultValue){
		String value = get(key);
		return value != null ? value : defaultValue;
	}
	/**
	 * 取得key值，转为int类型，如果没值，则返回defaults参数值
	 * @author Ych
	 * 2017年6月19日
	 * @param key		key
	 * @param defaults	默认值
	 * @return
	 */
	public static int getInt(String key, int defaults){
		String rs = get(key);
		try {
			return Integer.valueOf(rs);
		} catch (NumberFormatException e) {
			return defaults;
		}
	}
	/**
	 * 取得key值，转为boolean类型，如果没值，则返回defaults参数值
	 * @author Ych
	 * 2017年6月19日
	 * @param key
	 * @return
	 */
	public static boolean getBoolean(String key, boolean defaults){
		String rs = get(key);
		return rs == null ? defaults : Boolean.valueOf(rs);
	}
	
	/**
	 * 取得key值，转为long类型，如果没值，则返回defaults参数值
	 * @author Ych
	 * @param key
	 * @param defaults
	 * @return
	 */
	public static long getLong(String key, long defaults) {
		String value = get(key);
		try {
			return Long.parseLong(value);
		} catch (NumberFormatException e) {
			return defaults;
		}
	}
	
	/**
	 * 是否包含某个键
	 * @author Ych
	 * @param key
	 * @return
	 */
	public static boolean containsKey(String key) {
		return PROPERTIES_MAP.containsKey(key);
	}
}

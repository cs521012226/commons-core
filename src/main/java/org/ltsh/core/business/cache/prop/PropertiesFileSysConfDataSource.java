package org.ltsh.core.business.cache.prop;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.ltsh.core.core.constants.Config;
import org.ltsh.core.core.util.FileUtil;
import org.ltsh.core.core.util.StringUtil;

/**
 * Properties配置文件数据源
 * @author Ych
 * 2018年3月2日
 */
public class PropertiesFileSysConfDataSource implements SysConfDataSource {
	
	private List<Properties> propList = new ArrayList<Properties>();
	
	public PropertiesFileSysConfDataSource(){
		
	}
	
	public PropertiesFileSysConfDataSource(String basePath, String fileNamePattern){
		addProperties(basePath, fileNamePattern);
	}
	
	/**
	 * 添加配置信息文件
	 * @author Ych
	 * @param basePath		文件根目录
	 * @param fileNamePattern	文件名称正则匹配
	 */
	public void addProperties(String basePath, final String fileNamePattern){
		File baseDir = new File(basePath);
		//获取匹配的properties文件
		List<File> matchFileList = FileUtil.getFiles(baseDir, new FileFilter() {
			@Override
			public boolean accept(File file) {
				return file.getName().matches(fileNamePattern);
			}
		});
		//加载properties文件
		for(File file : matchFileList){
			InputStreamReader in = null;
			try {
				Properties p = new Properties();
				in = new InputStreamReader(new FileInputStream(file), Config.CHARSET);
				p.load(in);
				propList.add(p);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				FileUtil.close(in);
			}
		}
	}
	public void addProperties(String fileName){
		//加载properties文件
		InputStream in = null;
		try {
			Properties p = new Properties();
			in = PropertiesFileSysConfDataSource.class.getClassLoader().getResourceAsStream(fileName);
//				in = new InputStreamReader(new FileInputStream(file), Config.CHARSET);
			p.load(in);
			propList.add(p);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			FileUtil.close(in);
		}
	}
	
	/**
	 * 清空配置文件列表
	 * TODO
	 * @author Ych
	 */
	public void cleanProperties(){
		propList.clear();
	}
	
	/**
	 * 获取键值
	 * @author Ych
	 * @param key
	 * @return
	 */
	public String get(String key){
		for (int i = propList.size() - 1; i >= 0; i--) {
			String value = propList.get(i).getProperty(key);
			if(!StringUtil.isBlank(value)){
				return value;
			}
		}
		return null;
	}

	/**
	 * 取得key值，转为int类型
	 * @author Ych
	 * 2017年6月19日
	 * @param key		key
	 * @return
	 */
	public int getInt(String key){
		String rs = get(key);
		return Integer.valueOf(rs);
	}
	/**
	 * 取得key值，转为boolean类型
	 * @author Ych
	 * 2017年6月19日
	 * @param key
	 * @return
	 */
	public boolean getBoolean(String key){
		String rs = get(key);
		return Boolean.valueOf(rs);
	}
	
	/**
	 * 取得key值，转为long类型，如果没值，则返回defaults参数值
	 * @author Ych
	 * @param key
	 * @return
	 */
	public long getLong(String key) {
		String value = get(key);
		return Long.parseLong(value);
	}
	
	@Override
	public SysConfInfo getSysConf(String key) {
		String value = get(key);
		SysConfInfo rs = null;
		if(!StringUtil.isBlank(value)){
			rs = new SysConfInfo();
			rs.setKey(key);
			rs.setValue(value);
			return rs;
		}
		return rs;
	}

}

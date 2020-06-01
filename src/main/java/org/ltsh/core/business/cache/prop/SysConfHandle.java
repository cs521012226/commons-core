package org.ltsh.core.business.cache.prop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ltsh.core.business.exp.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 系统配置参数集合处理类
 * @author Ych
 * 2018年2月8日
 */
public class SysConfHandle {
	private Logger log = LoggerFactory.getLogger(getClass());
	private List<SysConfDataSource> list = new ArrayList<SysConfDataSource>();
	private Map<String, SysConfInfo> source = new HashMap<String, SysConfInfo>();
	
	private static SysConfHandle SINGLE = new SysConfHandle();
	
	private SysConfHandle(){}
	
	public static SysConfHandle getInstance(){
		return SINGLE;
	}
	
	/**
	 * 添加数据来源实例
	 * @author Ych
	 * @param source
	 */
	public synchronized SysConfHandle addSource(SysConfDataSource source){
		list.add(source);
		return this;
	}
	/**
	 * 添加数据来源实例
	 * @author Ych
	 * @param source
	 */
	public synchronized SysConfHandle removeSource(SysConfDataSource source){
		list.remove(source);
		return this;
	}
	
	/**
	 * 清空指定数据缓存
	 * @author Ych
	 * @param code
	 * @return
	 */
	public synchronized SysConfHandle clearCache(String key){
		source.remove(key);
		return this;
	}
	
	/**
	 * 清空全部数据缓存
	 * @author Ych
	 * @param code
	 * @return
	 */
	public synchronized SysConfHandle clearCacheAll(){
		source.clear();
		return this;
	}
	
	/**
	 * 根据参数编码获取相关参数
	 * @param key 参数编码
	 * @return
	 */
	public String get(String key){
		//从缓存获取配置信息
		SysConfInfo data = source.get(key);
		
		//如果缓存里面没，就从数据源处加载
		if(data == null){
			loadData(key);
			data = source.get(key);
			if(data == null){	//如果从数据源都获取不了，那抛异常
				BusinessException.err("无系统配置" + key + "的值");
			}
		}
		return data.getValue();
	}
	
	/**
	 * 取得key值，转为int类型
	 * @author Ych
	 * 2017年6月19日
	 * @param key		key
	 * @param defaults	默认值
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
	 * @param defaults
	 * @return
	 */
	public long getLong(String key) {
		String value = get(key);
		return Long.parseLong(value);
	}
	
	
	/**
	 * 加载数据
	 * @author Ych
	 * @param groups
	 */
	private synchronized void loadData(String key){
		SysConfInfo temp = source.get(key);
		
		if(temp != null){
			return ;
		}
		for (SysConfDataSource query : list) {
			temp = query.getSysConf(key);
			if(temp != null){
				source.put(temp.getKey(), temp);
			}
		}
		if(temp != null){
			log.info("成功加载系统参数配置（缓存）: " + temp.getKey() + " = " + temp.getValue());
		}
	}
}

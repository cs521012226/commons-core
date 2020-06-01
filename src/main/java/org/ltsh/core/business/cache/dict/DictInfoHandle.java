package org.ltsh.core.business.cache.dict;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ltsh.core.core.util.StringUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 数据字典通用处理类
 * @author Ych
 * 2018年1月26日
 */
public class DictInfoHandle {
	private Logger log = LoggerFactory.getLogger(getClass());
	private List<DictInfoDataSource> list = new ArrayList<DictInfoDataSource>();
	private Map<String, List<DictInfo>> source = new HashMap<String, List<DictInfo>>();
	
	private static DictInfoHandle SINGLE = new DictInfoHandle();
	
	private DictInfoHandle(){}
	
	public static DictInfoHandle getInstance(){
		return SINGLE;
	}
	
	/**
	 * 添加数据来源实例
	 * @author Ych
	 * @param source
	 */
	public synchronized DictInfoHandle addSource(DictInfoDataSource source){
		list.add(source);
		return this;
	}
	
	/**
	 * 清空数据源来源实例
	 * @author Ych
	 * @return
	 */
	public synchronized DictInfoHandle clearSource(){
		list.clear();
		return this;
	}
	
	/**
	 * 清空所有数据缓存
	 * @author Ych
	 * @return
	 */
	public synchronized DictInfoHandle clearCache(){
		source.clear();
		return this;
	}
	
	/**
	 * 清空指定数据缓存
	 * @author Ych
	 * @param code
	 * @return
	 */
	public synchronized DictInfoHandle clearCache(String code){
		source.remove(code);
		return this;
	}
	
	/**
	 * 根据参数编码获取相关参数
	 * @param key 参数编码
	 * @return
	 */
	public List<DictInfo> getByCode(String code){
		List<DictInfo> data = source.get(code);
		
		//如果缓存里面没，就从数据源处加载
		if(data == null || data.isEmpty()){
			List<String> codeList = new ArrayList<String>(1);
			codeList.add(code);
			loadData(codeList);
			data = source.get(code);
		}
		return data;
	}
	
	
	/**
	 * 根据参数编码和参数值 获取相关参数
	 * @author 谢明才
	 * @return code 数据字典类型
	 */
	public List<DictInfo> getByCodeAndCfgValue(String code, List<String> cfgValueList){
		List<DictInfo> data = getByCode(code);
		List<DictInfo> resultData = new ArrayList<DictInfo>();
		for (DictInfo dictInfo : data) {
			for (int i = 0; i < cfgValueList.size(); i++) {
				if(dictInfo.getValue().equals(cfgValueList.get(i))) {
					resultData.add(dictInfo);
					break;
				}
			}
		}
		return resultData;
	}
	
	/**
	 * 获取单个参数值，如果是列表则返回第一个
	 * @param code
	 * @param value
	 * @return
	 */
	public DictInfo getByCodeForOne(String code){
		List<DictInfo> list = getByCode(code);
		DictInfo sp = null; 
		if(list != null && !list.isEmpty()){
			sp = list.get(0);
		}
		return sp;
	}
	
	/**
	 * 获取指定参数详细信息 
	 * @param code
	 * @param value
	 * @return
	 */
	public DictInfo getByCodeAndValue(String code, String value){
		if(StringUtil.isBlank(value)){
			return null;
		}
		value = value.trim();
		List<DictInfo> list = getByCode(code);
		DictInfo sp = null; 
		if(list != null && !list.isEmpty()){
			for (DictInfo info : list) {
				if(info.getValue().trim().equals(value)){
					sp = info;
					break;
				}
			}
		}
		return sp;
	}
	
	/**
	 * 获取指定编码的参数集合
	 * @param codes
	 * @return
	 */
	public Map<String, List<DictInfo>> getByCodes(String[] codes){
		Map<String, List<DictInfo>> rs = new HashMap<String, List<DictInfo>>();
		if(codes == null){
			return rs;
		}
		List<String> noCacheGroups = new ArrayList<String>();	//没缓存的group列表
		
		for(String group : codes){
			List<DictInfo> temp = source.get(group);
			if(temp == null){
				noCacheGroups.add(group);	//加进非缓存列表
			}else{
				rs.put(group, temp);
			}
		}
		if(!noCacheGroups.isEmpty()){
			loadData(noCacheGroups);
			for(String group : noCacheGroups){
				List<DictInfo> temp = source.get(group);
				rs.put(group, temp);
			}
		}
		return rs;
	}
	
	/**
	 * 传入code、value，获取name的值
	 * @author YeChao
	 * 2017年5月15日
	 * @param code
	 * @param value
	 * @return
	 */
	public String formatDisplay(String code, String value){
		DictInfo rs = getByCodeAndValue(code, value);
		return rs == null ? "" : rs.getName();
	}
	
	/**
	 * 传入code、name，获取value的值
	 * @author YeChao
	 * 2017年5月15日
	 * @param code
	 * @param value
	 * @return
	 */
	public String formatDisplayByName(String code, String name){
		List<DictInfo> list = getByCode(code);
		String value = "";
		
		if(list != null){
			for(DictInfo i : list){
				if(i.getName().equals(name)){
					if(i.isUsed()){
						value = i.getValue();
					}else if(StringUtil.isBlank(value)){
						value = i.getValue();
					}
				}
			}
		}
		return value;
	}
	
	/**
	 * 加载数据
	 * @author Ych
	 * @param groups
	 */
	private synchronized void loadData(List<String> groups){
		List<String> noCacheGroups = new ArrayList<String>();	//没缓存的group列表
		
		for(String group : groups){
			List<DictInfo> temp = source.get(group);
			if(temp == null){
				noCacheGroups.add(group);	//加进非缓存列表
			}
		}
		
		List<DictInfo> temp = null;
		
		if(!noCacheGroups.isEmpty()){
			log.info("开始加载数据字典（缓存）, groups = " + groups);
			
			for(DictInfoDataSource query : list){
				// 处理新数据并加入内存缓存
				List<DictInfo> all = query.queryList(noCacheGroups);
				if(all != null){
					for(DictInfo info : all){
						String code = info.getCode();
						temp = source.get(code);
						
						if(temp == null){
							temp = new ArrayList<DictInfo>();
							source.put(code, temp);
						}
						temp.add(info);
					}
				}
			}
			log.info("加载数据字典（缓存）完毕");
		}
	}
}

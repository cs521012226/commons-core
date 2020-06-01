package org.ltsh.core.business.cache.dict;

import com.alibaba.fastjson.JSON;

/**
 * 数据字典
 * @author Ych
 * 2017年5月15日
 */
public class DictInfo {

	private String code;
	private String name;
	private String value;
	private boolean used;
	
	public DictInfo(String code, String name, String value){
		this.code = code;
		this.name = name;
		this.value = value;
	}
	
	public DictInfo(String code, String name, String value, boolean used){
		this.code = code;
		this.name = name;
		this.value = value;
		this.used = used;
	}
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}

}

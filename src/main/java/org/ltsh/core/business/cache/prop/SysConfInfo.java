package org.ltsh.core.business.cache.prop;

import com.alibaba.fastjson.JSON;

/**
 * 系统配置信息实体类
 * @author Ych
 * 2018年2月8日
 */
public class SysConfInfo {
	private String key;
	private String value;
	private String description;
	
	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	public String getValue() {
		return value;
	}
	public void setValue(String value) {
		this.value = value;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}

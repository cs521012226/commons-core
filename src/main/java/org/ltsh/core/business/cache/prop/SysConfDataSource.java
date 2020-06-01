package org.ltsh.core.business.cache.prop;


/**
 * 系统参数配置接口
 * @author Ych
 * 2018年2月8日
 */
public interface SysConfDataSource {
	
	/**
	 * 获取列表接口
	 * @author Ych
	 * @return
	 */
	SysConfInfo getSysConf(String key);

}

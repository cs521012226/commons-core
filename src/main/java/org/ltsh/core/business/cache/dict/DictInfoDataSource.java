package org.ltsh.core.business.cache.dict;

import java.util.List;

/**
 * 数据字典查询接口
 * @author Ych
 * 2017年5月15日
 */
public interface DictInfoDataSource {
	/**
	 * 查询指定code字典列表
	 * @author Ych
	 * @param code
	 * @return
	 */
	public List<DictInfo> queryList(List<String> code);
}

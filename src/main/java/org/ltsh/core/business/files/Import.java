package org.ltsh.core.business.files;

import java.util.List;


/**
 * 导入接口
 * @author Ych
 * 2017年5月17日
 */
public interface Import<T> {
	
	/**
	 * 写数据
	 * @author Ych
	 * 2017年5月17日
	 */
	public List<T> readData();
	
	
}

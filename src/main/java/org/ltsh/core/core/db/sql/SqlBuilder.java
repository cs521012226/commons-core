package org.ltsh.core.core.db.sql;

/**
 * SQL构造器
 * @author Ych
 * 2017年5月16日
 */
public interface SqlBuilder {

	/**
	 * 获取脚本
	 * @author Ych
	 * 2017年5月16日
	 * @return
	 */
	public String getSql();
	
	/**
	 * 获取参数数组对象
	 * @author Ych
	 * 2017年5月16日
	 * @return
	 */
	public Object[] getParam();
	
}

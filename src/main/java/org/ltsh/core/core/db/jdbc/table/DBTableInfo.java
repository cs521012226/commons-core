package org.ltsh.core.core.db.jdbc.table;

import java.util.List;

import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.db.jdbc.bean.DBTableColumn;

/**
 * 数据库表信息
 * @author Ych
 * 2018年4月28日
 */
public interface DBTableInfo {
	
	/**
	 * 构建、组装表信息实体
	 * @author Ych
	 * @return
	 */
	List<DBTable> getTableInfo(boolean refresh);
	
	/**
	 * 数据库字段类型转换
	 * @author Ych
	 * @return
	 */
	String dataTypeMapper(DBTableColumn dbDataColumn);
	
	/**
	 * 数据库字段JDBCTYPE类型转换
	 * @author Ych
	 * @return
	 */
	String jdbcTypeMapper(DBTableColumn dbDataColumn);

}

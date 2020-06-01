package org.ltsh.core.core.db.sql;

import java.util.LinkedHashMap;
import java.util.Map;

import org.ltsh.core.core.util.StringUtil;

/**
 * SQL Insert构建器
 * @author Ych
 * 2018年3月19日
 */
public class InsertSqlBuilder extends BaseSqlBuilder{
	
	/**
	 * 字段键值对
	 */
	private Map<String, Object> values = new LinkedHashMap<String, Object>();

	public InsertSqlBuilder(String tableName) {
		super(tableName);
	}

	@Override
	public String toString() {
		StringBuilder sql = new StringBuilder("{");
		sql.append("sql : \"").append(getSql());
		sql.append("\", param : [").append(StringUtil.join(DOT, getParam())).append("]}");
		return sql.toString();
	}



	@Override
	public String getSql() {
		if(values.isEmpty()){
			throw new IllegalArgumentException("没设置所需插入的值");
		}
		
		StringBuilder fieldStr = new StringBuilder();
		StringBuilder valueStr = new StringBuilder();
		
		for(String key : values.keySet()){
			fieldStr.append(DOT).append(key);
			valueStr.append(DOT).append(PLACE_HD);
		}
		StringBuilder sql = new StringBuilder("insert into ");
		sql.append(tableName).append("(");
		sql.append(fieldStr.substring(DOT.length())).append(") values (");
		sql.append(valueStr.substring(DOT.length())).append(")");
		
		return sql.toString();
	}

	@Override
	public Object[] getParam() {
		Object[] param = new Object[values.size()];
		int i=0; 
		for(Map.Entry<String, Object> m : values.entrySet()){
			param[i] = m.getValue();
			i++;
		}
		return param;
	}
	
	/**
	 * 设置字段
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public InsertSqlBuilder values(String key, Object value){
		values.put(key, value);
		return this;
	}
	
}

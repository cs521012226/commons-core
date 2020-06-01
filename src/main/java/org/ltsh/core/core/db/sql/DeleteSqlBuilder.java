package org.ltsh.core.core.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.ltsh.core.core.util.StringUtil;

/**
 * SQL Delete构建器
 * @author Ych
 * 2018年3月19日
 */
public class DeleteSqlBuilder extends BaseSqlBuilder{
	
	private List<String> setterSql = new ArrayList<String>();
	private List<String> whereSql = new ArrayList<String>();
	
	private List<Object> param = new ArrayList<Object>();

	public DeleteSqlBuilder(String tableName) {
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
		if(param.isEmpty()){
			throw new IllegalArgumentException("没设置所需的值");
		}
		
		StringBuilder sql = new StringBuilder("delete from ");
		sql.append(tableName);
		sql.append(StringUtil.join(DOT, setterSql));
		sql.append(" where ");
		sql.append(StringUtil.join(AND, whereSql));
		return sql.toString();
	}

	@Override
	public Object[] getParam() {
		return param.toArray();
	}
	
	/**
	 * 设置where条件“=”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteSqlBuilder eq(String key, Object value){
		whereSql.add(key + EQ + PLACE_HD);
		param.add(value);
		return this;
	}
	/**
	 * 设置where条件“<=”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteSqlBuilder leq(String key, Object value){
		whereSql.add(key + LEQ + PLACE_HD);
		param.add(value);
		return this;
	}
	/**
	 * 设置where条件“>=”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteSqlBuilder geq(String key, Object value){
		whereSql.add(key + GEQ + PLACE_HD);
		param.add(value);
		return this;
	}
	
	/**
	 * 设置where条件“like”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteSqlBuilder like(String key, Object value){
		whereSql.add(key + LIKE + PLACE_HD);
		param.add(value);
		return this;
	}
	/**
	 * 设置where条件“<>”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteSqlBuilder notEq(String key, Object value){
		whereSql.add(key + NOT_EQ + PLACE_HD);
		param.add(value);
		return this;
	}
	/**
	 * 设置where条件“between”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteSqlBuilder between(String key, String valueOne, String valueTwo){
		whereSql.add(key + BETWEEN + valueOne + AND + valueTwo);
		param.add(valueOne);
		param.add(valueTwo);
		return this;
	}
	/**
	 * 设置where条件“in”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public DeleteSqlBuilder in(String key, List<String> value){
		if(value == null || value.isEmpty()){
			return this;
		}
		whereSql.add(key + IN + " (" + StringUtil.join(",", PLACE_HD, value.size()) + ")");
		param.addAll(value);
		return this;
	}
}

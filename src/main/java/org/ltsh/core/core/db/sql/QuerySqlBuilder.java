package org.ltsh.core.core.db.sql;

import java.util.ArrayList;
import java.util.List;

import org.ltsh.core.core.util.StringUtil;
import org.ltsh.core.core.util.TypeConvert;

/**
 * SQL Select构建器
 * @author Ych
 * 2018年3月19日
 */
public class QuerySqlBuilder extends BaseSqlBuilder{
	
	private List<String> conditionList = new ArrayList<String>();
	private List<String> fieldNameList = new ArrayList<String>();
	
	private List<Object> param = new ArrayList<Object>();
	private String selectSQL;
	private String whereSQL;
	private String orderBy = "";

	public QuerySqlBuilder(String tableName) {
		super(tableName);
	}
	public QuerySqlBuilder(String selectSQL, String whereSQL) {
		super(null);
		this.selectSQL = selectSQL;
		this.whereSQL = whereSQL;
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
		return getSelectSql() + getWhereSql();
	}

	@Override
	public Object[] getParam() {
		return param.toArray();
	}
	
	/**
	 * 获取select语句前段
	 * @author Ych
	 * @return
	 */
	public String getSelectSql(){
		if(selectSQL != null){
			return selectSQL;
		}
		StringBuilder sql = new StringBuilder(SELECT);
		if(fieldNameList.isEmpty()){
			sql.append(" * ");
		}else{
			sql.append(StringUtil.join(DOT, fieldNameList));
		}
		return sql.toString();
	}
	
	/**
	 * 获取where语句后端
	 * @author Ych
	 * @return
	 */
	public String getWhereSql(){
		StringBuilder sql = new StringBuilder();
		if(whereSQL != null){
			sql.append(" ").append(whereSQL);
		}else{
			sql.append(FROM).append(tableName);
		}
		if(!conditionList.isEmpty()){
			sql.append(WHERE);
			sql.append(StringUtil.join(AND, conditionList));
		}
		sql.append(orderBy);
		return sql.toString();
	}
	
	/**
	 * 添加查询字段
	 * @author Ych
	 * @param fieldName
	 * @return
	 */
	public QuerySqlBuilder addField(String fieldName){
		fieldNameList.add(fieldName);
		return this;
	}
	
	/**
	 * 设置where条件“=”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder eq(String key, Object value){
		if(!StringUtil.isBlank(value)){
			conditionList.add(key + EQ + PLACE_HD);
			param.add(value);
		}
		return this;
	}
	/**
	 * 设置where条件“<=”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder leq(String key, Object value){
		if(!StringUtil.isBlank(value)){
			conditionList.add(key + LEQ + PLACE_HD);
			param.add(value);
		}
		return this;
	}
	/**
	 * 设置where条件“<”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder lt(String key, Object value){
		if(!StringUtil.isBlank(value)){
			conditionList.add(key + LT + PLACE_HD);
			param.add(value);
		}
		return this;
	}
	/**
	 * 设置where条件“>=”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder geq(String key, Object value){
		if(!StringUtil.isBlank(value)){
			conditionList.add(key + GEQ + PLACE_HD);
			param.add(value);
		}
		return this;
	}
	/**
	 * 设置where条件“>”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder gt(String key, Object value){
		if(!StringUtil.isBlank(value)){
			conditionList.add(key + GT + PLACE_HD);
			param.add(value);
		}
		return this;
	}
	/**
	 * 设置where条件“like”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder like(String key, Object value){
		if(!StringUtil.isBlank(value)){
			conditionList.add(key + LIKE + PLACE_HD);
			param.add(value);
		}
		return this;
	}
	/**
	 * 设置where条件“<>”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder notEq(String key, Object value){
		if(!StringUtil.isBlank(value)){
			conditionList.add(key + NOT_EQ + PLACE_HD);
			param.add(value);
		}
		return this;
	}
	
	/**
	 * 设置where条件“in”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder in(String key, List<String> value){
		if(value == null || value.isEmpty()){
			return this;
		}
		conditionList.add(key + IN + " (" + StringUtil.join(",", PLACE_HD, value.size()) + ")");
		param.addAll(value);
		return this;
	}
	
	/**
	 * 设置where条件“in”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder in(String key, String... value){
		return in(key, TypeConvert.toList(value));
	}
	/**
	 * 设置where条件“in”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder notIn(String key, List<String> value){
		if(value == null || value.isEmpty()){
			return this;
		}
		conditionList.add(key + NOT_IN + " (" + StringUtil.join(",", PLACE_HD, value.size()) + ")");
		param.addAll(value);
		return this;
	}
	/**
	 * 设置where条件“in”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder notIn(String key, String... value){
		return notIn(key, TypeConvert.toList(value));
	}
	/**
	 * 设置where条件“between”
	 * @author Ych
	 * @param key
	 * @param value
	 * @return
	 */
	public QuerySqlBuilder between(String key, String valueOne, String valueTwo){
		conditionList.add(key + BETWEEN + valueOne + AND + valueTwo);
		param.add(valueOne);
		param.add(valueTwo);
		return this;
	}
	
	
	public QuerySqlBuilder orderBy(String orderBy){
		this.orderBy = " " + orderBy;
		return this;
	}
}

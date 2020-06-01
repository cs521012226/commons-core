package org.ltsh.core.core.db.sql;


/**
 * SQL构建器基础父类
 * @author Ych
 * 2018年3月19日
 */
public abstract class BaseSqlBuilder implements SqlBuilder{
	/**
	 * 表名
	 */
	protected String tableName;
	
	protected final String AND = " and ";
	protected final String EQ = " = ";
	protected final String LEQ = " <= ";
	protected final String LT = " < ";
	protected final String GEQ = " >= ";
	protected final String GT = " > ";
	protected final String NOT_EQ = " <> ";
	protected final String LIKE = " like ";
	protected final String IN = " in ";
	protected final String NOT_IN = " not in ";
	protected final String BETWEEN = " between ";
	
	protected final String DOT = ",";
	protected final String PLACE_HD = "?";
	
	protected final String WHERE = " where ";
	protected final String FROM = " from ";
	protected final String SELECT = "select ";
	
	public BaseSqlBuilder(String tableName){
		this.tableName = tableName;
	}
}

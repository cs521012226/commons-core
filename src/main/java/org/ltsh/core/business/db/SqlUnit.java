package org.ltsh.core.business.db;

import java.util.List;

import org.ltsh.core.core.util.StringUtil;
import org.ltsh.core.core.util.TypeConvert;

/**
 * sql语句存储单元
 * @author Ych
 * 2017年6月9日
 */
public class SqlUnit {
	
	private String selectSql;
	private String whereSql;
	private Object[] param;
	
	public SqlUnit(String selectSql, String whereSql, Object[] param){
		this.selectSql = selectSql;
		this.whereSql = whereSql;
		this.param = param;
	}
	
	public SqlUnit(String sql, Object[] param){
		this.selectSql = sql;
		this.param = param;
	}
	
	public String getSql(){
		if(StringUtil.isBlank(selectSql)){
			selectSql = "";
		}
		if(StringUtil.isBlank(whereSql)){
			whereSql = "";
		}
		return selectSql + whereSql;
	}

	public String getSelectSql() {
		return selectSql;
	}

	public void setSelectSql(String selectSql) {
		this.selectSql = selectSql;
	}

	public String getWhereSql() {
		return whereSql;
	}

	public void setWhereSql(String whereSql) {
		this.whereSql = whereSql;
	}

	public Object[] getParam() {
		return param;
	}
	
	public List<Object> getParamToList(){
		return TypeConvert.toList(param);
	}

	public void setParam(Object[] param) {
		this.param = param;
	}
}

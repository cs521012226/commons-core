package org.ltsh.core.core.db.jdbc.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * 表信息
 *
 */
public class DBTable {
	private String tableSchema;
	private String tableName;
	private String tableComment;	//表注释
	private List<DBTableColumn> columns = new ArrayList<DBTableColumn>();	// 列信息
	
	public DBTable(String tableName) {
		this.tableName = tableName;
	}
	
	public DBTable(String tableName, List<DBTableColumn> columns) {
		this.tableName = tableName;
		this.columns = columns;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("{");
		sb.append("tableName:").append(tableName);
		sb.append(",columns:").append(columns);
		sb.append("}");
		return sb.toString();
	}

	
	public String getTableSchema() {
		return tableSchema;
	}

	public void setTableSchema(String tableSchema) {
		this.tableSchema = tableSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public List<DBTableColumn> getColumns() {
		return columns;
	}

	public void setColumns(List<DBTableColumn> columns) {
		this.columns = columns;
	}

	public String getTableComment() {
		return tableComment;
	}

	public void setTableComment(String tableComment) {
		this.tableComment = tableComment;
	}
	
}

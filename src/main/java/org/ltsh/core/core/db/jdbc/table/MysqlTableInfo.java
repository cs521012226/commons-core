package org.ltsh.core.core.db.jdbc.table;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.ltsh.core.core.db.jdbc.bean.DBConnector;
import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.db.jdbc.bean.DBTableColumn;
import org.ltsh.core.core.db.jdbc.bean.PreparedStatementCallback;
import org.ltsh.core.core.util.StringUtil;

/**
 * MYSQL数据库表获取实现
 * @author Ych
 * 2018年4月28日
 */
public class MysqlTableInfo implements DBTableInfo{
	
	private static String DB_DRIVER_NAME = "com.mysql.jdbc.Driver";
	private static String DB_URL = "jdbc:mysql://{host}:{port}/{sid}?useUnicode=true&amp;characterEncoding=utf-8";
	
	private String schemaPattern;
	private String tableNamePattern;
	private DBConnector dbConnector;
	
	private List<DBTable> tables;
	
	public MysqlTableInfo(String host, int port, String sid, String username, String password) throws SQLException{
		String url = DB_URL
				.replaceAll("\\{host\\}", host)
				.replaceAll("\\{port\\}", String.valueOf(port))
				.replaceAll("\\{sid\\}", sid);
		this.schemaPattern = sid;
		this.dbConnector = new DBConnector(DB_DRIVER_NAME, url, username, password);
	}

	@Override
	public List<DBTable> getTableInfo(boolean refresh) {
		if(tables == null || refresh){
			tables = findTables();
		}
		return tables;
	}
	
	private List<DBTable> findTables(){
		try {
			StringBuilder sql = new StringBuilder("select ");
			sql.append("c.table_schema");
			sql.append(",c.table_name");
			sql.append(",c.column_name");
			sql.append(",c.data_type");
			sql.append(",c.character_maximum_length");
			sql.append(",c.numeric_precision");
			sql.append(",c.numeric_scale");
			sql.append(",c.is_nullable");
			sql.append(",c.column_comment");
			sql.append(",t.table_comment");
			
			sql.append(" from information_schema.columns c");
			sql.append(" inner join information_schema.tables t on c.table_schema = t.table_schema and c.table_name = t.table_name");
			sql.append(" where 1=1");
			
			final String AND = " and ";
			final String OR = " or ";
			final String SEP = ",";
			final String PERC = "%";
			if(schemaPattern.contains(SEP)){
				String[] schemaPatterns = schemaPattern.split(SEP);
				sql.append(" and c.table_schema in ('").append(StringUtil.join("','", schemaPatterns)).append("')");
			}else {
				sql.append(" and c.table_schema = '").append(schemaPattern.toUpperCase()).append("'");
			}
			
			
			if(!StringUtil.isBlank(tableNamePattern)){
				tableNamePattern = tableNamePattern.toUpperCase();
				
				if(tableNamePattern.contains(SEP)){
					String[] tableNamePatterns = tableNamePattern.split(SEP);
					StringBuilder inSb = new StringBuilder();
					StringBuilder likeSb = new StringBuilder();
					
					
					for(String tnp : tableNamePatterns){
						tnp = tnp.toUpperCase();
						
						if(tnp.contains(PERC)){
							likeSb.append(OR).append("c.table_name like '").append(tnp).append("'");
						}else{
							inSb.append(SEP).append("'").append(tnp).append("'");
						}
					}
					sql.append(AND);
					if(inSb.length() > 0 && likeSb.length() > 0){
						sql.append("(");
						sql.append("c.table_name in (").append(inSb.substring(SEP.length())).append(")");
						sql.append(likeSb);
						sql.append(")");
					}else if(inSb.length() > 0){
						sql.append("c.table_name in (").append(inSb.substring(SEP.length())).append(")");
					}else if(likeSb.length() > 0){
						sql.append("(");
						sql.append(likeSb.substring(OR.length()));
						sql.append(")");
					}
					
				}else if(tableNamePattern.contains(PERC)){
					sql.append(" and c.table_name like '").append(tableNamePattern).append("'");
				}else{
					sql.append(" and c.table_name = '").append(tableNamePattern).append("'");
				}
			}
			sql.append(" order by c.table_schema,c.table_name,c.ordinal_position");
			
			return dbConnector.execute(sql.toString(), new PreparedStatementCallback<List<DBTable>>(){

				@Override
				public List<DBTable> doInPreparedStatement(PreparedStatement ps)
						throws SQLException {
					
					ResultSet rs = ps.executeQuery();
					
					List<DBTable> dbTableList = new ArrayList<DBTable>(); 
					DBTable table = null;
					
			        while(rs.next()){
			        	String tableName = rs.getString("table_name");
			        	if(table == null){
			        		table = new DBTable(tableName);
			        		dbTableList.add(table);
			        	}
			        	
			        	if(!tableName.equals(table.getTableName())){
			        		table = new DBTable(tableName);
			        		dbTableList.add(table);
		        		}
			        	
			        	String columnName = rs.getString("column_name"); 
		        		String dataType = rs.getString("data_type"); 
		        		int length = rs.getInt("character_maximum_length"); 
		        		int precision = rs.getInt("numeric_precision"); 
		        		int scale = rs.getInt("numeric_scale"); 
		        		boolean nullable = "YES".equalsIgnoreCase(rs.getString("is_nullable")) ? true : false;
		        		String columnComment = rs.getString("column_comment"); 
		        		String tableComment = rs.getString("table_comment"); 
		        		
		        		DBTableColumn tableColumn = new DBTableColumn();
		        		tableColumn.setColumnName(columnName);
		        		tableColumn.setDataType(dataType);
		        		tableColumn.setLength(length);
		        		tableColumn.setPrecision(precision);
		        		tableColumn.setScale(scale);
		        		tableColumn.setNullable(nullable);
		        		tableColumn.setComment(columnComment);
		        		
		        		table.setTableComment(tableComment);
		        		table.getColumns().add(tableColumn);
			        }
			        rs.close();
					return dbTableList;
				}
			});
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@Override
	public String dataTypeMapper(DBTableColumn dbDataColumn) {
		String type = dbDataColumn.getDataType().toLowerCase();
		
		if("date datetime time timestamp".contains(type)){
			return Date.class.getName();
		}else if("tinyint mediumint int integer".contains(type)){
			return Integer.class.getSimpleName();
		}else if("smallint".contains(type)){
			return Short.class.getSimpleName();
		}else if("bigint".contains(type)){
			return Long.class.getSimpleName();
		}else if("float double decimal numeric".contains(type)){
			return BigDecimal.class.getName();
		}else{
			return String.class.getSimpleName();
		}
	}
	

	@Override
	public String jdbcTypeMapper(DBTableColumn dbDataColumn) {
		String type = dbDataColumn.getDataType();
		
		if("int integer".contains(type)){
			return Integer.class.getSimpleName().toUpperCase();
		}else if("date datetime ".contains(type)){
			return Date.class.getSimpleName().toUpperCase();
		}else{
			return type.toUpperCase();
		}
	}

	public String getSchemaPattern() {
		return schemaPattern;
	}

	public void setSchemaPattern(String schemaPattern) {
		this.schemaPattern = schemaPattern;
	}

	public String getTableNamePattern() {
		return tableNamePattern;
	}

	public void setTableNamePattern(String tableNamePattern) {
		this.tableNamePattern = tableNamePattern;
	}
}

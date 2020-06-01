package org.ltsh.core.core.db.jdbc.table;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ltsh.core.core.db.jdbc.bean.DBConnector;
import org.ltsh.core.core.db.jdbc.bean.DBTable;
import org.ltsh.core.core.db.jdbc.bean.DBTableColumn;
import org.ltsh.core.core.db.jdbc.bean.PreparedStatementCallback;
import org.ltsh.core.core.util.StringUtil;

/**
 * Oracle数据库表获取实现
 * @author Ych
 * 2018年4月28日
 */
public class OracleTableInfo implements DBTableInfo{
	private static String DB_DRIVER_NAME = "oracle.jdbc.driver.OracleDriver";
	private static String DB_URL = "jdbc:oracle:thin:@{host}:{port}:{sid}";
	
	private String schemaPattern;
	private String tableNamePattern;
	private DBConnector dbConnector;
	private List<DBTable> tables;
	
	public OracleTableInfo(String host, int port, String sid, String username, String password) throws SQLException{
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
			sql.append("c.owner");
			sql.append(", c.table_name");
			sql.append(", c.column_name");
			sql.append(", c.data_type");
			sql.append(", c.char_length");
			sql.append(", c.data_precision");
			sql.append(", c.data_scale");
			sql.append(", c.nullable");
			sql.append(", cm.comments");
			sql.append(" from all_tab_columns c"
					+ " left join all_col_comments cm on c.owner = cm.owner and c.table_name = cm.table_name"
					+ "where 1=1");
			
			final String AND = " and ";
			final String OR = " or ";
			final String SEP = ",";
			final String PERC = "%";
			
			if(schemaPattern.contains(SEP)){
				String[] schemaPatterns = schemaPattern.split(SEP);
				sql.append(" and c.owner in ('").append(StringUtil.join("','", schemaPatterns)).append("')");
			}else {
				sql.append(" and c.owner = '").append(schemaPattern.toUpperCase()).append("'");
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
			sql.append(" order by c.owner,c.column_id");
			
			return dbConnector.execute(sql.toString(), new PreparedStatementCallback<List<DBTable>>(){

				@Override
				public List<DBTable> doInPreparedStatement(PreparedStatement ps)
						throws SQLException {
					
					ResultSet rs = ps.executeQuery();
					
					List<DBTable> dbTableList = new ArrayList<DBTable>(); 
					Map<String, DBTable> map = new HashMap<String, DBTable>();
					
			        while(rs.next()){
			        	String tableName = rs.getString("table_name");
			        	
			        	DBTable dbTable = map.get(tableName);
			        	if(dbTable == null){
			        		dbTable = new DBTable(tableName, new ArrayList<DBTableColumn>());
			        		dbTable.setTableSchema(rs.getString("owner"));
			        		map.put(tableName, dbTable);
			        		dbTableList.add(dbTable);
			        	}
			        	List<DBTableColumn> dbTableColList = dbTable.getColumns();
		        		
		        		String columnName = rs.getString("column_name"); 
		        		String dataType = rs.getString("data_type"); 
		        		int length = rs.getInt("char_length"); 
		        		int precision = rs.getInt("data_precision"); 
		        		int scale = rs.getInt("data_scale"); 
		        		boolean nullable = "Y".equals(rs.getString("nullable")) ? true : false;
		        		String comment = rs.getString("comments"); 
		        		
		        		DBTableColumn tableColumn = new DBTableColumn();
		        		tableColumn.setColumnName(columnName);
		        		tableColumn.setDataType(dataType);
		        		tableColumn.setLength(length);
		        		tableColumn.setPrecision(precision);
		        		tableColumn.setScale(scale);
		        		tableColumn.setNullable(nullable);
		        		tableColumn.setComment(comment);
			        	
		        		dbTableColList.add(tableColumn);
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
		
		int precision = dbDataColumn.getPrecision();
		int scale = dbDataColumn.getScale();
		
		if("date timestamp(6)".contains(type)){
			return Date.class.getName();
		}else if("float number".contains(type)){
			if(precision <= 19 && scale == 0){
				return Long.class.getName();
			}else{
				return BigDecimal.class.getName();
			}
		}else{
			return String.class.getName();
		}
	}

	@Override
	public String jdbcTypeMapper(DBTableColumn dbDataColumn) {
		// TODO Auto-generated method stub
		return null;
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

	public DBConnector getDbConnector() {
		return dbConnector;
	}

	public void setDbConnector(DBConnector dbConnector) {
		this.dbConnector = dbConnector;
	}
}

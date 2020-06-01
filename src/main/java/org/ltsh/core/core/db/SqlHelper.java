package org.ltsh.core.core.db;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.ltsh.core.core.db.sql.DeleteSqlBuilder;
import org.ltsh.core.core.db.sql.InsertSqlBuilder;
import org.ltsh.core.core.db.sql.SqlBuilder;
import org.ltsh.core.core.db.sql.UpdateSqlBuilder;

/**
 * SQL工具类
 * @author Ych
 * 2017年5月16日
 */
public class SqlHelper {
	
	/**
	 * 数据单元内部类
	 * @author Ych
	 * 2017年5月16日
	 */
	private static class DataUnit implements SqlBuilder{
		private String sql;
		private Object[] param;
		public DataUnit(String sql, Object[] param){
			this.sql = sql;
			this.param = param;
		}
		@Override
		public String toString() {
			String sep = ", ";
			StringBuilder sub = new StringBuilder();
			for(Object o : param){
				sub.append(sep).append(o);
			}			
			
			StringBuilder sb = new StringBuilder("{");
			sb.append("sql : ").append(sql);
			sb.append(", param : [").append(sub.length() > 0 ? sub.substring(sep.length()) : "").append("]");
			
			sb.append("}");
			return sb.toString();
		}
		@Override
		public String getSql() {
			return sql;
		}

		@Override
		public Object[] getParam() {
			return param;
		}
		
	}
	
	/**
	 * SQL参数绑定
	 * @author Ych
	 * 2017年5月16日
	 * @param sql	例：select * from XXX where name = :name, age = :age
	 * @param param 例：{name : "小鸣", age : 18}
	 * @return
	 */
	public static SqlBuilder paramBind(String sql, Map<String, ?> param, char keyPrefix){
		StringBuilder newSql = new StringBuilder();
		
		List<Object> rsParam = new ArrayList<Object>();
		
		int len = sql.length();
		boolean startKey = false;
		StringBuilder key = new StringBuilder();
		
		for (int i = 0; i < len; i++) {
			char c = sql.charAt(i);
			
			if(keyPrefix == c){
				startKey = true;
				continue;
			}
			if(startKey){
				if(' ' == c || ')' == c || ',' == c || ';' == c){
					startKey = false;
					newSql.append('?');
					
					Object value = param.get(key.toString());
					rsParam.add(value);
					key.delete(0, key.length());
				}else{
					key.append(c);
				}
				/**
				 * 如果到了最后一个字符还没有加载完key，要另外处理
				 */
				if(i == len - 1){
					newSql.append('?');
					Object value = param.get(key.toString());
					rsParam.add(value);
				}
			}else{
				newSql.append(c);
			}
		}
		return new DataUnit(newSql.toString(), rsParam.toArray());
	}
	
	/**
	 * SQL参数绑定
	 * @author Ych
	 * 2017年5月16日
	 * @param sql	例：select * from XXX where name = :name, age = :age
	 * @param param 例：{name : "小鸣", age : 18}
	 * @return
	 */
	public static SqlBuilder paramBind(String sql, Map<String, ?> param){
		return paramBind(sql, param, ':');
	}
	
	/**
	 * 获取SQL insert语句SQL构建器
	 * @author Ych
	 * @param tableName
	 * @return
	 */
	public static InsertSqlBuilder insert(String tableName){
		return new InsertSqlBuilder(tableName);
	}
	/**
	 * 获取SQL update语句SQL构建器
	 * @author Ych
	 * @param tableName
	 * @return
	 */
	public static UpdateSqlBuilder update(String tableName){
		return new UpdateSqlBuilder(tableName);
	}
	/**
	 * 获取SQL delete语句SQL构建器
	 * @author Ych
	 * @param tableName
	 * @return
	 */
	public static DeleteSqlBuilder delete(String tableName){
		return new DeleteSqlBuilder(tableName);
	}
}

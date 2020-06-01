package org.ltsh.core.core.db.jdbc.bean;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.ltsh.core.core.util.FileUtil;

/**
 * 数据库连接工具类
 * @author Ych
 */
public class DBConnector {
	
	private String driverName;
	private String url;
	private String username;
	private String password;
	
	public DBConnector(String driverName, String url, String username, String password) throws SQLException{
		this.driverName = driverName;
		this.url = url;
		this.username = username;
		this.password = password;
		
		try{
			Class.forName(this.driverName);
		}catch(ClassNotFoundException e){
			throw new SQLException("错误: 数据库连接池驱动加载失败.");
		}
	}
	
	/**
	 * 执行sql脚本，每执行一次建立一次连接和关闭该连接
	 * @author Ych
	 * @param sql	sql脚本
	 * @param action	业务逻辑实现
	 * @return
	 * @throws SQLException
	 */
	public <T> T execute(final String sql, final PreparedStatementCallback<T> action) throws SQLException{
		Connection conn = DriverManager.getConnection(url, username, password);
		PreparedStatement ps = null;
		T result = null;
		try {
			ps = conn.prepareStatement(sql);
			result = action.doInPreparedStatement(ps);
		} finally {
			FileUtil.close(ps, conn);
		}
		return result;
	}
}

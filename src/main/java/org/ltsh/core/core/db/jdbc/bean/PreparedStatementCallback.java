package org.ltsh.core.core.db.jdbc.bean;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 预处理回调函数
 * @author YeChao
 * @param <T>
 */
public interface PreparedStatementCallback<T> {

	T doInPreparedStatement(PreparedStatement ps) throws SQLException;
}

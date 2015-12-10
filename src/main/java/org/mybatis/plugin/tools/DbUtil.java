package org.mybatis.plugin.tools;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.mybatis.plugin.config.DbConnection;

/**
 * 数据库工具类
 * @author Administrator
 *
 */
public class DbUtil {

	private static Connection conn = null;
	//打开连接
	public static Connection openConnection() throws SQLException, ClassNotFoundException, IOException {
		
		if (null == conn || conn.isClosed()) {
			
			Class.forName(DbConnection.DB_DRIVER);
			conn = DriverManager.getConnection(DbConnection.url, DbConnection.userName,DbConnection.password);
		}
		return conn;
	}
}

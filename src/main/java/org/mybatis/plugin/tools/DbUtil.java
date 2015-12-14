package org.mybatis.plugin.tools;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.mybatis.plugin.config.DbConnectionConfig;

/**
 * 数据库工具类
 * @author Administrator
 *
 */
public class DbUtil {

	private static Connection conn = null;
	//打开连接
	public static Connection openConnection(DbConnectionConfig dbConfig) throws SQLException, ClassNotFoundException, IOException {
		
		if (null == conn || conn.isClosed()) {
			
			Class.forName(MessageConstant.DB_DRIVER);
			conn = DriverManager.getConnection(dbConfig.getUrl(), dbConfig.getUserName(),dbConfig.getPassword());
		}
		return conn;
	}
}

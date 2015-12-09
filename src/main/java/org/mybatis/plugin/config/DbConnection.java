package org.mybatis.plugin.config;
/**
 * 数据库连接
 * @author bravefc
 *
 */
public class DbConnection {

	public static final String DB_DRIVER = "com.mysql.jdbc.Driver";
	
	public static String userName;
	
	public static String password;
	
	public static String url;
	
	

	public DbConnection() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DbConnection(String userName, String password, String url) {
		super();
		this.userName = userName;
		this.password = password;
		this.url = url;
	}


	
}

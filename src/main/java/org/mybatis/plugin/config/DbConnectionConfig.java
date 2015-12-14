package org.mybatis.plugin.config;
/**
 * 数据库连接
 * @author bravefc
 *
 */
public class DbConnectionConfig {

	
	private  String userName;
	
	private  String password;
	
	private  String url;
	
	private String dbSchema;

	public DbConnectionConfig() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DbConnectionConfig(String userName, String password, String url,String dbSchema) {
		super();
		this.userName = userName;
		this.password = password;
		this.url = url;
		this.dbSchema = dbSchema;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	
	
}

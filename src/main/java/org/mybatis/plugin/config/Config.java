package org.mybatis.plugin.config;

public class Config {

	//数据库连接
	public  DbConnection dbconn;

	public Config(DbConnection dbconn) {

		this.dbconn = dbconn;
	}
	
	
}

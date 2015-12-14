package org.mybatis.plugin.config;

public class GeneratorConfig {

	//要生成的数据库表
	private String[] db_table_arrays;
	//数据库连接
	public  DbConnectionConfig dbconn;

	public GeneratorConfig(DbConnectionConfig dbconn) {

		this.dbconn = dbconn;
	}
    
	public GeneratorConfig(String[] db_table_arrays, DbConnectionConfig dbconn) {
		super();
		this.db_table_arrays = db_table_arrays;
		this.dbconn = dbconn;
	}
    
	public String[] getDb_table_arrays() {
		return db_table_arrays;
	}

	public void setDb_table_arrays(String[] db_table_arrays) {
		this.db_table_arrays = db_table_arrays;
	}

	public DbConnectionConfig getDbconn() {
		return dbconn;
	}

	public void setDbconn(DbConnectionConfig dbconn) {
		this.dbconn = dbconn;
	}
	
	
}

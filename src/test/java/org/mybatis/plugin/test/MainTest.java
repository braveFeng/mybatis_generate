package org.mybatis.plugin.test;

import org.mybatis.plugin.DbOperationHelper;
import org.mybatis.plugin.config.DbConnectionConfig;
import org.mybatis.plugin.config.GeneratorConfig;

public class MainTest {

	public static void main(String[] args) {
		
		DbConnectionConfig dbConfig = new DbConnectionConfig("root", "123456","jdbc:mysql://192.168.1.16:3306/healthy", "healthy");
		GeneratorConfig generateConfig = new GeneratorConfig(dbConfig);
		generateConfig.setDb_table_arrays(new String[]{"H_FRIENDS"});
		DbOperationHelper helper = new DbOperationHelper(generateConfig);
		helper.run();
	}
}

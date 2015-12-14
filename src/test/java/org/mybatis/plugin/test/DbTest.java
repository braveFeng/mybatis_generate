package org.mybatis.plugin.test;

import java.sql.Connection;

import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.mybatis.plugin.config.DbConnectionConfig;
import org.mybatis.plugin.model.HUserInfoModel;
import org.mybatis.plugin.tools.DbUtil;

public class DbTest {

	public static void main(String[] args) {
		
		try {
			QueryRunner queryRunner = new QueryRunner();
			DbConnectionConfig connConfig = new DbConnectionConfig("root","123456","jdbc:mysql://192.168.1.16:3306/healthy","healthy");
			Connection conn = DbUtil.openConnection(connConfig);
			HUserInfoModel user = queryRunner.query(conn,  
                    "select * from H_USER_INFO LIMIT 1",  
                    new BeanHandler<HUserInfoModel>(HUserInfoModel.class)); 
			System.out.println(user.getNickName());
		} catch (Exception e) {
			// TODO: handle exception
		}
		
	}
}

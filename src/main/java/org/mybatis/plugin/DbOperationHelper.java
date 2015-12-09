package org.mybatis.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.plugin.model.ModelDetailDto;


public class DbOperationHelper {

	
	/**
	 * Description:根据表名获取对应的类和字段信息
	 * @param: GetAllFriendRequest
	 * @return ModelDetailDto
	 * @Author:bravefc
	 * @Create Date: 2015-11-05
	 */
	 public ModelDetailDto getModelDetailByTable(Connection conn,String tableName,String dbSchema) throws Exception{
		 
		 if(conn == null){
			 throw new NullPointerException("数据库连接不能为空");
		 }
		 if(StringUtils.isBlank(tableName)){
			 throw new NullPointerException("表名不能为空");
		 }
		 ModelDetailDto detail = new ModelDetailDto();
		 detail.setTableName(tableName);
		 detail.setDbSchema(dbSchema);
		return detail;
	 }
	 
	 public void setDetailInfo(Connection conn,ModelDetailDto detailDto) throws Exception{
		 
		 //获取字段和字段注释
		 String tableSql = "select  column_name, column_comment from information_schema.columns where table_schema ='"+detailDto.getDbSchema()+"'  and table_name ='" + detailDto.getTableName().toUpperCase() + "'";
		 PreparedStatement ps = conn.prepareStatement(tableSql);
		 ResultSet result = ps.executeQuery();
		 Map detailMap = new HashMap();
		 if(result.next()){
			 
			 detailMap.put(result.getString("column_name"),result.getString("column_comment"));
		 }
		 //获取主键
		 String pkInfo = "SELECT  DISTINCT c.COLUMN_NAME  FROM  INFORMATION_SCHEMA.TABLE_CONSTRAINTS AS t,  INFORMATION_SCHEMA.KEY_COLUMN_USAGE AS c WHERE  t.TABLE_NAME = c.TABLE_NAME  AND t.TABLE_SCHEMA = '"+detailDto.getDbSchema()+"'  AND t.CONSTRAINT_TYPE = 'PRIMARY KEY' AND t.TABLE_NAME = '" +  detailDto.getTableName().toUpperCase() + "'";
		 PreparedStatement ps2 = conn.prepareStatement(tableSql);
		 ResultSet pkResult = ps.executeQuery();
		 StringBuilder builder = new StringBuilder();
		 if(pkResult.next()){
			 
		 }
	 }
	 
}

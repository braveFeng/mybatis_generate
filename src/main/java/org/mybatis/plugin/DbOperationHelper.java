package org.mybatis.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.mybatis.plugin.model.ColumnInfoDto;
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
		 PreparedStatement ps2 = conn.prepareStatement(pkInfo);
		 ResultSet pkResult = ps.executeQuery();
		 StringBuilder builder = new StringBuilder();
		 if(pkResult.next()){
			 if(builder.length() > 0){
				 builder.append(",");
			 }
			 builder.append(getPropertyName(pkResult.getString(1)));
		 }
		 detailDto.setPkList(builder.toString());
		 //获取数据库的所有字段
		 String metaSql = "select * from " + detailDto.getTableName();
		 PreparedStatement ps3 = conn.prepareStatement(metaSql);
		 ResultSet metaDataResult = ps3.executeQuery();
		 ResultSetMetaData metaData = metaDataResult.getMetaData();
		 //循环获取的元数据
		 for(int i = 1; i < metaData.getColumnCount(); i++){
			 
		 }
	 }
	    /**
		 * Description:给ColumnInfo对象设置信息
		 * @param: metaData  表的元数据
		 * @Author:bravefc
		 * @Create Date: 2015-11-05
		 */
	 public void setColumnInfo(ResultSetMetaData metaData ,int i,Map map) throws Exception{
		 
		 ColumnInfoDto info = new ColumnInfoDto();
		 info.setColumnName(metaData.getColumnName(i));
		 info.setPropertyName(getPropertyName(info.getColumnName()));
		 info.setColumnComment((String)map.get(info.getColumnName()));
		 if(metaData.getColumnType(i) == 2){
				//System.out.println(tableName + " : " + columnIDto.getColumnName() + " : " + metaData.getScale(i) + " : " + metaData.getPrecision(i));
				if(metaData.getScale(i) > 0){
					info.setSqlType(6);
					info.setJavaType(getJavaTypeBySqlType(6));
				}else if(metaData.getPrecision(i) > 10){
					info.setSqlType(-5);
					info.setJavaType(getJavaTypeBySqlType( -5));
				}else{
					info.setSqlType(4);
					info.setJavaType(getJavaTypeBySqlType(4));
				}
			}else{
				info.setSqlType(metaData.getColumnType(i));
				info.setJavaType(getJavaTypeBySqlType( metaData.getColumnType(i)));
			}

	 }
	 /**
	 * Description:通过sqltype来获取javatype
	 * @param: GetAllFriendRequest
	 * @return ModelDetailDto
	 * @Author:bravefc
	 * @Create Date: 2015-11-05
	 */
	 public String getJavaTypeBySqlType(int type){
		 
		 
		return null;
		 
	 }
	 /**
	 * Description:通过字段名获取属性名
	 * @param: GetAllFriendRequest
	 * @return ModelDetailDto
	 * @Author:bravefc
	 * @Create Date: 2015-11-05
	 */
	 public String getPropertyName(String columnName){
		 
		 StringBuilder builder = new StringBuilder();
		 columnName = columnName.toLowerCase();
		 String [] array = columnName.split(",");
		 builder.append(array[0]);
		 for(int i = 1;i < builder.length(); i++){
			 if(StringUtils.isBlank(array[i])){
				 continue;
			 }
			 builder.append(array[i].substring(0,1).toUpperCase()).append(array[i].substring(1));
		 }
		 return builder.toString();
	 }
	 
}

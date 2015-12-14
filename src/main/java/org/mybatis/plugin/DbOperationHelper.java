package org.mybatis.plugin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.mybatis.plugin.config.GeneratorConfig;
import org.mybatis.plugin.model.ColumnInfoDto;
import org.mybatis.plugin.model.ModelDetailDto;
import org.mybatis.plugin.tools.CommonUtil;
import org.mybatis.plugin.tools.DbUtil;
import org.mybatis.plugin.type.JavaTypeResolver;


public class DbOperationHelper {

	//配置文件
	private GeneratorConfig config;
	//属性配置文件
	private Properties configProperties;
	//数据库连接
	private Connection conn;
	
	public DbOperationHelper(GeneratorConfig config) {
		super();
		this.config = config;
	}

	/**
	 * Description:main方法
	 * @param: GetAllFriendRequest
	 * @return ModelDetailDto
	 * @Author:bravefc
	 * @Create Date: 2015-11-05
	 */
	public void run(){
		
		if(this.config == null){
			throw new NullPointerException("配置文件不能为空"); 
		}
		String[] tableArrays = config.getDb_table_arrays();
		if(ArrayUtils.isNotEmpty(tableArrays)){
			throw new NullPointerException("数据库表名不能为空"); 
		}
		try {
			conn = DbUtil.openConnection(config.getDbconn());			
		} catch (Exception e) {
			throw new NullPointerException(e.getMessage()); 
		}
		try {
			//初始化配置
			initData();
			 //循环遍历数据库表
			for(String table:tableArrays){
				ModelDetailDto detailDto = getModelDetailByTable(conn, table, config.getDbconn().getDbSchema());
				//生成java和xml文件
			}
		} catch (Exception e) {
			throw new RuntimeException(e.getMessage()); 
		}
	   
	}
	/**
	 * Description:生成java文件和xml配置文件
	 * @throws Exception 
	 * @Author:bravefc
	 * @Create Date: 2015-11-05
	 */
	public void generateJavaAndXml() throws Exception{
		
		
	}
	/**
	 * Description:初始化配置信息
	 * @throws Exception 
	 * @Author:bravefc
	 * @Create Date: 2015-11-05
	 */
	public void initData() throws Exception{
		
		configProperties = CommonUtil.getPropertiesByInputStream(getClass().getClassLoader().getResourceAsStream("config.properties"));
	}
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
		 setDetailInfo(conn, detail);
		return detail;
	 }
	 
	 public ModelDetailDto setDetailInfo(Connection conn,ModelDetailDto detailDto) throws Exception{
		 
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
			 //设置column集合
			 setColumnInfo(metaData,i,detailMap,detailDto);
		 }
		return detailDto;
	 }
    /**
	 * Description:给ColumnInfo对象设置信息
	 * @param: metaData  表的元数据
	 * @Author:bravefc
	 * @Create Date: 2015-11-05
	 */
	 public void setColumnInfo(ResultSetMetaData metaData ,int i,Map map,ModelDetailDto detailDto) throws Exception{
		 
		 ColumnInfoDto info = new ColumnInfoDto();
		 info.setColumnName(metaData.getColumnName(i));
		 info.setPropertyName(getPropertyName(info.getColumnName()));
		 info.setColumnComment((String)map.get(info.getColumnName()));
		 //通过元数据获取jdbc和java的数据类型
		 int jdbcType = JavaTypeResolver.calculateJavaType(metaData, i);
		 info.setSqlType(jdbcType);
		 info.setJavaType(JavaTypeResolver.typeMap.get(jdbcType).toString());
         detailDto.getColumnList().add(info);
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

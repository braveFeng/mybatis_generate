package org.mybatis.plugin;

import java.io.StringWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;
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
		if(ArrayUtils.isEmpty(tableArrays)){
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
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("detailDto", detailDto);
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
	public void generateJavaAndXml(Map<String, Object> map) throws Exception{
		
		//生成接口的源文件
		String mapperInterfaceSource = generateInterfaceSource(map);
	}
	//生成source文件
	public String generateInterfaceSource(Map<String, Object> map) throws Exception{
		
		//读取模板配置信息 mapper interface
		String mapper_interface_template_file = configProperties.getProperty("DAO_INTERFACE_TEMPLATE_FILE");
		
		return generateSource(mapper_interface_template_file,map);
		
		
	}
	//生成source
	public String generateSource(String templateFile,Map<String, Object> paramMap) throws Exception{
		
		VelocityContext context = new VelocityContext();	
		Set<Entry<String, Object>> entrySet = paramMap.entrySet();
		for(Entry<String, Object> entry:entrySet){
			context.put(entry.getKey(), entry.getValue());
		}	
		StringWriter sw = new StringWriter();

		VelocityEngine engine = getVelocityEngine();

		Template template = engine.getTemplate(templateFile, "UTF-8");

		template.merge(context, sw);
		
		return sw.toString();
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
		 JavaTypeResolver resolver = new JavaTypeResolver();
		 info.setJavaType(resolver.getTypeMap().get(jdbcType).toString());
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
		 for(int i = 1;i < array.length; i++){
			 if(StringUtils.isBlank(array[i])){
				 continue;
			 }
			 builder.append(array[i].substring(0,1).toUpperCase()).append(array[i].substring(1));
		 }
		 return builder.toString();
	 }
		private static VelocityEngine getVelocityEngine() throws Exception {
			Properties properties = new Properties();
			// 可选值："class"--从classpath中读取，"file"--从文件系统中读取
			properties.setProperty("resource.loader", "file");
			// 如果从文件系统中读取模板，那么属性值为org.apache.velocity.runtime.resource.loader.FileResourceLoader
			properties
					.setProperty("jar.resource.loader.class",
							"org.apache.velocity.runtime.resource.loader.FileResourceLoader");
			// properties.setProperty("jar.resource.loader.path", "jar:file:/" +
			// DAOTool.getLibFilePath() + ToolsConst.SEPARATOR +
			// ToolsConst.DAOTOOLS_JAR_NAME);
			properties.setProperty(VelocityEngine.RUNTIME_LOG_LOGSYSTEM_CLASS,
					"org.apache.velocity.runtime.log.NullLogSystem");

			VelocityEngine engine = new VelocityEngine();

			engine.init(properties);
			return engine;

		}
}

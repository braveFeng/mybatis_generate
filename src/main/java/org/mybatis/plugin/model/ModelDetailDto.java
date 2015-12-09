package org.mybatis.plugin.model;

import java.util.ArrayList;
import java.util.List;


public class ModelDetailDto {

	//表名
	private String tableName;
	//类名
	private String className;
	//表的字段list
	private List<ColumnInfoDto> columnList = new ArrayList<ColumnInfoDto>();
    //schema
	private String dbSchema;
	
	//主键
	private String pkList;
	
	
	public String getPkList() {
		return pkList;
	}

	public void setPkList(String pkList) {
		this.pkList = pkList;
	}

	public String getDbSchema() {
		return dbSchema;
	}

	public void setDbSchema(String dbSchema) {
		this.dbSchema = dbSchema;
	}

	public String getTableName() {
		return tableName;
	}

	public void setTableName(String tableName) {
		this.tableName = tableName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public List<ColumnInfoDto> getColumnList() {
		return columnList;
	}

	public void setColumnList(List<ColumnInfoDto> columnList) {
		this.columnList = columnList;
	}
	
	
}

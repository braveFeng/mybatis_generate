package org.mybatis.plugin.model;

public class ColumnInfoDto {

	private String propertyName = null;

	private String columnName = null;

	private String javaType = null;
	
	private String columnComment = null;

	private int sqlType;

	public String getGetPropertyMethod() {
		return "get" + propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
	}

	public String getSetPropertyMethod() {
		return "set" + propertyName.substring(0, 1).toUpperCase()
				+ propertyName.substring(1);
	}

	public String getPropertyName() {
		return propertyName;
	}

	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}

	public String getJavaType() {
		return javaType;
	}

	public void setJavaType(String javaType) {
		this.javaType = javaType;
	}

	public int getSqlType() {
		return sqlType;
	}

	public void setSqlType(int sqlType) {
		this.sqlType = sqlType;
	}

	public String getColumnName() {
		return columnName;
	}

	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}

	public String getColumnComment() {
		return columnComment;
	}

	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
}
